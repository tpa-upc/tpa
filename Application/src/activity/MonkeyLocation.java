package activity;

import rendering.*;
import rendering.materials.*;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFilter;
import tpa.joml.Vector4f;

/**
 * Created by germangb on 13/04/16.
 */
public class MonkeyLocation extends LocationActivity {

    LambertMaterial monkeyMaterial;

    GeometryActor monkey;
    GeometryActor solid;
    GeometryActor plane;
    GeometryActor sphere;
    DecalActor decal;

    @Override
    public void onLoad(Context context) {
        // load resources of the location
        resources.load("res/monkey.json", Mesh.class);
        resources.load("res/thing.json", Mesh.class);
        resources.load("res/plane.json", Mesh.class);
        resources.load("res/sphere.json", Mesh.class);
        resources.load("res/corpse.png", Texture.class);
        resources.load("res/floor.jpg", Texture.class);
    }

    @Override
    public void onFinishLoad(Context context) {
        Texture floorTes = resources.get("res/floor.jpg", Texture.class);
        floorTes.setGenerateMipmaps(true);
        floorTes.setMin(TextureFilter.MipmapLinear);

        monkeyMaterial = new LambertMaterial();
        OutlineMaterial sphereMaterial = new OutlineMaterial();
        Material floorMaterial = new TexturedMaterial(floorTes);
        Material solidMaterial = new WireframeMaterial();
        Material decalMaterial = new DecalMaterial(resources.get("res/corpse.png", Texture.class), depth);

        monkeyMaterial.reflective = 1;
        monkeyMaterial.hardness = 4;
        monkeyMaterial.ambient.set(0.25f, 0, 0);
        monkeyMaterial.diffuse.set(1,0,0);
        monkeyMaterial.specular.set(1);
        monkeyMaterial.light.set(0, 1, 3);

        // create geometry of the "room"
        sphere = new GeometryActor(resources.get("res/monkey.json", Mesh.class), sphereMaterial);
        monkey = new GeometryActor(resources.get("res/monkey.json", Mesh.class), monkeyMaterial);
        plane = new GeometryActor(resources.get("res/plane.json", Mesh.class), floorMaterial);
        solid = new GeometryActor(resources.get("res/thing.json", Mesh.class), solidMaterial);
        decal = new DecalActor(decalMaterial);
    }

    @Override
    public void onEntered(Context context) {
        addGeometry(monkey);
        addGeometry(plane);
        addGeometry(solid);
        addGeometry(sphere);
        addDecal(decal);

        // set positions, rotations and stuff
        monkey.model.identity().translate(-6, 0, 1).scale(2).rotate(0.7f, 0, 1, 0);
        solid.model.identity().translate(6, 1, 0);
        sphere.model.identity().translate(3, 0.5f, 6);
        plane.model.identity().scale(0.5f, 1, 0.5f);
        decal.model.identity().translate(0, 0.2f, 0).rotate(0.125f, 1, 0, 0).rotate(0.25f, 0, 1, 0).scale(2,0.75f,2);

        // create camera projection
        camera.projection.setPerspective(50*3.14f/180, 4f/3f, 0.1f, 1000f);
        camera.clearColor.set(0.25f);
    }

    @Override
    public void onTick(Context context) {
        Vector4f cam = camera.view.getColumn(3, new Vector4f());
        monkeyMaterial.light.set(0, 0, 3);

        decal.model.identity()
                .translate(0, 0.2f, 0)
                .rotate(0.5f*(float)Math.sin(context.time.getTime() * 1.715f), 1, 0, 0)
                .rotate(0.5f*(float) Math.cos(context.time.getTime()*1.167f), 0, 1, 0)
                .scale(2,1.5f,2);
    }

    @Override
    public void onLeft(Context context) {
        // room is left, do something here right before it is left
    }
}
