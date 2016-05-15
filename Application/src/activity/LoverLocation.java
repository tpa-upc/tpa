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
    DecalActor door1;
    DecalActor bloodWall, bloodFloor;
    DecalActor forensicCard, barCard;


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
        Game.getInstance().getResources().load("res/textures/Blood-Paret.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/Blood-Terra.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/forensicCard.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/bar_card.png", Texture.class);

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
        Texture bloodWallTex = Game.getInstance().getResources().get("res/textures/Blood-Paret.png", Texture.class);
        Texture bloodFloorTex = Game.getInstance().getResources().get("res/textures/Blood-Terra.png", Texture.class);
        Texture forensicCardTex = Game.getInstance().getResources().get("res/textures/forensicCard.png", Texture.class);
        Texture barCardTex = Game.getInstance().getResources().get("res/textures/bar_card.png", Texture.class);

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
        DecalMaterial forensicCardMat = new DecalMaterial(forensicCardTex, depth);
        DecalMaterial barCardMat = new DecalMaterial(barCardTex, depth);

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
        forensicCard.scale.set(0.05f,1f,0.05f);
        forensicCard.update();

        barCard = new DecalActor(barCardMat);
        barCard.position.set(7.5f,1,0);
        barCard.scale.set(0.05f, 1, 0.05f);
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
        addDecal(door1);
        addDecal(bloodWall);
        addDecal(bloodFloor);
        addDecal(forensicCard);
        addDecal(barCard);

        /** Adding Picker Box for forensic and bar Cards**/
        addPickerBox(new Vector3f(7,0,0), new Vector3f(0.05f,1,0.05f), "forensicCard");
        addPickerBox(new Vector3f(7.5f,1,0), new Vector3f(0.05f,1,0.05f), "barCard");

        /**Set camera position**/
        float aspect = (float) context.window.getWidth() / context.window.getHeight();
        camera.projection.setPerspective((float) Math.toRadians(45), aspect, 0.01f, 100f);
    }

    @Override
    public void onTick(Context context) {
        fps.update(context);
        barmanHead.rotation.set(camera.rotation).invert();
        barmanHead.rotation.identity().lookRotate(new Vector3f(fps.position).sub(barmanHead.position).normalize(), new Vector3f(0, 1, 0)).invert();
        barmanHead.update();
    }

    @Override
    public void onSelected(Context context, Object data) {
        if(data.equals("forensicCard")){
            Game.getInstance().pushActivity(GameActivity.ForensicCard);
        }else if(data.equals("barCard")){
            Game.getInstance().pushActivity(GameActivity.BarCard);
        }

    }

    @Override
    public void onLeft(Context context) {

    }

}
