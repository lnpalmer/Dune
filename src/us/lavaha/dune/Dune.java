package us.lavaha.dune;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Dune extends JavaPlugin {
    @Override
    public void onEnable() {
        instance = this;

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
    }

    @Override
    public void onDisable() {
    }

    public static Dune get() {
        return instance;
    }

    private static Dune instance;
}