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
import rendering.materials.DecalMaterial;
import rendering.materials.TexturedMaterial;
import rendering.utils.CameraController;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureWrap;
import tpa.joml.Vector3f;

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
    GeometryActor barman, barmanHead;
    DecalActor door1;

    int count = 0;
    boolean success = true;

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/wall_left.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/capsule.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/bar_texture.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/cara_sup.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/bar_texture_left.png", Texture.class);
        Game.getInstance().getResources().load("res/models/capsule.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/chair.json", Texture.class);
        Game.getInstance().getResources().load("res/models/heavy_door.json", Texture.class);
        Game.getInstance().getResources().load("res/models/box.json", Texture.class);
        Game.getInstance().getResources().load("res/models/monkey.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/pixel.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/door0.png", Texture.class);

        fps = new FpsInput(camera);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/bar_texture.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/bar_texture_left.png", Texture.class);
        Mesh chairMesh = Game.getInstance().getResources().get("res/models/chair.json", Mesh.class);
        Mesh barmanMesh = Game.getInstance().getResources().get("res/models/capsule.json", Mesh.class);
        Mesh monkeyMesh = Game.getInstance().getResources().get("res/models/monkey.json", Mesh.class);
        Texture barmanTex = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);
        //Texture chairTex = Game.getInstance().getResources().get(, Texture.class);
        //Mesh doorMesh = Game.getInstance().getResources().get("res/models/heavy_door.json", Mesh.class);
        Texture doorTex = Game.getInstance().getResources().get("res/textures/door0.png", Texture.class);

        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);
        TexturedMaterial barmanMat = new TexturedMaterial(barmanTex);
        DecalMaterial doorMat = new DecalMaterial(doorTex, depth);

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

        barmanMat.setTint(0,0,1);
        barman = new GeometryActor(barmanMesh, barmanMat);
        barman.position.set(5,0,1);
        barman.update();

        barmanHead = new GeometryActor(monkeyMesh, barmanMat);
        barmanHead.position.set(5,1.15f,1);
        barmanHead.update();

        door1 = new DecalActor(doorMat);
        door1.model.translate(0, 0.85f + 1e-3f, -1).rotateY(180*3.1415f/180).rotateZ(90*3.1415f/180).scale(0.85f, 0.1f, 0.85f);

        fps.position.set(4, 1f, 0);
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
        addGeometry(barman);
        addGeometry(barmanHead);
        addDecal(door1);

        if(Values.ARGUMENTO == 2){
            addPickerBox(new Vector3f(5,0,1), new Vector3f(0.2f, 1, 0.2f), "barman");
        }
        if(Values.ARGUMENTO == 3){
            addPickerBox(new Vector3f(5,0,1), new Vector3f(0.2f, 1, 0.2f), "barmanp");
            addPickerBox(new Vector3f(0,0,-1), new Vector3f(0.1f, 1.75f, 0.5f), "gohome");
        }

        if (success) {
            tasks.add(new DoSomethingTask(() -> {
                Values.ARGUMENTO = 8;
                success = false;
                Game.getInstance().popActivity();
                Game.getInstance().pushActivity(GameActivity.Room);
            }));
        } else {
            if (Values.ARGUMENTO == 7 && questionCount <= MAX_QUESTIONS) {
                // primer "interrogatori"
                addPickerBox(new Vector3f(5, 0, 1), new Vector3f(0.2f, 1, 0.2f), "interrog0");
            }
        }

        if(count == 666){ /*change to 666*/
            System.out.println("You're the killer. The end.");
            Game.getInstance().pushActivity(GameActivity.ShortEnd);
            count = 0;
            /**set up variable to finish the game**/
        }

        if(Values.ARGUMENTO == 7){
            if (success) {

            } else {
                addPickerBox(new Vector3f(5, 0, 1), new Vector3f(0.2f, 1, 0.2f), "barman2");
            }

        }
        // set camera
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(45), aspect, 0.01f, 100f);
    }

    TaskManager tasks = new TaskManager();

    @Override
    public void onTick(Context context) {
        tasks.update();
        fps.update(context);
        barmanHead.rotation.set(camera.rotation).invert();
        barmanHead.rotation.identity().lookRotate(new Vector3f(fps.position).sub(barmanHead.position).normalize(), new Vector3f(0, 1, 0)).invert();
        barmanHead.update();
    }

    private int MAX_QUESTIONS = 4;
    private int round = 0;
    private int questionCount = 0;
    private int hehehe = 0;

    @Override
    public void onSelected(Context context, Object data) {
        if(data.equals("barman")) {
            Game.getInstance().pushActivity(GameActivity.Bar0, (act, dat) -> {
                if (dat.equals("finish")) {
                    Values.ARGUMENTO = 3;
                    Game.getInstance().popActivity();
                }
            });
        } else if (data.equals("interrog0")) {
            if (round == 0) {
                Game.getInstance().pushActivity(GameActivity.Bar2Round0, (lol, dat) -> {
                    questionCount++;
                    if (dat.equals("sep0")) hehehe |= 1<<0;
                    if (dat.equals("sep1")) hehehe |= 1<<1;
                    if (hehehe == 3) round++;
                });
            } else if (round == 1) {
                Game.getInstance().pushActivity(GameActivity.Bar2Round1, (lol, dat) -> {
                    questionCount++;
                    if (dat.equals("sep")) {
                        round++;
                        success = true;
                    }
                });
            }
        }else if(data.equals("barmanp")){
            Game.getInstance().pushActivity(GameActivity.Bar1, (act, dat) -> {
                if (dat.equals("satan")) {
                  count ++;
                }
            });
        }else if(data.equals("gohome")){
            Game.getInstance().popActivity();
            Game.getInstance().pushActivity(GameActivity.Room);
        }/*else if(data.equals("barman2")){
            Game.getInstance().pushActivity(GameActivity.Bar2, (act, dat) -> {
                //do sth here
            });
        }*/
    }

    @Override
    public void onLeft(Context context) {

    }
}
