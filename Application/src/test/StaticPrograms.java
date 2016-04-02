package test;

/**
 * Created by germangb on 01/04/16.
 */
public class StaticPrograms {

    //language=GLSL
    public static final String SHADOWMAP_VERT = "#version 130\n" +
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

    //language=GLSL
    public static final String SHADOWMAP_FRAG = "#version 130\n" +
            "void main () {\n" +
            "}";

    // -----------------------------------------------------------------------------------------------------------------

    //language=GLSL
    public static final String WIRE_VERT = "#version 130\n" +
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

    //language=GLSL
    public static final String WIRE_FRAG = "#version 130\n" +
            "\n" +
            "out vec4 frag_color;\n" +
            "\n" +
            "void main () {\n" +
            "    frag_color = vec4(1.0);\n" +
            "}";

    // -----------------------------------------------------------------------------------------------------------------

    //language=GLSL
    public static final String DIFFUSE_VERT = "#version 130\n" +
            "\n" +
            "in vec3 a_position;\n" +
            "in vec3 a_normal;\n" +
            "in vec2 a_uv;\n" +
            "\n" +
            "out vec2 v_uv;\n" +
            "out vec4 v_position;\n" +
            "out vec3 v_normal;\n" +
            "out vec4 v_shadow_position;\n" +
            "\n" +
            "uniform mat4 u_projection;\n" +
            "uniform mat4 u_view;\n" +
            "uniform mat4 u_model;\n" +
            "\n" +
            "uniform mat4 u_projection_shadow;\n" +
            "uniform mat4 u_view_shadow;\n" +
            "\n" +
            "void main () {\n" +
            "    vec4 model_trans = u_model * vec4(a_position, 1.0);\n" +
            "    gl_Position = u_projection * u_view * model_trans;\n" +
            "    v_position = model_trans;\n" +
            "    v_normal = normalize((u_model * vec4(a_normal, 0.0)).xyz);\n" +
            "    v_shadow_position = u_projection_shadow * u_view_shadow * u_model * vec4(a_position, 1.0);\n" +
            "    v_uv = a_uv;\n" +
            "}";

    //language=GLSL
    public static final String DIFFUSE_FRAG = "#version 130\n" +
            "\n" +
            "in vec2 v_uv;\n" +
            "in vec4 v_position;\n" +
            "in vec3 v_normal;\n" +
            "in vec4 v_shadow_position;\n" +
            "\n" +
            "out vec4 frag_color;\n" +
            "\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_shadowmap;\n" +
            "\n" +
            "void main () {\n" +
            "    vec2 shadow_uv = v_shadow_position.xy * 0.5 + 0.5;\n" +
            "    float bias = 0.005;\n" +
            "    float shade = 1;\n" +
            "    \n" +
            "    vec3 light_dir = normalize(vec3(1, 1, 1));\n" +
            "    float light = clamp(dot(light_dir, v_normal), 0.0, 1.0);\n" +
            "    \n" +
            "    if (length(v_position.xz) < 8) {\n" +
            "        for (int i = 0; i < 5; ++i) {\n" +
            "            float a = 3.1415*2*float(i)/5.0;\n" +
            "            if (texture2D(u_shadowmap, shadow_uv + vec2(sin(a), cos(a))*0.0005).r * 2 - 1 < v_shadow_position.z - bias)\n" +
            "                shade -= 1/5.0;\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    shade = min(shade, light);\n" +
            "    \n" +
            "    frag_color = vec4(texture2D(u_texture, v_uv).rgb*mix(0.25, 1, shade), 1.0);\n" +
            "}";

    private StaticPrograms () {
    }
}
