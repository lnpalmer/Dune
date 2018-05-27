package us.lavaha.dune;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;

public class House {
    public House(String id) {
        this.id = id;
        this.major = false;
    }

    public Faction getFaction() {
        return FactionColl.get().get(this.id);
    }

    public String getId() {
        return id;
    }

    private String id;
    private boolean major;
}
