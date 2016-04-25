package activity;

import activity.tasks.DelayTask;
import activity.tasks.Task;
import activity.tasks.TaskManager;
import game.Game;
import rendering.SpriteBatch;
import tpa.application.Context;
import tpa.audio.Sound;
import tpa.graphics.render.RendererState;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFilter;
import tpa.joml.Matrix4f;

/**
 * Created by germangb on 25/04/2016.
 */
public class IntroActivity extends Activity {

    SpriteBatch batch = null;
    String text = "";
    TaskManager tasks;
    Texture mono;
    Sound type;

    @Override
    public void onPreLoad(Context context) {
        tasks = new TaskManager();
        batch = new SpriteBatch(context.renderer);

        // load resources
        Game.getInstance().getResources().load("res/mono.png", Texture.class);
        Game.getInstance().getResources().load("res/sfx/type_writer.wav", Sound.class);
    }

    @Override
    public void onPostLoad(Context context) {
        type = Game.getInstance().getResources().get("res/sfx/type_writer.wav", Sound.class);
        mono = Game.getInstance().getResources().get("res/mono.png", Texture.class);
        mono.setMag(TextureFilter.Linear);
        mono.setMin(TextureFilter.Linear);
    }

    @Override
    public void onBegin(Context context) {
        int w = context.window.getWidth();
        int h = context.window.getHeight();
        batch.setProjection(new Matrix4f().setOrtho2D(0, w, h, 0));
        tasks.clear();

        text = "";
        String show = "Some_location\n10:20AM\nHello_world...";
        //tasks.add(new DelayTask(1, context.time));
        for (int i = 0; i < 13; ++i) {
            int ind = i;
            tasks.add(new Task() {
                @Override
                public void onBegin() {
                    blink = (ind&1)==0;
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });
            tasks.add(new DelayTask(0.25f, context.time));
        }

        for (int i = 0; i < show.length(); ++i) {
            int ind = i;
            tasks.add(new Task() {
                @Override
                public void onBegin() {
                    text += show.charAt(ind);
                    context.audioRenderer.playSound(type);
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });

            if (show.charAt(ind) != '\n')
                tasks.add(new DelayTask(0.075f + 0.075f*(float)Math.random(), context.time));
            else
                tasks.add(new DelayTask(0.25f, context.time));
        }

        for (int i = 0; i < 32; ++i) {
            int ind = i;
            tasks.add(new Task() {
                @Override
                public void onBegin() {
                    blink = (ind&1)==0;
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });
            tasks.add(new DelayTask(0.25f, context.time));
        }
    }

    private boolean blink = true;

    @Override
    public void onUpdate(Context context) {
        tasks.update();
        context.renderer.clearColorBuffer();
        context.renderer.setClearColor(0, 0, 0, 1);
        batch.begin();
        batch.addText(mono, 32, 256+64, text+(blink?'_':""), 24);
        //batch.add(mono, 0, 0, 512, 512, 0, 0, 1, 1);
        batch.end();
    }

    @Override
    public void onEnd(Context context) {

    }
}
