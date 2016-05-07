package game;

import activity.*;

/**
 * Created by germangb on 12/04/16.
 */
public enum GameActivity {

    /** Loading screen */
    Loading(new LoadingActivity()),

    /** Room location */
    Room(new RoomLocation()),

    /** Amant location */
    Amant(new AmantLocation()),

    /** Club location */
    Club(new ClubLocation()),

    /** Interrogation location */
    Interrogation(new InterrogationLocation()),

    /** Test dialog */
    DialoguePhone(new DialogActivity("res/dialogues/casa1.json")),

    /** Open email dialog */
    DialogueEmail(new DialogActivity("res/dialogues/email0.json")),

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
    Albert (new ImageActivity("res/textures/albert_big.png")),

    /** Alter Ego first conversation **/
    AlterEgo1 (new DialogActivity("res/dialogues/casa2_alter_ego.json")),

    /** Alter Ego first pointless conversation **/
    AlterEgoP (new DialogActivity("res/dialogues/casa2_alter_ego_repeat.json"));

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
