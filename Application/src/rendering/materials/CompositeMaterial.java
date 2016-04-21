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
            "\n" +
            "uniform vec2 u_resolution;\n" +
            "\n" +
            "float rand(vec2 co){\n" +
            "    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
            "}\n" +
            "\n" +
            "void main () {\n" +
            "    float noise = rand(floor(gl_FragCoord.xy/2));\n" +
            "    vec3 dither = texture2D(u_dither, gl_FragCoord.xy/vec2(8.0*1)).rrr;\n" +
            "    vec3 color = texture2D(u_texture, v_uv).rgb;\n" +
            "    vec3 dithered = step(dither, color);\n" +
            "    gl_FragColor = vec4(mix(color, dithered, 0.0), 1.0);\n" +
            "    gl_FragColor.rgb = pow(gl_FragColor.rgb, vec3(0.77));" +
            "}";

    private static ShaderProgram PROGRAM = new ShaderProgram(VERT, FRAG, Attribute.Position);

    private Texture diffuse, dither;
    public Vector2f resolution;

    public CompositeMaterial (Texture diffuse, Texture dither, Vector2f resolution) {
        super(PROGRAM);
        this.diffuse = diffuse;
        this.dither = dither;
        this.resolution = resolution;
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        renderer.setState(state);
        renderer.setShaderProgram(program);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);
        program.setUniform("u_dither", UniformType.Sampler2D, 1);
        program.setUniform("u_resolution", UniformType.Vector2, resolution);
        renderer.setTexture(0, diffuse);
        renderer.setTexture(1, dither);
        renderer.renderMesh(mesh);
    }
}
