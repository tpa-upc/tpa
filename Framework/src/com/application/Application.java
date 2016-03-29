package com.application;

/**
 * Created by german on 28/03/2016.
 */
public abstract class Application {

    protected Context context;

    public Application (Context context) {
        this.context = context;
    }

    /**
     * Called at the beginning of the program
     */
    public abstract void onInit ();

    /**
     * Called once per frame
     */
    public void onUpdate () {}

    /**
     * Called at the end of the program
     */
    public void onDestroy () {}
}
