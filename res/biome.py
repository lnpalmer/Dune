import os
import random
from collections import defaultdict

def parse_bc_line(line):
    line = line.rstrip("\r\n")
    ind = line.find(': ')
    if ind == -1 or line[0] == "#":
        return False, None, None
    else:
        return True, line[:ind], line[ind + len(': '):]

presets = {}
def load_preset(preset_root, preset_name):
    preset = {}
    with open(os.path.join(preset_root, preset_name + ".bc")) as file:
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
            

class Biome(dict):
    """ Load a list of biome presets. Doesn't load everything, just what we need for compatibility checks """
    @staticmethod
    def load_presets(preset_root, preset_names):
        for preset_name in preset_names:
            presets[preset_name] = load_preset(preset_root, preset_name)

    """ Sample a random biome """
    @staticmethod
    def sample():
        biome = Biome()
        biome["BiomeExtends"] = random.choice(presets.keys())
        return biome
    
    @staticmethod
    def sample_compatible_pair():
        biome_a = Biome.sample()
        while True:
            biome_b = Biome.sample()
            if biome_a.compatible_with(biome_b):
                return biome_a, biome_b

    def __init__(self):
        dict.__init__(self)

        self.skyland = False
    
    def __getitem__(self, key):
        return dict.__getitem__(self, key)
    
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
    
    def write(self, path):
        with open(path, "w+") as file:
            lines = ["%s: %s\n" % (key, str(value)) for key, value in self.items()]
            file.writelines(lines)
    
    def compatible_with(self, other):
        if self.skyland != other.skyland:
            return False

        if abs(self.preset()["BiomeTemperature"] - other.preset()["BiomeTemperature"]) > 0.8:
            return False

        if abs(self.preset()["BiomeWetness"] - other.preset()["BiomeWetness"]) > 0.6:
            return False

        if self.preset()["InheritMobsBiomeName"] == other.preset()["InheritMobsBiomeName"]:
            return True

        if self.preset()["SurfaceBlock"] == other.preset()["SurfaceBlock"]:
            return True
        
        return False
    
    def preset(self):
        return presets[self["BiomeExtends"]]