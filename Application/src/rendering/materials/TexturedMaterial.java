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
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "void main () {\n" +
            "    mat4 view_model = u_view * u_model;\n" +
            "    gl_Position = u_projection * u_view * u_model * vec4(a_position, 1.0);\n" +
            "    v_normal = normalize((view_model * vec4(a_normal, 0.0)).xyz);\n" +
            "    v_uv = a_uv;\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 120\n" +
            "\n" +
            "varying vec2 v_uv;\n" +
            "varying vec3 v_normal;\n" +
            "\n" +
            "uniform sampler2D u_texture;\n" +
            "\n" +
            "void main () {\n" +
            "    vec3 color = texture2D(u_texture, v_uv).rgb;\n" +
            "    \n" +
            "    gl_FragData[0] = vec4(color, 1.0);\n" +
            "    gl_FragData[1] = vec4(v_normal * 0.5 + 0.5, 1.0);\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position, Attribute.Uv, Attribute.Normal);

    /** material texture */
    private Texture texture;

    /** Creates a Lambert material */
    public TexturedMaterial(Texture texture) {
        super(PROGRAM);
        this.texture = texture;
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
        program.setUniform("u_texture", UniformType.Sampler2D, 0);

        // bind texture
        renderer.setTexture(0, texture);

        // render mesh
        renderer.renderMesh(mesh);
    }
}
