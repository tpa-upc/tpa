package activity;

import game.Game;
import rendering.FontMeshHelper;
import resources.ResourceManager;
import resources.ResourceUtils;
import tpa.application.Context;
import tpa.application.Window;
import tpa.audio.Sound;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.geometry.MeshUsage;
import tpa.graphics.geometry.Primitive;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Framebuffer;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFilter;
import tpa.graphics.texture.TextureFormat;
import tpa.input.keyboard.KeyboardInput;
import tpa.input.keyboard.KeyboardListener;
import tpa.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by germangb on 12/04/16.
 */
public class ComputerActivity implements Activity, KeyboardListener {

    private float animation = 0;

    private FontMeshHelper font;
    private ShaderProgram program;
    private ShaderProgram composite;
    private Mesh fullQuad;
    private Framebuffer monitor;
    private Texture texture;

    boolean logged = false;
    boolean failed = false;
    String username = "";
    String password = "";
    String cmd = "";
    String out = "";
    int idState = 0;

    Sound beep;
    Sound[] keys;

    Context con;

    HashMap<String, String> users = new HashMap<>();

    @Override
    public void onInit(Context context) {
        users.put("german", "password");
        users.put("tonto", "123");

        monitor = new Framebuffer(480/2, 480/2, new TextureFormat[] {TextureFormat.Rgb}, true);
        monitor.getTargets()[0].setMag(TextureFilter.Nearest);

        beep = new Sound();
        this.con = context;

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

        //language=GLSL
        String compVert = "#version 130\n" +
                "\n" +
                "in vec3 a_position;\n" +
                "\n" +
                "out vec2 v_uv;\n" +
                "\n" +
                "uniform mat4 u_transform;\n" +
                "\n" +
                "void main () {\n" +
                "    gl_Position = u_transform * vec4(a_position, 1.0);\n" +
                "    v_uv = a_position.xy*0.5+0.5;\n" +
                "}";
        //language=GLSL
        String compFrag = "#version 130\n" +
                "\n" +
                "in vec2 v_uv;\n" +
                "\n" +
                "out vec4 frag_color;\n" +
                "\n" +
                "uniform sampler2D u_texture;\n" +
                "\n" +
                "uniform float u_time;\n" +
                "\n" +
                "vec3 color (vec2 uv) {\n" +
                "    float t = (sin(u_time*64)*0.5+0.5)*0.025;\n" +
                "    return mix (vec3(0.05), vec3(0.7), texture2D(u_texture, uv).r + ((sin(uv.y*512))*0.5+0.5)*(0.05+t));\n" +
                "}\n" +
                "\n" +
                "void main () {\n" +
                "    frag_color = vec4(color(v_uv), 1.0);\n" +
                "}";
        composite = new ShaderProgram(compVert, compFrag, Attribute.Position);

        fullQuad = new Mesh(MeshUsage.Static);
        fullQuad.setPrimitive(Primitive.Triangles);
        fullQuad.setData(Attribute.Position, ByteBuffer.allocateDirect(4*3<<2).order(ByteOrder.nativeOrder()).asFloatBuffer().put(new float[] {
                -1, -1, 0,
                +1, -1, 0,
                +1, +1, 0,
                -1, +1, 0
        }).flip());
        fullQuad.setIndices(ByteBuffer.allocateDirect(100).order(ByteOrder.nativeOrder()).asIntBuffer().put(new int[]{
                0, 1, 2,
                0, 2, 3
        }).flip());
        fullQuad.setLength(6);

        // load amiga font
        ResourceManager resources = Game.getInstance().getResources();
        resources.load("res/font/amiga.png", Texture.class);
        resources.finishLoading();

        // grad font texture
        texture = resources.get("res/font/amiga.png", Texture.class);
        texture.setMag(TextureFilter.Nearest);
        texture.setMin(TextureFilter.Nearest);

        try {
            keys = new Sound[2];
            keys[0] = ResourceUtils.loadSound("res/key.wav");
            keys[1] = ResourceUtils.loadSound("res/key1.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create font mesh
        font = new FontMeshHelper();

        System.out.println("onInit()");
    }

    @Override
    public void onBegin(Context context) {
        animation = 0;
        context.keyboard.setKeyboardListener(this);
        idState = 0;
        logged = false;
        failed = false;
        System.out.println("onBegin()");
    }

    @Override
    public void onUpdate(Context context) {
        animation += context.time.getFrameTime();
        Renderer renderer = context.renderer;
        Window win = context.window;

        Matrix4f projection = new Matrix4f()
                .setOrtho2D(0, monitor.getWidth(), monitor.getHeight(), 0);

        renderer.setFramebuffer(monitor);
        renderer.viewport(0, 0, monitor.getWidth(), monitor.getHeight());

        renderer.clearColor(0, 0, 0, 1);
        renderer.clearColorBuffer();

        renderer.setShaderProgram(program);
        program.setUniform("u_projection", UniformType.Matrix4, projection);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);

        renderer.setTexture(0, texture);

        boolean blink = (int)(context.time.getTime()/0.25f)%2 == 0;

        if (!logged) {
            font.setText((failed?"Invalid credentials\n\n":"")+"username: "+username+(idState==0&&blink?'_':"")+"\npassword: "+password.replaceAll("(?s).", "*")+(idState==1&&blink?'_':""));
        } else {
            font.setText("Welcome to TpaOS, "+username+"\n"+(!out.isEmpty()?"\n"+out:"")+"\ncmd> "+cmd+(blink?"_":""));
        }
        renderer.renderMesh(font.getMesh());

        // ---------------------------------------------------

        renderer.setFramebuffer(null);
        renderer.clearColor(0.5f, 0.5f, 0.5f, 1);
        renderer.clearColorBuffer();

        renderer.viewport(0, 0, context.window.getWidth(), context.window.getHeight());
        renderer.setShaderProgram(composite);
        composite.setUniform("u_texture", UniformType.Sampler2D, 0);
        composite.setUniform("u_time", UniformType.Float, context.time.getTime());
        float aspect = (float)con.window.getWidth()/con.window.getHeight();
        composite.setUniform("u_transform", UniformType.Matrix4, new Matrix4f()
                .perspective(50*3.1415f/180, aspect, 0.1f, 100)
                .lookAt((float) Math.cos(animation*0.5f), 1+(float)Math.exp(-animation)*4, 2.5f, 0, 0, 0, 0, 1, 0));
        renderer.setTexture(0, monitor.getTargets()[0]);
        renderer.renderMesh(fullQuad);
    }

    @Override
    public void onEnd(Context context) {
        context.keyboard.setKeyboardListener(null);
        System.out.println("onBegin()");
    }

    @Override
    public void onKeyDown(int key) {
        con.audioRenderer.playSound(this.keys[(int)(Math.random()*100)%2]);
        if (key == KeyboardInput.KEY_ESCAPE) {
            // exit activity
            Game.getInstance();
            return;
        }
        if (key == KeyboardInput.KEY_BACKSPACE) {
            if (!logged) {
                if (idState == 0) username = username.substring(0, Math.max(0, username.length()-1));
                else if (idState == 1) password = password.substring(0, Math.max(0, password.length()-1));
            } else {
                cmd = cmd.substring(0, Math.max(0, cmd.length()-1));
            }
            return;
        }
        if (!logged) {
            if (key == KeyboardInput.KEY_ENTER) {
                idState++;
                if (idState == 2) {
                    if (users.containsKey(username) && users.get(username).equals(password)) {
                        logged = true;
                        System.out.println("yay!");
                    } else {
                        username = "";
                        password = "";
                        con.audioRenderer.playSound(beep);
                        failed = true;
                        idState = 0;
                    }
                }
            }
        } else {
            if (key == KeyboardInput.KEY_ENTER) {
                // process command
                failed = false;
                out = "";
                switch (cmd) {
                    case "logout":
                        logged = false;
                        username = "";
                        password = "";
                        idState = 0;
                        failed = false;
                        break;
                    case "whoami":
                        out = "You are "+username;
                        break;
                    case "time":
                        out = new Date().toString();
                        break;
                    case "email":
                        out = "You have 100000 unread email(s)";
                        break;
                    default:
                        con.audioRenderer.playSound(beep);
                        out = "Unknown command \""+cmd+"\"";
                }
                cmd = "";
            }
        }
    }

    @Override
    public void onKeyUp(int key) {

    }

    @Override
    public void onChar(int unicode) {
        if (!logged) {
            if (idState == 0)
                username += (char) unicode;
            else if (idState == 1)
                password += (char) unicode;
        } else {
            cmd += (char) unicode;
        }
    }
}
