package game;

import activity.Activity;
import activity.ComputerActivity;

/**
 * Created by germangb on 12/04/16.
 */
public enum GameActivity {

    /** Computer activity */
    Computer (new ComputerActivity());

    /** referenced game activity */
    private activity.Activity activity;

    GameActivity(activity.Activity activity) {
        this.activity = activity;
    }

    /**
     * Get eumarated activity
     * @return game activity
     */
    public Activity getActivity() {
        return activity;
    }
}
