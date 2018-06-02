package us.lavaha.dune;

import com.earth2me.essentials.api.Economy;
import com.google.gson.annotations.JsonAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.logging.Level;

@JsonAdapter(SmugportJsonAdapter.class)
public class Smugport {

    public Smugport(Location location) {
        this.location = location;
        this.destination = "";
        this.travelFee = new BigDecimal(0);
        this.balance = new BigDecimal(0);
        this.menu = new SmugportMenu(this);
    }

    public boolean transmit(Player player) {
        try {
            Smugport target = this.getTarget();
            if (target != null) {
                BigDecimal departureFee = travelFee;
                BigDecimal arrivalFee = target.travelFee;
                BigDecimal totalFee = departureFee.add(arrivalFee);

                if (Economy.hasEnough(player.getName(), totalFee)) {
                    Economy.substract(player.getName(), totalFee);
                    this.deposit(departureFee);
                    target.deposit(arrivalFee);
                    player.sendMessage("Paid $" + totalFee + " to travel to " + destination + "...");

                    Location targetLocation = target.getLocation().clone();
                    targetLocation.add(1.5, 1.0, 0.5);
                    targetLocation.setDirection(new Vector(1.0, 0.0, 0.0));
                    player.teleport(targetLocation);
                } else {
                    player.sendMessage("You can't afford to travel to " + destination + "! The price is $" + totalFee + ".");
                    return false;
                }
            } else {
                player.sendMessage("There isn't a port set up to receive you. Get one built on " + destination + "!");
                return false;
            }
        } catch (Exception e) {
            Dune.get().getLogger().log(Level.SEVERE, e.toString());
        }
        return false;
    }

    public boolean contains(Location otherLocation) {
        if (otherLocation.getWorld().getName().equals(location.getWorld().getName())) {
            Location d = location.clone().subtract(otherLocation);
            return (d.getX() <= 1.0 && d.getX() >= -1.0 &&
                    d.getY() <= 1.0 && d.getY() >= -1.0 &&
                    d.getZ() <= 1.0 && d.getZ() >= -1.0);
        }

        return false;
    }

    public Smugport getTarget() {
        return SmugportColl.get().endOfPath(location.getWorld().getName(), destination);
    }

    public static boolean canCreate(Location location) {
        return true;
    }

    public void build() {
        location.getBlock().setType(Material.STAINED_GLASS);
        location.getBlock().setData((byte) 15);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Location supportLocation = location.clone().add(dx, -1.0, dz);
                if (dx == 0 && dz == 0) {
                    supportLocation.getBlock().setType(Material.ENDER_PORTAL);
                } else {
                    supportLocation.getBlock().setType(Material.ENDER_PORTAL_FRAME);
                }

                Location pillarLocation = location.clone().add(dx, 0.0, dz);
                if (dx != 0 && dz != 0) {
                    pillarLocation.getBlock().setType(Material.STAINED_GLASS_PANE);
                    pillarLocation.getBlock().setData((byte) 15);
                }
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean canReach(String worldName) {
        String ownWorldName = this.location.getWorld().getName();
        if (worldName.equals(ownWorldName)) return false;

        // two smugports on the same planet can't point to the same destination
        if (SmugportColl.get().startOfPath(ownWorldName, worldName) != null) return false;

        return true;
    }

    public SmugportMenu getMenu() {
        return menu;
    }

    public BigDecimal getTravelFee() {
        return travelFee;
    }

    public void setTravelFee(BigDecimal travelFee) {
        this.travelFee = travelFee;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public BigDecimal withdrawAll() {
        BigDecimal all = this.balance;
        this.balance = new BigDecimal(0);
        return all;
    }

    public static final BigDecimal price = new BigDecimal(80000);

    private Location location;
    private String destination;
    private BigDecimal travelFee;
    private BigDecimal balance;

    private SmugportMenu menu;

}
