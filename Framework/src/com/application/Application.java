package com.application;

/**
 * Created by german on 28/03/2016.
 */
public abstract class Application {

    /**
     * Called at the beginning of the program
     */
    public abstract void onInit (Context context);

    /**
     * Called once per frame
     */
    public void onUpdate (Context context) {
    }

    /**
     * Called at the end of the program
     */
    public void onDestroy (Context context) {
    }
}
