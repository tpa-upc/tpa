package rendering.materials;

import rendering.Camera;
import rendering.materials.Material;
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
public class DecalMaterial extends Material {

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
            "uniform sampler2D u_diffuse;\n" +
            "uniform sampler2D u_depth;\n" +
            "uniform sampler2D u_reflective;\n" +
            "uniform sampler2D u_reflective_map;\n" +
            "uniform int u_has_reflective;\n" +
            "\n" +
            "uniform mat4 u_inv_mvp;\n" +
            "uniform vec2 u_resolution;\n" +
            "\n" +
            "float rand(vec2 co){\n" +
            "    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
            "}\n" +
            "void main () {\n" +
            "    // reconstruct from screenspace\n" +
            "    vec2 uv = gl_FragCoord.xy / u_resolution;\n" +
            "    float z = texture2D(u_depth, uv).r;\n" +
            "    vec4 clip = vec4(uv*2-1, z*2-1, 1);\n" +
            "    vec4 model = u_inv_mvp * clip;\n" +
            "    model.xyz /= model.w;\n" +
            "    \n" +
            "    if (abs(model.x) > 1 || abs(model.z) > 1 || abs(model.y) > 1)\n" +
            "        discard;\n" +
            "        \n" +
            "    vec2 tex_uv = model.xz*0.5+0.5;\n" +
            "    vec4 diff = texture2D(u_diffuse, tex_uv).rgba;\n" +
            "\n" +
            "    if (u_has_reflective == 1) {\n" +
            "        float cont = texture2D(u_reflective, tex_uv).r;\n" +
            "        vec2 refl_uv = gl_FragCoord.xy/u_resolution;\n" +
            "        refl_uv.y = 1-refl_uv.y;// + sin(refl_uv.y * 256) * 0.005;\n" +
            "        vec3 reflection = texture2D(u_reflective_map, refl_uv + vec2(rand(gl_FragCoord.xy)*2-1, rand(gl_FragCoord.xy)*2-1)*0.0075).rgb;\n" +
            "        diff.rgb = mix(diff.rgb, reflection, cont);\n" +
            "    }\n" +
            "    gl_FragData[0] = vec4(diff.rgb, diff.a);\n" +
            "    gl_FragData[1] = vec4(0.0);\n" +
            "}";

    /** shader program */
    private static ShaderProgram PROGRAM = new ShaderProgram(VERTEX, FRAGMENT, Attribute.Position);

    /** Decal texture */
    private Texture diffuse;

    /** Depth texture */
    private Texture depth;

    /** reflective map */
    private Texture reflective = null;
    private Texture reflectiveMap = null;
    public boolean discardReflectPass = false;

    /** Creates a Lambert material */
    public DecalMaterial(Texture diffuse, Texture depth) {
        super(PROGRAM);
        this.diffuse = diffuse;
        this.depth = depth;

        state.culling = Culling.BackFace;
        state.blending = Blending.Alpha;
        state.depthMask = false;
        state.depthTest = true;
    }

    public void setReflective (Texture map, Texture render) {
        reflective = map;
        reflectiveMap = render;
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
        program.setUniform("u_inv_mvp", UniformType.Matrix4, iMvp.set(camera.viewProjection).mul(model).invert());

        // send resolution
        program.setUniform("u_resolution", UniformType.Vector2, new Vector2f(depth.getWidth(), depth.getHeight()));
        program.setUniform("u_reflective", UniformType.Sampler2D, 2);
        program.setUniform("u_reflective_map", UniformType.Sampler2D, 3);
        program.setUniform("u_has_reflective", UniformType.Integer, reflective==null ? 0 : 1);
        if (reflective != null)
            program.setUniform("u_resolution", UniformType.Vector2, new Vector2f(reflectiveMap.getWidth(), reflectiveMap.getHeight()));

        if (reflective != null) {
            renderer.setTexture(2, reflective);
            renderer.setTexture(3, reflectiveMap);
        }
        // render mesh
        renderer.renderMesh(mesh);
    }
}
