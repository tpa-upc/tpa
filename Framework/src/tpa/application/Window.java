package tpa.application;

/**
 * Created by germangb on 10/04/16.
 */
public interface Window {

    interface WindowListener {

        /**
         * Called when window is resized
         * @param width new window width
         * @param height new window height
         */
        void onResized (int width, int height);
    }

    /**
     * Set window listener
     * @param listener
     */
    void setWindowListener (WindowListener listener);

    /**
     * Get window listener
     * @return
     */
    WindowListener getListener ();

    /**
     * Get window width
     * @return
     */
    int getWidth();

    /**
     * Get window height
     * @return
     */
    int getHeight();
}
