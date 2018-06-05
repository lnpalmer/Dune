import sys
sys.path.append("Elite")

import argparse
import json
import random
import pyplanets

parser = argparse.ArgumentParser(
    prog="Dune World Set Generator",
    formatter_class=argparse.ArgumentDefaultsHelpFormatter)
parser.add_argument("--config-path", type=str, default="worldset_config.json")
args = parser.parse_args()

config = None
with open(args.config_path) as config_file:
    config = json.load(config_file)

num_planets = config["num_planets"]
resources = config["resources"]

names = random.sample(pyplanets.Galaxy().planets, num_planets)
planets = [{"resources": [], "name": names[i]} for i in range(num_planets)]

for resource in resources:
    resource_planets = random.sample(planets, resource["num_planets"])
    for planet in resource_planets:
        planet["resources"].append(resource)

for planet in planets:
    resource_names = [res["material"] for res in planet["resources"]]
    print("<%s> resources: %s" % (planet["name"], ", ".join(resource_names)))