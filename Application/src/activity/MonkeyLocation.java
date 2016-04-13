package activity;

import rendering.*;
import tpa.graphics.geometry.Mesh;

/**
 * Created by germangb on 13/04/16.
 */
public class MonkeyLocation extends LocationActivity {

    @Override
    public void loadLocation() {
        resources.load("res/monkey.json", Mesh.class);
        resources.finishLoading();
    }

    @Override
    public void onEntered() {
        System.out.println("onEntered()");

        Material material = new WireframeMaterial();
        GeometryActor monkey = new GeometryActor(resources.get("res/monkey.json", Mesh.class), material);
        addGeometry(monkey);

        // place camera
        camera.projection.setPerspective(50*3.14f/180, 4f/3f, 0.1f, 1000f);
        camera.view.setLookAt(4, 2, 3, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onLeft() {
        System.out.println("onLeft()");
    }
}
