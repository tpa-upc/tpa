package game;

import activity.*;

/**
 * Created by germangb on 12/04/16.
 */
public enum GameActivity {

    /** Room location */
    Room(new RoomLocation()),

    /** Club location */
    Club(new ClubLocation()),

    /** demo location */
    Demo(new DemoLocation()),

    /** Interrogation location */
    Interrogation(new InterrogationLocation()),

    /** Test dialog */
    Dialog (new DialogActivity("res/dialogues/casa1.json")),

    /** fix door inner monologue */
    FixDoor (new DialogActivity("res/dialogues/fix_door.json")),

    /** Intro activity */
    Intro (new IntroActivity()),

    /** List of public enemies. */
    Enemies (new ImageActivity("res/textures/enemies_big.png")),

    /** Post-it note #0. */
    Note0 (new ImageActivity("res/textures/note0.png")),

    Puerta (new ImageActivity("res/textures/girl.jpg")),

    /** Post-it note #1. */
    Note1 (new ImageActivity("res/textures/note1.png")),

    /** Albert Oliveras. */
    Albert (new ImageActivity("res/textures/albert_big.png"));

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
