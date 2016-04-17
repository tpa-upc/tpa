package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Culling;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.joml.Matrix4f;

/**
 * Created by germangb on 13/04/16.
 */
public class OutlineMaterial extends Material {

    /** vertex shader */
    private static String VERTEX = "#version 120\n" +
            "\n" +
            "attribute vec3 a_position;\n" +
            "attribute vec3 v_normal;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "uniform int u_outline;\n" +
            "\n" +
            "void main () {\n" +
            "    vec3 pos = a_position;\n" +
            "    if (u_outline == 1)\n" +
            "        pos += v_normal*0.025;\n" +
            "    \n" +
            "    gl_Position = u_projection * u_view * u_model * vec4(pos, 1.0);\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 120\n" +
            "\n" +
            "uniform int u_outline;\n" +
            "\n" +
            "void main () {\n" +
            "\n" +
            "    if (u_outline == 1) {\n" +
            "        gl_FragColor = vec4(0, 0, 0, 1);\n" +
            "    } else {\n" +
            "        gl_FragColor = vec4(1.0);\n" +
            "    }\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position, Attribute.Normal);

    /** Creates a Lambert material */
    public OutlineMaterial() {
        super(PROGRAM);
        state.culling = Culling.Disabled;
        state.depthTest = true;
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        // set shader
        renderer.setShaderProgram(program);

        // transform uniforms
        program.setUniform("u_projection", UniformType.Matrix4, camera.projection);
        program.setUniform("u_view", UniformType.Matrix4, camera.view);
        program.setUniform("u_model", UniformType.Matrix4, model);

        // render outline
        state.culling = Culling.FrontFace;
        renderer.setState(state);
        program.setUniform("u_outline", UniformType.Integer, 1);
        renderer.renderMesh(mesh);

        // render model
        state.culling = Culling.BackFace;
        renderer.setState(state);
        program.setUniform("u_outline", UniformType.Integer, 0);
        renderer.renderMesh(mesh);
    }
}
