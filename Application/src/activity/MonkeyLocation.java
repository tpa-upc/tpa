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
public class MonkeyLocation extends LocationActivity {


    FpsInput fps = new FpsInput(camera);
    GeometryActor monkey;
    GeometryActor solid;
    GeometryActor plane;
    GeometryActor sphere;
    DecalActor decal;

    @Override
    public void onLoad(Context context) {
        // load resources
        resources.load("res/dragon.json", Mesh.class);
        resources.load("res/monkey.json", Mesh.class);
        resources.load("res/thing.json", Mesh.class);
        resources.load("res/plane.json", Mesh.class);
        resources.load("res/sphere.json", Mesh.class);
        resources.load("res/corpse.png", Texture.class);
        resources.load("res/floor.jpg", Texture.class);
    }

    @Override
    public void onFinishLoad(Context context) {
        // set resource attributes
        Texture floorTex = resources.get("res/floor.jpg", Texture.class);
        floorTex.setGenerateMipmaps(true);
        floorTex.setMin(TextureFilter.MipmapLinear);

        // materials define how geometry is renderer
        LambertMaterial monkeyMaterial = new LambertMaterial();
        OutlineMaterial sphereMaterial = new OutlineMaterial();
        Material floorMaterial = new GridMaterial();
        Material solidMaterial = new WireframeMaterial();
        Material decalMaterial = new DecalMaterial(resources.get("res/corpse.png", Texture.class), depth);

        // set material parameters
        monkeyMaterial.reflective = 1;
        monkeyMaterial.hardness = 4;
        monkeyMaterial.ambient.set(0.25f, 0, 0);
        monkeyMaterial.diffuse.set(1,0,0);
        monkeyMaterial.specular.set(1);
        monkeyMaterial.light.set(0, 1, 3);

        // create geometry of the "room"
        sphere = new GeometryActor(resources.get("res/dragon.json", Mesh.class), sphereMaterial);
        monkey = new GeometryActor(resources.get("res/monkey.json", Mesh.class), monkeyMaterial);
        plane = new GeometryActor(resources.get("res/plane.json", Mesh.class), floorMaterial);
        solid = new GeometryActor(resources.get("res/thing.json", Mesh.class), solidMaterial);
        decal = new DecalActor(decalMaterial);
    }

    @Override
    public void onEntered(Context context) {
        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyboardInput.KEY_ENTER)
                    Game.getInstance().pushActivity(GameActivity.Dialog, (act, data) -> {
                        if (data.equals("ans0")) Game.getInstance().putBool("option_1", true);
                        if (data.equals("ans1")) Game.getInstance().putBool("option_2", true);
                    });
            }
        });

        // when room is entered, put geometry on it
        addGeometry(plane);
        addGeometry(solid);
        addDecal(decal);

        if (!Game.getInstance().getBool("option_1")) addGeometry(monkey);
        if (!Game.getInstance().getBool("option_2")) addGeometry(sphere);

        // set positions, rotations and stuff
        monkey.model.identity().translate(-6, 0, 1).scale(2).rotate(0.7f, 0, 1, 0);
        solid.model.identity().translate(6, 1, 0);
        sphere.model.identity().translate(3, 0.5f, 6);
        plane.model.identity().scale(0.5f, 1, 0.5f);
        decal.model.identity().translate(0, 0.2f, 0).rotate(0.125f, 1, 0, 0).rotate(0.25f, 0, 1, 0).scale(2,0.75f,2);
        plane.model.identity().scale(64, 1, 64);

        // create camera projection
        camera.projection.setPerspective(50*3.14f/180, 4f/3f, 0.1f, 1000f);
        camera.clearColor.set(41/255f, 79/255f, 122/255f);
    }

    @Override
    public void onTick(Context context) {
        fps.update(context);
    }

    @Override
    public void onLeft(Context context) {
        // do something when room is left
    }
}
