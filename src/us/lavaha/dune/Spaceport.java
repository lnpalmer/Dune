package us.lavaha.dune;

import com.google.gson.annotations.JsonAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

@JsonAdapter(SpaceportJsonAdapter.class)
public class Spaceport {
    public Spaceport(Location location) {
        this.location = location;
        this.menu = new SpaceportMenu(this);
        this.travelFee = new BigDecimal(0);
        this.balance = new BigDecimal(0);
    }

    public static boolean canCreate(Location location) {
        Spaceport worldHolder = SpaceportColl.get().findByWorldName(location.getWorld().getName());
        return worldHolder == null;
    }

    public static SpaceportSession connect(Spaceport spaceport, Player player) {
        if (SpaceportSessionColl.get().findByPlayer(player) != null) {
            return null;
        }

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

    public void build() {
        location.getBlock().setType(Material.BEACON);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Location supportLocation = location.clone().add(dx, -1.0, dz);
                supportLocation.getBlock().setType(Material.DIAMOND_BLOCK);
            }
        }

        for (Location glassLocation : this.glassLocations()) {
            glassLocation.getBlock().setType(Material.STAINED_GLASS);
            glassLocation.getBlock().setData((byte) 4);
        }
    }

    public House getHouse() {
        return HouseColl.get().findByLocation(location);
    }

    public Location getLocation() {
        return location;
    }

    public SpaceportMenu getMenu() {
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

    private Location[] glassLocations() {
        Location[] locations = new Location[5];

        locations[0] = location.clone().add(0, 1, 0);
        locations[1] = location.clone().add(1, 0, 1);
        locations[2] = location.clone().add(1, 0, -1);
        locations[3] = location.clone().add(-1, 0, 1);
        locations[4] = location.clone().add(-1, 0, -1);

        return locations;
    }

    public boolean canReach(String worldName) {
        // can't travel to own world
        if (worldName.equals(location.getWorld().getName())) return false;

        // spaceports can reach any other planet with a spaceport
        Spaceport target = SpaceportColl.get().findByWorldName(worldName);
        if (target != null) return true;

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

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public BigDecimal withdrawAll() {
        BigDecimal all = this.balance;
        this.balance = new BigDecimal(0);
        return all;
    }

    public static final BigDecimal price = new BigDecimal(10000000);

    private SpaceportMenu menu;

    private Location location;

    private BigDecimal travelFee;

    private BigDecimal balance;
}