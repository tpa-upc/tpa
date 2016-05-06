package activity;

import game.Game;
import rendering.FpsInput;
import rendering.GeometryActor;
import rendering.materials.TexturedMaterial;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;

/**
 * Created by germangb on 06/05/16.
 */
public class DemoLocation extends LocationActivity {

    FpsInput fps = new FpsInput(camera);
    GeometryActor cubo;


    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/box.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/girl.jpg", Texture.class);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh cuboMesh = Game.getInstance().getResources().get("res/models/box.json", Mesh.class);
        Texture cuboTex = Game.getInstance().getResources().get("res/textures/girl.jpg", Texture.class);

        TexturedMaterial cuboMat = new TexturedMaterial(cuboTex);

        // creas actor (modelo 3d)
        cubo = new GeometryActor(cuboMesh, cuboMat);

    }

    @Override
    public void onEntered(Context context) {
        System.out.println("on enter!");
        addGeometry(cubo);

        cubo.position.set(0, 0, 0);
        cubo.rotation.identity().rotateX(23423).rotateY(87934).rotateZ(32423);
        cubo.update();

        camera.projection.setPerspective(45*3.1415f/180, 4/3f, 0.01f, 1000f);
        camera.view.setLookAt(4, 2, 3, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onTick(Context context) {
        fps.update(context);
    }

    @Override
    public void onSelected(Context context, Object data) {

    }

    @Override
    public void onLeft(Context context) {

    }
}
