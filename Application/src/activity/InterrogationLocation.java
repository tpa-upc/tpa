package activity;

import activity.tasks.DoSomethingTask;
import activity.tasks.Task;
import activity.tasks.TaskManager;
import game.Game;
import game.GameActivity;
import game.Values;
import rendering.DecalActor;
import rendering.FpsInput;
import rendering.GeometryActor;
import rendering.TextActor;
import rendering.materials.DecalMaterial;
import rendering.materials.TexturedMaterial;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureWrap;
import tpa.joml.Vector3f;

/**
 * Test room
 *
 * Created by germangb on 13/04/16.
 */
public class InterrogationLocation extends LocationActivity {

    //CameraController cam;
    FpsInput fps;

    GeometryActor window;
    GeometryActor door;
    GeometryActor table;
    GeometryActor wall0;
    GeometryActor wall1;
    GeometryActor wall2;
    GeometryActor wall3;
    GeometryActor tile0, tile1;
    GeometryActor thompson;
    GeometryActor anthony;
    GeometryActor chair, chair2;

    TextActor antonText;
    TextActor thomText;

    DecalActor albert;
    DecalActor enemies;

    final static int MAX_QUESTIONS = 3;
    int questionCount = 0;
    int round = 0;

    private float doorAnimation = 1;
    boolean youFuckedUp = false;

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/wall_left.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/heavy_door.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/window.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/table.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/capsule.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/interrogation_texture.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/interrogation_texture_left.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/interrogation_texture_top.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/heavy_door.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/albert.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/enemies.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/window.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/pixel.png", Texture.class);

        fps = new FpsInput(camera);
        //cam = new CameraController(camera);
        //cam.tiltZ = 0.025f;
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Mesh doorMesh = Game.getInstance().getResources().get("res/models/heavy_door.json", Mesh.class);
        Mesh windowMesh = Game.getInstance().getResources().get("res/models/window.json", Mesh.class);
        Mesh tableMesh = Game.getInstance().getResources().get("res/models/table.json", Mesh.class);
        Mesh capsuleMesh = Game.getInstance().getResources().get("res/models/capsule.json", Mesh.class);
        Mesh chairMesh = Game.getInstance().getResources().get("res/models/chair.json",Mesh.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/interrogation_texture.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/interrogation_texture_left.png", Texture.class);
        Texture wallTopTex = Game.getInstance().getResources().get("res/textures/interrogation_texture_top.png", Texture.class);
        Texture doorTex = Game.getInstance().getResources().get("res/textures/heavy_door.png", Texture.class);
        Texture albertTex = Game.getInstance().getResources().get("res/textures/albert.png", Texture.class);
        Texture enemiesTex = Game.getInstance().getResources().get("res/textures/enemies.png", Texture.class);
        Texture windowTex = Game.getInstance().getResources().get("res/textures/window.png", Texture.class);
        Texture capsuleTex = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);
        Texture chairTex = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);

        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);
        TexturedMaterial wallTopMat = new TexturedMaterial(wallTopTex);
        TexturedMaterial doorMat = new TexturedMaterial(doorTex);
        TexturedMaterial windowMat = new TexturedMaterial(windowTex);
        TexturedMaterial tableMat = new TexturedMaterial(doorTex);
        TexturedMaterial capsuleMat = new TexturedMaterial(capsuleTex);
        DecalMaterial albertMat = new DecalMaterial(albertTex, depth);
        DecalMaterial enemiesMat = new DecalMaterial(enemiesTex, depth);
        TexturedMaterial chairMat = new TexturedMaterial(chairTex);

        albert = new DecalActor(albertMat);
        albert.model.translate(0, 1, -0.5f).rotate(-90*3.1415f/180, 0, 0, 1).rotateY(0.15f).scale(0.35f, 0.1f, 0.35f);
        enemies = new DecalActor(enemiesMat);
        enemies.model.translate(0, 1, -1.25f).rotateY(180*3.1415f/180).rotate(-90*3.1415f/180, 0, 0, 1).rotateY(0.05f).scale(0.45f, 0.1f, 0.45f);
        door = new GeometryActor(doorMesh, doorMat);
        table = new GeometryActor(tableMesh, tableMat);
        table.model.translate(1.5f, 0, 0.5f).rotateY(95*3.1415f/180);
        window = new GeometryActor(windowMesh, windowMat);
        window.model.translate(1.5f, 0.5f, -2f);
        wall0 = new GeometryActor(wallMesh, wallMat);

        wall2 = new GeometryActor(wallMesh, wallMat);
        wall2.position.set(0, 0, 2);
        wall2.update();

        wall1 = new GeometryActor(wallMesh, wallMat);
        wall1.model.translate(4, 0, -2).rotateY(180*3.1415f/180);

        wall3 = new GeometryActor(wallMesh, wallMat);
        wall3.rotation.rotateY((float)Math.toRadians(180));
        wall3.position.set(4, 0, 0);
        wall3.update();

        tile0 = new GeometryActor(tileMesh, tileMat);
        tile1 = new GeometryActor(tileMesh, tileMat);
        tile1.rotation.rotateY((float)Math.toRadians(180));
        tile1.position.set(4, 0, 0);
        tile1.update();
        capsuleMat.setTint(0,1,0);

        anthony = new GeometryActor(capsuleMesh, capsuleMat);
        anthony.position.set(3,0,0);
        anthony.update();

        thompson = new GeometryActor(capsuleMesh, capsuleMat);
        thompson.position.set(0.25f, 0, 1.5f);
        thompson.scale.set(1, 1.2f, 1);
        thompson.update();

        antonText = new TextActor("Anthony guy");
        antonText.position.set(3, 1.25f, 0);
        antonText.update();

        thomText = new TextActor("Thompson");
        thomText.position.set(0.25f, 1.5f, 1.5f);
        thomText.update();

        chairMat.setTint(0.2f, 0.2f, 0.2f);
        chair = new GeometryActor(chairMesh, chairMat);
        chair.position.set(1,0,0);
        chair.rotation.rotateY((float)Math.toRadians(-90));
        chair.update();

        chair2 = new GeometryActor(chairMesh, chairMat);
        chair2.position.set(3,0,0);
        chair2.rotation.rotateY((float)Math.toRadians(90));
        chair2.update();
    }

    private TaskManager tasks = new TaskManager();

    @Override
    public void onEntered(Context context) {
        addGeometry(wall0);
        addGeometry(wall1);
        addGeometry(wall2);
        addGeometry(wall3);
        addGeometry(tile0);
        addGeometry(tile1);
        //addGeometry(door);
        addGeometry(window);
        addGeometry(table);
        addDecal(albert);
        addDecal(enemies);
        addGeometry(chair);
        addGeometry(chair2);
        addText(antonText);
        addText(thomText);

        /*
Do you have a child?
Yeah, his name is Alex. I hope he’s fine… I don’t want to talk about this now.
(Thompson: Don’t worry, we’ll do our best to find Alex).
Do you think she was having an affair with someone?
What?!? Well… maybe… I mean, I didn’t know where she has been most of the time so… why not…. Maybe she met someone in the pub… (sniff sniff)
         */

        addGeometry(anthony);

        if (!youFuckedUp) {
            addPickerBox(new Vector3f(3, 0.2f, 0), new Vector3f(0.2f, 1, 0.2f), "anton");
        } else {
            round = 0;
            questionCount = 0;
            composite.setTimer(1);
            tasks.add(new Task() {
                float t = 1;
                @Override
                public void onBegin() {
                }

                @Override
                public boolean onUpdate() {
                    t -= context.time.getFrameTime() * 0.25f;
                    composite.setTimer(Math.max(t, 0));
                    return t < 0;
                }
            });

            tasks.add(new DoSomethingTask(() -> {
                // Go back to room :)
                youFuckedUp = false;
                Game.getInstance().popActivity();
                Game.getInstance().pushActivity(GameActivity.Room);
                Values.ARGUMENTO = 3;
            }));
        }

        addGeometry(thompson);

        // set camera
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(50), aspect, 0.01f, 100f);
        camera.clearColor.set(0.125f);
        //cam.position.set(cam.position.x, 1.5f, 2.5f);
        //cam.tiltX = 0.1f;
        fps.position.y=1.25f;
        fps.position.x = 1;
        fps.position.z = 0;
        fps.setMovable(false);

        // test ray picker
        addPickerBox(new Vector3f(0, 1f, -1.25f), new Vector3f(0.1f, 0.35f, 0.35f), 16);
        addPickerBox(new Vector3f(0, 1f, -0.5f), new Vector3f(0.1f, 0.35f, 0.25f), 32);
    }

    private float time = 0;

    @Override
    public void onTick(Context context) {
        tasks.update();
        fps.update(context);

        antonText.billboard(camera);
        thomText.billboard(camera);

        //cam.update();
        //cam.position.x = 2.45f;// + 0.05f * (float) Math.sin(time);
        time += context.time.getFrameTime();

        doorAnimation -= context.time.getFrameTime();
        if (doorAnimation < 0) doorAnimation = 0;
        door.model.identity()
                .translate(3, 0, -2)
                .rotateY(-2.5f*Math.min(doorAnimation, 1));
    }

    @Override
    public void onSelected(Context context, Object data) {
        if (data.equals(16)) {
            Game.getInstance().pushActivity(GameActivity.Enemies);
        } else if (data.equals(32)) {
            Game.getInstance().pushActivity(GameActivity.Albert);
        } if(data.equals("anton") && questionCount < MAX_QUESTIONS){
            if (round == 0) {
                Game.getInstance().pushActivity(GameActivity.Interrogation0, (lol, d0) -> {
                    questionCount++;
                    if (d0.equals("nope")) {
                        if (questionCount >= MAX_QUESTIONS) {
                            // :(
                            youFuckedUp = true;
                            Game.getInstance().popActivity();
                        }
                    } else if (d0.equals("sip")) {
                        round++;
                    }
                });
            } else if (round == 2) {
                Game.getInstance().pushActivity(GameActivity.Interrogation1, (lol, d0) -> {
                    questionCount++;
                    if (d0.equals("nope")) {

                    } else if (d0.equals("sip")) {

                    }
                });
            }
        }
    }

    @Override
    public void onLeft(Context context) {

    }
}
