package us.lavaha.dune;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Dune extends JavaPlugin {
    private static Dune instance;

    @Override
    public void onEnable() {

        instance = this;

        this.getCommand("dune").setExecutor(new CommandDune());
        this.getServer().getPluginManager().registerEvents(new DuneListener(), this);

        Spaceport.init();

    }

    @Override
    public void onDisable() {
        Spaceport.term();
    }

    public static Dune getInstance() {
        return instance;
    }
}