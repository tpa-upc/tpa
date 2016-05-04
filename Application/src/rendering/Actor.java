package rendering;

import tpa.joml.Matrix4f;
import tpa.joml.Quaternionf;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 13/04/16.
 */
public abstract class Actor {

    /** Transformation matrix */
    public final Matrix4f model = new Matrix4f();

    /** Actor's position */
    public final Vector3f position = new Vector3f(0);

    /** Actor's scale */
    public final Vector3f scale = new Vector3f(1);

    /** Actor's rotation */
    public final Quaternionf rotation = new Quaternionf();

    /** Update model transformation using position and rotation */
    public void update () {
        // apply rotation
        model.identity()
                .translate(position)
                .rotate(rotation)
                .scale(scale);
    }
}
