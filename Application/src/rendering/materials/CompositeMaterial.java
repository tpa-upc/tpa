package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFormat;
import tpa.graphics.texture.TextureWrap;
import tpa.joml.Interpolationf;
import tpa.joml.Matrix4f;
import tpa.joml.Vector2f;
import tpa.timing.Time;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * Created by germangb on 14/04/16.
 */
public class CompositeMaterial extends Material {

    private static String VERT = "#version 120\n" +
            "\n" +
            "attribute vec3 a_position;\n" +
            "\n" +
            "varying vec2 v_uv;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = vec4(a_position, 1.0);\n" +
            "    v_uv = a_position.xy*0.5+0.5;\n" +
            "}";

    private static String FRAG = "#version 120\n" +
            "\n" +
            "varying vec2 v_uv;\n" +
            "\n" +
            "uniform float u_timer;\n" +
            "uniform float u_aspect;\n" +
            "uniform float u_noise;\n" +
            "\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_normal;\n" +
            "uniform sampler2D u_random;\n" +
            "\n" +
            "float smoothstep (float edge0, float edge1, float x) {\n" +
            "    float t;  /* Or genDType t; */\n" +
            "    t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);\n" +
            "    return t * t * (3.0 - 2.0 * t);\n" +
            "}\n" +
            "\n" +
            "void main () {\n" +
            "    vec3 color = texture2D(u_texture, v_uv).rgb;\n" +
            "    vec3 normal = texture2D(u_normal, v_uv).rgb * 2.0 - 1.0;\n" +
            "    float rand = texture2D(u_random, v_uv*vec2(640, 480)/4/vec2(64)).r * 2 - 1;\n" +
            "    \n" +
            "    float diff = clamp(dot(normal, normalize(vec3(-1, 3, 2))), 0.0, 1.0);\n" +
            "    diff = mix(0.5, 1.0, diff);\n" +
            "    \n" +
            "    vec3 final_color = color;\n" +
            "    float vignet = smoothstep(2.0, 0.25, length(v_uv*2-1));\n" +
            "    \n" +
            "    float bars = smoothstep(0.7+0.01, 0.7, -(v_uv.y*2-1));\n" +
            "    \n" +
            "    gl_FragColor = vec4(final_color*vignet*diff + rand*u_noise, 1.0);\n" +
            "    \n" +
            "    gl_FragColor.rgb = mix(gl_FragColor.rgb, vec3(0.0), exp(-u_timer * 0.35f));\n" +
            "    \n" +
            "    vec2 uv2 = v_uv*2-1;\n" +
            "    uv2.x *= u_aspect;\n" +
            "    float center = smoothstep(0.02, 0.025, length(uv2));\n" +
            "    gl_FragColor.rgb = mix(vec3(1.0) - gl_FragColor.rgb * 0.75, gl_FragColor.rgb, center);\n" +
            "}";

    private static ShaderProgram PROGRAM = new ShaderProgram(VERT, FRAG, Attribute.Position);

    private Texture diffuse;
    private Texture normal;
    private Time time;
    private Texture randTex;
    private float timer = 0, noise = 0;

    public CompositeMaterial (Texture diffuse, Texture normal, Time time) {
        super(PROGRAM);
        this.diffuse = diffuse;
        this.normal = normal;
        this.time = time;

        randTex = new Texture(64, 64, TextureFormat.Red);
        randTex.setWrapU(TextureWrap.Repeat);
        randTex.setWrapV(TextureWrap.Repeat);
        randTex.setKeepData(true);
        randTex.setData(ByteBuffer.allocateDirect(64*64).order(ByteOrder.nativeOrder()));
    }

    public void setNoise (float noise) {
        this.noise = noise;
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        timer += time.getFrameTime();
        updateRandom();
        renderer.setState(state);
        renderer.setShaderProgram(program);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);
        program.setUniform("u_normal", UniformType.Sampler2D, 1);
        program.setUniform("u_random", UniformType.Sampler2D, 2);
        program.setUniform("u_timer", UniformType.Float, timer);
        program.setUniform("u_aspect", UniformType.Float, (float)diffuse.getWidth()/diffuse.getHeight());
        program.setUniform("u_noise", UniformType.Float, noise);
        renderer.setTexture(0, diffuse);
        renderer.setTexture(1, normal);
        renderer.setTexture(2, randTex);
        renderer.renderMesh(mesh);
    }

    public void setTimer (float timer) {
        this.timer = timer;
    }

    private static Random rand = new Random();
    private float lastTime = 0;

    private void updateRandom() {
        if (time.getTime() - lastTime > 0.0125f) {
            lastTime = time.getTime();
        } else return;

        ByteBuffer data = randTex.getData();
        while (data.remaining() > 0) {
            float rnd = rand.nextFloat();
            byte val = (byte) (255 * rnd);
            //if (rand.nextInt() > 0.75) val = (byte) 255;
            data.put(val);
        }
        data.flip();
        randTex.setData(data);
    }
}
