package us.lavaha.dune;

import com.google.gson.annotations.JsonAdapter;
import net.minecraft.server.v1_12_R1.BlockStainedGlass;
import net.minecraft.server.v1_12_R1.EnumColor;
import net.minecraft.server.v1_12_R1.MaterialDecoration;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;

@JsonAdapter(SpaceportJsonAdapter.class)
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
            glassLocation.getBlock().setData(DyeColor.MAGENTA.getDyeData());
        }
    }

    public Location getLocation() {
        return location;
    }

    public SpaceportMenu getMenu() {
        return menu;
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

    private SpaceportMenu menu;

    private Location location;
}