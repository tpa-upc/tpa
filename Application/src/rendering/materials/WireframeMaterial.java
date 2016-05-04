package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
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
public class WireframeMaterial extends Material {

    /** vertex shader */
    private static String VERTEX = "#version 120\n" +
            "\n" +
            "attribute vec3 a_position;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 120\n" +
            "\n" +
            "uniform vec3 u_tint;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_FragColor = vec4(u_tint, 1.0);\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position);

    /** wireframe tint */
    private Vector3f tint = new Vector3f(1);

    /** Creates a Lambert material */
    public WireframeMaterial() {
        super(PROGRAM);
        state.culling = Culling.Disabled;
        state.renderMode = RenderMode.Wireframe;
        state.depthTest = true;
    }

    public void setTint (float r, float g, float b) {
        tint.set(r, g, b);
    }

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        // set shader
        renderer.setShaderProgram(program);

        renderer.setState(state);

        /*renderer.setDepthMask(true);
        renderer.setColorMask(true, true, true, true);
        renderer.setRenderMode(RenderMode.Wireframe);
        renderer.setCulling(Culling.BackFace);
        renderer.setBlending(Blending.Disabled);*/

        // transform uniforms
        program.setUniform("u_projection", UniformType.Matrix4, camera.projection);
        program.setUniform("u_view", UniformType.Matrix4, camera.view);
        program.setUniform("u_model", UniformType.Matrix4, model);
        program.setUniform("u_tint", UniformType.Vector3, tint);

        // render mesh
        renderer.renderMesh(mesh);
    }
}
