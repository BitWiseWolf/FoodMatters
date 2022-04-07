package me.thekindwolf.foodmatters.commands;

import me.thekindwolf.foodmatters.FoodMatters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand extends JavaPlugin implements CommandExecutor
{
    //dependency injection to get access to the logger in our main class FoodMatters
    private final FoodMatters foodMatters;
    public ReloadCommand(FoodMatters foodMatters)
    {
        this.foodMatters = foodMatters;
    }

    //get the plugin class so you can fetch the config and use config reload commands
    Plugin myPlugin = Bukkit.getServer().getPluginManager().getPlugin("foodmatters");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        //check to see if it's a player who typed the command.
        if(sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.isOp() || player.hasPermission("reloadcommand"))
            {
                //reload the config
                reloadConfig();

                //Let the player know that the plugin is reloaded
                player.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
                return true;
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You don't have the permissions to use that command.");
                return true;
            }
        }
        else
        {
            foodMatters.getLogger().info("You have to be a player to use that command.");
            return true;
        }
    }
}
