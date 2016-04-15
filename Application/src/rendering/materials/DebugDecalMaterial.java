package rendering.materials;

import rendering.Camera;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Blending;
import tpa.graphics.render.Culling;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Texture;
import tpa.joml.Matrix4f;
import tpa.joml.Vector2f;

/**
 * Created by germangb on 13/04/16.
 */
public class DebugDecalMaterial extends Material {

    /** vertex shader */
    private static String VERTEX = "#version 130\n" +
            "\n" +
            "in vec3 a_position;\n" +
            "in vec3 a_normal;\n" +
            "\n" +
            "out vec3 v_normal;\n" +
            "out vec3 v_position;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "void main () {\n" +
            "    vec4 mv_pos = u_view * u_model * vec4(a_position, 1.0);\n" +
            "    gl_Position = u_projection * mv_pos;\n" +
            "    v_normal = normalize((u_view * u_model * vec4(a_normal, 0.0)).xyz);\n" +
            "    v_position = mv_pos.xyz;\n" +
            "}";

    /** fragment shader */
    private static String FRAGMENT = "#version 130\n" +
            "\n" +
            "in vec3 v_normal;\n" +
            "in vec3 v_position;\n" +
            "\n" +
            "out vec4 frag_color;\n" +
            "\n" +
            "uniform sampler2D u_diffuse;\n" +
            "uniform sampler2D u_depth;\n" +
            "\n" +
            "uniform mat4 u_inv_mvp;\n" +
            "uniform vec2 u_resolution;\n" +
            "uniform int u_box;\n" +
            "\n" +
            "void main () {\n" +
            "    if (u_box == 0) {\n" +
            "        // reconstruct from screenspace\n" +
            "        vec2 uv = gl_FragCoord.xy / u_resolution;\n" +
            "        float z = texture2D(u_depth, uv).r;\n" +
            "        vec4 clip = vec4(uv*2-1, z*2-1, 1);\n" +
            "        vec4 model = u_inv_mvp * clip;\n" +
            "        model.xyz /= model.w;\n" +
            "        \n" +
            "        vec2 tex_uv = model.xz*0.5+0.5;\n" +
            "        vec4 diff = texture2D(u_diffuse, tex_uv).rgba;\n" +
            "        \n" +
            "        //frag_color = vec4(diff.rgb, diff.a);\n" +
            "        frag_color = vec4(0);\n" +
            "        \n" +
            "        if (abs(model.x) < 1 && abs(model.z) < 1) {\n" +
            "            vec2 grid = fract(tex_uv * 4 - 0.5) - 0.5;\n" +
            "            float g = smoothstep(-0.025, 0.025, abs(grid.x)-0.05);\n" +
            "            float gy = smoothstep(-0.025, 0.025, abs(grid.y)-0.05);\n" +
            "            frag_color = mix(vec4(0,0,0,0.5), vec4(0), min(g, gy));\n" +
            "        }\n" +
            "        \n" +
            "        frag_color = mix(frag_color, vec4(diff.rgb, diff.a), diff.a);\n" +
            "    } else {    \n" +
            "        float light = clamp(dot(v_normal, -normalize(v_position)), 0.0, 1.0);\n" +
            "        light = mix(0, 1, light);\n" +
            "        frag_color = vec4(vec3(0.63, 0.76, 1)*light + vec3(0.1)*pow(light, 1), 0.5);\n" +
            "    }\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position, Attribute.Normal);

    /** Decal texture */
    private Texture diffuse;

    /** Depth texture */
    private Texture depth;

    /** Creates a Lambert material */
    public DebugDecalMaterial(Texture diffuse, Texture depth) {
        super(PROGRAM);
        this.diffuse = diffuse;
        this.depth = depth;

        state.culling = Culling.BackFace;
        state.blending = Blending.Alpha;
        state.depthMask = false;
        state.depthTest = true;
    }

    private static Matrix4f iMvp = new Matrix4f();

    @Override
    public void render(Renderer renderer, Camera camera, Mesh mesh, Matrix4f model) {
        // set shader
        renderer.setShaderProgram(program);

        renderer.setState(state);

        // set textures
        program.setUniform("u_diffuse", UniformType.Sampler2D, 0);
        program.setUniform("u_depth", UniformType.Sampler2D, 1);

        renderer.setTexture(0, diffuse);
        renderer.setTexture(1, depth);

        // transform uniforms
        program.setUniform("u_projection", UniformType.Matrix4, camera.projection);
        program.setUniform("u_view", UniformType.Matrix4, camera.view);
        program.setUniform("u_model", UniformType.Matrix4, model);

        // compute mvp and invert
        iMvp.set(camera.projection).mul(camera.view).mul(model).invert();
        program.setUniform("u_inv_mvp", UniformType.Matrix4, iMvp);

        // send resolution
        program.setUniform("u_resolution", UniformType.Vector2, new Vector2f(depth.getWidth(), depth.getHeight()));

        // render mesh
        program.setUniform("u_box", UniformType.Integer, 0);
        renderer.renderMesh(mesh);

        // render box
        program.setUniform("u_box", UniformType.Integer, 1);
        renderer.renderMesh(mesh);
    }
}
