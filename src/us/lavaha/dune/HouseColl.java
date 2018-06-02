package us.lavaha.dune;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class HouseColl {
    HouseColl() {
        this.houses = new ArrayList<House>();
    }

    public House findById(String id) {
        for (House house : houses) {
            if (house.getId().equals(id)) {
                return house;
            }
        }

        return null;
    }

    public House findByLocation(Location location) {
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(location));
        String houseId = faction.getId();
        return findById(houseId);
    }

    public void add(House house) {
        houses.add(house);
    }

    public void remove(House house) {
        houses.remove(house);
    }

    public void save(Path path) {
        String json = Dune.get().getGson().toJson(houses.toArray());
        try {
            Files.write(path, json.getBytes());
        } catch (Exception e) {
            Dune.get().getLogger().log(Level.SEVERE, e.toString());
        }
    }

    public void init() {
    }

    public void load(Path path) {
        if (Files.exists(path)) {
            try {
                String json = new String(Files.readAllBytes(path));
                houses = new ArrayList<House>(Arrays.asList(Dune.get().getGson().fromJson(json, House[].class)));
            } catch (Exception e) {
                Dune.get().getLogger().log(Level.SEVERE, e.toString());
            }
        }
    }

    public static HouseColl get() {
        return HouseColl.instance;
    }

    private static HouseColl instance = new HouseColl();
    private List<House> houses;
}
