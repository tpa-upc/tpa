package com.input.keyboard;

/**
 * Created by german on 27/03/2016.
 */
public interface KeyboardListener {

    /**
     * Called when key is pressed
     * @param key KeyboardInput key
     */
    void onKeyDown (int key);

    /**
     * Called when key is released
     * @param key KeyboardInput key
     */
    void onKeyUp (int key);

    /**
     * Called when a unicode character is input
     * @param unicode unicode code point
     */
    void onChar (int unicode);
}
