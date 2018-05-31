package us.lavaha.dune;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class Dune extends JavaPlugin {
    @Override
    public void onEnable() {
        instance = this;

        // IO tools
        try {
            Path dataPath = Paths.get(this.getDataFolder().getPath());
            if(!Files.exists(dataPath)) {
                Files.createDirectory(Paths.get(this.getDataFolder().getPath()));
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.toString());
        }
        this.gson = new Gson();

        this.getCommand("dune").setExecutor(new CommandDune());
        this.getServer().getPluginManager().registerEvents(new SpaceportListener(), this);
        this.getServer().getPluginManager().registerEvents(new HouseListener(), this);

        GameTickEvent gameTickEvent = new GameTickEvent();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(gameTickEvent);
            }
        }, 1, 1);

        SpaceportColl.get().load(Paths.get(this.getDataFolder().getPath(), "spaceports.json"));
        SpaceportColl.get().init();
    }

    @Override
    public void onDisable() {
        SpaceportColl.get().save(Paths.get(this.getDataFolder().getPath(), "spaceports.json"));
        getLogger().log(Level.SEVERE, Paths.get(this.getDataFolder().getPath(), "spaceports.json").toAbsolutePath().toString());
    }

    public Gson getGson() {
        return gson;
    }

    public static Dune get() {
        return instance;
    }

    private static Dune instance;

    private Gson gson;
}