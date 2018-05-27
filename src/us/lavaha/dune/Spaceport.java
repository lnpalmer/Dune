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
    public Spaceport(Location location) {
        this.location = location;
        this.menu = new SpaceportMenu(this);
    }

    public static SpaceportSession connect(Spaceport spaceport, Player player) {
        SpaceportSession spaceportSession = new SpaceportSession(spaceport, player);
        SpaceportSessionColl.get().add(spaceportSession);
        spaceportSession.init();
        return spaceportSession;
    }

    public static void disconnect(Player player) {
        player.closeInventory();
        SpaceportSession spaceportSession = SpaceportSessionColl.get().findByPlayer(player);
        spaceportSession.term();
        SpaceportSessionColl.get().remove(spaceportSession);
    }

    public Location getLocation() {
        return location;
    }

    public SpaceportMenu getMenu() {
        return menu;
    }

    private SpaceportMenu menu;
    private Location location;
}
