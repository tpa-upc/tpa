package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFilter;
import tpa.graphics.texture.TextureFormat;
import tpa.graphics.texture.TextureWrap;
import tpa.joml.Matrix4f;
import tpa.joml.Vector2f;
import tpa.timing.Time;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * Created by germangb on 14/04/16.
 */
public class GrainMaterial extends Material {

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
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_random;\n" +
            "uniform vec2 u_resolution;\n" +
            "\n" +
            "void main () {\n" +
            "    float rand0 = texture2D(u_random, v_uv*u_resolution/1/vec2(256)).r*2-1;\n" +
            "    float rand1 = texture2D(u_random, v_uv*u_resolution/2/vec2(256) + vec2(0.125671, 0.7235)).r*2-1;\n" +
            "    vec3 color = texture2D(u_texture, v_uv).rgb;\n" +
            "    color = mix(color + rand0*0.05, color*0.75, (rand0*0.5 + rand1*0.5)*0.5+0.5);\n" +
            "    gl_FragColor = vec4(pow(color, vec3(0.85)), 1.0);\n" +
            "}";

    private static ShaderProgram PROGRAM = new ShaderProgram(VERT, FRAG, Attribute.Position);

    private Texture diffuse;
    private Texture randTex;
    private Time time;
    private Vector2f resolution;

    public GrainMaterial(Texture diffuse, Time time, Vector2f res) {
        super(PROGRAM);
        this.resolution = res;
        this.time = time;
        this.diffuse = diffuse;
        randTex = new Texture(256, 256, TextureFormat.Red);
        randTex.setWrapU(TextureWrap.Repeat);
        randTex.setWrapV(TextureWrap.Repeat);
        randTex.setMag(TextureFilter.Linear);
        randTex.setKeepData(true);
        randTex.setData(ByteBuffer.allocateDirect(256*256).order(ByteOrder.nativeOrder()));
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        updateRandom();
        renderer.setState(state);
        renderer.setShaderProgram(program);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);
        program.setUniform("u_random", UniformType.Sampler2D, 1);
        program.setUniform("u_resolution", UniformType.Vector2, resolution);
        renderer.setTexture(0, diffuse);
        renderer.setTexture(1, randTex);
        renderer.renderMesh(mesh);
    }

    private static Random rand = new Random();
    private float lastTime = 0;

    private void updateRandom() {
        if (time.getTime() - lastTime > 0.05f) {
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
