package test;

import rendering.FontMeshHelper;
import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Application;
import tpa.application.Context;
import tpa.application.Window;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFilter;
import tpa.joml.Matrix4f;

/**
 * Created by germangb on 12/04/16.
 */
public class TextTest extends Application {

    ResourceManager resources;
    FontMeshHelper font;
    ShaderProgram program;

    @Override
    public void onInit(Context context) {
        resources = new SimpleResourceManager();
        font = new FontMeshHelper();

        //language=GLSL
        String vert = "#version 130\n" +
                "\n" +
                "in vec3 a_position;\n" +
                "in vec2 a_uv;\n" +
                "\n" +
                "out vec2 v_uv;\n" +
                "\n" +
                "uniform mat4 u_projection;\n" +
                "\n" +
                "void main () {\n" +
                "    gl_Position = u_projection * vec4(a_position, 1.0);\n" +
                "    v_uv = a_uv;\n" +
                "}";
        //language=GLSL
        String frag = "#version 130\n" +
                "\n" +
                "in vec2 v_uv;\n" +
                "\n" +
                "out vec4 frag_color;\n" +
                "\n" +
                "uniform sampler2D u_texture;\n" +
                "\n" +
                "void main () {\n" +
                "    frag_color = vec4(texture2D(u_texture, v_uv).rgb, 1.0);\n" +
                "}";
        program = new ShaderProgram(vert, frag, Attribute.Position, Attribute.Uv);

        resources.load("res/font/amiga.png", Texture.class);
        resources.finishLoading();
        resources.get("res/font/amiga.png", Texture.class).setMin(TextureFilter.Nearest);
        resources.get("res/font/amiga.png", Texture.class).setMag(TextureFilter.Nearest);
    }

    @Override
    public void onUpdate(Context context) {
        Renderer renderer = context.renderer;
        Window win = context.window;

        Matrix4f projection = new Matrix4f()
                .setOrtho2D(0, win.getWidth()/2, win.getHeight()/2, 0);

        renderer.beginFrame();
        renderer.clearColorBuffer();
        renderer.clearColor(0, 0, 0, 1);

        renderer.setShaderProgram(program);
        program.setUniform("u_projection", UniformType.Matrix4, projection);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);

        renderer.setTexture(0, resources.get("res/font/amiga.png", Texture.class));

        font.setText("Hello World!");
        renderer.renderMesh(font.getMesh());

        renderer.endFrame();
    }
}
