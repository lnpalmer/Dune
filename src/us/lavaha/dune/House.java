package us.lavaha.dune;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class House {
    public House(String id) {
        this.id = id;
    }

    public Faction getFaction() {
        return FactionColl.get().get(this.id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return getFaction().getName();
    }

    public Spaceport getSpaceport() {
        return SpaceportColl.get().findByHouse(this);
    }

    public String getCapitalName() {
        Spaceport spaceport = this.getSpaceport();
        if (spaceport != null) {
            return spaceport.getLocation().getWorld().getName();
        }

        return "";
    }

    public boolean isMajor() {
        return this.getSpaceport() != null;
    }

    // this is sketchy
    public List<Player> getPlayers() {
        Faction faction = this.getFaction();
        List<MPlayer> mPlayers = faction.getMPlayers();
        ArrayList<Player> players = new ArrayList<Player>();
        for (MPlayer mPlayer : mPlayers) {
            players.add(mPlayer.getPlayer());
        }
        return players;
    }

    private String id;
}
