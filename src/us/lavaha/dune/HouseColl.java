package us.lavaha.dune;

import java.util.ArrayList;
import java.util.List;

public class HouseColl {
    HouseColl() {
        this.houses = new ArrayList<House>();
    }

    public House findById(String id) {
        for (House house : houses) {
            if (house.getId().equals(id)) {
                return house;
            }
        }

        return null;
    }

    public void add(House house) {
        houses.add(house);
    }

    public void remove(House house) {
        houses.remove(house);
    }

    public static HouseColl get() {
        return HouseColl.instance;
    }

    private static HouseColl instance = new HouseColl();
    private List<House> houses;
}
