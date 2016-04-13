package game;

import activity.Activity;
import activity.ActivityListener;
import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by germangb on 12/04/16.
 */
public class Game {
    private static Game ourInstance = new Game();

    public static Game getInstance() {
        return ourInstance;
    }

    /** Stack of activities */
    private Stack<GameActivity> activities = new Stack<>();

    /** Game's flahs used by the game logic */
    private HashMap<String, Boolean> flags = new HashMap<>();

    /** Application context */
    private Context context;

    private Game() {
    }

    /**
     * Called to init game
     * @param context Application context
     */
    public void onInit (Context context) {
        this.context = context;

        // init activities
        for (GameActivity act : GameActivity.values())
            act.getActivity().onInit(context);
    }

    /**
     * Called to update the game
     * @param context application context
     */
    public void onUpdate (Context context) {
        if (activities.isEmpty())
            return;

        // update activity
        Activity top = activities.peek().getActivity();
        top.onUpdate(context);
    }

    /**
     * Launches an activity
     * @param activity activity to be launched
     * @param listener activity listener
     */
    public void pushActivity (GameActivity activity, ActivityListener listener) {
        if (!activities.isEmpty())
            activities.peek().getActivity().onEnd(context);

        activities.push(activity);
        activity.getActivity().setListener(listener);
        activity.getActivity().onBegin(context);
    }

    public void popActivity () {
        if (!activities.isEmpty()) {
            GameActivity activity = activities.pop();
            activity.getActivity().onEnd(context);

            if (!activities.isEmpty())
                activities.peek().getActivity().onBegin(context);
        }
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
}
