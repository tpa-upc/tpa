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
import tpa.audio.Sound;
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
    DecalActor bottle;
    DecalActor bottle2;
    GeometryActor chair;
    GeometryActor chair2,chair3,chair4;
    GeometryActor table,table2;
    GeometryActor bar;
    GeometryActor bar2;
    DecalActor darts;
    Sound steps, slide, open, floor, snap;

    DecalActor logo;
    DecalActor logo2;


    int count = 0;
    boolean success = false;

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/sfx/beer_open.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/camera_snap.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/beer_slide.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/steps.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/can_floor.wav", Sound.class);
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
        Game.getInstance().getResources().load("res/textures/bottles_texture.png",Texture.class);
        Game.getInstance().getResources().load("res/models/chair.json", Mesh.class);
        //Game.getInstance().getResources().load("res/textures/pc.png", Texture.class);
        Game.getInstance().getResources().load("res/models/table.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/wood.jpg",Texture.class);
        Game.getInstance().getResources().load("res/models/box.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/darts.png",Texture.class);
        Game.getInstance().getResources().load("res/textures/sweet_malibu.png",Texture.class);
        fps = new FpsInput(camera);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/bar_texture.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/bar_texture_left.png", Texture.class);
        Mesh barmanMesh = Game.getInstance().getResources().get("res/models/capsule.json", Mesh.class);
        Mesh monkeyMesh = Game.getInstance().getResources().get("res/models/monkey.json", Mesh.class);
        Texture barmanTex = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);
        //Texture chairTex = Game.getInstance().getResources().get(, Texture.class);
        //Mesh doorMesh = Game.getInstance().getResources().get("res/models/heavy_door.json", Mesh.class);
        Texture doorTex = Game.getInstance().getResources().get("res/textures/door0.png", Texture.class);
        Texture bottleTex = Game.getInstance().getResources().get("res/textures/bottles_texture.png",Texture.class);
        Mesh chairMesh = Game.getInstance().getResources().get("res/models/chair.json", Mesh.class);
        Texture pcTex = Game.getInstance().getResources().get("res/textures/pc.png", Texture.class);
        Mesh tableMesh = Game.getInstance().getResources().get("res/models/table.json", Mesh.class);
        Texture woodTex = Game.getInstance().getResources().get("res/textures/wood.jpg",Texture.class);
        Mesh cubeMesh = Game.getInstance().getResources().get("res/models/box.json", Mesh.class);
        Texture dartsTex = Game.getInstance().getResources().get("res/textures/darts.png",Texture.class);
        Texture logoTex = Game.getInstance().getResources().get("res/textures/sweet_malibu.png",Texture.class);
        steps = Game.getInstance().getResources().get("res/sfx/steps.wav", Sound.class);
        slide = Game.getInstance().getResources().get("res/sfx/beer_slide.wav", Sound.class);
        open = Game.getInstance().getResources().get("res/sfx/beer_open.wav", Sound.class);
        floor = Game.getInstance().getResources().get("res/sfx/can_floor.wav", Sound.class);
        snap = Game.getInstance().getResources().get("res/sfx/camera_snap.wav", Sound.class);

        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);
        TexturedMaterial barmanMat = new TexturedMaterial(barmanTex);
        DecalMaterial doorMat = new DecalMaterial(doorTex, depth);
        DecalMaterial bottleMat = new DecalMaterial(bottleTex,depth);
        TexturedMaterial woodMat = new TexturedMaterial(woodTex);
        TexturedMaterial tableMat = new TexturedMaterial(woodTex);
        DecalMaterial dartsMat = new DecalMaterial(dartsTex,depth);
        DecalMaterial logoMat = new DecalMaterial(logoTex,depth);


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
        barman.position.set(7,0,1);
        barman.update();

        barmanHead = new GeometryActor(monkeyMesh, barmanMat);
        barmanHead.position.set(7,1.15f,1);
        barmanHead.update();

        door1 = new DecalActor(doorMat);
        door1.model.translate(0, 0.85f + 1e-3f, -1).rotateY(180*3.1415f/180).rotateZ(90*3.1415f/180).scale(0.85f, 0.1f, 0.85f);

        bottle = new DecalActor(bottleMat);
        bottle.rotation.rotateX((float)Math.toRadians(90));
        bottle.rotation.rotateY((float)Math.toRadians(-90));
        bottle.position.set(7, 1, 2);
        bottle.scale.set(1, 0.05f, 1);
        bottle.update();

        bottle2 = new DecalActor(bottleMat);
        bottle2.rotation.rotateX((float)Math.toRadians(180));
        bottle2.rotation.rotateZ((float)Math.toRadians(90));
        bottle2.position.set(8, 1, 1);
        bottle2.scale.set(1, 0.05f, 1);
        bottle2.update();

        darts= new DecalActor(dartsMat);
        darts.position.set(8,1.2f,-1.1f);
        darts.scale.set(0.4f,0.05f,0.4f);
        darts.rotation.rotateX((float)Math.toRadians(0));
        darts.rotation.rotateZ((float)Math.toRadians(-90));

        darts.update();

        logo = new DecalActor(logoMat);
        logo.position.set(3,1.3f,2);
        logo.scale.set(1,0.05f,1);
        logo.rotation.rotateX((float)Math.toRadians(90));
        logo.rotation.rotateY((float)Math.toRadians(-90));
        logo.update();

        logo2 = new DecalActor(logoMat);
        logo2.position.set(3,1.3f,-2);
        logo2.scale.set(1,0.05f,1);
        logo2.rotation.rotateX((float)Math.toRadians(90));
        logo2.rotation.rotateY((float)Math.toRadians(90));
        logo2.rotation.rotateZ((float)Math.toRadians(-180));
        logo2.update();


        chair = new GeometryActor(chairMesh, woodMat);
        chair.model.translate(3.5f, 0, -0.95f).rotateY(1.5f).scale(0.85f);

        chair2 = new GeometryActor(chairMesh, woodMat);
        chair2.model.translate(2.5f, 0, -0.95f).rotateY(-1.5f).scale(0.85f);

        chair3 = new GeometryActor(chairMesh,woodMat);
        chair3.model.translate(2.5f, 0, 0.95f).rotateY(-1.5f).scale(0.85f);

        chair4 = new GeometryActor(chairMesh,woodMat);
        chair4.model.translate(3.5f, 0, 0.95f).rotateY(1.5f).scale(0.85f);



        table = new GeometryActor(tableMesh, tableMat);
        table.position.set(3.1f,0,-1.45f);
        table.scale.set(0.5f,1,1);
        table.update();

        table2 = new GeometryActor(tableMesh,tableMat);
        table2.position.set(3.1f,0,0.45f);
        table2.scale.set(0.5f,1,1);
        table2.update();

        bar = new GeometryActor(cubeMesh, wallMat);
        bar.position.set(7.1f,0.3f,0);
        bar.scale.set(0.9f,0.2f,0.3f);
        bar.rotation.rotateX((float)Math.toRadians(90));
        bar.update();

        bar2 = new GeometryActor(cubeMesh, wallMat);
        bar2.position.set(6f,0.3f,0.9f);
        bar2.scale.set(1.1f,0.2f,0.3f);
        bar2.rotation.rotateY((float)Math.toRadians(90));
        bar2.rotation.rotateX((float)Math.toRadians(90));
        bar2.update();

        fps.setSteps(steps);
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
        addDecal(bottle);
        addDecal(bottle2);
        addGeometry(table);
        addGeometry(chair2);
        addGeometry(chair);
        addGeometry(bar);
        addGeometry(bar2);
        addDecal(darts);
        addGeometry(chair3);
        addGeometry(chair4);
        addGeometry(table2);
        addDecal(logo);
        addDecal(logo2);

        if(Values.ARGUMENTO == 2){
            addPickerBox(new Vector3f(7,0,1), new Vector3f(0.2f, 1, 0.2f), "barman");
        }
        if(Values.ARGUMENTO == 3){
            addPickerBox(new Vector3f(7,0,1), new Vector3f(0.2f, 1, 0.2f), "barmanp");
            addPickerBox(new Vector3f(0,0,-1), new Vector3f(0.1f, 1.75f, 0.5f), "gohome");
        }

        if (success) {
            tasks.add(new Task() {
                float t = 1;
                @Override
                public void onBegin() {
                    composite.setTimer(1);

                }

                @Override
                public boolean onUpdate() {
                    t -= context.time.getFrameTime() * 0.25f;
                    composite.setTimer(Math.max(t, 0));
                    return t < 0;
                }
            });
            tasks.add(new DoSomethingTask(() -> {
                Values.ARGUMENTO = 8;
                success = false;
                Game.getInstance().popActivity();
                Game.getInstance().pushActivity(GameActivity.Room);
            }));
        } else {
            if (Values.ARGUMENTO == 7 && questionCount <= MAX_QUESTIONS) {
                // primer "interrogatori"
                addPickerBox(new Vector3f(7, 0, 1), new Vector3f(0.2f, 1, 0.2f), "interrog0");
            } else if (Values.ARGUMENTO == 19 && questionCount <= MAX_QUESTIONS) {
                // primer "interrogatori"
                addPickerBox(new Vector3f(7, 0, 1), new Vector3f(0.2f, 1, 0.2f), "interrog1");
            }
        }

        if(count == 666){ /*change to 666*/
            System.out.println("You're the killer. The end.");
            Game.getInstance().pushActivity(GameActivity.ShortEnd);
            count = 0;
            /**set up variable to finish the game**/
        }

        // set camera
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(60), aspect, 0.01f, 100f);
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

    boolean doRecordings = false;
    boolean asdasdasd = false;

    @Override
    public void onSelected(Context context, Object data) {
        if (asdasdasd) return;
        if(data.equals("barman")) {
            Game.getInstance().pushActivity(GameActivity.Bar0, (act, dat) -> {
                if (dat.equals("finish")) {
                    Values.ARGUMENTO = 3;
                    Game.getInstance().popActivity();
                }
            });
        } else if (data.equals("interrog1")) {
            Game.getInstance().pushActivity(GameActivity.Bar3, (asd, dat) -> {
                if (dat.equals("recordings")) {
                    doRecordings = true;
                    Game.getInstance().popActivity();
                    Game.getInstance().pushActivity(GameActivity.Bar3Acuse, (asdasd, dd) -> {
                        if (dd.equals("accuse")) {
                            Game.getInstance().popActivity();
                            Game.getInstance().popActivity();
                            Values.TEXT = "The End\nSorry\nYou have reached the 1st ending";
                            Game.getInstance().pushActivity(GameActivity.Intro);
                            Game.getInstance().pushActivity(GameActivity.NewspaperBad);
                            context.audioRenderer.stopEverything();
                        } else if (dd.equals("keep")) {
                            asdasdasd = true;
                            Values.BAR_BIF |= 0x1;
                            Game.getInstance().popActivity();
                            tasks.add(new Task() {
                                float t = 1;
                                @Override
                                public void onBegin() {
                                    composite.setTimer(1);
                                }

                                @Override
                                public boolean onUpdate() {
                                    t -= context.time.getFrameTime() * 0.25f;
                                    composite.setTimer(Math.max(t, 0));
                                    return t < 0;
                                }
                            });
                            tasks.add(new DoSomethingTask(() -> {
                                if (Values.BAR_BIF == 0x3) Values.ARGUMENTO = 21;
                                else Values.ARGUMENTO = 20;
                                Game.getInstance().popActivity();
                                Game.getInstance().pushActivity(GameActivity.Room);
                            }));
                        }
                    });
                    Game.getInstance().pushActivity(GameActivity.PokerReaction);
                    Game.getInstance().pushActivity(GameActivity.Poker);
                    context.audioRenderer.playSound(snap, false);
                }
            });
        } else if (data.equals("interrog0")) {
            if (round == 0) {
                Game.getInstance().pushActivity(GameActivity.Bar2Round0, (lol, dat) -> {
                    if (dat.equals("slide")) {
                        context.audioRenderer.playSound(slide, false);
                    } else if (dat.equals("open")) {
                        context.audioRenderer.playSound(open, false);
                    } else {
                        questionCount++;
                        if (dat.equals("sep0")) hehehe |= 1 << 0;
                        if (dat.equals("sep1")) hehehe |= 1 << 1;
                        if (hehehe == 3) round++;
                    }
                });
            } else if (round == 1) {
                Game.getInstance().pushActivity(GameActivity.Bar2Round1, (lol, dat) -> {
                    if (dat.equals("floor")) {
                        context.audioRenderer.playSound(floor, false);
                    } else {
                        questionCount++;
                        if (dat.equals("sip")) {
                            round++;
                            success = true;
                        }
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
