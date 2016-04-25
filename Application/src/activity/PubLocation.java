package activity;

import game.Game;
import game.GameActivity;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;

/**
 * Test room
 *
 * Created by germangb on 13/04/16.
 */
public class PubLocation extends LocationActivity {

    @Override
    public void onLoad(Context context) {
        resources.load("res/plane.json", Mesh.class);
    }

    @Override
    public void onFinishLoad(Context context) {
    }

    @Override
    public void onEntered(Context context) {
        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyboardInput.KEY_ENTER) {
                    Game.getInstance().pushActivity(GameActivity.Dialog);
                }
            }
        });
    }

    @Override
    public void onTick(Context context) {

    }

    @Override
    public void onLeft(Context context) {

    }
}
