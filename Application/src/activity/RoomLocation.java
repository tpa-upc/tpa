package activity;

import activity.tasks.DelayTask;
import activity.tasks.DoSomethingTask;
import activity.tasks.Task;
import activity.tasks.TaskManager;
import game.Game;
import game.GameActivity;
import rendering.*;
import rendering.materials.*;
import rendering.utils.CameraController;
import tpa.application.Context;
import tpa.audio.Sound;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureWrap;
import tpa.input.keyboard.KeyboardInput;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 13/04/16.
 */
public class RoomLocation extends LocationActivity {

    CameraController cam;

    DecalActor door0;
    DecalActor door1;
    DecalActor door2;
    DecalActor notes0;
    DecalActor notes1;
    DecalActor notes2;
    DecalActor notes3;
    DecalActor notes4;
    DecalActor poster;
    DecalActor poster1;

    GeometryActor telf;
    GeometryActor wall;
    GeometryActor wall1;
    GeometryActor pc;
    GeometryActor tile0;
    GeometryActor tile1;
    GeometryActor table;
    GeometryActor chair;

    Sound telfSound, hangPhone;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    TaskManager tasks = new TaskManager();

    boolean phoneRing = true;
    boolean emailReceived = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/sfx/telf.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/hang_phone.wav", Sound.class);
        Game.getInstance().getResources().load("res/models/telf.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/wall_left.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/table.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/chair.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/room_texture.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/room_texture_left.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/door0.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/door1.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/door2.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/table.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/pc.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/notes.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/poster.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/poster1.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/telephone.png", Texture.class);
        Game.getInstance().getResources().load("res/models/pc.json", Mesh.class);

        cam = new CameraController(camera);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        telfSound = Game.getInstance().getResources().get("res/sfx/telf.wav", Sound.class);
        hangPhone = Game.getInstance().getResources().get("res/sfx/hang_phone.wav", Sound.class);

        Mesh telfModel = Game.getInstance().getResources().get("res/models/telf.json", Mesh.class);
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Mesh tableMesh = Game.getInstance().getResources().get("res/models/table.json", Mesh.class);
        Mesh chairMesh = Game.getInstance().getResources().get("res/models/chair.json", Mesh.class);
        Mesh pcMesh = Game.getInstance().getResources().get("res/models/pc.json", Mesh.class);
        Texture telfTex = Game.getInstance().getResources().get("res/textures/telephone.png", Texture.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/room_texture.png", Texture.class);
        Texture doorTex = Game.getInstance().getResources().get("res/textures/door0.png", Texture.class);
        Texture door1Tex = Game.getInstance().getResources().get("res/textures/door1.png", Texture.class);
        Texture door2Tex = Game.getInstance().getResources().get("res/textures/door2.png", Texture.class);
        Texture tableTex = Game.getInstance().getResources().get("res/textures/table.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/room_texture_left.png", Texture.class);
        Texture pcTex = Game.getInstance().getResources().get("res/textures/pc.png", Texture.class);
        Texture notesTex = Game.getInstance().getResources().get("res/textures/notes.png", Texture.class);
        Texture posterTex = Game.getInstance().getResources().get("res/textures/poster.png", Texture.class);
        Texture poster1Tex = Game.getInstance().getResources().get("res/textures/poster1.png", Texture.class);

        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        TexturedMaterial telfMat = new TexturedMaterial(telfTex);
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);
        TexturedMaterial tableMat = new TexturedMaterial(tableTex);
        TexturedMaterial pcMat = new TexturedMaterial(pcTex);
        DecalMaterial doorMat = new DecalMaterial(doorTex, depth);
        DecalMaterial door1Mat = new DecalMaterial(door1Tex, depth);
        DecalMaterial door2Mat = new DecalMaterial(door2Tex, depth);
        DecalMaterial notesMat = new DecalMaterial(notesTex, depth);
        DecalMaterial posterMat = new DecalMaterial(posterTex, depth);
        DecalMaterial poster1Mat = new DecalMaterial(poster1Tex, depth);

        telf = new GeometryActor(telfModel, telfMat);
        telf.model.translate(2.25f, 1.0f, -2f);

        door0 = new DecalActor(doorMat);
        door0.model.translate(1, 0, -1f).rotateY(56*3.14f/180).scale(0.85f, 0.1f, 0.85f);

        notes0 = new DecalActor(notesMat);
        notes1 = new DecalActor(notesMat);
        notes2 = new DecalActor(notesMat);
        notes3 = new DecalActor(notesMat);
        notes4 = new DecalActor(notesMat);
        poster = new DecalActor(posterMat);
        poster1 = new DecalActor(poster1Mat);
        notes0.model.translate(2.25f, 1.0f, -1.5f).rotateX(90*3.1415f/180).rotateY(-90*3.1415f/180).scale(0.85f, 0.25f, 0.85f);
        notes1.model.translate(2.25f+1.5f, 1.25f, -1.5f).rotateX(90*3.1415f/180).rotateY(-90*3.1415f/180).scale(0.85f, 0.25f, 0.85f);
        notes2.model.translate(2.25f+1.25f, 1f, -1.5f).rotateX(90*3.1415f/180).rotateY(-90*3.1415f/180).scale(0.85f, 0.25f, 0.85f);
        notes3.model.translate(2.25f + 0.5f, 1.1f, -1.5f).rotateX(80*3.1415f/180).rotateY(-90*3.1415f/180).scale(0.85f, 0.25f, 0.85f);
        notes4.model.translate(0, 1.1f, -1f).rotateZ(-90*3.1415f/180).scale(0.85f, 0.25f, 0.85f);
        poster.model.translate(2.25f + 3.5f, 1.1f, -2f).rotateZ(0.1f).rotateX(90*3.1415f/180).rotateY(-90*3.1415f/180).scale(0.75f, 0.25f, 0.75f);
        poster1.model.translate(2.25f + 4.75f, 1.0f, -2f).rotateZ(-0.1f).rotateX(90*3.1415f/180).rotateY(-90*3.1415f/180).scale(0.65f, 0.25f, 0.65f);

        door1 = new DecalActor(door1Mat);
        door1.model.translate(0, 0.85f + 1e-3f, -1).rotateY(180*3.1415f/180).rotateZ(90*3.1415f/180).scale(0.85f, 0.1f, 0.85f);

        door2 = new DecalActor(door2Mat);
        door2.model.translate(1, 0.85f + 1e-3f, -2).rotateY(90*3.1415f/180).rotateZ(90*3.1415f/180).scale(0.85f, 0.1f, 0.85f);

        wall = new GeometryActor(wallMesh, wallMat);
        wall1 = new GeometryActor(wallMesh, wallMat);
        wall1.model.translate(8, 0, -2).rotateY(180*3.1415f/180);
        pc = new GeometryActor(pcMesh, pcMat);
        pc.model.translate(3.5f, 0.675f, -1.5f).rotateY(0.25f);
        chair = new GeometryActor(chairMesh, pcMat);
        chair.model.translate(5f, 0, -2.25f).rotateY(0.25f).scale(0.85f);
        table = new GeometryActor(tableMesh, tableMat);
        table.model.translate(4f, 0, -2).rotateY(-0.1f);
        tile0 = new GeometryActor(tileMesh, tileMat);
        tile1 = new GeometryActor(tileMesh, tileMat);
        tile1.model.translate(4, 0, 0);
    }

    @Override
    public void onEntered(Context context) {
        //tasks.clear();

        addGeometry(telf);
        addGeometry(table);
        addGeometry(chair);
        addGeometry(pc);
        addGeometry(wall);
        addGeometry(wall1);
        addGeometry(tile0);
        addGeometry(tile1);
        addDecal(door0);
        addDecal(door1);
        addDecal(door2);

        //addDecal(notes0);
        //addDecal(notes1);
        //addDecal(notes2);
        //addDecal(notes3);
        addDecal(notes4);

        addDecal(poster);
        addDecal(poster1);

        // add picks
        if (phoneRing) {
            phoneRing = false;
            tasks.add(new DelayTask(5, context.time));
            System.out.println("RINGING-END "+tasks.remaining());
            tasks.add(new DoSomethingTask(() -> {
                System.out.println("enter!");
                addPickerBox(new Vector3f(2.25f, 1.0f, -2f), new Vector3f(0.25f, 0.35f, 0.1f), "telf");
                context.audioRenderer.playSound(telfSound, true);
                phoneRing = true;
            }));
            System.out.println("RINGING-END "+tasks.remaining());
        }

        //addPickerBox(new Vector3f(3.5f, 1, -1.25f), new Vector3f(0.3f, 0.2f, 0.3f), "pc");
        addPickerBox(new Vector3f(1, 1, -2), new Vector3f(0.5f, 1f, 0.2f), "door");
        //addPickerBox(new Vector3f(2.5f, 1, -2f), new Vector3f(0.75f, 0.75f, 0.2f), "notes");

        // set camera
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(50), aspect, 0.01f, 100f);
        camera.clearColor.set(0.0f);

        cam.position.y = 1.5f;
        cam.position.z = 2.5f;
        cam.position.x = 2.5f;
        cam.tiltX = 0.1f;
    }

    @Override
    public void onTick(Context context) {
        tasks.update();
        //System.out.println(tasks.remaining()+" remm");

        TexturedMaterial telfMat = (TexturedMaterial) telf.getMaterial();
        if (phoneRing) {
            if ((int) (context.time.getTime() / 0.5f) % 2 == 0)
                telfMat.setTint(1, 0.68f, 0.68f);
            else telfMat.setTint(1, 1, 1);
        } else {
            telfMat.setTint(1, 1, 1);
        }

        cam.update();
        if (context.keyboard.isKeyDown(KeyboardInput.KEY_RIGHT)) {
            cam.position.x += context.time.getFrameTime()*2;
        } else if (context.keyboard.isKeyDown(KeyboardInput.KEY_LEFT)) {
            cam.position.x -= context.time.getFrameTime()*2;
        }

        //cam.position.x += (float) Math.sin(context.time.getTime()) * 0.001f;

        //if (cam.position.x < 2.5f) cam.position.x = 2.5f;

        fps.update(context);
    }

    FpsInput fps = new FpsInput(camera);

    @Override
    public void onSelected(Context context, Object data) {
        if (data.equals("notes")) {
            Game.getInstance().pushActivity(GameActivity.Note1);
        } else if (data.equals("door")) {
            Game.getInstance().popActivity();
            Game.getInstance().pushActivity(GameActivity.Interrogation);
        } else if (data.equals("telf")) {
            Game.getInstance().pushActivity(GameActivity.Dialog, (act, data1) -> {
                if (data1.equals("finish")) {
                    phoneRing = false;
                    emailReceived = true;
                    context.audioRenderer.playSound(hangPhone, false);
                } else if (data1.equals("screw_you")) {
                    context.audioRenderer.playSound(hangPhone, false);
                } else if (data1.equals("ignore")) {
                    phoneRing = true;
                    context.audioRenderer.playSound(hangPhone, false);
                    context.audioRenderer.stopSound(telfSound);
                } else if (data1.equals("pickup")) {
                    context.audioRenderer.stopSound(telfSound);
                }
            });
        }
    }

    @Override
    public void onLeft(Context context) {

    }
}
