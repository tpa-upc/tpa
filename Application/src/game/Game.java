package game;

import activity.Activity;
import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Context;

import java.util.HashMap;
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

    /** Resource manager */
    private ResourceManager resources = new SimpleResourceManager();

    /** Application context */
    private Context context;

    private Game() {
    }

    public void setContext (Context context) {
        this.context = context;
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
     * Push an activity to the stack
     * @param activity activity to be stacked
     */
    public void pushActivity (GameActivity activity) {
        pushActivity(activity, null);
    }

    /**
     * Launches an activity
     * @param activity activity to be launched
     * @param listener activity listener
     */
    public void pushActivity (GameActivity activity, Activity.ActivityListener listener) {
        if (!activities.isEmpty())
            activities.peek().getActivity().onEnd(context);

        activities.push(activity);
        activity.getActivity().setListener(listener);
        activity.getActivity().onBegin(context);
    }

    /**
     * Pop activity from the stack
     */
    public void popActivity () {
        if (!activities.isEmpty()) {
            GameActivity activity = activities.pop();
            activity.getActivity().onEnd(context);

            if (!activities.isEmpty())
                activities.peek().getActivity().onBegin(context);
        }
    }

    /**
     * Get resource manager
     * @return resource manager
     */
    public ResourceManager getResources () {
        return resources;
    }
}
