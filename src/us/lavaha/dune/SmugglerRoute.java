package us.lavaha.dune;

public class SmugglerRoute {

    private String fromId;
    private String toId;
    private float danger;

    public SmugglerRoute(String toId, String fromId, float danger) {
        this.fromId = fromId;
        this.toId = toId;
        this.danger = danger;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public float getDanger() {
        return danger;
    }
}
