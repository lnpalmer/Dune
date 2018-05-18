package us.lavaha.dune;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Dune extends JavaPlugin {

    private Logger log;

    @Override
    public void onEnable() {

        log = getLogger();

        this.getCommand("dune").setExecutor(new CommandDune());
        this.getServer().getPluginManager().registerEvents(new DuneListener(), this);

        Spaceport.init();

    }

    @Override
    public void onDisable() {
        Spaceport.term();
    }
}