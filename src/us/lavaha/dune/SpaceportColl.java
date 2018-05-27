package us.lavaha.dune;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class SpaceportColl {
    public SpaceportColl() {
        this.spaceports = new ArrayList<Spaceport>();
    }

    public Spaceport findByLocation(Location location) {
        for (Spaceport spaceport : spaceports) {
            if (spaceport.getLocation().equals(location)) {
                return spaceport;
            }
        }

        return null;
    }

    public Spaceport findByWorldName(String worldName) {
        for (Spaceport spaceport : spaceports) {
            if (spaceport.getLocation().getWorld().getName().equals(worldName)) {
                return spaceport;
            }
        }

        return null;
    }

    public void add(Spaceport spaceport) {
        this.spaceports.add(spaceport);
    }

    public void remove(Spaceport spaceport) {
        this.spaceports.remove(spaceport);
    }

    public static SpaceportColl get() {
        return instance;
    }

    private static SpaceportColl instance = new SpaceportColl();
    private List<Spaceport> spaceports;
}