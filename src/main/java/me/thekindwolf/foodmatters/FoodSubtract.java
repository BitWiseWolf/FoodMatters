package me.thekindwolf.foodmatters;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.session.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class FoodSubtract implements Runnable
{
    //create a list of players to modify
    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    float exhaustionIncrement;
    //set up the link to the FoodMatters class variables
    public FoodSubtract(float exhaustionIncrement)
    {
         this.exhaustionIncrement = exhaustionIncrement;
    }

    public void run()
    {
        for(Player player: players)
        {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
            if(set.testState(WorldGuardPlugin.inst().wrapPlayer(player), FoodMatters.FOOD_MATTERS_FLAG))
            {
                //get the player's current exhaustion
                float currentExhaustion = player.getExhaustion();
                player.setExhaustion(currentExhaustion + exhaustionIncrement);
                //set the currentFoodLevel again for checking to see if the number is busted.
                currentExhaustion = player.getExhaustion();
            }
        }
    }
}
