package test;

/**
 * Created by germangb on 01/04/16.
 */
public class StaticPrograms {

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

    private StaticPrograms () {
    }
}
