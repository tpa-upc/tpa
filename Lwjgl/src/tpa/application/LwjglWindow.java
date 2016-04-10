package tpa.application;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import tpa.utils.Destroyable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by germangb on 10/04/16.
 */
public class LwjglWindow implements Window, Destroyable {

    long window;
    GLFWWindowSizeCallback sizeCallback;
    int width, height;
    WindowListener listener;

    public LwjglWindow (long window) {
        this.window = window;

        IntBuffer w = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        IntBuffer h = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GLFW.glfwGetWindowSize(window, w, h);
        width = w.get(0);
        height = h.get(0);

        GLFW.glfwSetWindowSizeCallback(window, sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int w, int h) {
                width = w;
                height = h;
                if (listener != null)
                    listener.onResized(width, height);
            }
        });
    }

    @Override
    public void setWindowListener(WindowListener listener) {
        this.listener = listener;
    }

    @Override
    public WindowListener getListener() {
        return listener;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void destroy() {
        sizeCallback.free();
    }
}
