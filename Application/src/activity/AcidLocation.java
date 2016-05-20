package activity;

import activity.tasks.DelayTask;
import activity.tasks.DoSomethingTask;
import activity.tasks.Task;
import activity.tasks.TaskManager;
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
import tpa.graphics.texture.TextureFilter;
import tpa.joml.Vector3f;

/**
 * Created by german on 04/05/2016.
 */
public class AcidLocation extends LocationActivity {

    FpsInput fps;
    Sound telf, hang, violin;
    GeometryActor cubo;
    GeometryActor person, smallPerson;
    DecalActor notas;
    boolean dialogoOn = true;

    @Override
    public void onRoomPreLoad(Context context) {
        fps = new FpsInput(camera);

        Game.getInstance().getResources().load("res/sfx/steps.wav", Sound.class);
        Game.getInstance().getResources().load("res/models/capsule.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/room_tile.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/girl.jpg", Texture.class);
        Game.getInstance().getResources().load("res/textures/debug.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/pixel.png", Texture.class);
        Game.getInstance().getResources().load("res/sfx/telf.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/violin0.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/hang_phone.wav", Sound.class);
    }

    @Override
    public void onRoomPostLoad(Context context) {
        Sound steps = Game.getInstance().getResources().get("res/sfx/steps.wav", Sound.class);
        violin = Game.getInstance().getResources().get("res/sfx/violin0.wav", Sound.class);
        Mesh personMesh = Game.getInstance().getResources().get("res/models/capsule.json", Mesh.class);
        Mesh cuboMEsh = Game.getInstance().getResources().get("res/models/room_tile.json", Mesh.class);
        Texture texture = Game.getInstance().getResources().get("res/textures/girl.jpg", Texture.class);
        Texture notesTexture = Game.getInstance().getResources().get("res/textures/debug.png", Texture.class);
        Texture personTexture = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);
        telf = Game.getInstance().getResources().get("res/sfx/telf.wav", Sound.class);
        hang = Game.getInstance().getResources().get("res/sfx/hang_phone.wav", Sound.class);

        fps.setSteps(steps);

        DecalMaterial decMat = new DecalMaterial(notesTexture, depth);

        TexturedMaterial personMaterial = new TexturedMaterial(personTexture);

        TexturedMaterial mat = new TexturedMaterial(texture);
        mat.setTint(1, 0, 1);

        cubo = new GeometryActor(cuboMEsh, mat);
        notas = new DecalActor(decMat);

        // mover la posicion del Decal
        float RAD2DEG = 3.14159265f/180f;
        notas.position.set(2, 0.4f, -1.75f);    // modifica posicion
        notas.scale.set(0.75f); // modifica la escala
        notas.rotation.rotateX(45*RAD2DEG); // rota sobre el eje X
        notas.rotation.rotateY(10*RAD2DEG); // rota sobre el eje Y
        notas.update(); // actualiza la transformacion

        // crear y mover la capsulas (por ahora, personas)
        person = new GeometryActor(personMesh, personMaterial);

        smallPerson = new GeometryActor(personMesh, personMaterial);
        smallPerson.position.set(0.5f, 0, 0);
        smallPerson.scale.set(1, 0.45f, 1);  // componente Y multiplicada por 0.45
        smallPerson.update();   // actualiza transformación

        // definir perspectiva de la camara
        camera.projection.setPerspective((float) Math.toRadians(120), 4/3f, 0.1f, 1000f);
        camera.clearColor.set(1,1,1);
    }

    TaskManager task = new TaskManager();

    @Override
    public void onEntered(Context context) {
        setNoise(0.5f);
        //Game.getInstance().pushActivity(GameActivity.Acid1);
        task.clear();
        task.add(new DelayTask(13, context.time));
        task.add(new Task() {
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
        task.add(new DoSomethingTask(() -> {
            Game.getInstance().popActivity();
            Game.getInstance().pushActivity(GameActivity.Lover);
            Game.getInstance().pushActivity(GameActivity.Acid2);
        }));

        addGeometry(cubo);
        addGeometry(person);
        addGeometry(smallPerson);
        addDecal(notas);

        context.audioRenderer.playSound(violin, false);



        addPickerBox(new Vector3f(0, 0.6f, 0), new Vector3f(0.2f, 0.6f, 0.2f), "persona");

        if (dialogoOn) {
            addPickerBox(new Vector3f(0.35f, 1, -2), new Vector3f(0.25f), "caja!");
        }

        fps.position.y = 1;
    }

    @Override
    public void onTick(Context context) {
        fps.update(context);
        task.update();
    }

    @Override
    public void onSelected(Context context, Object data) {
        if (data.equals("caja!")) {
            context.audioRenderer.playSound(telf, false);
            //Game.getInstance().pushActivity(GameActivity.Note0);
        } else if (data.equals("persona")) {
            context.audioRenderer.playSound(hang, false);
        }
    }

    @Override
    public void onLeft(Context context) {

    }
}
