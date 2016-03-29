package test;

import com.application.Application;
import com.application.Context;
import com.audio.Sound;
import com.graphics.geometry.*;
import com.graphics.render.RenderMode;
import com.graphics.render.Renderer;
import com.graphics.shader.ShaderProgram;
import com.graphics.shader.UniformType;
import com.input.keyboard.KeyboardAdapter;
import com.input.keyboard.KeyboardInput;
import com.joml.Matrix4f;
import com.joml.Vector4f;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by german on 27/03/2016.
 */
public class HelloWorld extends Application {

    DebugGui debug;

    Sound sound;
    ShaderProgram program;
    Mesh mesh;
    int count = 0;

    public HelloWorld(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                debug = new DebugGui();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyboardInput.KEY_A) {
                    count += 100;
                }
            }
        });

        Renderer renderer = context.renderer;

        sound = new Sound();
        context.audioRenderer.playSound(sound);

        //language=GLSL
        String vert = "#version 130\n" +
                "\n" +
                "in vec3 a_position;\n" +
                "in vec3 a_color;\n" +
                "\n" +
                "out vec3 v_color;\n" +
                "\n" +
                "uniform mat4 u_projection;\n" +
                "\n" +
                "void main () {\n" +
                "    gl_Position = u_projection * vec4(a_position, 1.0);\n" +
                "    v_color = a_color;\n" +
                "}";
        //language=GLSL
        String frag = "#version 130\n" +
                "\n" +
                "in vec3 v_color;\n" +
                "\n" +
                "out vec4 frag_color;\n" +
                "\n" +
                "uniform float u_time;\n" +
                "\n" +
                "void main () {\n" +
                "    frag_color = vec4( v_color, 1 );\n" +
                "}";

        program = new ShaderProgram(vert, frag, Attribute.Position, Attribute.Color);

        mesh = new Mesh(MeshUsage.Static);

        FloatBuffer pos = ByteBuffer.allocateDirect(9<<2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        FloatBuffer col = ByteBuffer.allocateDirect(9<<2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        IntBuffer ind = ByteBuffer.allocateDirect(3<<2).order(ByteOrder.nativeOrder()).asIntBuffer();

        pos.put(new float[] {
                -1, -1, 0,
                1, -1, 0,
                0, 1, 0
        }).flip();

        col.put(new float[] {
                1, 0, 0,
                0, 1, 0,
                0, 0, 1
        }).flip();

        ind.put(new int[] {
                0, 1, 2
        }).flip();

        mesh.setLength(3);
        mesh.setData(Attribute.Position, pos);
        mesh.setData(Attribute.Color, col);
        mesh.setIndices(ind);
    }

    float t = 0;

    @Override
    public void onUpdate() {
        Renderer renderer = context.renderer;

        t += 1/60f;

        renderer.beginFrame();
        renderer.setDepth(true);
        renderer.setRenderMode(RenderMode.Fill);
        renderer.clearColor(new Vector4f(0, 0, 0, 1));
        renderer.clearColorBuffer();
        renderer.clearDepthBuffer();
        renderer.setShaderProgram(program);

        for (int i = 0; i < count; ++i) {
            program.setUniform("u_projection", UniformType.Matrix4, new Matrix4f().perspective(50 * 3.1415f / 180, 4 / 3f, 0.1f, 1000f)
                    .lookAt(2 * (float) Math.cos(t), 8, 16 * (float) Math.sin(t), 0, 0, 0, 0, 1, 0)
                    .translate((float)Math.sin(i)*16, 0, (float)Math.cos(i*2.4571)*16));
            renderer.renderMesh(mesh);
        }

        renderer.endFrame();

        if (context.time.elapsedFrames()%60 == 0)
        System.out.println(context.time.getFramesPerSecond());

        try {
            SwingUtilities.invokeAndWait(() -> {
                debug.doSomething(context.renderer.getStatistics());
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                debug.dispose();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
