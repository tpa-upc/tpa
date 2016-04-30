package activity;

import game.Game;
import rendering.DecalActor;
import rendering.GeometryActor;
import rendering.materials.DecalMaterial;
import rendering.materials.TexturedMaterial;
import rendering.materials.WireframeMaterial;
import rendering.utils.CameraController;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureWrap;
import tpa.input.keyboard.KeyboardInput;

/**
 * Test room
 *
 * Created by germangb on 13/04/16.
 */
public class InterrogationLocation extends LocationActivity {

    CameraController cam;

    GeometryActor box;

    GeometryActor window;
    GeometryActor door;
    GeometryActor table;
    GeometryActor wall;
    GeometryActor wall1;
    GeometryActor tile0;

    DecalActor albert;
    DecalActor enemies;

    private float doorAnimation = 1;

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/box.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/wall_left.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/heavy_door.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/window.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/table.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/interrogation_texture.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/interrogation_texture_left.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/heavy_door.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/albert.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/enemies.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/window.png", Texture.class);

        cam = new CameraController(camera);
        cam.tiltZ = 0.025f;
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh boxMesh = Game.getInstance().getResources().get("res/models/box.json", Mesh.class);
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Mesh doorMesh = Game.getInstance().getResources().get("res/models/heavy_door.json", Mesh.class);
        Mesh windowMesh = Game.getInstance().getResources().get("res/models/window.json", Mesh.class);
        Mesh tableMesh = Game.getInstance().getResources().get("res/models/table.json", Mesh.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/interrogation_texture.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/interrogation_texture_left.png", Texture.class);
        Texture doorTex = Game.getInstance().getResources().get("res/textures/heavy_door.png", Texture.class);
        Texture albertTex = Game.getInstance().getResources().get("res/textures/albert.png", Texture.class);
        Texture enemiesTex = Game.getInstance().getResources().get("res/textures/enemies.png", Texture.class);
        Texture windowTex = Game.getInstance().getResources().get("res/textures/window.png", Texture.class);

        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        WireframeMaterial boxMat = new WireframeMaterial();
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);
        TexturedMaterial doorMat = new TexturedMaterial(doorTex);
        TexturedMaterial windowMat = new TexturedMaterial(windowTex);
        TexturedMaterial tableMat = new TexturedMaterial(doorTex);
        DecalMaterial albertMat = new DecalMaterial(albertTex, depth);
        DecalMaterial enemiesMat = new DecalMaterial(enemiesTex, depth);

        box = new GeometryActor(boxMesh, boxMat);
        box.model.translate(4, 1f, -1).scale(0.25f);

        albert = new DecalActor(albertMat);
        albert.model.translate(0, 1, -0.5f).rotate(-90*3.1415f/180, 0, 0, 1).rotateY(0.15f).scale(0.35f);

        enemies = new DecalActor(enemiesMat);
        enemies.model.translate(0, 1, -1.25f).rotateY(180*3.1415f/180).rotate(-90*3.1415f/180, 0, 0, 1).rotateY(0.05f).scale(0.45f);

        door = new GeometryActor(doorMesh, doorMat);
        table = new GeometryActor(tableMesh, tableMat);
        table.model.translate(1.5f, 0, 0.5f).rotateY(95*3.1415f/180);
        window = new GeometryActor(windowMesh, windowMat);
        window.model.translate(1.5f, 0.5f, -2f);

        wall = new GeometryActor(wallMesh, wallMat);
        wall1 = new GeometryActor(wallMesh, wallMat);
        wall1.model.translate(4, 0, -2).rotateY(180*3.1415f/180);
        tile0 = new GeometryActor(tileMesh, tileMat);
    }

    @Override
    public void onEntered(Context context) {
        addGeometry(box);
        addGeometry(wall);
        addGeometry(wall1);
        addGeometry(tile0);
        addGeometry(door);
        addGeometry(window);
        addGeometry(table);

        addDecal(albert);
        addDecal(enemies);

        // open door
        doorAnimation = 2;

        // set camera
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(50), aspect, 0.01f, 100f);
        camera.clearColor.set(0f);

        cam.position.y = 1.5f;
        cam.position.z = 3;
        cam.position.x = 2.45f;
    }

    @Override
    public void onTick(Context context) {
        cam.update();

        cam.position.x = 2.45f + 0.05f * (float) Math.sin(context.time.getTime());

        doorAnimation -= context.time.getFrameTime();
        if (doorAnimation < 0) doorAnimation = 0;
        door.model.identity()
                .translate(3, 0, -2)
                .rotateY(-2.5f*Math.min(doorAnimation, 1));
    }

    @Override
    public void onLeft(Context context) {

    }
}
