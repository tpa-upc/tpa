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

    /** Friendface dialogue */
    DialogueFriendface(new DialogActivity("res/dialogues/terminal1.json")),

    /** toggle */
    DialogueToggle(new DialogActivity("res/dialogues/terminal0.json")),

    /** fix door inner monologue */
    FixDoor (new DialogActivity("res/dialogues/fix_door.json")),

    /** Intro activity */
    Intro (new IntroActivity()),

    /** List of public enemies. */
    Enemies (new ImageActivity("res/textures/enemies_big.png")),

    Friendface (new ImageActivity("res/textures/friendface.png")),

    /** Post-it note #0. */
    Note0 (new ImageActivity("res/textures/note0.png")),

    Puerta (new ImageActivity("res/textures/girl.jpg")),

    /** Post-it note #1. */
    Note1 (new ImageActivity("res/textures/note1.png")),

    /** Albert Oliveras. */
    Albert (new ImageActivity("res/textures/albert_big.png")),

    MapImage (new ImageActivity("res/textures/map.png")),

    /** Alter Ego first conversation **/
    AlterEgo1 (new DialogActivity("res/dialogues/casa2_alter_ego.json")),

    /** Alter Ego first pointless conversation **/
    AlterEgoP (new DialogActivity("res/dialogues/casa2_alter_ego_repeat.json")),

    /** Bar Card **/
    BarCard (new ImageActivity("res/textures/bar_card.png")),

    /** Fist Bar Conversation **/
    Bar0 (new DialogActivity("res/dialogues/bar0.json")),

    /** First Bar Pointless Conversations **/
    Bar1 (new DialogActivity("res/dialogues/bar1.json")),

    /** Satan End **/
    ShortEnd (new ImageActivity("res/textures/theend.png")),

    MonologueFriendface(new DialogActivity("res/dialogues/mono_friendface.json")),

    /**Thompson conversation by phone**/
    Thompson (new DialogActivity("res/dialogues/casa2_after_search.json")),

    /**Interrogation Room Conversation - all**/
    Interrog (new DialogActivity("res/dialogues/Interrogation_Room1.json")),
    Delete1 (new DialogActivity("res/dialogues/int_room2.json")),
    Delete2 (new DialogActivity("res/dialogues/int_room3.json"));
    /** end of interrogation room conversations **/

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
