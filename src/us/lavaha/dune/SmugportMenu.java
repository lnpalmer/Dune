package us.lavaha.dune;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SmugportMenu {

    public SmugportMenu(Smugport smugport) {
        this.smugport = smugport;
        this.mainInventory = Bukkit.createInventory(null, 54, "Destination");
    }

    private void refresh() {
        this.mainInventory.clear();

        int slot = 0;
        for (World world : Dune.get().getServer().getWorlds()) {
            String worldName = world.getName();

            if (this.smugport.canReach(worldName)) {
                ItemStack destinationStack = new ItemStack(Material.FIREBALL, 1);
                ItemMeta destinationMeta = destinationStack.getItemMeta();
                destinationMeta.setDisplayName(worldName);
                destinationStack.setItemMeta(destinationMeta);
                this.mainInventory.setItem(slot, destinationStack);
                slot++;
            }
        }
    }

    void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack clickedStack = event.getCurrentItem();
        String destination = clickedStack.getItemMeta().getDisplayName();
        if (Dune.get().getServer().getWorld(destination) != null) {
            this.smugport.setDestination(destination);
            player.closeInventory();
        }
    }

    void show(Player player) {
        this.refresh();
        player.openInventory(this.mainInventory);
    }

    public Inventory getMainInventory() {
        return mainInventory;
    }

    private Smugport smugport;
    private Inventory mainInventory;

}
