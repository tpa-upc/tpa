package rendering;

import tpa.application.Context;
import tpa.input.keyboard.KeyboardInput;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 15/04/16.
 */
public class FpsInput {

    private Camera camera;
    public Vector3f position = new Vector3f();

    private float pitch = 0;
    private float yaw = 0;

    private float sPitch = 0;
    private float sYaw = 0;

    private float forward = 0;
    private float left = 0;

    public FpsInput (Camera camera) {
        this.camera = camera;
    }

    public void update (Context context) {
        //pitch += context.mouse.getCursorDY() * 0.01f;
        //yaw += context.mouse.getCursorDX() * 0.01f;
        //float dpitch = (context.mouse.getCursorY() - context.window.getHeight()/2)*0.01f;
        //float dyaw = (context.mouse.getCursorX() - context.window.getWidth()/2)*0.01f;
        pitch += context.mouse.getCursorDY() * 0.005f;
        yaw += context.mouse.getCursorDX() * 0.005f;

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_RIGHT)) yaw += 0.035f;
        if (context.keyboard.isKeyDown(KeyboardInput.KEY_LEFT)) yaw -= 0.035f;
        if (context.keyboard.isKeyDown(KeyboardInput.KEY_UP)) pitch -= 0.035f;
        if (context.keyboard.isKeyDown(KeyboardInput.KEY_DOWN)) pitch += 0.035f;

        if (pitch > 3.1415f/2) pitch = 3.1415f/2;
        if (pitch < -3.1415f/3) pitch = -3.1415f/3;

        float v = 0.025f;

        /*if (context.keyboard.isKeyDown(KeyboardInput.KEY_SPACE))
            position.y += v;

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_LEFT_SHIFT))
            position.y -= v;*/

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_W)) {
            position.x += (float) Math.sin(sYaw) * v;
            position.z -= (float) Math.cos(sYaw) * v;
        }

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_S)) {
            position.x -= (float) Math.sin(sYaw) * v;
            position.z += (float) Math.cos(sYaw) * v;
        }

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_D)) {
            position.z += (float) Math.sin(sYaw) * v;
            position.x += (float) Math.cos(sYaw) * v;
        }

        if (context.keyboard.isKeyDown(KeyboardInput.KEY_A)) {
            position.z -= (float) Math.sin(sYaw) * v;
            position.x -= (float) Math.cos(sYaw) * v;
        }

        sPitch += (pitch - sPitch) * context.time.getFrameTime() * 16;
        sYaw += (yaw - sYaw) * context.time.getFrameTime() * 16;

        camera.view.identity()
                .rotate(sPitch, 1, 0, 0)
                .rotate(sYaw, 0, 1, 0)
                .translate(-position.x, -position.y, -position.z);
    }
}
