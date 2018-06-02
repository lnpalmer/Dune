package us.lavaha.dune;

import org.bukkit.Location;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class SmugportColl {
    public SmugportColl() {
        this.smugports = new ArrayList<Smugport>();
    }

    public Smugport findByLocation(Location location) {
        for (Smugport smugport : smugports) {
            if (smugport.getLocation().equals(location)) {
                return smugport;
            }
        }

        return null;
    }

    public Smugport findByContainedLocation(Location location) {
        for (Smugport smugport : smugports) {
            if (smugport.contains(location)) {
                return smugport;
            }
        }

        return null;
    }

    public List<Smugport> findByWorldName(String worldName) {
        List<Smugport> found = new ArrayList<Smugport>();

        for (Smugport smugport : smugports) {
            if (smugport.getLocation().getWorld().getName().equals(worldName)) {
                found.add(smugport);
            }
        }

        return found;
    }

    public Smugport startOfPath(String fromWorldName, String toWorldName) {
        for (Smugport smugport : smugports) {
            if (smugport.getLocation().getWorld().getName().equals(fromWorldName) &&
                    smugport.getDestination().equals(toWorldName)) {
                return smugport;
            }
        }

        return null;
    }

    public Smugport endOfPath(String fromWorldName, String toWorldName) {
        for (Smugport smugport : smugports) {
            if (smugport.getLocation().getWorld().getName().equals(toWorldName) &&
                smugport.getDestination().equals(fromWorldName)) {
                return smugport;
            }
        }

        return null;
    }

    public void add(Smugport smugport) {
        this.smugports.add(smugport);
    }

    public void remove(Smugport smugport) {
        this.smugports.remove(smugport);
    }

    public static SmugportColl get() {
        return instance;
    }

    public void init() {
    }

    public void save(Path path) {
        String json = Dune.get().getGson().toJson(smugports.toArray());
        try {
            Files.write(path, json.getBytes());
        } catch (Exception e) {
            Dune.get().getLogger().log(Level.SEVERE, e.toString());
        }
    }

    public void load(Path path) {
        if (Files.exists(path)) {
            try {
                String json = new String(Files.readAllBytes(path));
                smugports = new ArrayList<Smugport>(Arrays.asList(Dune.get().getGson().fromJson(json, Smugport[].class)));
            } catch (Exception e) {
                Dune.get().getLogger().log(Level.SEVERE, e.toString());
            }
        }
    }

    private static SmugportColl instance = new SmugportColl();

    private List<Smugport> smugports;
}
