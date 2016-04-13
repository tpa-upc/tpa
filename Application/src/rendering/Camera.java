package rendering;

import tpa.joml.Matrix4f;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 13/04/16.
 */
public class Camera {

    /** Camera background color */
    public final Vector3f clearColor = new Vector3f(0);

    /** Camera projection matrix */
    public final Matrix4f projection = new Matrix4f();

    /** Camera view matrix */
    public final Matrix4f view = new Matrix4f();
}
