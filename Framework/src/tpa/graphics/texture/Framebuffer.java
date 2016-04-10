package tpa.graphics.texture;

/**
 * Created by german on 26/03/2016.
 */
public class Framebuffer {

    private boolean dirty = true;

    /** Framebuffer width */
    private int width;

    /** Framebuffer height */
    private int height;

    /** texture formats */
    private TextureFormat[] formats;

    /** targets */
    private Texture[] targets;

    /** Depth texture */
    private Texture depth;

    public Framebuffer (int width, int height, TextureFormat[] formats, boolean hasDepth) {
        this.width = width;
        this.height = height;
        this.formats = formats;

        targets = new Texture[formats.length];
        for (int i = 0; i < formats.length; ++i)
            targets[i] = new Texture(width, height, formats[i]);

        if (hasDepth)
            depth = new Texture(width, height, TextureFormat.Depth);
        else depth = null;
    }

    public boolean hasDepth () {
        return depth != null;
    }

    public Texture getDepth () {
        return depth;
    }

    public Texture[] getTargets () {
        return targets;
    }

    public boolean isDirty () {
        return dirty;
    }

    public void setDirty (boolean dirty) {
        this.dirty = dirty;
    }

    public void setWidth (int width) {
        this.width = width;
        for (int i = 0; i < targets.length; ++i) {
            targets[i].setWidth(width);
            targets[i].setDataDirty(true);
        }

        if (depth != null) {
            depth.setWidth(width);
            depth.setDataDirty(true);
        }

        setDirty(true);
    }

    public void setHeight (int height) {
        this.height = height;
        for (int i = 0; i < targets.length; ++i) {
            targets[i].setHeight(height);
            targets[i].setDataDirty(true);
        }

        if (depth != null) {
            depth.setHeight(height);
            depth.setDataDirty(true);
        }

        setDirty(true);
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }
}
