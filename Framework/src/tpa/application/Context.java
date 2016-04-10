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

    /** Renderer handle */
    public Renderer renderer;

    /** Audio renderer handle */
    public AudioRenderer audioRenderer;

    /** Timing handle */
    public Time time;

    /** Mouse input handle */
    public MouseInput mouse;

    /** Keyboard input handler */
    public KeyboardInput keyboard;
}
