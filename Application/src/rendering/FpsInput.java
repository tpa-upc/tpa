package rendering;

import tpa.application.Context;
import tpa.input.keyboard.KeyboardInput;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 15/04/16.
 */
public class FpsInput {

    private Camera camera;
    private Vector3f position = new Vector3f();

    private float pitch = 0;
    private float yaw = 0;

    private float forward = 0;
    private float left = 0;

    public FpsInput (Camera camera) {
        this.camera = camera;
    }

    public void update (Context context) {
        //pitch += context.mouse.getCursorDY() * 0.01f;
        //yaw += context.mouse.getCursorDX() * 0.01f;

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_RIGHT)) yaw += 0.035f;
        if (context.keyboard.isKeyDown(KeyboardInput.KEY_LEFT)) yaw -= 0.035f;
        if (context.keyboard.isKeyDown(KeyboardInput.KEY_UP)) pitch -= 0.035f;
        if (context.keyboard.isKeyDown(KeyboardInput.KEY_DOWN)) pitch += 0.035f;

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_SPACE))
            position.y += 0.1f;

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_LEFT_SHIFT))
            position.y -= 0.1f;

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_W)) {
            position.x += (float) Math.sin(yaw) * 0.1f;
            position.z -= (float) Math.cos(yaw) * 0.1f;
        }

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_S)) {
            position.x -= (float) Math.sin(yaw) * 0.1f;
            position.z += (float) Math.cos(yaw) * 0.1f;
        }

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_D)) {
            position.z += (float) Math.sin(yaw) * 0.1f;
            position.x += (float) Math.cos(yaw) * 0.1f;
        }

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_A)) {
            position.z -= (float) Math.sin(yaw) * 0.1f;
            position.x -= (float) Math.cos(yaw) * 0.1f;
        }

        camera.view.identity()
                .rotate(pitch, 1, 0, 0)
                .rotate(yaw, 0, 1, 0)
                .translate(-position.x, -position.y, -position.z);

        camera.update();
    }
}
