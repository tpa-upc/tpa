package activity;

import game.Game;
import game.GameActivity;
import rendering.*;
import rendering.materials.*;
import resources.ResourceManager;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFilter;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;
import tpa.joml.Vector3f;
import tpa.joml.Vector4f;

/**
 * Test room
 *
 * Created by germangb on 13/04/16.
 */
public class RoomLocation extends LocationActivity {

    @Override
    public void onLoad(Context context) {
        resources.load("res/plane.json", Mesh.class);
    }

    @Override
    public void onFinishLoad(Context context) {

    }

    @Override
    public void onEntered(Context context) {

    }

    @Override
    public void onTick(Context context) {

    }

    @Override
    public void onLeft(Context context) {

    }
}
