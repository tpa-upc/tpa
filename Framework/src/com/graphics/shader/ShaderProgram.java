package com.graphics.shader;

import com.graphics.geometry.Attribute;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by german on 26/03/2016.
 */
public class ShaderProgram {

    /** Vertex shader source */
    private String vert;

    /** Fragment shader source */
    private String frag;

    /** List of set uniform variables */
    private Set<Uniform> uniforms = new HashSet<>();

    /** uniform state */
    private boolean uniformsDirty = false;

    /** shader attributes */
    private Attribute[] attrs;

    public ShaderProgram(String vert, String frag, Attribute... attrs) {
        this.vert = vert;
        this.frag = frag;
        this.attrs = attrs;
    }

    /**
     * Set a uniform variable.
     * @param name uniform name
     * @param type variable type
     * @param object value
     */
    public void setUniform (String name, UniformType type, Object object) {
        Uniform un = new Uniform(this, type, name, object);
        uniforms.remove(un);
        uniforms.add(un);
        setUniformsDirty(true);
    }

    /**
     * Used attributes
     * @return attributes
     */
    public Attribute[] getAttributes () {
        return attrs;
    }

    /**
     * State of the uniforms
     * @return
     */
    public boolean isUniformsDirty () {
        return uniformsDirty;
    }

    /**
     * Signal the renderer to update uniforms
     * @param dirty
     */
    public void setUniformsDirty (boolean dirty) {
        this.uniformsDirty = dirty;
    }

    /**
     * Get set of uniforms
     * @return
     */
    public Set<Uniform> getUniforms () {
        return uniforms;
    }

    /**
     * Get fragment shader source
     * @return source
     */
    public String getVertSource () {
        return vert;
    }

    /**
     * Get fragment shader source
     * @return source
     */
    public String getFragSource () {
        return frag;
    }
}
