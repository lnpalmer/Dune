package us.lavaha.dune;

import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class HouseListener implements Listener {
    @EventHandler
    public void onFactionsCreate(EventFactionsCreate event) {
        String id = event.getFactionId();
        House house = new House(id);
        HouseColl.get().add(house);
    }

    @EventHandler
    public void onFactionsDisband(EventFactionsDisband event) {
        String id = event.getFactionId();
        House house = HouseColl.get().findById(id);
        HouseColl.get().remove(house);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(EventFactionsChunksChange event) {
        if (event.isCancelled()) return;

        House claimingHouse = HouseColl.get().findByFaction(event.getNewFaction());
        for (PS ps : event.getChunks()) {
            Spaceport spaceport = SpaceportColl.get().findByChunk(ps.asBukkitChunk());
            if (spaceport != null) {
                // a spaceport has been taken!

                if (claimingHouse != null) {
                    if (claimingHouse.isMajor()) {
                        event.getMPlayer().getPlayer().sendMessage("A house can only own one spaceport!");
                        event.setCancelled(true);
                    }
                }

                if (!event.isCancelled()) {
                    House cedingHouse = HouseColl.get().findByFaction(event.getOldChunkFaction().get(ps));

                    if (claimingHouse != null && cedingHouse == null) {
                        Dune.get().getServer().broadcastMessage("The " + claimingHouse.getName() + " have made " + ps.getWorld() + " their capital!");
                    }
                    if (claimingHouse != null && cedingHouse != null) {
                        Dune.get().getServer().broadcastMessage("The " + claimingHouse.getName() + " have taken " + ps.getWorld() + " from the " + cedingHouse.getName() + "!");
                    }
                    if (claimingHouse == null && cedingHouse != null) {
                        Dune.get().getServer().broadcastMessage("The " + cedingHouse.getName() + " have lost " + ps.getWorld() + " to the wild!");
                    }
                }
            }
        }
    }
}
