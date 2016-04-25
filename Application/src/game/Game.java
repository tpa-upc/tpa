package game;

import activity.Activity;
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

    /** Game's flahs used by the game logic */
    private HashMap<String, Object> flags = new HashMap<>();

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

    public void popActivity () {
        if (!activities.isEmpty()) {
            GameActivity activity = activities.pop();
            activity.getActivity().onEnd(context);

            if (!activities.isEmpty())
                activities.peek().getActivity().onBegin(context);
        }
    }
}
