package tpa.graphics.shader;

import tpa.joml.*;

/**
 * Created by german on 26/03/2016.
 */
public enum UniformType {

    /** int */
    Sampler2D(java.lang.Integer.class),

    /** int */
    Integer(java.lang.Integer.class),

    /** float */
    Float(java.lang.Float.class),

    /** vec2 */
    Vector2(Vector2f.class),

    /** vec2[] */
    Vector2Array(Vector2f[].class),

    /** vec3 */
    Vector3(Vector3f.class),

    /** vec3[] */
    Vector3Array(Vector3f[].class),

    /** vec4 */
    Vector4(Vector4f.class),

    /** vec4[] */
    Vector4Array(Vector4f[].class),

    /** mat3 */
    Matrix3(Matrix3f.class),

    /** mat3[] */
    Matrix3Array(Matrix3f[].class),

    /** mat4 */
    Matrix4(Matrix4f.class),

    /** mat4[] */
    Matrix4Array(Matrix4f[].class);

    /** type in java form */
    private Class<?> type;

    UniformType(Class<?> type) {
        this.type = type;
    }

    /**
     * Throw a runtime exception if the passed value doesn't match the type
     * of the uniform type
     * @param val app value
     */
    void assettValue (Object val) {
        if (val.getClass() != type)
            throw new IllegalArgumentException();
    }
}
