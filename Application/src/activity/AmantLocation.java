package activity;

import game.Game;
import game.GameActivity;
import rendering.DecalActor;
import rendering.FpsInput;
import rendering.GeometryActor;
import rendering.materials.DecalMaterial;
import rendering.materials.TexturedMaterial;
import tpa.application.Context;
import tpa.audio.Sound;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;
import tpa.joml.Vector3f;

/**
 * Created by german on 04/05/2016.
 */
public class AmantLocation extends LocationActivity {

    FpsInput fps;
    Sound telf;
    GeometryActor cubo;
    DecalActor notas;
    boolean dialogoOn = true;

    @Override
    public void onRoomPreLoad(Context context) {
        fps = new FpsInput(camera);

        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/pixel.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/notes.png", Texture.class);
        Game.getInstance().getResources().load("res/sfx/telf.wav", Sound.class);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh cuboMEsh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Texture texture = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);
        Texture notesTexture = Game.getInstance().getResources().get("res/textures/notes.png", Texture.class);
        telf = Game.getInstance().getResources().get("res/sfx/telf.wav", Sound.class);

        DecalMaterial decMat = new DecalMaterial(notesTexture, depth);
        TexturedMaterial mat = new TexturedMaterial(texture);
        mat.setTint(1, 0, 1);

        cubo = new GeometryActor(cuboMEsh, mat);
        notas = new DecalActor(decMat);

        camera.projection.setPerspective(45f, 4/3f, 0.1f, 1000f);
    }

    @Override
    public void onEntered(Context context) {
        cubo.model.identity().translate(0, 0, 0);
        notas.model.identity().translate(1, 0, -2).scale(1).rotateX(45*3.1415f/180);

        addGeometry(cubo);
        addDecal(notas);

        if (dialogoOn) {
            addPickerBox(new Vector3f(1, 1, -2), new Vector3f(0.5f, 0.5f, 0.5f), "caja!");
        }

        camera.view.setLookAt(1, 1, 2, 1, 1, 0, 0, 1, 0);
    }

    @Override
    public void onTick(Context context) {
        fps.update(context);
    }

    @Override
    public void onSelected(Context context, Object data) {
        if (data.equals("caja!")) {
            context.audioRenderer.playSound(telf, false);
            Game.getInstance().pushActivity(GameActivity.Note0);
        }
    }

    @Override
    public void onLeft(Context context) {

    }
}
