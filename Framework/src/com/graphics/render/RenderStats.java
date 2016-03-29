package com.graphics.render;

/**
 * Created by german on 26/03/2016.
 */
public class RenderStats {

    // some values
    public int allocatedTextures = 0;
    public int allocatedVbos = 0;
    public int allocatedEbos = 0;
    public int allocatedFramebuffers = 0;
    public int allocatedPrograms = 0;

    /** number of vbos in the scene */
    public int vboCount = 0;

    /** Number of texture switches */
    public int textureSwitch = 0;

    /** number of shader switches */
    public int shaderSwitch = 0;

    /** number of fbo switches */
    public int fboSwitch = 0;

    /** Rendered vertices */
    public int vertices = 0;

    /** Reset statistics */
    public void reset () {
        vboCount = 0;
        textureSwitch = 0;
        shaderSwitch = 0;
        fboSwitch = 0;
        vertices = 0;
    }

    public RenderStats copy (RenderStats to) {
        to.vboCount = vboCount;
        to.textureSwitch = textureSwitch;
        to.shaderSwitch = shaderSwitch;
        to.fboSwitch = fboSwitch;
        to.vertices = vertices;
        to.allocatedPrograms = allocatedPrograms;
        to.allocatedTextures = allocatedTextures;
        to.allocatedFramebuffers = allocatedFramebuffers;
        to.allocatedEbos = allocatedEbos;
        to.allocatedVbos = allocatedVbos;
        return to;
    }
}
