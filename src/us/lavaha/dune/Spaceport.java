package us.lavaha.dune;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spaceport {

    private static List<Spaceport> spaceports;

    private static Map<Player, Spaceport> clients;

    private SpaceportMenu menu;

    private Location location;

    public Spaceport(Location location) {
        this.location = location;
        this.menu = new SpaceportMenu(this);
    }

    public void connect(Player player) {
        menu.showTo(player);
        clients.put(player, this);
    }

    public void disconnect(Player player) {
        clients.remove(player);
    }

    public static void register(Spaceport spaceport) {
        spaceports.add(spaceport);
    }

    public static void init() {
        spaceports = new ArrayList<Spaceport>();
        clients = new HashMap<Player, Spaceport>();
    }

    public static void term() {
    }

    public static Spaceport findByLocation(Location location) {
        for (Spaceport spaceport : spaceports) {
            if (spaceport.location.equals(location)) {
                return spaceport;
            }
        }

        return null;
    }

    public static Spaceport findByClient(Player player) {
        return clients.getOrDefault(player, null);
    }
}
