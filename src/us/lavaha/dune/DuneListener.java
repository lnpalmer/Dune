package us.lavaha.dune;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DuneListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock().getType() == Material.BEACON) {
            Location location = event.getClickedBlock().getLocation();
            Spaceport spaceport = Spaceport.findByLocation(location);

            if (spaceport != null) {
                event.setCancelled(true);
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    spaceport.connect(player);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Spaceport spaceport = Spaceport.findByClient(player);

        if (spaceport != null) {
            ItemStack clicked = event.getCurrentItem();
            Inventory inventory = event.getClickedInventory();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Spaceport spaceport = Spaceport.findByClient(player);

        if (spaceport != null) {
            spaceport.disconnect(player);
        }
    }
}
