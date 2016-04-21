package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Blending;
import tpa.graphics.render.Culling;
import tpa.graphics.render.RenderMode;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.joml.Matrix4f;

/**
 * Created by germangb on 13/04/16.
 */
public class GridMaterial extends Material {

    /** vertex shader */
    private static String VERTEX = "#version 120\n" +
            "\n" +
            "attribute vec3 a_position;\n" +
            "\n" +
            "varying vec3 v_view_position;\n" +
            "varying vec2 v_grid;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "void main () {\n" +
            "    vec4 pos_model = u_model * vec4(a_position, 1.0);\n" +
            "    gl_Position = u_projection * u_view * pos_model;\n" +
            "    v_grid = pos_model.xz;\n" +
            "    v_view_position = (u_view * pos_model).xyz;\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 120\n" +
            "\n" +
            "varying vec2 v_grid;\n" +
            "varying vec3 v_view_position;\n" +
            "\n" +
            "float smoothstep (float edge0, float edge1, float x) {\n" +
            "    float t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);\n" +
            "    return t * t * (3.0 - 2.0 * t);\n" +
            "}\n" +
            "\n" +
            "void main () {\n" +
            "    float zl = abs(v_view_position.z);" +
            "    if (zl > 32) discard;" +
            "    vec3 color = vec3(0.5);\n" +
            "    \n" +
            "    vec2 grid_1 = fract(v_grid + 0.5) - 0.5;\n" +
            "    vec2 grid_4 = fract(v_grid / 4.0) - 0.5;\n" +
            "    \n" +
            "    color = mix(color * 0.9, color, smoothstep(-0.025, 0.025, min(abs(grid_1.x), abs(grid_1.y)) - 0.05));\n" +
            "    color = mix(color * 0.85, color, smoothstep(-0.025 / 4, 0.025 / 4, min(abs(grid_4.x), abs(grid_4.y)) - 0.05 / 4));\n" +
            "    color = mix(vec3(1, 0.25, 0.25), color, smoothstep(-0.025, 0.025, abs(v_grid.x) - 0.05));\n" +
            "    color = mix(vec3(0.25, 0.25, 1), color, smoothstep(-0.025, 0.025, abs(v_grid.y) - 0.05));\n" +
            "    \n" +
            "    gl_FragColor = vec4(color, smoothstep(32, 0, zl));\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position);

    /** Creates a Lambert material */
    public GridMaterial() {
        super(PROGRAM);
        state.culling = Culling.Disabled;
        state.depthTest = true;
        state.depthMask = false;
        state.blending = Blending.Alpha;
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

        // render mesh
        renderer.renderMesh(mesh);
    }
}
