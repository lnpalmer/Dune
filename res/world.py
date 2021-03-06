import os
import shutil
import copy

from biome import Biome

custom_id_blacklist = [
    "Ocean",
    "Plains",
    "Desert",
    "Extreme Hills",
    "Forest",
    "Taiga",
    "Swampland",
    "River",
    "Hell",
    "The End",
    "FrozenOcean",
    "FrozenRiver",
    "Ice Plains",
    "Ice Mountains",
    "MushroomIsland",
    "MustroomIslandShore",
    "Beach",
    "DesertHills",
    "ForestHills",
    "TaigaHills",
    "Extreme Hills Edge",
    "Jungle",
    "JungleHills",
    "JungleEdge",
    "Deep Ocean",
    "Stone Beach",
    "Cold Beach",
    "Birch Forest",
    "Birch Forest Hills",
    "Roofed Forest",
    "Cold Taiga",
    "Cold Taiga Hills",
    "Mega Taiga",
    "Mega Taiga Hills",
    "Extreme Hills+",
    "Savanna",
    "Savanna Plateau",
    "Mesa",
    "Mesa Plateau F",
    "Mesa Plateau",
    # divider
    "Sunflower Plains",
    "Desert M",
    "Extreme Hills M",
    "Flower Forest",
    "Taiga M",
    "Swampland M",
    "Ice Plains Spikes",
    "Jungle M",
    "Jungle Edge M",
    "Birch Forest M",
    "Birch Forest Hills M",
    "Roofed Forest M",
    "Cold Taiga M",
    "Mega Spruce Taiga",
    "Redwood Taiga Hills M",
    "Exteme Hills+ M",
    "Savanna M",
    "Savanna Plateau M",
    "Mesa (Bryce)",
    "Mesa Plateau F M",
    "Mesa Plateau M",
]

class World:
    """
    Represents the configuration required to generate a world with OpenTerrainGenerator.
    Can be written to a file tree.
    """

    """
    Create a world from two biomes and a world pattern

    Patterns
        "island spill": mix of A and B, with islands of B in A near the A-B border.
    """
    @staticmethod
    def create_from_biome_pair(biome_a, biome_b, pattern):
        world = World()
        world_biome_a, world_biome_b = biome_a.copy(), biome_b.copy()

        if pattern == "island spill":
            spill_biome = Biome()
            spill_biome["BiomeExtends"] = biome_a.name

            world_biome_a["BiomeSize"] = 4
            world_biome_b["BiomeSize"] = 4

            world_biome_a["BiomeHeight"] = -1.7
            world_biome_b["BiomeHeight"] = -0.8

            # border the spill biome on B
            spill_biome.border_of_biomes.append(world_biome_b)
            spill_biome["BiomeSizeWhenBorder"] = 6

            # put islands of A in the spill biome
            world_biome_a.island_in_biomes.append(spill_biome)
            world_biome_a["BiomeSizeWhenIsle"] = 7
            world_biome_a["BiomeRarityWhenIsle"] = 100

            world.biomes = [world_biome_a, world_biome_b, spill_biome]
            world.border_biomes = [spill_biome]
            world.island_biomes = [world_biome_b]

            world.biome_groups.append(("NormalBiomes", 3, 100, [world_biome_a, world_biome_b]))

        return world
    
    def __init__(self):
        self.props = dict()
        self.biomes = []
        self.island_biomes = []
        self.border_biomes = []
        # format for biome groups: (name, size, rarity, biomes)
        self.biome_groups = []
        self.skyland = False

        # world info
        self["Author"] = "lnpalmer (worldgen.py)"
        self["Description"] = "Automatically generated by worldgen.py"
        self["SettingsMode"] = "WriteWithoutComments"

        # general modes
        self["TerrainMode"] = "Normal"
        self["BiomeMode"] = "BeforeGroups"

        self["GenerationDepth"] = 13
        self["BiomeRarityScale"] = 100

        # landmass settings
        self["LandRarity"] = 100
        self["LandSize"] = 2
        self["LandFuzzy"] = 4

        # ice area settings
        self["FrozenOcean"] = "false"
        self["OceanFreezingTemperature"] = 0.15
        self["FreezeAllBiomesInColdGroup"] = "false"

        # rivers
        self["RiverRarity"] = 4
        self["RiverSize"] = 3
        self["RiversEnabled"] = "true"
        self["ImprovedRivers"] = "false"
        self["RandomRivers"] = "true"

        # terrain height and volatility
        self["WorldHeightScaleBits"] = 8
        self["WorldHeightCapBits"] = 8
        self["FractureHorizontal"] = 0.2
        self["FractureVertical"] = -2.0

        # blocks
        self["RemoveSurfaceStone"] = "false"
        self["DisableBedrock"] = "false"
        self["CeilingBedrock"] = "false"
        self["FlatBedrock"] = "false"
        self["BedrockBlock"] = "BEDROCK"
        self["PopulationBoundsCheck"] = "false"

        # water / lava
        self["WaterLevelMax"] = 63
        self["WaterLevelMin"] = 0
        self["WaterBlock"] = "STATIONARY_WATER"
        self["IceBlock"] = "ICE"
        self["CooledLavaBlock"] = "STATIONARY_LAVA"

        # world only
        self["FullyFreezeLakes"] = "true"
        self["UseTemperatureForSnowHeight"] = "true"
        self["BetterSnowFall"] = "true"

        # resources
        self["ResourcesSeed"] = 0
        self["ObjectSpawnRatio"] = 20

        # structures
        self["StrongholdsEnabled"] = "false"
        self["StrongholdCount"] = 128
        self["StrongholdDistance"] = 32.0
        self["StrongholdSpread"] = 3
        self["VillagesEnabled"] = "true"
        self["VillageSize"] = 0
        self["VillageDistance"] = 64
        self["RareBuildingsEnabled"] = "true"
        self["MinimumDistanceBetweenRareBuildings"] = 32
        self["MaximumDistanceBetweenRareBuildings"] = 64
        self["WoodLandMansionsEnabled"] = "true"
        self["OceanMonumentsEnabled"] = "true"
        self["OceanMonumentGridSize"] = 32
        self["OceanMonumentRandomOffset"] = 26
        self["IsOTGPlus"] = "false"
        self["ReplaceBlocksList"] = []
        self["MaximumCustomStructureRadius"] = 6
        self["MineshaftsEnabled"] = "true"
        self["NetherFortressesEnabled"] = "false"

        # cave settings
        self["CaveRarity"] = 12
        self["CaveFrequency"] = 25
        self["CaveMinAltitude"] = 1
        self["CaveMaxAltitude"] = 255
        self["IndividualCaveRarity"] = 25
        self["CaveSystemFrequency"] = 1
        self["CaveSystemPocketChance"] = 0
        self["CaveSystemPocketMinSize"] = 0
        self["CaveSystemPocketMaxSize"] = 4
        self["EvenCaveDistrubution"] = "false"

        # ravine settings
        self["RavineRarity"] = 1
        self["RavineMinAltitude"] = 20
        self["RavineMaxAltitude"] = 199
        self["RavineMinLength"] = 40
        self["RavineMaxLength"] = 79
        self["RavineDepth"] = 4.0

        self["PreGenerationRadius"] = 0
        self["WorldBorderRadius"] = 0

        self.rank = World.next_rank
        World.next_rank += 1

    def __getitem__(self, key):
        return self.props.__getitem__(key)
    
    def __setitem__(self, key, value):
        self.props.__setitem__(key, value)
    
    def make_skyland(self):
        for biome in self.biomes:
            biome.make_skyland()
        self.skyland = True
    
    """ Writes to file in folder at path """
    def write(self, path):
        self.gen_props_()

        if os.path.exists(path):
            shutil.rmtree(path)
        os.makedirs(path)
        with open(os.path.join(path, "WorldConfig.ini"), "w+") as file:
            prop_lines = ["%s: %s\n" % (key, str(value)) for key, value in self.props.items()]
            file.writelines(prop_lines)

            biome_group_lines = [self.biome_group_line_(biome_group) for biome_group in self.biome_groups]
            file.writelines(biome_group_lines)
        
        Biome.copy_presets(path)
        for biome in self.biomes:
            biome.write(os.path.join(path, "WorldBiomes"))

    """ Generate file properties from biomes, etc. """
    def gen_props_(self):
        self["CustomBiomes"] = self.custom_biomes_string_()
        self["IsleBiomes"] = ", ".join([biome.name for biome in self.island_biomes])
        self["BorderBiomes"] = ", ".join([biome.name for biome in self.border_biomes])
    
    def custom_biomes_string_(self):
        biome_names = [biome.name for biome in self.biomes]
        for biome in self.biomes:
            if not biome["BiomeExtends"] in biome_names and not biome["BiomeExtends"] in custom_id_blacklist:
                biome_names.append(biome["BiomeExtends"])
            
        entries = ["%s:%i" % (biome_name, 800 + self.rank * 10 + i) for i, biome_name in enumerate(biome_names)]
        return ", ".join(entries)
    
    def biome_group_line_(self, biome_group):
        name, size, rarity, biomes = biome_group
        biomes_string = ", ".join([biome.name for biome in biomes])
        return "BiomeGroup(%s, %i, %i, %s)\n" % (name, size, rarity, biomes_string)

World.next_rank = 0