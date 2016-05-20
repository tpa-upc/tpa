package test;

import org.lwjgl.PointerBuffer;
import tpa.application.Application;
import tpa.application.Context;
import tpa.application.LwjglWindow;
import tpa.audio.LwjglAudioRenderer;
import tpa.graphics.render.LwjglRenderer;
import tpa.input.keyboard.LwjglKeyboardInput;
import tpa.input.mouse.LwjglMouseInput;
import tpa.timing.LwjglTime;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

/**
 * Created by german on 26/03/2016.
 */
public class Launch {

    public static void main (String[] argv) {
        GLFW.glfwInit();

        // create a window
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        long monitor = MemoryUtil.NULL;
        if (argv.length >= 1 && argv[0].equals("--fullscreen"))
            monitor = GLFW.glfwGetPrimaryMonitor();

        //long window = GLFW.glfwCreateWindow(800, 600, "Hello world", GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);
        long window = GLFW.glfwCreateWindow(800, 600, "Hello world", monitor, MemoryUtil.NULL);
        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);

        // instantiate implementation for input, graphics, audio, etc...
        LwjglRenderer renderer = new LwjglRenderer();
        LwjglAudioRenderer audio = new LwjglAudioRenderer();
        LwjglMouseInput mouse = new LwjglMouseInput(window);
        LwjglKeyboardInput keyboard = new LwjglKeyboardInput(window);
        LwjglTime time = new LwjglTime(window);
        LwjglWindow win = new LwjglWindow(window);

        // create a context for the application with all the needed stuff
        Context con = new Context(win, renderer, audio, time, mouse, keyboard);

        // names
        System.err.println("\nAriadna Sanchez");
        System.err.println("Nil Oleart");
        System.err.println("German Gomez @germangb");
        System.err.println("Luc Espinach");
        System.err.println("Fran Roldan");
        System.err.println("Eduard Salado");

        // create an application from which to access the context
        Application program = new GameTest();
        program.onInit(con);

        // main loop
        while (GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE) {
            // update what needs to be updated
            try {
                time.update();
                program.onUpdate(con);
                mouse.update();
                audio.update();
            } catch (Exception e) {
                e.printStackTrace();
                GLFW.glfwSetWindowShouldClose(window, GLFW.GLFW_TRUE);
            }

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        // destroy what needs to be destroyed
        renderer.destroy();
        mouse.destroy();
        audio.destroy();
        keyboard.destroy();
        win.destroy();

        // terminate window
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
