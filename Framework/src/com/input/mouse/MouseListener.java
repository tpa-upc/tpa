package com.input.mouse;

/**
 * Created by german on 27/03/2016.
 */
public interface MouseListener {

    /**
     * Called when mouse button is down
     * @param button mouse button
     */
    void onMouseDown (int button);

    /**
     * Called when mouse button is released
     * @param button mouse button
     */
    void onMouseUp (int button);

    /**
     * Called when mouse is moved while button is being held down
     * @param button mouse button
     */
    void onMouseDrag (int button);
}
