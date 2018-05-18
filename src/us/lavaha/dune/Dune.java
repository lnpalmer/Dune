package us.lavaha.dune;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Dune extends JavaPlugin {

    private Logger log;

    @Override
    public void onEnable() {

        log = getLogger();

        log.info("Setting up...");

    }

    @Override
    public void onDisable() {
    }
}