package us.lavaha.dune;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Location;

import java.io.IOException;
import java.math.BigDecimal;

public class SmugportJsonAdapter extends TypeAdapter<Smugport> {
    @Override public void write(JsonWriter out, Smugport smugport) throws IOException {
        out.beginObject();

        // write location
        out.name("location");
        out.beginObject();

        Location location = smugport.getLocation();
        out.name("world");
        out.value(location.getWorld().getName());

        out.name("x");
        out.value(location.getX());

        out.name("y");
        out.value(location.getY());

        out.name("z");
        out.value(location.getZ());

        out.name("yaw");
        out.value(location.getYaw());

        out.name("pitch");
        out.value(location.getPitch());

        out.endObject();

        out.name("destination");
        out.value(smugport.getDestination());

        out.name("travelFee");
        Dune.get().getGson().toJson(smugport.getTravelFee(), BigDecimal.class, out);

        out.name("balance");
        Dune.get().getGson().toJson(smugport.getBalance(), BigDecimal.class, out);

        out.endObject();
    }

    @Override public Smugport read(JsonReader in) throws IOException {
        Location location = null;
        String destination = "";
        BigDecimal travelFee = new BigDecimal(0);
        BigDecimal balance = new BigDecimal(0);

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();

            if (name.equals("location")) {

                String world = "";
                double x = 0.0, y = 0.0, z = 0.0;
                float yaw = 0.0f, pitch = 0.0f;

                in.beginObject();
                while (in.hasNext()) {
                    String fieldName = in.nextName();
                    if (fieldName.equals("world")) {
                        world = in.nextString();
                    } else if (fieldName.equals("x")) {
                        x = in.nextDouble();
                    } else if (fieldName.equals("y")) {
                        y = in.nextDouble();
                    } else if (fieldName.equals("z")) {
                        z = in.nextDouble();
                    } else if (fieldName.equals("yaw")) {
                        yaw = (float) in.nextDouble();
                    } else if (fieldName.equals("pitch")) {
                        pitch = (float) in.nextDouble();
                    }
                }
                in.endObject();

                location = new Location(Dune.get().getServer().getWorld(world), x, y, z, yaw, pitch);

            }

            if (name.equals("destination")) {
                destination = in.nextString();
            }

            if (name.equals("travelFee")) {
                travelFee = Dune.get().getGson().fromJson(in, BigDecimal.class);
            }

            if (name.equals("balance")) {
                balance = Dune.get().getGson().fromJson(in, BigDecimal.class);
            }
        }
        in.endObject();

        Smugport smugport = new Smugport(location);
        smugport.setDestination(destination);
        smugport.setTravelFee(travelFee);
        smugport.setBalance(balance);
        return smugport;
    }
}
