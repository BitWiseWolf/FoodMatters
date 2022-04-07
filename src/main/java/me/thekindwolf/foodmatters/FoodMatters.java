package me.thekindwolf.foodmatters;

import me.thekindwolf.foodmatters.commands.ReloadCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getScheduler;

public final class FoodMatters extends JavaPlugin {
    //config variables, init a config
    Plugin myPlugin = this;
    public int taskId;
    FileConfiguration config = getConfig();
    public int exhaustionInterval = config.getInt("exhaustionInterval");
    public float exhaustionIncrement = (float)config.getDouble("exhaustionIncrement");

    @Override
    public void onEnable() {
        // Plugin startup logic
        //make sure any key values that are dynamically added get saved as defaults
        config.options().copyDefaults(true);
        //create the config
        this.saveDefaultConfig();

        //Food timer start
        taskId = getScheduler().scheduleSyncRepeatingTask(this, new FoodSubtract(exhaustionIncrement), 0, exhaustionInterval);

        //Register commands
        //getServer().getPluginCommand("fmreload").setExecutor(new (this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getConfigFile()
    {
        return config;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (label.equalsIgnoreCase("fmreload"))
        {
            //check to see if it's a player who typed the command.
            if(sender instanceof Player)
            {
                Player player = (Player) sender;
                if(player.isOp() || player.hasPermission("foodmatters.reload"))
                {
                    //reload the config
                    this.reloadConfig();
                    FileConfiguration newConfig = getConfig();
                    exhaustionInterval = newConfig.getInt("exhaustionInterval");
                    exhaustionIncrement = (float)newConfig.getDouble("exhaustionIncrement");
                    this.saveDefaultConfig();
                    getScheduler().cancelTask(taskId);
                    taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(myPlugin, new FoodSubtract(exhaustionIncrement), 0, exhaustionInterval);

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
                getLogger().info("You have to be a player to use that command.");
                return true;
            }
        }
        return false;
    }
}


