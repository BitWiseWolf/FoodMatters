package me.thekindwolf.foodmatters;

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
            //get the player's current exhaustion
            float currentExhaustion = player.getExhaustion();
            player.setExhaustion(currentExhaustion + exhaustionIncrement);
            //set the currentFoodLevel again for checking to see if the number is busted.
            currentExhaustion = player.getExhaustion();
        }
    }
}
