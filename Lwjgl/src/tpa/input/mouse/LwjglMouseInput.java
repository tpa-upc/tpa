package tpa.input.mouse;

import org.lwjgl.glfw.GLFWScrollCallback;
import tpa.utils.Destroyable;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Created by german on 27/03/2016.
 */
public class LwjglMouseInput implements MouseInput, Destroyable {

    long window;
    GLFWMouseButtonCallback mouseCallback;
    GLFWCursorPosCallback cursorPosCallback;
    GLFWScrollCallback scrollCallback;

    int x, y;
    int lastX, lastY;
    boolean[] down = new boolean[8];

    MouseListener mouseListener;

    long normalCursor;
    long handCursor;
    long crossCursor;

    public LwjglMouseInput (long window) {
        this.window = window;

        normalCursor = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        crossCursor = glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR);
        handCursor = glfwCreateStandardCursor(GLFW_HAND_CURSOR);

        this.window = window;
        lastX = x = 0;
        lastY = y = 0;

        LwjglMouseInput handle = this;

        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double x, double y) {
                if (mouseListener != null) {
                    mouseListener.onMouseScroll((int) x, (int) y);
                }
            }
        });

        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                handle.lastX = handle.x;
                handle.lastY = handle.y;
                handle.x = (int) x;
                handle.y = (int) y;
                if (mouseListener != null) {
                    for (int i = 0; i < 8; ++i) {
                        if (handle.down[i])
                            mouseListener.onMouseDrag(i);
                    }
                }
            }
        });

        glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                int bt = button;
                handle.down[bt] = action == GLFW_PRESS;

                switch (action) {
                    case GLFW_PRESS:
                        if (mouseListener != null)
                            mouseListener.onMouseDown(bt);
                        break;
                    case GLFW_RELEASE:
                        if (mouseListener != null)
                            mouseListener.onMouseUp(bt);
                        break;
                }
            }
        });
    }

    public void update () {
        lastX = x;
        lastY = y;
    }

    @Override
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    @Override
    public void setMouseListener(MouseListener listener) {
        mouseListener = listener;
    }

    @Override
    public void setCursor(Cursor cursor) {
        switch (cursor) {
            case Arrow:
                glfwSetCursor(window, normalCursor);
                break;
            case Cross:
                glfwSetCursor(window, crossCursor);
                break;
            case Hand:
                glfwSetCursor(window, handCursor);
                break;
        }
    }

    @Override
    public void setGrabbed(boolean grab) {
        glfwSetInputMode(window, GLFW_CURSOR, grab ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    @Override
    public boolean isButtonDown(int button) {
        if (button < GLFW_MOUSE_BUTTON_1 || button > GLFW_MOUSE_BUTTON_2)
            throw new IllegalArgumentException();
        return down[button];
    }

    @Override
    public int getCursorX() {
        return x;
    }

    @Override
    public int getCursorY() {
        return y;
    }

    @Override
    public int getCursorDX() {
        return x-lastX;
    }

    @Override
    public int getCursorDY() {
        return y-lastY;
    }

    @Override
    public void destroy() {
        mouseCallback.free();
        cursorPosCallback.free();
        scrollCallback.free();
        glfwDestroyCursor(normalCursor);
        glfwDestroyCursor(handCursor);
        glfwDestroyCursor(crossCursor);
    }
}
