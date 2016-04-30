package tpa.graphics.geometry;

/**
 * Created by german on 28/03/2016.
 */
public enum Attribute {

    /** x, y, z */
    Position(0, 3, "a_position"),   // vec3

    /** nx, ny, nz */
    Normal(1, 3, "a_normal"),   // vec3

    /** u, v */
    Uv(2, 2, "a_uv"),   // vec2

    /** r, g, b */
    Color(3, 4, "a_color"),   // vec3

    /** tx, ty, tz */
    Tangent(4, 3, "a_tangent"),   // vec3

    /** j0, j1, j2, j3 */
    Joints(5, 3, "a_joints"),   // ivec4

    /** w0, w1, w2, w3 */
    Weights(6, 3, "a_weights");   // vec4

    /** attribute name */
    private String name;

    /** size of the attribute in floats */
    private int size;

    /** attribute id */
    private int id;

    Attribute (int id, int size, String name) {
        this.id = id;
        this.size = size;
        this.name = name;
    }

    /**
     * Get attribute id
     * @return attribute id
     */
    public int getId () {
        return id;
    }

    /**
     * Get attribute name
     * @return
     */
    public String getName () {
        return name;
    }

    /**
     * Get size (or stride) of attribute in floats
     * @return size in floats
     */
    public int getSize () {
        return size;
    }
}
