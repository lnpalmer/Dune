import copy
import uuid
import os
import shutil
import random
from collections import defaultdict

def parse_bc_line(line):
    line = line.rstrip("\r\n")
    ind = line.find(': ')
    if ind == -1 or line[0] == "#":
        return False, None, None
    else:
        return True, line[:ind], line[ind + len(': '):]

def load_preset(preset_root, preset_name):
    preset = {}
    with open(os.path.join(preset_root, "WorldBiomes", preset_name + ".bc")) as file:
        lines = file.readlines()
        for line in lines:
            valid, key, raw_value = parse_bc_line(line)
            if valid:
                value = raw_value
                # print("%s: %s" % (key, raw_value))
                if key == "BiomeDictId":
                    value = [token.strip() for token in raw_value.split(",")]
                if key == "BiomeTemperature":
                    value = float(raw_value)
                if key == "BiomeWetness":
                    value = float(raw_value)
                preset[key] = value
    return preset


class Biome():
    """
    Represents an OpenTerrainGenerator biome. Can be written (lossily) to a .bc file.
    """

    """ Load a list of biome presets. Doesn't load everything, just what we need for compatibility checks """
    @staticmethod
    def load_presets(preset_root, preset_names):
        Biome.preset_root = preset_root
        for preset_name in preset_names:
            Biome.presets[preset_name] = load_preset(preset_root, preset_name)

    """ Sample a random biome """
    @staticmethod
    def sample():
        biome = Biome()
        biome["BiomeExtends"] = random.choice(Biome.presets.keys())
        return biome
    
    @staticmethod
    def sample_compatible_pair():
        biome_a = Biome.sample()
        while True:
            biome_b = Biome.sample()
            if biome_a.compatible_with(biome_b):
                return biome_a, biome_b
    
    @staticmethod
    def copy_presets(dest_path):
        shutil.copytree(os.path.join(Biome.preset_root, "WorldBiomes"),
                os.path.join(dest_path, "WorldBiomes"))

        shutil.copytree(os.path.join(Biome.preset_root, "WorldObjects"),
                        os.path.join(dest_path, "WorldObjects"))

    def __init__(self):
        self.props = dict()
        self.skyland = False
        self.name = "worldset-" + str(uuid.uuid4())
        self.island_in_biomes = []
        self.border_of_biomes = []

        self["BiomeRarity"] = 100
 
    def __getitem__(self, key):
        return self.props.__getitem__(key)
    
    def __setitem__(self, key, value):
        self.props.__setitem__(key, value)

    """ Returns a copy of the Biome """    
    def copy(self):
        other = Biome()
        other.props = copy.deepcopy(self.props)
        other.skyland = self.skyland
        other.name = self.name
        other.island_in_biomes = copy.copy(self.island_in_biomes)
        other.border_of_biomes = copy.copy(self.border_of_biomes)
        return other

    # make this biome a skyland
    def make_skyland(self):
        height_ctrl = [
            -75.0, -70.0, -65.0, -60.0, -55.0, -45.0, -30.0, -15.0,
            0.0, -30.0, -60.0, -120.0, -2500.0, -2500.0, -2500.0, -2500.0,
            -2500.0, -2500.0, -2500.0, -2500.0, -2500.0, -2500.0, -2500.0, -2500.0,
            -2500.0, -2500.0, -2500.0, -2500.0, -2500.0, -2500.0, -2500.0, -2500.0,
            -2500.0]
        self["CustomHeightControl"] = str(height_ctrl)[1:-1]
        self["VillageType"] = "disabled"
        self["RareBuildingType"] = "disabled"
        self["MineshaftType"] = "disabled"
        self["WoodLandMansionsEnabled"] = "false"
        self["OceanMonumentsEnabled"] = "false"
        self["NetherFortressesEnabled"] = "false"
        self.skyland = True
    
    """ Write properties to file in folder at path """
    def write(self, path):
        self.gen_props_()

        if not os.path.exists(path):
            os.makedirs(path)
        with open(os.path.join(path, self.name + ".bc"), "w+") as file:
            lines = ["%s: %s\n" % (key, str(value)) for key, value in self.props.items()]
            file.writelines(lines)
    
    def compatible_with(self, other):
        if self.skyland != other.skyland:
            return False
        
        if self["BiomeExtends"] == other["BiomeExtends"]:
            return False

        if abs(self.preset()["BiomeTemperature"] - other.preset()["BiomeTemperature"]) < 0.8:
            return True

        if abs(self.preset()["BiomeWetness"] - other.preset()["BiomeWetness"]) < 0.6:
            return True

        if self.preset()["InheritMobsBiomeName"] == other.preset()["InheritMobsBiomeName"]:
            return True

        if self.preset()["SurfaceBlock"] == other.preset()["SurfaceBlock"]:
            return True
        
        return False
    
    def preset(self):
        return Biome.presets[self["BiomeExtends"]]
    
    def gen_props_(self):
        self["IsleInBiome"] = ", ".join([biome.name for biome in self.island_in_biomes])
        self["BiomeIsBorder"] = ", ".join([biome.name for biome in self.border_of_biomes])

Biome.presets = {}