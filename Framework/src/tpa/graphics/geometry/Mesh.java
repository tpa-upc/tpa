package tpa.graphics.geometry;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by german on 28/03/2016.
 */
public class Mesh {

    /** Mesh attributes */
    private Map<Attribute, Buffer> data = new HashMap<>();

    /** vertex index buffer */
    private Buffer indices = null;

    /** offset in vertices */
    private int offset;

    /** Lench in vertices */
    private int length;

    /** State of mesh */
    private boolean dirty = true;

    /** Mesh usage */
    private MeshUsage usage;

    /** Rendering primitive */
    private Primitive primitive = Primitive.Triangles;

    /** If set to false, data will be removed once it's uploaded to the GPU */
    private boolean keepData = false;

    public Mesh (MeshUsage usage) {
        offset = 0;
        length = 0;
        this.usage = usage;
    }

    /**
     * Get attribute buffer
     * @param att what attribute
     * @param type data type
     * @param <T> buffer type
     * @return buffer with raw data, null if it doesn't exists
     */
    public <T extends Buffer> T getData (Attribute att, Class<T> type) {
        return (T) data.get(att);
    }

    /**
     * Get attribute buffer
     * @param att what attribute
     * @return buffer with raw data, null if it doesn't exists
     */
    public Buffer getData (Attribute att) {
        return data.get(att);
    }

    public void setData (Attribute att, Buffer data) {
        setData(att, data, false);
    }

    /**
     * Set buffer data
     * @param att attribute
     * @param data raw data
     */
    public void setData (Attribute att, Buffer data, boolean silent) {
        if (data == null) this.data.remove(att);
        else this.data.put(att, data);
        if (!silent) setDirty(true);
    }

    /**
     * Get indices
     * @param type data type (int or short tipically)
     * @param <T> type
     * @return index buffer
     */
    public <T extends Buffer> T getIndices(Class<T> type) {
        return (T) indices;
    }

    /**
     * Get indices
     * @return index buffer
     */
    public Buffer getIndices() {
        return indices;
    }

    /**
     * Set index buffer
     * @param indices new index buffer
     */
    public void setIndices(Buffer indices) {
        this.indices = indices;
        setDirty(true);
    }

    /**
     * Get mesh usage. This is used by the renderer to hint the GPU on
     * how to store the vertex buffers
     * @return mesh usage
     */
    public MeshUsage getUsage () {
        return usage;
    }

    /**
     * Set vertex offset in vertices
     * @param offset new offset
     */
    public void setOffset (int offset) {
        this.offset = offset;
    }

    /**
     * Get offset in vertices
     * @return offset in vertices
     */
    public int getOffset () {
        return offset;
    }

    /**
     * Set vertices length
     * @param length new length
     */
    public void setLength (int length) {
        this.length = length;
    }

    /**
     * Get length in vertices
     * @return length in vertices
     */
    public int getLength () {
        return length;
    }

    /**
     * Set dirty state
     * @param dirty true to signal renderer
     */
    public void setDirty (boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * Get state of the mesh
     * @return mesh state
     */
    public boolean isDirty () {
        return dirty;
    }

    public Primitive getPrimitive() {
        return primitive;
    }

    public void setPrimitive(Primitive primitive) {
        this.primitive = primitive;
    }

    public boolean isKeepData() {
        return keepData;
    }

    public void setKeepData(boolean keepData) {
        this.keepData = keepData;
    }
}
