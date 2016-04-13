package rendering;

import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Blending;
import tpa.graphics.render.Culling;
import tpa.graphics.render.RenderMode;
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
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);\n" +
            "    v_normal = normalize((u_view * u_model * vec4(a_normal, 0.0)).xyz);\n" +
            "    v_position = (u_view * u_model * vec4(a_position, 1.0)).xyz;\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 130\n" +
            "\n" +
            "in vec3 v_position;\n" +
            "in vec3 v_normal;\n" +
            "\n" +
            "out vec4 frag_color;\n" +
            "\n" +
            "uniform vec3 u_ambient;\n" +
            "uniform vec3 u_diffuse;\n" +
            "uniform vec3 u_specular;\n" +
            "\n" +
            "void main () {\n" +
            "    vec3 surf2eye = -normalize(v_position);\n" +
            "    vec3 surf2light = -normalize(v_position);\n" +
            "    \n" +
            "    float diff_comp = clamp(dot(v_normal, surf2light), 0.0, 1.0);\n" +
            "    \n" +
            "    frag_color = vec4(u_diffuse*diff_comp, 1.0);\n" +
            "}";

    /** shader program */
    private static ShaderProgram LAMBERT_PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position, Attribute.Normal);

    /** Ambient color */
    private Vector3f ambient = new Vector3f(0.1f);

    /** Diffuse color */
    private Vector3f diffuse = new Vector3f(1);

    /** Specular color */
    private Vector3f specular = new Vector3f(1);

    /** Creates a Lambert material */
    public LambertMaterial() {
        super(LAMBERT_PROGRAM);
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        // set shader
        renderer.setShaderProgram(program);

        renderer.setDepthMask(true);
        renderer.setColorMask(true, true, true, true);
        renderer.setRenderMode(RenderMode.Fill);
        renderer.setCulling(Culling.BackFace);
        renderer.setBlending(Blending.Disabled);

        // transform uniforms
        program.setUniform("u_projection", UniformType.Matrix4, camera.projection);
        program.setUniform("u_view", UniformType.Matrix4, camera.view);
        program.setUniform("u_model", UniformType.Matrix4, model);

        // set color uniforms
        program.setUniform("u_ambient", UniformType.Vector3, ambient);
        program.setUniform("u_diffuse", UniformType.Vector3, diffuse);
        program.setUniform("u_specular", UniformType.Vector3, specular);

        // render mesh
        renderer.renderMesh(mesh);
    }

    public Vector3f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector3f ambient) {
        this.ambient = ambient;
    }

    public Vector3f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector3f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector3f specular) {
        this.specular = specular;
    }
}
