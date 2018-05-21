package us.lavaha.dune;

import org.bukkit.Bukkit;
        import org.bukkit.Material;
        import org.bukkit.World;
        import org.bukkit.entity.Item;
        import org.bukkit.entity.Player;
        import org.bukkit.event.inventory.InventoryClickEvent;
        import org.bukkit.event.inventory.InventoryCloseEvent;
        import org.bukkit.inventory.Inventory;
        import org.bukkit.inventory.ItemStack;
        import org.bukkit.inventory.meta.ItemMeta;

        import java.util.Arrays;
        import java.util.List;

public class SpaceportMenu {

    private Spaceport spaceport;
    private Inventory mainInventory;
    private Inventory travelInventory;
    private Inventory tradeInventory;

    private ItemStack travelStack;
    private ItemStack aboutStack;
    private ItemStack tradeStack;

    public SpaceportMenu(Spaceport spaceport) {
        this.spaceport = spaceport;
        this.mainInventory = Bukkit.createInventory(null, 54, "Spaceport");

        travelStack = new ItemStack(Material.BEACON, 1);
        ItemMeta travelMeta = travelStack.getItemMeta();
        travelMeta.setDisplayName("Travel");
        travelMeta.setLore(Arrays.asList(
                "Travel to other planets"
        ));
        travelStack.setItemMeta(travelMeta);
        this.mainInventory.setItem(48, travelStack);

        aboutStack = new ItemStack(Material.FIREBALL, 1);
        ItemMeta aboutMeta = aboutStack.getItemMeta();
        aboutMeta.setDisplayName(this.spaceport.getLocation().getWorld().getName());
        aboutMeta.setLore(Arrays.asList(
                "Informally known as Dune"
        ));
        aboutStack.setItemMeta(aboutMeta);
        this.mainInventory.setItem(49, aboutStack);

        tradeStack = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta tradeMeta = tradeStack.getItemMeta();
        tradeMeta.setDisplayName("Trade");
        tradeMeta.setLore(Arrays.asList(
                "Money can be exchanged",
                "for goods and services"
        ));
        tradeStack.setItemMeta(tradeMeta);
        this.mainInventory.setItem(50, tradeStack);

        this.mainInventory.setItem(45, this.genBackStack());

        this.travelInventory = this.genTravelInventory();

        this.tradeInventory = Bukkit.createInventory(null, 54, "Trade");
        this.tradeInventory.setItem(45, this.genBackStack());
    }

    public void showTo(Player player, SUBMENU submenu) {
        Inventory targetInventory = this.getSubmenuInventory(submenu);
        if (!targetInventory.equals(player.getOpenInventory())) {
            player.openInventory(targetInventory);
        }
    }

    public void onInventoryClick(InventoryClickEvent event, SpaceportSession session) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack itemStack = event.getCurrentItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        int slot = event.getSlot();

        if (inventory.equals(this.mainInventory)) {
            if (itemStack.equals(this.travelStack)) {
                session.setSubmenu(SUBMENU.TRAVEL);
            }
            if (itemStack.equals(this.tradeStack)) {
                session.setSubmenu(SUBMENU.TRADE);
            }
            if (slot == 45) {
                Spaceport.disconnect(player);
            }
        }

        if (inventory.equals(this.travelInventory)) {
            if (slot == 45) {
                session.setSubmenu(SUBMENU.MAIN);
            } else if (itemStack.getType() != Material.AIR) {
                String destPlanet = itemMeta.getDisplayName();
                player.sendMessage("Travelling to " + destPlanet + "...");
            }
        }
    }

    public enum SUBMENU {
        MAIN, TRAVEL, TRADE
    }

    private Inventory genTravelInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Travel");

        List<World> worlds = Dune.getInstance().getServer().getWorlds();
        World ownWorld = this.spaceport.getLocation().getWorld();
        int slot = 0;
        for (World world : worlds) {
            if (!world.equals(ownWorld)) {
                ItemStack worldStack = new ItemStack(Material.FIREBALL, 1);
                ItemMeta worldMeta = worldStack.getItemMeta();
                worldMeta.setDisplayName(world.getName());
                worldStack.setItemMeta(worldMeta);

                inventory.setItem(slot, worldStack);

                slot++;
            }
        }

        inventory.setItem(45, this.genBackStack());

        return inventory;
    }

    private ItemStack genBackStack() {
        ItemStack itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Back");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private Inventory getSubmenuInventory(SUBMENU submenu) {
        switch (submenu) {
            case MAIN:
                return this.mainInventory;

            case TRAVEL:
                return this.travelInventory;

            case TRADE:
                return this.tradeInventory;

            default:
                return null;
        }
    }
}
