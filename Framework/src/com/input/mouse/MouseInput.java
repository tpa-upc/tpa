package com.input.mouse;

/**
 * Created by german on 27/03/2016.
 */
public interface MouseInput {

    int MOUSE_BUTTON_1 = 0;
    int MOUSE_BUTTON_2 = 1;
    int MOUSE_BUTTON_3 = 2;
    int MOUSE_BUTTON_4 = 3;
    int MOUSE_BUTTON_5 = 4;
    int MOUSE_BUTTON_6 = 5;
    int MOUSE_BUTTON_7 = 6;
    int MOUSE_BUTTON_8 = 7;
    int MOUSE_BUTTON_LEFT = 0;
    int MOUSE_BUTTON_RIGHT = 1;
    int MOUSE_BUTTON_MIDDLE = 2;

    /**
     * Get current mouse listener
     * @return
     */
    MouseListener getMouseListener ();

    /**
     * Set mouse listener
     * @param listener
     */
    void setMouseListener (MouseListener listener);

    /**
     * Check if mouse button is being held down
     * @param button button index
     * @return true if button is being held down, false otherwise
     */
    boolean isButtonDown(int button);

    /**
     * Get mouse position in pixels
     * @return mouse X position
     */
    int getCursorX();

    /**
     * Get mouse position in pixels
     * @return mouse Y position
     */
    int getCursorY();

    /**
     * Get mouse movement in the X direction
     * @return mouse movement in pixels
     */
    int getCursorDX();

    /**
     * Get mouse movement in the Y direction
     * @return mouse movement in pixels
     */
    int getCursorDY();
}
