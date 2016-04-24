package game;

import activity.Activity;
import activity.DialogActivity;
import activity.RoomLocation;

/**
 * Created by germangb on 12/04/16.
 */
public enum GameActivity {

    /** Monkey location */
    Monkey (new RoomLocation()),

    /** Test dialog */
    Dialog (new DialogActivity("res/dialog.json"));

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
