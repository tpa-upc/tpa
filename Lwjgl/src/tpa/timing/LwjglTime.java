package tpa.timing;

import org.lwjgl.glfw.GLFW;

/**
 * Created by german on 27/03/2016.
 */
public class LwjglTime implements Time {

    private long window;

    private float time = 0;

    private float lastFrame = 0;
    private float delta = 0;

    private int fps = 0, ticks = 0;
    private float lastFrameFps = 0;

    private int frames = 0;

    public LwjglTime (long window) {
        this.window = window;
    }

    public void update () {
        frames++;
        time = (float) GLFW.glfwGetTime();
        delta = time-lastFrame;
        lastFrame = time;
        if (time-lastFrameFps > 1) {
            fps = ticks;
            ticks = 0;
            lastFrameFps = time;
        } else ticks++;
    }

    @Override
    public float getTime() {
        return time;
    }

    @Override
    public float getFrameTime() {
        return delta;
    }

    @Override
    public int getFramesPerSecond() {
        return fps;
    }

    @Override
    public int elapsedFrames() {
        return frames;
    }
}
