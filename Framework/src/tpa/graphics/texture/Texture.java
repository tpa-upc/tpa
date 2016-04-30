package tpa.graphics.texture;

import java.nio.ByteBuffer;

/**
 * Created by german on 26/03/2016.
 */
public class Texture {

    /** Texture format */
    private TextureFormat format;

    /** Min filter */
    private TextureFilter min = TextureFilter.Nearest;

    /** Mag filter */
    private TextureFilter mag = TextureFilter.Nearest;

    /** wrap on U coordinate */
    private TextureWrap wrapU = TextureWrap.Clamp;

    /** wrap on V coordinate */
    private TextureWrap wrapV = TextureWrap.Clamp;

    /** Texture width */
    private int width;

    /** Texture height */
    private int height;

    /** Dirty state to signal the renderer */
    private boolean dataDirty = true;

    /** parameters */
    private boolean paramsDirty = true;

    /** pixel data */
    private ByteBuffer data = null;

    /** signal the renderer to clear the data once it's been sent to the GPU */
    private boolean keepData = true;

    /** Generate mipmap flag */
    private boolean generateMipmaps = false;

    public Texture (int width, int height, TextureFormat format) {
        this.format = format;
        this.width = width;
        this.height = height;
    }

    /**
     * Get state of the texture
     * @return true if the texture needs an upload
     */
    public boolean isDataDirty() {
        return dataDirty;
    }

    public boolean isParamsDirty() {
        return paramsDirty;
    }

    /**
     * Set dataDirty status
     * @param dataDirty
     */
    public void setDataDirty(boolean dataDirty) {
        this.dataDirty = dataDirty;
    }

    public void setParamsDirty(boolean paramsDirty) {
        this.paramsDirty = paramsDirty;
    }

    /**
     * Get texture width
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set texture height
     * @param height
     */
    public void setHeight (int height) {
        this.height = height;
        setDataDirty(true);
    }

    /**
     * Get texture width
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set texture width
     * @param width
     */
    public void setWidth (int width) {
        this.width = width;
        setDataDirty(true);
    }

    /**
     * Get texture mag filter
     * @return
     */
    public TextureFilter getMag() {
        return mag;
    }

    /**
     * Set texture mag filter
     * @param mag
     */
    public void setMag(TextureFilter mag) {
        setParamsDirty(true);
        this.mag = mag;
    }

    /**
     * Get texture min filter
     * @return
     */
    public TextureFilter getMin() {
        return min;
    }

    /**
     * Get texture min filter
     * @param min
     */
    public void setMin(TextureFilter min) {
        setParamsDirty(true);
        this.min = min;
    }

    /**
     * Get texture format
     * @return
     */
    public TextureFormat getFormat() {
        return format;
    }

    /**
     * Get texture data
     * @return texture data
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     * Set texture data
     * @param data new texture data
     */
    public void setData(ByteBuffer data) {
        setData(data, false);
    }

    /**
     * update the data
     * @param data new data
     * @param silent if true, the texture will not be set to dirty
     */
    public void setData(ByteBuffer data, boolean silent) {
        if (!silent) setDataDirty(true);
        this.data = data;
    }

    /**
     * Wrap in U coordinate
     * @return wrap
     */
    public TextureWrap getWrapU() {
        return wrapU;
    }

    /**
     * Wrap in U coordinate
     * @param wrapU wrap in U
     */
    public void setWrapU(TextureWrap wrapU) {
        this.wrapU = wrapU;
        setParamsDirty(true);
    }

    /**
     * Wrap in V coordinate
     * @return wrap
     */
    public TextureWrap getWrapV() {
        return wrapV;
    }

    /**
     * Wrap in U coordinate
     * @param wrapV wrap in U
     */
    public void setWrapV(TextureWrap wrapV) {
        this.wrapV = wrapV;
        setParamsDirty(true);
    }

    /**
     * if false, the renderer will remove the data once it's uploaded to the GPU
     * @return true if the data is kept
     */
    public boolean isKeepData() {
        return keepData;
    }

    /**
     * Set to false to destroy data once it's sent to the GPU
     * @param keepData true to keep the data on the CPU, false to keep it only on the GPU
     */
    public void setKeepData(boolean keepData) {
        this.keepData = keepData;
    }

    public boolean isGenerateMipmaps() {
        return generateMipmaps;
    }

    public void setGenerateMipmaps(boolean generateMipmaps) {
        this.generateMipmaps = generateMipmaps;
    }
}
