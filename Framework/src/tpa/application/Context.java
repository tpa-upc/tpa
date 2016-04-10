package tpa.application;

import tpa.audio.AudioRenderer;
import tpa.graphics.render.Renderer;
import tpa.input.keyboard.KeyboardInput;
import tpa.input.mouse.MouseInput;
import tpa.timing.Time;

/**
 * Created by german on 27/03/2016.
 */
public class Context {

    /** window width */
    public final Window window;

    /** Renderer handle */
    public final Renderer renderer;

    /** Audio renderer handle */
    public final AudioRenderer audioRenderer;

    /** Timing handle */
    public final Time time;

    /** Mouse input handle */
    public final MouseInput mouse;

    /** Keyboard input handler */
    public final KeyboardInput keyboard;

    public Context (Window window, Renderer renderer, AudioRenderer audioRenderer, Time time, MouseInput mouse, KeyboardInput keyboard) {
        this.window = window;
        this.renderer = renderer;
        this.audioRenderer = audioRenderer;
        this.time = time;
        this.mouse = mouse;
        this.keyboard = keyboard;
    }
}
