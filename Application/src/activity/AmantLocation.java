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
import tpa.joml.Vector3f;

/**
 * Created by german on 04/05/2016.
 */
public class AmantLocation extends LocationActivity {

    FpsInput fps = new FpsInput(camera);

    GeometryActor box;
    DecalActor notas;

    boolean estado = false;
    int var = 42;

    @Override
    public void onRoomPreLoad(Context context) {
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/girl.jpg", Texture.class);
        Game.getInstance().getResources().load("res/textures/notes.png", Texture.class);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Mesh boxMesh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Texture boxTex = Game.getInstance().getResources().get("res/textures/girl.jpg", Texture.class);
        Texture notesTex = Game.getInstance().getResources().get("res/textures/notes.png", Texture.class);

        DecalMaterial notasMat = new DecalMaterial(notesTex, depth);
        TexturedMaterial boxMat = new TexturedMaterial(boxTex);

        box = new GeometryActor(boxMesh, boxMat);
        notas = new DecalActor(notasMat);
    }

    @Override
    public void onEntered(Context context) {
        addGeometry(box);
        addDecal(notas);

        addPickerBox(new Vector3f(1, 1, -2), new Vector3f(0.5f, 0.5f, 0.5f), "caja!");

        notas.model.identity().translate(1, 0, -2).rotateX(45*3.14f/180);

        camera.projection.setPerspective(45f*3.1415f/180, 4/3f, 0.1f, 1000f);
        camera.view.setLookAt(8, 4, 5, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onTick(Context context) {
        fps.update(context);
    }

    @Override
    public void onSelected(Context context, Object data) {
        if (data.equals("caja!")) {
            Game.getInstance().pushActivity(GameActivity.Note0);
            Game.getInstance().pushActivity(GameActivity.Dialog, new ActivityListener() {
                @Override
                public void onResult(Activity act, Object data) {
                    if (data.equals("ignore")) {
                        System.out.println("IGNORAR LLAMADA");
                    }
                }
            });
        }
    }

    @Override
    public void onLeft(Context context) {

    }
}
