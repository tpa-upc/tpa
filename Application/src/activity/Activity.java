package activity;

import tpa.application.Context;

/**
 * Created by germangb on 12/04/16.
 */
public abstract class Activity {

    public interface ActivityListener {

        /**
         * Called when activity does something worth picking up
         * @param act activity being used
         * @param data data launched by activity
         */
        void onResult (Activity act, Object data);
    }

    /** Activity's listener */
    private ActivityListener listener;

    /**
     * Set activity's listener
     * @param listener
     */
    public void setListener (ActivityListener listener) {
        this.listener = listener;
    }

    /**
     * Report some data
     * @param data data to report
     */
    protected void report (Object data) {
        if (listener != null) {
            listener.onResult(this, data);
        }
    }

    /**
     * Caled to initialize resources for activity
     * @param context
     */
    public abstract void onPreLoad (Context context);

    /**
     * Called when resources are loaded
     * @param context
     */
    public abstract void onPostLoad (Context context);

    /**
     * Called when the activity is begun
     * @param context application context
     */
    public abstract void onBegin (Context context);

    /**
     * Called once per frame
     * @param context application context
     */
    public abstract void onUpdate (Context context);

    /**
     * Called when the activity is ended
     * @param context application context
     */
    public abstract void onEnd (Context context);
}
