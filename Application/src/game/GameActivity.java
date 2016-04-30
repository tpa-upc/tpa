package game;

import activity.*;

/**
 * Created by germangb on 12/04/16.
 */
public enum GameActivity {

    /** Room location */
    Room(new RoomLocation()),

    /** Interrogation location */
    Interrogation(new InterrogationLocation()),

    /** Test dialog */
    Dialog (new DialogActivity("res/dialog.json")),

    /** Intro activity */
    Intro (new IntroActivity());

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
