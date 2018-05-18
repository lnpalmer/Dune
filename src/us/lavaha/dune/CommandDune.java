package us.lavaha.dune;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class CommandDune implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            BlockIterator raytracer = new BlockIterator(player, 10);

            Block bPrev = null;

            while(raytracer.hasNext()) {
                Block b = raytracer.next();

                if (b.getType() != Material.AIR && bPrev != null && bPrev.getType() == Material.AIR) {
                    Block spBlock = bPrev;
                    spBlock.setType(Material.BEACON);
                    Spaceport spaceport = new Spaceport(spBlock.getLocation());
                    Spaceport.register(spaceport);
                    break;
                }

                bPrev = b;
            }

            return true;
        }

        return false;
    }
}
