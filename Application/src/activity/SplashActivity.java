package activity;

import activity.tasks.DelayTask;
import activity.tasks.DoSomethingTask;
import activity.tasks.Task;
import activity.tasks.TaskManager;
import game.Game;
import rendering.SpriteBatch;
import tpa.application.Context;
import tpa.graphics.texture.Texture;
import tpa.joml.Matrix4f;

/**
 * Created by german on 20/05/2016.
 */
public class SplashActivity extends Activity {

    static SpriteBatch sprite = null;
    Texture font, splash;
    String text, image;
    float wait = 1;

    public SplashActivity (String image, String text, float wait) {
        this.text = text;
        this.image = image;
        this.wait = wait;
    }

    @Override
    public void onPreLoad(Context context) {
        if (sprite == null) sprite = new SpriteBatch(context.renderer);
        Game.getInstance().getResources().load("res/textures/ubuntu24.png", Texture.class);
        Game.getInstance().getResources().load(image, Texture.class);
    }

    @Override
    public void onPostLoad(Context context) {
        font = Game.getInstance().getResources().get("res/textures/ubuntu24.png", Texture.class);
        splash = Game.getInstance().getResources().get(image, Texture.class);
        projection.setOrtho2D(0, context.window.getWidth(), context.window.getHeight(), 0);
    }

    float alpha = 0;
    Matrix4f projection = new Matrix4f();
    TaskManager tasks = new TaskManager();

    @Override
    public void onBegin(Context context) {
        //if (image.equals("res/textures/black.png")) System.out.println("begin!");
        //tasks.clear();
        //tasks.add(new DelayTask(1, context.time));
        tasks.add(new Task() {
            @Override
            public void onBegin() {
                alpha = 0;
            }

            @Override
            public boolean onUpdate() {
                alpha += 1/60f;
                boolean ret = alpha > 1;
                if (ret) alpha = 1;
                return ret;
            }
        });

        tasks.add(new DelayTask(wait, context.time));

        tasks.add(new Task() {
            @Override
            public void onBegin() {
                alpha = 1;
            }

            @Override
            public boolean onUpdate() {
                alpha -= 1/60f;
                boolean ret = alpha < 0;
                if (ret) alpha = 0;
                return ret;
            }
        });

        //tasks.add(new DelayTask(1, context.time));

        tasks.add(new DoSomethingTask(new Runnable() {
            @Override
            public void run() {
                end = true;
            }
        }));

        //if (image.endsWith("black.png")) System.out.println("asdasdasdasdsa");
    }

    boolean end = false;

    @Override
    public void onUpdate(Context context) {
        tasks.update();

        if (end) {
            tasks.clear();
            Game.getInstance().popActivity();
        }

        context.renderer.clearBuffers();
        context.renderer.setClearColor(0, 0, 0, 1);
        sprite.begin();
        sprite.setColor(alpha, alpha, alpha, 1);
        sprite.setProjection(projection);
        sprite.add(splash, context.window.getWidth()/2 - splash.getWidth()/2, context.window.getHeight()/2 - splash.getHeight()/2, splash.getWidth(), splash.getHeight(), 0, 0, 1, 1);
        sprite.addText(font, context.window.getWidth()/2-12*text.length()/2, context.window.getHeight() - 100, text, 12);
        sprite.end();
    }

    @Override
    public void onEnd(Context context) {
        tasks.clear();
        end = false;
    }
}
