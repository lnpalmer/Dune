package us.lavaha.dune;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
        import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
        import org.bukkit.event.inventory.InventoryClickEvent;
        import org.bukkit.event.inventory.InventoryCloseEvent;
        import org.bukkit.inventory.Inventory;
        import org.bukkit.inventory.ItemStack;
        import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.Arrays;
        import java.util.List;
import java.util.logging.Level;

public class SpaceportMenu {

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

        this.travelInventory = Bukkit.createInventory(null, 54, "Travel");

        this.tradeInventory = Bukkit.createInventory(null, 54, "Trade");
        this.tradeInventory.setItem(45, this.genBackStack());
    }

    public void tickSession(SpaceportSession session) {
        Player player = session.getPlayer();

        if (session.getTravelmode() == SpaceportSession.TRAVELMODE.NOT_TRAVELING) {
            boolean isPlayerWatching = false;
            List<HumanEntity> viewers = getSubmenuInventory(session.getSubmenu()).getViewers();
            for (HumanEntity viewer : viewers) {
                if (viewer instanceof Player) {
                    if (((Player) viewer).equals(player)) {
                        isPlayerWatching = true;
                    }
                }
            }

            if (!isPlayerWatching) {
                this.show(session);
            }
        }
    }

    public void show(SpaceportSession session) {
        this.refresh();

        Player player = session.getPlayer();
        Inventory targetInventory = this.getSubmenuInventory(session.getSubmenu());
        player.openInventory(targetInventory);
    }

    private void refresh() {
        ItemStack aboutStack = this.mainInventory.getItem(49);
        ItemMeta aboutMeta = aboutStack.getItemMeta();
        House spaceportOwner = this.spaceport.getHouse();

        String governanceString = "";
        if (spaceportOwner != null) governanceString = "Governed by house " + spaceportOwner.getName();
        else governanceString = "This planet is unclaimed!";
        aboutMeta.setLore(Arrays.asList(
                governanceString,
                "Travel fee: $" + this.spaceport.getTravelFee(),
                "Coffers: $" + this.spaceport.getBalance()
        ));

        aboutStack.setItemMeta(aboutMeta);

        // update travel menu
        travelInventory.clear();
        int slot = 0;
        for (World world : Dune.get().getServer().getWorlds()) {
            String worldName = world.getName();

            if (this.spaceport.canReach(worldName)) {
                BigDecimal departureFee = this.spaceport.getTravelFee();
                BigDecimal arrivalFee = SpaceportColl.get().findByWorldName(worldName).getTravelFee();
                BigDecimal totalFee = departureFee.add(arrivalFee);

                ItemStack destinationStack = new ItemStack(Material.FIREBALL, 1);
                ItemMeta destinationMeta = destinationStack.getItemMeta();
                destinationMeta.setDisplayName(worldName);
                destinationMeta.setLore(Arrays.asList(
                        "Departure fee: $" + departureFee,
                        "Arrival fee: $" + arrivalFee,
                        "Total travel fee: $" + totalFee
                ));
                destinationStack.setItemMeta(destinationMeta);
                this.travelInventory.setItem(slot, destinationStack);

                slot++;
            }
        }
        this.travelInventory.setItem(45, this.genBackStack());
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
                player.closeInventory();
            }
        }

        if (inventory.equals(this.travelInventory)) {
            if (slot == 45) {
                session.setSubmenu(SUBMENU.MAIN);
            } else if (itemStack.getType() != Material.AIR) {
                String destPlanet = itemMeta.getDisplayName();
                if (session.depart(destPlanet)) {
                    player.closeInventory();
                }
            }
        }

        if (inventory.equals(this.tradeInventory)) {
            if (slot == 45) {
                session.setSubmenu(SUBMENU.MAIN);
            }
        }
    }

    public enum SUBMENU {
        MAIN, TRAVEL, TRADE
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

    private Spaceport spaceport;

    private Inventory mainInventory;
    private Inventory travelInventory;
    private Inventory tradeInventory;

    private ItemStack travelStack;
    private ItemStack aboutStack;
    private ItemStack tradeStack;
}
