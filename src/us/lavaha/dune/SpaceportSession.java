package us.lavaha.dune;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class SpaceportSession {

    private Spaceport spaceport;
    private Player player;
    private SpaceportMenu.SUBMENU submenu;
    private boolean expectingClose;

    public SpaceportSession(Spaceport spaceport, Player player) {
        this.spaceport = spaceport;
        this.player = player;
        this.expectingClose = false;
    }

    public void init() {
        this.setSubmenu(SpaceportMenu.SUBMENU.MAIN);
        this.spaceport.getMenu().show(this);
    }

    public void term() {

    }

    public Spaceport getSpaceport() {
        return spaceport;
    }

    public SpaceportMenu.SUBMENU getSubmenu() {
        return submenu;
    }

    public void setSubmenu(SpaceportMenu.SUBMENU submenu) {
        this.submenu = submenu;
        this.spaceport.getMenu().show(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        this.spaceport.getMenu().onInventoryClick(event, this);
    }

    public void onInventoryClose(InventoryCloseEvent event) {
        this.spaceport.getMenu().onInventoryClose(event, this);
    }

    public void setExpectingClose(boolean expectingClose) {
        this.expectingClose = expectingClose;
    }

    public boolean isExpectingClose() {
        return expectingClose;
    }
}
