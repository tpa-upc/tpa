package activity;

import game.Game;
import game.GameActivity;
import game.Values;
import rendering.DecalActor;
import rendering.FpsInput;
import rendering.GeometryActor;
import rendering.materials.DecalMaterial;
import rendering.materials.TexturedMaterial;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureWrap;
import tpa.joml.Vector3f;

/**
 * Created by Fran Roldan 14/05/2016.
 */
public class LoverLocation extends LocationActivity{

    FpsInput fps;

    GeometryActor wall0;
    GeometryActor wall1;
    GeometryActor wall2;
    GeometryActor wall3;
    GeometryActor tile0, tile0flip;
    GeometryActor tile1, tile1flip;
    GeometryActor barman, barmanHead;
    GeometryActor cube1, cube2, cube3, cube4, cube5;
    GeometryActor cone1, cone2, cone3, cone4, cone5;
    GeometryActor sphere1, sphere2;
    DecalActor door1;
    DecalActor bloodWall, bloodFloor;
    DecalActor forensicCard, barCard;
    boolean forensicCardFound = false;
    boolean barCardFound = false;
    boolean phoneCallMade = false;



    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/wall_left.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/capsule.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/room_texture.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/cara_sup.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/room_texture_left.png", Texture.class);
        Game.getInstance().getResources().load("res/models/capsule.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/chair.json", Texture.class);
        Game.getInstance().getResources().load("res/models/heavy_door.json", Texture.class);
        Game.getInstance().getResources().load("res/models/box.json", Texture.class);
        Game.getInstance().getResources().load("res/models/monkey.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/pixel.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/door0.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/Blood-Paret.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/Blood-Terra.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/Blood-Terra-reflect.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/forensicCard.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/bar_card.png", Texture.class);
        Game.getInstance().getResources().load("res/models/box.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/cone.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/sphere.json", Mesh.class);

        fps = new FpsInput(camera);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh tileMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Mesh wallMesh = Game.getInstance().getResources().get("res/models/wall_left.json", Mesh.class);
        Texture tileTex = Game.getInstance().getResources().get("res/textures/room_texture.png", Texture.class);
        Texture wallTex = Game.getInstance().getResources().get("res/textures/room_texture_left.png", Texture.class);
        Mesh chairMesh = Game.getInstance().getResources().get("res/models/chair.json", Mesh.class);
        Mesh barmanMesh = Game.getInstance().getResources().get("res/models/capsule.json", Mesh.class);
        Mesh monkeyMesh = Game.getInstance().getResources().get("res/models/monkey.json", Mesh.class);
        Texture barmanTex = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);
        //Texture chairTex = Game.getInstance().getResources().get(, Texture.class);
        //Mesh doorMesh = Game.getInstance().getResources().get("res/models/heavy_door.json", Mesh.class);
        Texture doorTex = Game.getInstance().getResources().get("res/textures/door0.png", Texture.class);
        Texture bloodWallTex = Game.getInstance().getResources().get("res/textures/Blood-Paret.png", Texture.class);
        Texture bloodFloorTex = Game.getInstance().getResources().get("res/textures/Blood-Terra.png", Texture.class);
        Texture bloodFloorTexRefl = Game.getInstance().getResources().get("res/textures/Blood-Terra-reflect.png", Texture.class);
        Texture forensicCardTex = Game.getInstance().getResources().get("res/textures/forensicCard.png", Texture.class);
        Texture barCardTex = Game.getInstance().getResources().get("res/textures/bar_card.png", Texture.class);
        Mesh cubeMesh = Game.getInstance().getResources().get("res/models/box.json", Mesh.class);
        Mesh sphereMesh = Game.getInstance().getResources().get("res/models/sphere.json", Mesh.class);
        Mesh coneMesh = Game.getInstance().getResources().get("res/models/cone.json", Mesh.class);

        // modify textures
        tileTex.setWrapU(TextureWrap.Repeat);
        tileTex.setWrapV(TextureWrap.Repeat);

        // create materials
        TexturedMaterial tileMat = new TexturedMaterial(tileTex);
        TexturedMaterial wallMat = new TexturedMaterial(wallTex);
        TexturedMaterial barmanMat = new TexturedMaterial(barmanTex);
        DecalMaterial doorMat = new DecalMaterial(doorTex, depth);
        DecalMaterial bloodWallMat = new DecalMaterial(bloodWallTex, depth);
        DecalMaterial bloodFloorMat = new DecalMaterial(bloodFloorTex, depth);
        bloodFloorMat.setReflective(bloodFloorTexRefl, reflectRender);
        DecalMaterial forensicCardMat = new DecalMaterial(forensicCardTex, depth);
        DecalMaterial barCardMat = new DecalMaterial(barCardTex, depth);

        /**Toys creation**/
        cube1 = new GeometryActor(cubeMesh, barmanMat);
        cube1.position.set(3,0,0);
        cube1.scale.set(0.05f,0.1f,0.05f);
        cube1.update();
        cube2= new GeometryActor(cubeMesh, barmanMat);
        cube2.position.set(3.75f,0,1.5f);
        cube2.scale.set(0.05f,0.1f,0.05f);
        cube2.update();
        cube3 = new GeometryActor(cubeMesh, barmanMat);
        cube3.position.set(6,0,-1.25f);
        cube3.scale.set(0.05f,0.1f,0.05f);
        cube3.update();
        cube4 = new GeometryActor(cubeMesh, barmanMat);
        cube4.position.set(7,0,-0.25f);
        cube4.scale.set(0.05f,0.1f,0.05f);
        cube4.update();
        cube5 = new GeometryActor(cubeMesh, barmanMat);
        cube5.position.set(1,0,-1.3f);
        cube5.scale.set(0.05f,0.1f,0.05f);
        cube5.update();
        cone1 = new GeometryActor(coneMesh, barmanMat);
        cone1.position.set(1.3f, 0, -0.75f);
        cone1.scale.set(0.15f, 0.15f, 0.15f);
        cone1.update();
        cone2 = new GeometryActor(coneMesh, barmanMat);
        cone2.position.set(1.15f, 0, 0.75f);
        cone2.scale.set(0.15f, 0.15f, 0.15f);
        cone2.update();
        cone3 = new GeometryActor(coneMesh, barmanMat);
        cone3.position.set(5.5f, 0, 1.15f);
        cone3.scale.set(0.15f, 0.15f, 0.15f);
        cone3.update();
        cone4 = new GeometryActor(coneMesh, barmanMat);
        cone4.position.set(3.3f, 0, 0.1f);
        cone4.scale.set(0.15f, 0.15f, 0.15f);
        cone4.update();
        cone5 = new GeometryActor(coneMesh, barmanMat);
        cone5.position.set(6.6f, 0, -0.35f);
        cone5.scale.set(0.15f, 0.15f, 0.15f);
        cone5.update();
        sphere1 = new GeometryActor(sphereMesh, barmanMat);
        sphere1.position.set(7.75f,0.1f,1.75f);
        sphere1.scale.set(0.15f, 0.15f, 0.15f);
        sphere1.update();
        sphere2 = new GeometryActor(sphereMesh, barmanMat);
        sphere2.position.set(5f,0.1f,-1.1f);
        sphere2.scale.set(0.15f, 0.15f, 0.15f);
        sphere2.update();

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
        barman.position.set(5,0.75f,1);
        barman.update();

        barmanHead = new GeometryActor(monkeyMesh, barmanMat);
        barmanHead.position.set(5,2f,1);
        barmanHead.update();

        door1 = new DecalActor(doorMat);
        door1.model.translate(0, 0.85f + 1e-3f, -1).rotateY(180*3.1415f/180).rotateZ(90*3.1415f/180).scale(0.85f, 0.1f, 0.85f);

        bloodWall = new DecalActor(bloodWallMat);
        bloodFloor = new DecalActor(bloodFloorMat);

        bloodWall.position.set(5,1,2f);
        bloodWall.rotation.rotateX((float)Math.toRadians(90));
        bloodWall.rotation.rotateY((float)Math.toRadians(-90));
        bloodWall.scale.set(1,0.05f,1);
        bloodWall.update();

        bloodFloor.position.set(5,0,1);
        bloodFloor.scale.set(2,0.05f, 2);
        bloodFloor.update();

        forensicCard = new DecalActor(forensicCardMat);
        forensicCard.position.set(7,0,0);
        forensicCard.scale.set(0.1f,0.05f,0.1f);
        forensicCard.update();

        barCard = new DecalActor(barCardMat);
        barCard.position.set(7.5f,0,1.3f);
        barCard.scale.set(0.1f, 0.05f, 0.1f);
        barCard.update();


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
        addGeometry(cube1);
        addGeometry(cube2);
        addGeometry(cube3);
        addGeometry(cube4);
        addGeometry(cube5);
        addGeometry(cone1);
        addGeometry(cone2);
        addGeometry(cone3);
        addGeometry(cone4);
        addGeometry(cone5);
        addGeometry(sphere1);
        addGeometry(sphere2);
        addDecal(door1);
        addDecal(bloodWall);
        addDecal(bloodFloor);
        addDecal(forensicCard);
        addDecal(barCard);

        /** Adding Picker Box for forensic and bar Cards**/
        addPickerBox(new Vector3f(7,0,0), new Vector3f(0.1f,0.05f,0.1f), "forensicCard");
        addPickerBox(new Vector3f(7.5f,0,1.3f), new Vector3f(0.1f,0.05f,0.1f), "barCard");
        if(forensicCardFound && barCardFound && phoneCallMade) {
            addPickerBox(new Vector3f(0, 0.85f + 1e-3f, -1), new Vector3f(0.85f, 0.1f, 0.85f), "leaveRoom"); //Door PickerBox
        }

        /**Set camera position**/
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(45), aspect, 0.01f, 100f);
        camera.clearColor.set(0.85f);
        cameraReflect.projection.setPerspective((float) Math.toRadians(45), aspect, 0.01f, 100f);
    }

    private FpsInput fpsRefl = new FpsInput(cameraReflect);

    @Override
    public void onTick(Context context) {
        fpsRefl.position.set(fps.position).mul(1, -1, 1);
        fpsRefl.pitch = fpsRefl.sPitch = -fps.sPitch;
        fpsRefl.yaw = fpsRefl.sYaw = fps.sYaw;

        fps.update(context);
        fpsRefl.update(context);

        barmanHead.rotation.set(camera.rotation).invert();
        barmanHead.rotation.identity().lookRotate(new Vector3f(fps.position).sub(barmanHead.position).normalize(), new Vector3f(0, 1, 0)).invert();
        barmanHead.update();
    }

    @Override
    public void onSelected(Context context, Object data) {
        if(data.equals("forensicCard")){
            Game.getInstance().pushActivity(GameActivity.ForensicCardFound);
            Game.getInstance().pushActivity(GameActivity.ForensicCard);
            forensicCardFound = true;
        }else if(data.equals("barCard")){
            Game.getInstance().pushActivity(GameActivity.BarCardFound);
            Game.getInstance().pushActivity(GameActivity.BarCard);
            barCardFound = true;
        }else if (data.equals("leaveRoom")){

        }
    }

    @Override
    public void onLeft(Context context) {

    }

}
