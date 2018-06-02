package us.lavaha.dune;

import com.google.gson.Gson;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.json.simple.JSONArray;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

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

    public Spaceport findByContainedLocation(Location location) {
        for (Spaceport spaceport : spaceports) {
            if (spaceport.contains(location)) {
                return spaceport;
            }
        }

        return null;
    }

    public Spaceport findByChunk(Chunk chunk) {
        for (Spaceport spaceport : spaceports) {
            if (spaceport.getLocation().getChunk().equals(chunk)) {
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

    public Spaceport findByHouse(House house) {
        for (Spaceport spaceport : spaceports) {
            House spaceportHouse = spaceport.getHouse();
            if (spaceportHouse != null) {
                if (spaceportHouse.equals(house)) {
                    return spaceport;
                }
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

    public void init() {
    }

    public void save(Path path) {
        String json = Dune.get().getGson().toJson(spaceports.toArray());
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
                spaceports = new ArrayList<Spaceport>(Arrays.asList(Dune.get().getGson().fromJson(json, Spaceport[].class)));
            } catch (Exception e) {
                Dune.get().getLogger().log(Level.SEVERE, e.toString());
            }
        }
    }
}
