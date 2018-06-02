package us.lavaha.dune;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class SmugportListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location location = event.getClickedBlock().getLocation();
        List<Smugport> smugports = SmugportColl.get().findByWorldName(location.getWorld().getName());

        for (Smugport smugport : smugports) {

            Location locDiff = location.clone().subtract(smugport.getLocation());
            if (
                    locDiff.getX() >= -1 && locDiff.getX() <= 1 &&
                            locDiff.getY() >= -1 && locDiff.getY() <= 1 &&
                            locDiff.getZ() >= -1 && locDiff.getZ() <= 1) {
                event.setCancelled(true);
                if (event.getHand().equals(EquipmentSlot.HAND) &&
                    event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (smugport.getDestination().equals("")) {
                        smugport.getMenu().show(player);
                    } else {
                        if (player.isSneaking()) {
                            Smugport destinationSmugport = smugport.getTarget();
                            player.sendMessage("This port will take you to " + smugport.getDestination() + ".");
                            player.sendMessage("Coffers: $" + smugport.getBalance());
                            player.sendMessage("Departure fee: $" + smugport.getTravelFee());
                            player.sendMessage("Arrival fee: $" + destinationSmugport.getTravelFee());
                            player.sendMessage("Total fee: $" + smugport.getTravelFee().add(destinationSmugport.getTravelFee()));
                        } else {
                            smugport.transmit(player);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        List<Smugport> smugports = SmugportColl.get().findByWorldName(player.getWorld().getName());
        for (Smugport smugport : smugports) {
            if (event.getInventory().equals(smugport.getMenu().getMainInventory())) {
                smugport.getMenu().onInventoryClick(event);
                event.setCancelled(true);
                return;
            }
        }
    }
}
