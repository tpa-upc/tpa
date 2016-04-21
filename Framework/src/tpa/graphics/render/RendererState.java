package tpa.graphics.render;

/**
 * Created by germangb on 14/04/16.
 */
public class RendererState {

    /** Blending */
    public Blending blending = Blending.Disabled;

    /** cull face state */
    public Culling culling = Culling.Disabled;

    /** Rendering mode */
    public RenderMode renderMode = RenderMode.Fill;

    /** Depth testing flag */
    public boolean depthTest = false;

    /** Depth mask */
    public boolean depthMask = true;

    /** red color mask */
    public boolean redMask = true;

    /** green color mask */
    public boolean greenMask = true;

    /** blue color mask */
    public boolean blueMask = true;

    /** alpha color mask */
    public boolean alphaMask = true;
}
