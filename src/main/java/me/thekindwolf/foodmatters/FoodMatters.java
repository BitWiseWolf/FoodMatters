package me.thekindwolf.foodmatters;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public final class FoodMatters extends JavaPlugin {
    //config variables, init a config
    //custom specific instance of plugin for fetching
    public static FoodMatters plugin;

    {
        plugin = this;
    }

    public int taskId;
    FileConfiguration config = getConfig();

    // declare your flag as a field accessible to other parts of your code (so you can use this to check it)
    // note: if you want to use a different type of flag, make sure you change StateFlag here and below to that type
    public static StateFlag FOOD_MATTERS_FLAG;
    public int exhaustionInterval = config.getInt("exhaustionInterval");
    public float exhaustionIncrement = (float)config.getDouble("exhaustionIncrement");

    @Override
    public void onLoad()
    {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("food-matters", true);
            registry.register(flag);
            FOOD_MATTERS_FLAG = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("food-matters");
            if (existing instanceof StateFlag) {
                FOOD_MATTERS_FLAG = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
                System.out.println("The bad thing happened OH NO check FoodMatters");
            }
        }
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        //make sure any key values that are dynamically added get saved as defaults
        config.options().copyDefaults(true);
        //create the config
        this.saveDefaultConfig();

        // second param allows for ordering of handlers - see the JavaDocs
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(FoodMattersFlag.FACTORY, null);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("FoodMatters is Disabled.");
    }

    public FileConfiguration getConfigFile()
    {
        return config;
    }

    //custom method for returning the instance of the plugin.
    public static FoodMatters getPlugin()
    {
        return plugin;
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
                    config = getConfig();
                    this.saveDefaultConfig();

                    //Let the player know that the plugin is reloaded
                    player.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
                    SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
                    sessionManager.unregisterHandler(FoodMattersFlag.FACTORY);
                    // second param allows for ordering of handlers - see the JavaDocs
                    sessionManager.registerHandler(FoodMattersFlag.FACTORY, null);
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


