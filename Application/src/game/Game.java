package game;

import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Context;

import java.util.HashMap;

/**
 * Created by germangb on 12/04/16.
 */
public class Game {
    private static Game ourInstance = new Game();

    public static Game getInstance() {
        return ourInstance;
    }

    /** Game's resource manager */
    private ResourceManager resources;

    /** Game state */
    private GameState state;

    /** Game's current activity, if state is set to Activity */
    private GameActivity activity;
    private boolean activityBegan = false;

    /** Game's flahs used by the game logic */
    private HashMap<String, Boolean> flags;

    private Game() {
        this.state = GameState.Normal;
        this.resources = new SimpleResourceManager();
        this.flags = new HashMap<>();
    }

    /**
     * Called to init game
     * @param context Application context
     */
    public void onInit (Context context) {
        // init activities
        for (GameActivity act : GameActivity.values())
            act.getActivity().onInit(context);
    }

    /**
     * Called to update the game
     * @param context application context
     */
    public void onUpdate (Context context) {
        switch (state) {
            case Activity:
                if (!activityBegan) {
                    activity.getActivity().onBegin(context);
                    activityBegan = true;
                }
                activity.getActivity().onUpdate(context);
        }
    }

    /**
     * Launches an activity
     * @param activity activity to be launched
     */
    public void launchActivity (GameActivity activity) {
        // set state to activity
        this.state = GameState.Activity;
        this.activity = activity;
        this.activityBegan = false;
    }

    /**
     * Set a flag value
     * @param name flag name
     * @param value flag value
     */
    public void setFlag (String name, boolean value) {
        flags.put(name, value);
    }

    /**
     * Get the value of a particular flag.
     * @param name name of the flag
     * @return if the flag is set, returns it's value or false otherwise
     */
    public boolean getFlag (String name) {
        return flags.get(name);
    }

    /**
     * Get game's resource manager
     * @return resource manager
     */
    public ResourceManager getResources() {
        return resources;
    }
}
