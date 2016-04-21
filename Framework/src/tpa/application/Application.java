package tpa.application;

/**
 * Created by german on 28/03/2016.
 */
public interface Application {

    /**
     * Called at the beginning of the program
     * @param context
     */
    void onInit (Context context);

    /**
     * Called once per frame
     * @param context
     */
    void onUpdate (Context context);
}
