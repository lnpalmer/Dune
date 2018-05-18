package us.lavaha.dune;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SpaceportMenu {

    private Spaceport spaceport;
    private Inventory inventory;

    public SpaceportMenu(Spaceport spaceport) {
        this.spaceport = spaceport;
        this.inventory = Bukkit.createInventory(null, 54, "Spaceport");

        this.inventory.setItem(48, new ItemStack(Material.BEACON, 1));
        this.inventory.setItem(49, new ItemStack(Material.EYE_OF_ENDER, 1));
        this.inventory.setItem(50, new ItemStack(Material.GOLD_INGOT, 1));

        ItemStack travelStack = this.inventory.getItem(48);
        ItemMeta travelMeta = travelStack.getItemMeta();
        travelMeta.setDisplayName("Travel");
        travelMeta.setLore(Arrays.asList(
                "Travel to other planets"
        ));
        travelStack.setItemMeta(travelMeta);
        this.inventory.setItem(48, travelStack);
    }

    public void showTo(Player player) {
        player.openInventory(this.inventory);
    }

}
