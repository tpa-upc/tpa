package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Culling;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Texture;
import tpa.joml.Matrix4f;
import tpa.joml.Vector2f;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 13/04/16.
 */
public class TexturedMaterial extends Material {

    /** vertex shader */
    private static String VERTEX = "#version 120\n" +
            "\n" +
            "attribute vec3 a_position;\n" +
            "attribute vec3 a_normal;\n" +
            "attribute vec2 a_uv;\n" +
            "\n" +
            "varying vec2 v_uv;\n" +
            "varying vec3 v_normal;\n" +
            "varying vec3 v_position;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "void main () {\n" +
            "    mat4 view_model = u_view * u_model;\n" +
            "    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);\n" +
            "    v_normal = normalize((view_model * vec4(a_normal, 0.0)).xyz);\n" +
            "    v_position = (view_model * vec4(a_position, 1.0)).xyz;\n" +
            "    v_uv = a_uv;\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 120\n" +
            "\n" +
            "varying vec2 v_uv;\n" +
            "varying vec3 v_normal;\n" +
            "varying vec3 v_position;\n" +
            "\n" +
            "uniform vec2 u_resolution;\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_reflective;\n" +
            "uniform sampler2D u_reflective_map;\n" +
            "uniform int u_has_reflective;\n" +
            "uniform vec3 u_tint;\n" +
            "\n" +
            "float rand(vec2 co){\n" +
            "    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
            "}\n" +
            "\n" +
            "void main () {\n" +
            "    vec3 color = texture2D(u_texture, v_uv).rgb;\n" +
            "    \n" +
            "    if (u_has_reflective == 1) {\n" +
            "        float cont = texture2D(u_reflective, v_uv).r;\n" +
            "        vec2 refl_uv = gl_FragCoord.xy/u_resolution;\n" +
            "        refl_uv.x = 1-refl_uv.x;// + sin(refl_uv.y * 256) * 0.005;\n" +
            "        vec3 reflection = texture2D(u_reflective_map, refl_uv + vec2(rand(gl_FragCoord.xy)*2-1, rand(gl_FragCoord.xy)*2-1)*0.0075).rgb;\n" +
            "        color = mix(color, reflection, cont);\n" +
            "    }\n" +
            "    \n" +
            "    gl_FragData[0] = vec4(mix(color*u_tint, vec3(0.0), 1-exp(-max(0, length(v_position)-4) * 0.1)), 1.0);\n" +
            "    gl_FragData[1] = vec4(v_normal * 0.5 + 0.5, 1.0);\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position, Attribute.Uv, Attribute.Normal);

    /** material texture */
    private Texture texture;

    /** reflective map */
    private Texture reflective = null;
    private Texture reflectiveMap = null;
    public boolean discardReflectPass = false;

    /** Tint */
    private Vector3f tint = new Vector3f(1);

    /** Creates a Lambert material */
    public TexturedMaterial(Texture texture) {
        super(PROGRAM);
        this.texture = texture;
        state.culling = Culling.BackFace;
        state.depthTest = true;
    }

    public void setReflective (Texture map, Texture render) {
        reflective = map;
        reflectiveMap = render;
    }

    /**
     * Set material tint color
     * @param r red
     * @param g green
     * @param b blue
     */
    public void setTint (float r, float g, float b) {
        tint.set(r, g, b);
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        // set shader
        renderer.setShaderProgram(program);

        renderer.setState(state);

        // transform uniforms
        program.setUniform("u_projection", UniformType.Matrix4, camera.projection);
        program.setUniform("u_view", UniformType.Matrix4, camera.view);
        program.setUniform("u_model", UniformType.Matrix4, model);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);
        program.setUniform("u_reflective", UniformType.Sampler2D, 1);
        program.setUniform("u_reflective_map", UniformType.Sampler2D, 2);
        program.setUniform("u_has_reflective", UniformType.Integer, reflective==null ? 0 : 1);
        if (reflective != null)
            program.setUniform("u_resolution", UniformType.Vector2, new Vector2f(reflectiveMap.getWidth(), reflectiveMap.getHeight()));
        program.setUniform("u_tint", UniformType.Vector3, tint);

        // bind texture
        renderer.setTexture(0, texture);

        if (reflective != null) {
            renderer.setTexture(1, reflective);
            renderer.setTexture(2, reflectiveMap);
        }

        // render mesh
        renderer.renderMesh(mesh);
    }
}
