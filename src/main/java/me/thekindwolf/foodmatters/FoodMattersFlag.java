package me.thekindwolf.foodmatters;


import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.configuration.file.FileConfiguration;


public class FoodMattersFlag extends Handler {
    public static final Factory FACTORY = new Factory();
    public boolean foodMattersFlag = true;
    public FileConfiguration config = FoodMatters.getPlugin().getConfigFile();
    private long lastFoodSubtract = 0;
    public int exhaustionInterval = config.getInt("exhaustionInterval");
    public float exhaustionIncrement = (float)config.getDouble("exhaustionIncrement");

    public static class Factory extends Handler.Factory<FoodMattersFlag> {
        @Override
        public FoodMattersFlag create(Session session) {
            // create an instance of a handler for the particular session
            // if you need to pass certain variables based on, for example, the player
            // whose session this is, do it here
            return new FoodMattersFlag(session);
        }
    }
    // construct with your desired flag to track changes

    public FoodMattersFlag(Session session) {
        super(session);
    }
    // ... override handler methods here

    //method to see if a region has food matters flag enabled or not.
    public boolean hasFoodMattersFlag()
    {
        return foodMattersFlag;
    }

    //for setting the value of the flag
    public void setFoodMattersFlag(boolean foodMattersFlag)
    {
        this.foodMattersFlag = foodMattersFlag;
    }

    @Override
    public void initialize(LocalPlayer player, Location current, ApplicableRegionSet set)
    {
        FoodMatters.getPlugin().reloadConfig();
        config = FoodMatters.getPlugin().getConfig();
        FoodMatters.getPlugin().saveDefaultConfig();
        exhaustionInterval = config.getInt("exhaustionInterval");
        exhaustionIncrement = (float)config.getDouble("exhaustionIncrement");
    }

    @Override
    public void tick(LocalPlayer player, ApplicableRegionSet set)
    {
        //get the time
        long now = System.currentTimeMillis();

        boolean flagActive = set.testState(player, FoodMatters.FOOD_MATTERS_FLAG);

        if(flagActive)
        {
            if(now - lastFoodSubtract > exhaustionInterval * 1000)
            {
                //Food Subtract logic
                //get the player's current exhaustion
                float currentExhaustion = player.getExhaustion();
                player.setExhaustion(currentExhaustion + exhaustionIncrement);
                //set the currentFoodLevel again for checking to see if the number is busted.
                currentExhaustion = player.getExhaustion();
                lastFoodSubtract = now;
            }
        }
        else
        {
            player.setFoodLevel(20);
        }
    }
}
