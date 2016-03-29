package com.application;

import com.audio.AudioRenderer;
import com.graphics.render.Renderer;
import com.input.keyboard.KeyboardInput;
import com.input.mouse.MouseInput;
import com.timing.Time;

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
