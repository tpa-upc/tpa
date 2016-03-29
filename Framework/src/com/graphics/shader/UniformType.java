package com.graphics.shader;

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
    Vector2(com.joml.Vector2f.class),

    /** vec2[] */
    Vector2Array(com.joml.Vector2f[].class),

    /** vec3 */
    Vector3(com.joml.Vector3f.class),

    /** vec3[] */
    Vector3Array(com.joml.Vector3f[].class),

    /** vec4 */
    Vector4(com.joml.Vector4f.class),

    /** vec4[] */
    Vector4Array(com.joml.Vector4f[].class),

    /** mat3 */
    Matrix3(com.joml.Matrix3f.class),

    /** mat3[] */
    Matrix3Array(com.joml.Matrix3f[].class),

    /** mat4 */
    Matrix4(com.joml.Matrix4f.class),

    /** mat4[] */
    Matrix4Array(com.joml.Matrix4f[].class);

    /** type in java form */
    private Class<?> type;

    UniformType(Class<?> type) {
        this.type = type;
    }

    /**
     * Throw a runtime exception if the passed value doesn't match the type
     * of the uniform type
     * @param val test value
     */
    void assettValue (Object val) {
        if (val.getClass() != type)
            throw new IllegalArgumentException();
    }
}
