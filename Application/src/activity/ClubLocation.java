package activity;

import game.Game;
import rendering.FpsInput;
import rendering.GeometryActor;
import rendering.materials.TexturedMaterial;
import rendering.utils.CameraController;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureWrap;

/**
 * Created by germangb on 13/04/16.
 */
public class ClubLocation extends LocationActivity {

    FpsInput fps;

    GeometryActor wall0;
    GeometryActor wall1;
    GeometryActor wall2;
    GeometryActor wall3;
    GeometryActor tile0, tile0flip;
    GeometryActor tile1, tile1flip;

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/wall_left.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/bar_texture.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/bar_texture_left.png", Texture.class);

        fps = new FpsInput(camera);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/bar_texture.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/bar_texture_left.png", Texture.class);

        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);

        wall0 = new GeometryActor(wallMesh, wallMat);

        wall1 = new GeometryActor(wallMesh, wallMat);
        wall1.model.translate(8, 0, -2).rotateY((float)Math.toRadians(180));

        wall2 = new GeometryActor(wallMesh, wallMat);
        wall2.position.set(0, 0, 2);
        wall2.update();

        wall3 = new GeometryActor(wallMesh, wallMat);
        wall3.rotation.rotateY((float)Math.toRadians(180));
        wall3.position.set(8, 0, 0);
        wall3.update();

        tile0 = new GeometryActor(tileMesh, tileMat);
        tile0flip = new GeometryActor(tileMesh, tileMat);
        tile0flip.rotation.rotateY((float)Math.toRadians(180));
        tile0flip.position.set(4, 0, 0);
        tile0flip.update();
        tile1 = new GeometryActor(tileMesh, tileMat);
        tile1.model.translate(4,0,0);
        tile1flip = new GeometryActor(tileMesh, tileMat);
        tile1flip.rotation.rotateY((float)Math.toRadians(180));
        tile1flip.position.set(8, 0, 0);
        tile1flip.update();
    }

    @Override
    public void onEntered(Context context) {
        //addGeometry(box);

        addGeometry(wall0);
        addGeometry(wall1);
        addGeometry(wall2);
        addGeometry(wall3);
        addGeometry(tile0);
        addGeometry(tile1);
        addGeometry(tile0flip);
        addGeometry(tile1flip);

        // set camera
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(45), aspect, 0.01f, 100f);
        fps.position.set(4, 1, 0);
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
