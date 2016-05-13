package rendering;

import tpa.joml.Matrix4f;
import tpa.joml.Quaternionf;
import tpa.joml.Vector3f;
import tpa.joml.Vector4f;

/**
 * Created by germangb on 13/04/16.
 */
public class Camera {

    /** Camera background color */
    public final Vector3f clearColor = new Vector3f(0);

    /** Camera projection matrix */
    public final Matrix4f projection = new Matrix4f();

    /***/
    public final Matrix4f invProjection = new Matrix4f();

    /** Camera view matrix */
    public final Matrix4f view = new Matrix4f();

    /** Camera view matrix */
    public final Matrix4f invView = new Matrix4f();

    /** view projection matrix */
    public final Matrix4f viewProjection = new Matrix4f();

    /** view projection matrix */
    public final Matrix4f invViewProjection = new Matrix4f();

    /** camera rotation */
    public final Quaternionf rotation = new Quaternionf();

    /** look direction */
    public Vector3f look = new Vector3f();

    /**
     * Computes very useful matrices, use cautiously!
     */
    public void update () {
        // combine view & projection
        viewProjection.set(projection).mul(view);

        // invert matrices
        invView.set(view).invert();
        invProjection.set(projection).invert();
        invViewProjection.set(viewProjection).invert();

        // get rotation
        view.getNormalizedRotation(rotation);

        // get look direction
        invView.transformDirection(look.set(0, 0, -1)).normalize();
    }

    /**
     * Compute a ray perpendicular to the camera plane
     * @param x normalize cursor position in the [0, 1] range
     * @param y normalize cursor position in the [0, 1] range
     * @param ro output ray origin
     * @param rd output ray direction
     */
    public void ray (float x, float y, Vector3f ro, Vector3f rd) {
        Vector3f near = viewProjection.unproject(x, y, 0, new int[] {0, 0, 1, 1}, new Vector3f());
        Vector3f far = viewProjection.unproject(x, y, 1, new int[] {0, 0, 1, 1}, new Vector3f());
        rd.set(far.sub(near).normalize());
        ro.set(near);
    }
}
