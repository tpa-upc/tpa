package activity;

import rendering.*;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;

/**
 * Created by germangb on 13/04/16.
 */
public class MonkeyLocation extends LocationActivity {

    GeometryActor monkey;
    GeometryActor solid;
    GeometryActor plane;
    DecalActor decal;

    @Override
    public void onLoad(Context context) {
        // load resources of the location
        resources.load("res/monkey.json", Mesh.class);
        resources.load("res/thing.json", Mesh.class);
        resources.load("res/plane.json", Mesh.class);
        resources.load("res/corpse.png", Texture.class);
    }

    @Override
    public void onFinishLoad(Context context) {
        Material monkeyMaterial = new NormalMaterial();
        Material floorMaterial = new NormalMaterial();
        Material solidMaterial = new WireframeMaterial();
        DecalMaterial decalMaterial = new DecalMaterial(resources.get("res/corpse.png", Texture.class), depth);

        // create geometry of the "room"
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
        addDecal(decal);

        // set positions, rotations and stuff
        monkey.model.identity().translate(-3, 0, 1).rotate(0.7f, 0, 1, 0);
        solid.model.identity().translate(3, 0, 0);
        plane.model.identity().translate(0,-1,0);
        decal.model.identity().translate(0, -1, 0).scale(4, 3, 4);

        // create camera projection
        camera.projection.setPerspective(50*3.14f/180, 4f/3f, 0.1f, 1000f);
    }

    @Override
    public void onTick(Context context) {
        // animate camera
        float t = context.time.getTime()/2;
        float x = (float) Math.sin(t)*8;
        float z = (float) Math.cos(t)*8;
        camera.view.setLookAt(x, 3 + 3*(((float) Math.sin(t*2))*0.5f+0.5f), z, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onLeft(Context context) {
        // room is left, do something here right before it is left
    }
}
