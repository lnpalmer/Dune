package us.lavaha.dune;

import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;
import org.bukkit.event.EventHandler;
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
}
