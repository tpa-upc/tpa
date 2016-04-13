package rendering;

import tpa.graphics.geometry.Attribute;
import tpa.graphics.render.Blending;
import tpa.graphics.render.Culling;
import tpa.graphics.render.RenderMode;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;

/**
 * Created by germangb on 13/04/16.
 */
public class WireframeMaterial extends Material {

    /** vertex shader */
    private static String VERTEX = "#version 130\n" +
            "\n" +
            "in vec3 a_position;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 130\n" +
            "\n" +
            "out vec4 frag_color;\n" +
            "\n" +
            "void main () {\n" +
            "    frag_color = vec4(1.0);\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position);

    /** Creates a Lambert material */
    public WireframeMaterial() {
        super(PROGRAM);
    }

    @Override
    public void render(Renderer renderer, Camera camera, GeometryActor actor) {
        // set shader
        renderer.setShaderProgram(program);

        renderer.setDepthMask(true);
        renderer.setColorMask(true, true, true, true);
        renderer.setRenderMode(RenderMode.Wireframe);
        renderer.setCulling(Culling.BackFace);
        renderer.setBlending(Blending.Disabled);

        // transform uniforms
        program.setUniform("u_projection", UniformType.Matrix4, camera.projection);
        program.setUniform("u_view", UniformType.Matrix4, camera.view);
        program.setUniform("u_model", UniformType.Matrix4, actor.model);

        // render mesh
        renderer.renderMesh(actor.getMesh());
    }
}
