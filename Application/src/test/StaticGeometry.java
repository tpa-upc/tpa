package test;

/**
 * Created by germangb on 01/04/16.
 */
public class StaticGeometry {

    public static float[] CUBE_POSITION = new float[] {
            // bottom face
            -1, -1, -1,
            +1, -1, -1,
            +1, -1, +1,
            -1, -1, +1,

            // top face
            -1, +1, -1,
            +1, +1, -1,
            +1, +1, +1,
            -1, +1, +1,

            // left face
            -1, -1, -1,
            -1, +1, -1,
            -1, +1, +1,
            -1, -1, +1,

            // right face
            +1, -1, -1,
            +1, +1, -1,
            +1, +1, +1,
            +1, -1, +1,

            // back face
            -1, -1, -1,
            +1, -1, -1,
            +1, +1, -1,
            -1, +1, -1,

            // front face
            -1, -1, +1,
            +1, -1, +1,
            +1, +1, +1,
            -1, +1, +1
    };

    public static float[] CUBE_NORMAL = new float[] {
            // bottom face
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,

            // top face
            0, +1, 0,
            0, +1, 0,
            0, +1, 0,
            0, +1, 0,

            // left face
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,

            // right face
            +1, 0, 0,
            +1, 0, 0,
            +1, 0, 0,
            +1, 0, 0,

            //back face
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,

            // front face
            0, 0, +1,
            0, 0, +1,
            0, 0, +1,
            0, 0, +1
    };

    public static float[] CUBE_UV = new float[] {
            0, 0,
            1, 0,
            1, 1,
            0, 1,

            1, 0,
            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1,
            0, 1,

            0, 0,
            1, 0,
            1, 1,
            0, 1,

            0, 0,
            1, 0,
            1, 1,
            0, 1,

            0, 0,
            1, 0,
            1, 1,
            0, 1
    };

    public static short[] CUBE_INDICES = new short[] {
            // bottom
            0, 2, 1,
            0, 3, 2,

            // top
            4, 5, 6,
            4, 6, 7,

            // left
            8, 9, 10,
            8, 10, 11,

            // right
            12, 14, 13,
            12, 15, 14,

            // back
            16, 17, 18,
            16, 18, 19,

            // front
            20, 22, 21,
            20, 23, 22
    };

    // -----------------------------------------------------------------------------------------------------------------

    public  static float[] PLANE_POSITION = new float[] {
            -512, 0, -512,
            +512, 0, -512,
            +512, 0, +512,
            -512, 0, +512
    };

    public static float[] PLANE_NORMAL = new float[] {
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
    };

    public static float[] PLANE_UV = new float[] {
            0, 0,
            128, 0,
            128, 128,
            0, 128
    };

    public static short[] PLANE_INDICES = new short[] {
            0, 1, 2, 3  // triangle strip
    };

    private StaticGeometry () {}
}
