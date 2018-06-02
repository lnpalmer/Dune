package us.lavaha.dune;

import com.earth2me.essentials.api.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.math.BigDecimal;
import java.util.logging.Level;

public class CommandDune implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            if (strings[0].equals("spaceport")) return onCommandSpaceport(sender, command, s, strings);
            if (strings[0].equals("smugport")) return onCommandSmugport(sender, command, s, strings);
        }

        return false;
    }

    private boolean onCommandSpaceport(CommandSender sender, Command command, String s, String[] strings) {
        try {
            if (strings.length >= 2) {
                if (strings[1].equals("create")) {
                    // try to create a spaceport
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Block rootBlock = playerGazeAir(player);
                        if (rootBlock != null) {
                            Location location = rootBlock.getLocation();
                            if (Spaceport.canCreate(location)) {
                                if (Economy.hasEnough(player.getName(), Spaceport.price)) {

                                    Economy.substract(player.getName(), Spaceport.price);

                                    Spaceport spaceport = new Spaceport(location);
                                    spaceport.build();
                                    SpaceportColl.get().add(spaceport);

                                } else {
                                    player.sendMessage("You can't afford a spaceport! The cost is $" + Spaceport.price + ".");
                                }
                            } else {
                                player.sendMessage("Can't create spaceport here!");
                            }
                        }
                    }
                    return true;
                }

                if (strings[1].equals("fee")) {

                    if (strings.length >= 3) {

                        if (strings[2].equals("set")) {

                            if (strings.length == 4) {
                                Player player = (Player) sender;
                                Block gaze = playerGaze(player);

                                Spaceport spaceport = SpaceportColl.get().findByContainedLocation(gaze.getLocation());
                                if (spaceport != null) {
                                    spaceport.setTravelFee(new BigDecimal(strings[3]));
                                    player.sendMessage("Travel fee set to $" + spaceport.getTravelFee());
                                } else {
                                    player.sendMessage("Walk up to a spaceport and look at it to select it!");
                                }

                                return true;
                            }

                        }

                    }

                }

                if (strings[1].equals("withdraw")) {
                    Player player = (Player) sender;
                    Block gaze = playerGaze(player);

                    Spaceport spaceport = SpaceportColl.get().findByContainedLocation(gaze.getLocation());
                    if (spaceport != null) {
                        BigDecimal withdrawn = spaceport.withdrawAll();
                        Economy.add(player.getName(), withdrawn);
                        player.sendMessage("Withdrew $" + withdrawn + " from the spaceport...");
                    } else {
                        player.sendMessage("Walk up to a spaceport and look at it to select it!");
                    }

                    return true;
                }

            }
        } catch (Exception e) {
            Dune.get().getLogger().log(Level.SEVERE, e.toString());
        }

        return false;
    }

    private boolean onCommandSmugport(CommandSender sender, Command command, String s, String[] strings) {
        try {
            if (strings.length >= 2) {
                if (strings[1].equals("create")) {
                    // try to create a smugport
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Block rootBlock = playerGazeAir(player);
                        if (rootBlock != null) {

                            Location location = rootBlock.getLocation();
                            if (Smugport.canCreate(location)) {
                                if (Economy.hasEnough(player.getName(), Smugport.price)) {
                                    Economy.substract(player.getName(), Smugport.price);

                                    Smugport smugport = new Smugport(location);
                                    smugport.build();
                                    SmugportColl.get().add(smugport);
                                } else {
                                    player.sendMessage("You cannot afford a smuggler's port! The cost is $" + Smugport.price + ".");
                                }
                            } else {
                                player.sendMessage("Cannot create smugport here!");
                            }

                        }
                    }
                    return true;
                }

                if (strings[1].equals("fee")) {

                    if (strings.length >= 3) {

                        if (strings[2].equals("set")) {

                            if (strings.length == 4) {
                                Player player = (Player) sender;
                                Block gaze = playerGaze(player);

                                Smugport smugport = SmugportColl.get().findByContainedLocation(gaze.getLocation());
                                if (smugport != null) {
                                    smugport.setTravelFee(new BigDecimal(strings[3]));
                                    player.sendMessage("Travel fee set to $" + smugport.getTravelFee());
                                } else {
                                    player.sendMessage("Walk up to a smugport and look at it to select it!");
                                }

                                return true;
                            }

                        }

                    }

                }

                if (strings[1].equals("withdraw")) {
                    Player player = (Player) sender;
                    Block gaze = playerGaze(player);

                    Smugport smugport = SmugportColl.get().findByContainedLocation(gaze.getLocation());
                    if (smugport != null) {
                        BigDecimal withdrawn = smugport.withdrawAll();
                        Economy.add(player.getName(), withdrawn);
                        player.sendMessage("Withdrew $" + withdrawn + " from the smuggler's port...");
                    } else {
                        player.sendMessage("Walk up to a smugport and look at it to select it!");
                    }

                    return true;
                }
            }
        } catch (Exception e) {
            Dune.get().getLogger().log(Level.SEVERE, e.toString());
        }


        return false;
    }

    private static Block playerGazeAir(Player player) {
        BlockIterator raytracer = new BlockIterator(player, 10);
        Block bPrev = null;

        while(raytracer.hasNext()) {
            Block b = raytracer.next();
            if (b.getType() != Material.AIR && bPrev != null && bPrev.getType() == Material.AIR) {
                return bPrev;
            }
            bPrev = b;
        }

        return null;
    }

    private static Block playerGaze(Player player) {
        BlockIterator raytracer = new BlockIterator(player, 10);
        Block bPrev = null;

        while (raytracer.hasNext()) {
            Block b = raytracer.next();
            if (b.getType() != Material.AIR) {
                return b;
            }
        }

        return null;
    }
}
