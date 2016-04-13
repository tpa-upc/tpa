package activity;

import game.Game;
import game.GameActivity;
import rendering.*;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;

/**
 * Created by germangb on 13/04/16.
 */
public class MonkeyLocation extends LocationActivity {

    @Override
    public void loadLocation(Context context) {
        resources.load("res/monkey.json", Mesh.class);
        resources.load("res/thing.json", Mesh.class);
        resources.load("res/plane.json", Mesh.class);
        resources.load("res/corpse.png", Texture.class);
        resources.finishLoading();
    }

    @Override
    public void onEntered(Context context) {
        System.out.println("onEntered()");
        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyboardInput.KEY_C)
                    Game.getInstance().pushActivity(GameActivity.Computer, null);
            }
        });

        Material material = new NormalMaterial();
        Material material2 = new LambertMaterial();
        GeometryActor monkey = new GeometryActor(resources.get("res/monkey.json", Mesh.class), material);
        GeometryActor plane = new GeometryActor(resources.get("res/plane.json", Mesh.class), material);
        GeometryActor monkey2 = new GeometryActor(resources.get("res/thing.json", Mesh.class), material2);
        addGeometry(monkey);
        addGeometry(plane);
        addGeometry(monkey2);

        monkey.model.translate(-3, 0, 1).rotate(0.7f, 0, 1, 0);
        monkey2.model.translate(3, 0, 0);
        plane.model.translate(0,-1,0);

        DecalMaterial decalMat = getDecalMaterial(resources.get("res/corpse.png", Texture.class));
        DecalActor decal = new DecalActor(decalMat);
        decal.model.translate(0, -1, 0).scale(4, 3, 4);
        addDecal(decal);

        // place camera
        camera.projection.setPerspective(50*3.14f/180, 4f/3f, 0.1f, 1000f);
        camera.clearColor.set(0f);
    }

    @Override
    public void onTick(Context context) {
        float x = (float) Math.sin(context.time.getTime()/2)*8;
        float z = (float) Math.cos(context.time.getTime()/2)*8;
        camera.view.setLookAt(x, 3 + 3*(((float) Math.sin(context.time.getTime()))*0.5f+0.5f), z, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onLeft(Context context) {
        System.out.println("onLeft()");
    }
}
