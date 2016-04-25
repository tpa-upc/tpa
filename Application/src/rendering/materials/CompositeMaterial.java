package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Texture;
import tpa.joml.Matrix4f;
import tpa.joml.Vector2f;
import tpa.timing.Time;

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
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_dither;\n" +
            "uniform sampler2D u_rgb;\n" +
            "uniform sampler2D u_noise;\n" +
            "uniform float u_time;\n" +
            "uniform vec2 u_resolution;\n" +
            "\n" +
            "void main () {\n" +
            "    vec3 rgb = texture2D(u_rgb, gl_FragCoord.xy/3).rgb;\n" +
            "    vec2 uv = v_uv;\n" +
            "    \n" +
            "    vec3 color = texture2D(u_texture, uv).rgb;\n" +
            "   \n" +
            "    gl_FragColor.rgb = pow(color, vec3(0.7));\n" +
            "}";

    private static ShaderProgram PROGRAM = new ShaderProgram(VERT, FRAG, Attribute.Position);

    private Texture diffuse, dither, rgb, noise;
    public Vector2f resolution;
    private Time time;

    public CompositeMaterial (Texture diffuse, Texture dither, Texture rgb, Texture noise, Vector2f resolution, Time time) {
        super(PROGRAM);
        this.diffuse = diffuse;
        this.dither = dither;
        this.rgb = rgb;
        this.resolution = resolution;
        this.time = time;
        this.noise = noise;
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        renderer.setState(state);
        renderer.setShaderProgram(program);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);
        program.setUniform("u_dither", UniformType.Sampler2D, 1);
        program.setUniform("u_rgb", UniformType.Sampler2D, 2);
        program.setUniform("u_noise", UniformType.Sampler2D, 3);
        program.setUniform("u_resolution", UniformType.Vector2, resolution);
        program.setUniform("u_time", UniformType.Float, time.getTime());
        renderer.setTexture(0, diffuse);
        renderer.setTexture(1, dither);
        renderer.setTexture(2, rgb);
        renderer.setTexture(3, noise);
        renderer.renderMesh(mesh);
    }
}
