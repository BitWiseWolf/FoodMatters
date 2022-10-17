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
}
