package com.graphics.shader;

/**
 * Created by german on 26/03/2016.
 */
public class Uniform {

    /** program */
    private ShaderProgram program;

    /** type of the uniform value */
    private UniformType type;

    /** data being held by the uniform */
    private Object value;

    /** Uniform name */
    private String name;

    public Uniform (ShaderProgram program, UniformType type, String name, Object value) {
        type.assettValue(value);
        this.program = program;
        this.type = type;
        this.value = value;
        this.name = name;
    }

    /**
     * Get uniform name
     * @return uniform name
     */
    public String getName () {
        return name;
    }

    /**
     * Get uniform data type
     * @return data type
     */
    public UniformType getType () {
        return type;
    }

    /**
     * Get uniform value
     * @return uniform value
     */
    public Object getValue () {
        return value;
    }

    /**
     * Set value
     * @param val new value
     */
    public void setValue (Object val) {
        type.assettValue(val);
        this.value = val;
        program.setUniformsDirty(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Uniform uniform = (Uniform) o;

        if (type != uniform.type) return false;
        return name.equals(uniform.name);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
