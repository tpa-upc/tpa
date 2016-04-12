package activity;

import tpa.application.Context;

/**
 * Created by germangb on 12/04/16.
 */
public interface Activity {

    /**
     * Caled to initialize resources for activity
     * @param context
     */
    void onInit (Context context);

    /**
     * Called when the activity is begun
     * @param context application context
     */
    void onBegin (Context context);

    /**
     * Called once per frame
     * @param context application context
     */
    void onUpdate (Context context);

    /**
     * Called when the activity is ended
     * @param context application context
     */
    void onEnd(Context context);
}
