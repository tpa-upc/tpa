package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Culling;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.joml.Matrix4f;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 13/04/16.
 */
public class LambertMaterial extends Material {

    /** vertex shader */
    private static String VERTEX = "#version 130\n" +
            "\n" +
            "in vec3 a_position;\n" +
            "in vec3 a_normal;\n" +
            "\n" +
            "out vec3 v_position;\n" +
            "out vec3 v_normal;\n" +
            "out vec3 v_light;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "uniform vec3 u_light;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);\n" +
            "    v_normal = normalize((u_view * u_model * vec4(a_normal, 0.0)).xyz);\n" +
            "    v_position = (u_view * u_model * vec4(a_position, 1.0)).xyz;\n" +
            "    v_light = (u_view * vec4(u_light, 1.0)).xyz;\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 130\n" +
            "\n" +
            "in vec3 v_position;\n" +
            "in vec3 v_normal;\n" +
            "in vec3 v_light;\n" +
            "\n" +
            "out vec4 frag_color;\n" +
            "\n" +
            "uniform vec3 u_ambient;\n" +
            "uniform vec3 u_diffuse;\n" +
            "uniform vec3 u_specular;\n" +
            "uniform float u_hardness;\n" +
            "uniform float u_reflective;\n" +
            "\n" +
            "void main () {\n" +
            "    vec3 surf2eye = normalize(-v_position);\n" +
            "    vec3 surf2light = normalize(v_light-v_position);\n" +
            "    \n" +
            "    float diff_comp = clamp(dot(v_normal, surf2light), 0.0, 1.0);\n" +
            "    \n" +
            "    vec3 reflected = reflect(surf2eye, v_normal);\n" +
            "    float spec = clamp(dot(-reflected, surf2light), 0.0, 1.0);\n" +
            "    spec = pow(spec, u_hardness)*u_reflective;\n" +
            "    \n" +
            "    frag_color = vec4(u_ambient + u_diffuse*diff_comp + u_specular*spec, 1.0);\n" +
            "}";

    /** shader program */
    private static ShaderProgram LAMBERT_PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position, Attribute.Normal);

    /** Ambient color */
    public final Vector3f ambient = new Vector3f(0.1f);

    /** Diffuse color */
    public final Vector3f diffuse = new Vector3f(1);

    /** Specular color */
    public final Vector3f specular = new Vector3f(1);

    /** Light position */
    public final Vector3f light = new Vector3f(0);

    /** surface hardness */
    public float hardness = 32f;

    /** reflective scalar factor */
    public float reflective = 1f;

    /** Creates a Lambert material */
    public LambertMaterial() {
        super(LAMBERT_PROGRAM);
        state.culling = Culling.BackFace;
        state.depthTest = true;
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
        program.setUniform("u_light", UniformType.Vector3, light);
        program.setUniform("u_hardness", UniformType.Float, hardness);
        program.setUniform("u_reflective", UniformType.Float, reflective);

        // set color uniforms
        program.setUniform("u_ambient", UniformType.Vector3, ambient);
        program.setUniform("u_diffuse", UniformType.Vector3, diffuse);
        program.setUniform("u_specular", UniformType.Vector3, specular);

        // render mesh
        renderer.renderMesh(mesh);
    }

}
