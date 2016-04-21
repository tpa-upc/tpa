package activity;

/**
 * Created by germangb on 13/04/16.
 */
public interface ActivityListener {

    /**
     * Called when activity does something worth picking up
     * @param act activity being used
     * @param data data launched by activity
     */
    void onResult (Activity act, Object data);
}
