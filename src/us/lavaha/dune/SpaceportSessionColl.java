package us.lavaha.dune;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpaceportSessionColl {
    public SpaceportSessionColl() {
        this.spaceportSessions = new ArrayList<SpaceportSession>();
    }

    public SpaceportSession findByPlayer(Player player) {
        for (SpaceportSession spaceportSession : spaceportSessions) {
            if (spaceportSession.getPlayer().equals(player)) {
                return spaceportSession;
            }
        }

        return null;
    }

    void add(SpaceportSession spaceportSession) {
        this.spaceportSessions.add(spaceportSession);
    }

    void remove(SpaceportSession spaceportSession) {
        this.spaceportSessions.remove(spaceportSession);
    }

    public ArrayList<SpaceportSession> getSpaceportSessions() {
        return spaceportSessions;
    }

    public static SpaceportSessionColl get() {
        return instance;
    }

    private static SpaceportSessionColl instance = new SpaceportSessionColl();
    private ArrayList<SpaceportSession> spaceportSessions;
}
