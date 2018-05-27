package us.lavaha.dune;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class SpaceportListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock().getType() == Material.BEACON) {
            Location location = event.getClickedBlock().getLocation();
            Spaceport spaceport = SpaceportColl.get().findByLocation(location);

            if (spaceport != null) {
                event.setCancelled(true);
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Spaceport.connect(spaceport, player);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        SpaceportSession session = SpaceportSessionColl.get().findByPlayer(player);

        if (session != null) {
            session.onInventoryClick(event);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        ArrayList<SpaceportSession> sessions = (ArrayList<SpaceportSession>) SpaceportSessionColl.get().getSpaceportSessions().clone();
        for(SpaceportSession spaceportSession : sessions) {
            spaceportSession.tick();
        }
    }
}
