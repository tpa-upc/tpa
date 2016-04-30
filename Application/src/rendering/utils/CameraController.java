package rendering.utils;

import rendering.Camera;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 29/04/2016.
 */
public class CameraController {

    /** Camera being controlled */
    private Camera camera;

    /** Camera position */
    public Vector3f position = new Vector3f(0);

    public float tiltZ = 0.05f;

    public CameraController (Camera camera) {
        this.camera = camera;
    }

    public void update () {
        Vector3f p = position;
        camera.view.identity();
        camera.view.rotateZ(tiltZ);
        camera.view.rotateY(-0.1f);
        camera.view.lookAt(p.x, p.y, p.z, p.x, p.y-0.1f, 0, 0, 1, 0);
    }
}
