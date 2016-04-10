package app;

import tpa.application.Application;
import tpa.application.Context;
import tpa.audio.LwjglAudioRenderer;
import tpa.graphics.render.LwjglRenderer;
import tpa.input.keyboard.LwjglKeyboardInput;
import tpa.input.mouse.LwjglMouseInput;
import tpa.timing.LwjglTime;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;
import java.nio.ByteBuffer;

/**
 * Created by german on 26/03/2016.
 */
public class Launch {

    public static void main (String[] argv) {
        GLFW.glfwInit();
        long window = GLFW.glfwCreateWindow(640, 480, "Hello world", MemoryUtil.NULL, MemoryUtil.NULL);

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);

        // create OpenGL context
        GLCapabilities glcaps = GL.createCapabilities();

        // create OpenAL context
        long device = ALC10.alcOpenDevice((String) null);
        long context = ALC10.alcCreateContext(device, (ByteBuffer) null);
        ALCCapabilities alcCaps = ALC.createCapabilities(device);
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(alcCaps);

        LwjglRenderer renderer = new LwjglRenderer(glcaps);
        LwjglTime time = new LwjglTime(window);
        LwjglAudioRenderer audio = new LwjglAudioRenderer();
        LwjglMouseInput mouse = new LwjglMouseInput(window);
        LwjglKeyboardInput keyboard = new LwjglKeyboardInput(window);

        Context con = new Context();
        con.renderer = renderer;
        con.audioRenderer = audio;
        con.time = time;
        con.keyboard = keyboard;

        Application program = new Tests();
        program.onInit(con);

        while (GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE) {
            time.update();
            program.onUpdate(con);

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        program.onDestroy(con);

        renderer.destroy();
        mouse.destroy();
        audio.destroy();
        keyboard.destroy();

        // destroy openAL context
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
