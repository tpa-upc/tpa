package com.input.keyboard.keyboard;

import com.input.keyboard.KeyboardInput;
import com.input.keyboard.KeyboardListener;
import com.utils.Destroyable;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Created by german on 27/03/2016.
 */
public class LwjglKeyboard implements KeyboardInput, Destroyable {

    KeyboardListener listener;
    long windown;

    GLFWKeyCallback keyCallback;
    GLFWCharCallback charCallback;

    public LwjglKeyboard (long window) {
        this.windown = window;
        LwjglKeyboard handle = this;

        glfwSetCharCallback(window, charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int code) {
                if (listener != null)
                    listener.onChar(code);
            }
        });

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (listener != null) {
                    switch (action) {
                        case GLFW_REPEAT:
                            break;
                        case GLFW_PRESS:
                            listener.onKeyDown(key);
                            break;
                        case GLFW_RELEASE:
                            listener.onKeyUp(key);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public KeyboardListener getKeyListener() {
        return listener;
    }

    @Override
    public void setKeyboardListener(KeyboardListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isKeyDown(int key) {
        return false;
    }

    @Override
    public void destroy() {
        keyCallback.free();
        charCallback.free();
    }
}
