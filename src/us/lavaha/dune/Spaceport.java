package us.lavaha.dune;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spaceport {

    private static List<Spaceport> spaceports;

    private static Map<Player, SpaceportSession> sessions;

    private SpaceportMenu menu;

    private Location location;

    public Spaceport(Location location) {
        this.location = location;
        this.menu = new SpaceportMenu(this);
    }

    public static void connect(Spaceport spaceport, Player player) {
        SpaceportSession session = new SpaceportSession(spaceport, player);
        sessions.put(player, session);
        session.init();
    }

    public static void disconnect(Player player) {
        sessions.remove(player);
        player.closeInventory();
    }

    public Location getLocation() {
        return location;
    }

    public SpaceportMenu getMenu() {
        return menu;
    }

    public static void register(Spaceport spaceport) {
        spaceports.add(spaceport);
    }

    public static void init() {
        spaceports = new ArrayList<Spaceport>();
        sessions = new HashMap<Player, SpaceportSession>();
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

    public static SpaceportSession getPlayerSession(Player player) {
        return sessions.getOrDefault(player, null);
    }
}
