package us.lavaha.dune;

        import java.util.ArrayList;
        import java.util.List;

public class PlanetColl {
    public PlanetColl() {
        this.planets = new ArrayList<Planet>();
    }

    public Planet findById(String id) {
        for (Planet planet : planets) {
            if (planet.getId().equals(id)) {
                return planet;
            }
        }

        return null;
    }

    public PlanetColl get() {
        return instance;
    }

    private static PlanetColl instance = new PlanetColl();
    private List<Planet> planets;
}
