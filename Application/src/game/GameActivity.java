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

    /** Acid location */
    Acid(new AcidLocation()),

    /** Lover Location **/
    Lover(new LoverLocation()),

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
    ReactionNote2 (new DialogActivity("res/dialogues/note2_reaction.json")),

    /** Albert */
    Albert (new ImageActivity("res/textures/albert_big.png")),

    MapImage (new ImageActivity("res/textures/map.png")),

    /**Blood images for Lover Location**/
    WallBlood (new ImageActivity("res/textures/Blood-Paret.png")),
    FloorBlood (new ImageActivity("res/textures/Blood-Terra.png")),

    /** Alter Ego first conversation **/
    AlterEgo1 (new DialogActivity("res/dialogues/casa2_alter_ego.json")),

    /** Alter Ego first pointless conversation **/
    AlterEgoP (new DialogActivity("res/dialogues/casa2_alter_ego_repeat.json")),

    /** Second conversation with alter ego **/
    AlterEgo2 (new DialogActivity("res/dialogues/casa3_alter_ego.json")),
    AlterEgoP2 (new DialogActivity("res/dialogues/casa3_alter_ego_repeat.json")),

    /** Bar Card **/
    BarCard (new ImageActivity("res/textures/bar_card.png")),

    /** Forensic Card**/
    ForensicCard (new ImageActivity("res/textures/forensicCard.png")),

    /** Fist Bar Conversation **/
    Bar0 (new DialogActivity("res/dialogues/bar0.json")),

    /**Reaction to Bar Card **/
    ReactionBarCard (new DialogActivity("res/dialogues/card_bar_reaction.json")),

    /** First Bar Pointless Conversations **/
    Bar1 (new DialogActivity("res/dialogues/bar1.json")),

    Bar2 (new DialogActivity("res/dialogues/bar3.json")),

    /** Satan End **/
    ShortEnd (new ImageActivity("res/textures/theend.png")),

    MonologueFriendface(new DialogActivity("res/dialogues/mono_friendface.json")),

    /**Thompson conversation by phone**/
    Thompson (new DialogActivity("res/dialogues/casa2_after_search.json")),

    Interrogation0 (new DialogActivity("res/dialogues/interr_round0.json")),
    Interrogation1 (new DialogActivity("res/dialogues/interr_round1.json")),

    JamesImage (new ImageActivity("res/textures/JamesStuart.png")),
    AfterJamesSearchDialogue (new DialogActivity("res/dialogues/reaction_james_search.json")),

    /** Dialogue forensic Card found**/
    ForensicCardFound (new DialogActivity("res/dialogues/lover_house_forensic_card.json")),

    /** Dialogue bar card found in LoverLocation**/
    BarCardFound(new DialogActivity("res/dialogues/lover_house_bar_card.json")),

    AnthonyReaction (new DialogActivity("res/dialogues/togglereaction.json")),

    /** Alter ego 4 conversation. **/
    AlterEgo4 (new DialogActivity("res/dialogues/casa4_alter_ego.json")),
    AlterEgoRep4(new DialogActivity("res/dialogues/casa4_alter_ego_repeat.json")),

    /** After looking for the Forensic card**/
    afterSearchForensic(new DialogActivity("res/dialogues/casa4_after_search.json")),

    WantToInterrogate (new DialogActivity("res/dialogues/want_to_interrogate.json")),

    InterrThompsonGTFO (new DialogActivity("res/dialogues/interr_thom0.json")),
    InterrThompsonStart (new DialogActivity("res/dialogues/interr_thom1.json")),
    InterrThompsonSuccess (new DialogActivity("res/dialogues/interr_thom2.json")),
    InterrThompsonAwkward (new DialogActivity("res/dialogues/interr_thom3.json")),

    /** Terminal 2 **/
    Terminal2 (new DialogActivity("res/dialogues/terminal2.json")),

    /**Police Warning Database **/
    Terminal3 (new DialogActivity("res/dialogues/terminal3.json")),
    LogWarning (new DialogActivity("res/dialogues/LogWarning.json")),
    Pacman (new DialogActivity("res/dialogues/pacman.json")),

    /** First Bad Ending **/
    Ending1 (new DialogActivity("res/dialogues/firstending.json")),

    /**Newspapers**/
    NewspaperBad (new ImageActivity("res/textures/newspaper_FINALDOLENT.png")),
    NewspaperGood (new ImageActivity("res/textures/newspaper_FINALBO.png")),

    /** Dialogue with Thompson at Lover's House **/
    LoverHouse1 (new DialogActivity("res/dialogues/casa_amant1.json")),
    LoverHouse2 (new DialogActivity("res/dialogues/casa_amant2.json")),

    /** Comments at lover's house **/
    Kids (new DialogActivity("res/dialogues/kids.json")),
    Dead (new DialogActivity("res/dialogues/dead.json")),

    /**Leave Lover's House**/
    LeaveRoom (new DialogActivity("res/dialogues/leaveroom.json")),

    /**Teddy Bear Email**/
    EmailTB (new ImageActivity("res/textures/emailTeddyBear.png"));


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
