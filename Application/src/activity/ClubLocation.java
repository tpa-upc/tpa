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
 * Test room
 *
 * Created by germangb on 13/04/16.
 */
public class ClubLocation extends LocationActivity {

    CameraController cam;
    FpsInput fps;

    GeometryActor wall;
    GeometryActor wall1;
    GeometryActor wall2;
    GeometryActor wall3;
    GeometryActor wall4;
    GeometryActor wall5;
    GeometryActor wall6;
    GeometryActor wall7;
    GeometryActor tile0;
    GeometryActor tile1;

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/wall_left.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/bar_texture.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/bar_texture_left.png", Texture.class);
        Game.getInstance().getResources().load("res/models/capsule.json", Texture.class);
        Game.getInstance().getResources().load("res/models/chair.json", Texture.class);
        Game.getInstance().getResources().load("res/models/heavy_door.json", Texture.class);
        Game.getInstance().getResources().load("res/models/box.json", Texture.class);

        fps = new FpsInput(camera);
        cam = new CameraController(camera);
        cam.tiltZ = 0.025f;
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/bar_texture.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/bar_texture_left.png", Texture.class);
        Mesh chairMesh = Game.getInstance().getResources().get("res/models/chair.json", Mesh.class);
        Texture chairTex = Game.getInstance().getResources().get(, Texture.class);
        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);

        wall = new GeometryActor(wallMesh, wallMat);

        wall1 = new GeometryActor(wallMesh, wallMat);
        wall1.model.translate(8, 0, -2).rotateY(180*3.1415f/180);

        wall2 = new GeometryActor(wallMesh, wallMat);
        wall2.model.translate(8, 2, -2).rotateY(180*3.1415f/180);

        wall3 = new GeometryActor(wallMesh, wallMat);
        wall3.model.translate(6, 2, -2).rotateY(-90*3.1415f/180);

        wall4 = new GeometryActor(wallMesh, wallMat);
        wall4.model.translate(4, 2, -2).rotateY(-90*3.1415f/180);

        wall5 = new GeometryActor(wallMesh, wallMat);
        wall5.model.translate(2, 2, -2).rotateY(-90*3.1415f/180);

        wall6 = new GeometryActor(wallMesh, wallMat);
        wall6.model.translate(0, 2, 0);

        wall7 = new GeometryActor(wallMesh, wallMat);
        wall7.model.translate(0, 2, -2).rotateY(-90*3.1415f/180);

        tile0 = new GeometryActor(tileMesh, tileMat);
        tile1 = new GeometryActor(tileMesh, tileMat);
        tile1.model.translate(4,0,0);
    }

    @Override
    public void onEntered(Context context) {
        //addGeometry(box);

        addGeometry(wall);
        addGeometry(wall1);
        addGeometry(wall2);
        addGeometry(wall3);
        addGeometry(wall4);
        addGeometry(wall5);
        addGeometry(wall6);
        addGeometry(wall7);
        addGeometry(tile0);
        addGeometry(tile1);

        // set camera
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(50), aspect, 0.01f, 100f);
        camera.clearColor.set(0.125f);
        cam.position.set(4+2.45f, 1.5f, 2.5f);
        cam.tiltZ = -0.0125f;
        cam.tiltX = 0.1f;
    }

    private float time = 0;

    @Override
    public void onTick(Context context) {
        //fps.update(context);
        cam.update();
    }

    @Override
    public void onSelected(Context context, Object data) {

    }

    @Override
    public void onLeft(Context context) {

    }
}
