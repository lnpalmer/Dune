package us.lavaha.dune;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class SpaceportSession {
    public SpaceportSession(Spaceport spaceport, Player player) {
        this.spaceport = spaceport;
        this.player = player;
        this.travelmode = TRAVELMODE.NOT_TRAVELING;
        this.destination = "";
        this.ticks = 0;
    }

    public void init() {
        this.setSubmenu(SpaceportMenu.SUBMENU.MAIN);
    }

    public void term() {
    }

    public void tick() {
        if (travelmode.equals(TRAVELMODE.NOT_TRAVELING)) {
            if (ticks % 20 == 0) {
                spaceport.getMenu().show(this);
            }
        }

        if (travelmode.equals(TRAVELMODE.ASCENDING)) {
            Location playerLocation = player.getLocation();
            playerLocation.setDirection(new Vector(0.0f, 1.0f, 0.0f));
            player.setVelocity(new Vector(0.0f, 25.0f, 0.0f));

            if (playerLocation.getY() > 200.0f) {
                Spaceport.disconnect(player);

                Spaceport destinationSpaceport = SpaceportColl.get().findByWorldName(destination);
                Location incomingLocation = destinationSpaceport.getLocation().clone();
                incomingLocation.setX(incomingLocation.getX() + 0.5f + 1.0f);
                incomingLocation.setY(incomingLocation.getY() + 1.0f);
                incomingLocation.setZ(incomingLocation.getZ() + 0.5f);
                incomingLocation.setDirection(new Vector(1.0f, 0.0f, 0.0f));
                player.teleport(incomingLocation);
                player.setVelocity(new Vector(0.0f, 0.0f, 0.0f));
                player.setFallDistance(0.0f);
            }
        }
        ticks ++;
    }

    public void depart(String destination) {
        this.destination = destination;
        this.travelmode = TRAVELMODE.ASCENDING;
        Location liftoffLocation = spaceport.getLocation().clone();
        liftoffLocation.setX(liftoffLocation.getX() + 0.5f);
        liftoffLocation.setY(liftoffLocation.getY() + 1.0f);
        liftoffLocation.setZ(liftoffLocation.getZ() + 0.5f);
        liftoffLocation.setDirection(new Vector(0.0f, 1.0f, 0.0f));
        player.teleport(liftoffLocation);
    }

    public Spaceport getSpaceport() {
        return spaceport;
    }

    public SpaceportMenu.SUBMENU getSubmenu() {
        return submenu;
    }

    public void setSubmenu(SpaceportMenu.SUBMENU submenu) {
        this.submenu = submenu;
    }

    public Player getPlayer() {
        return player;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        this.spaceport.getMenu().onInventoryClick(event, this);
    }

    public void setTravelmode(TRAVELMODE travelmode) {
        this.travelmode = travelmode;
    }

    enum TRAVELMODE {
        NOT_TRAVELING,
        ASCENDING,
        DESCENDING
    }

    private Spaceport spaceport;
    private Player player;
    private SpaceportMenu.SUBMENU submenu;
    private SpaceportSession.TRAVELMODE travelmode;
    private String destination;
    private int ticks;
}
