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

    static float DELAY = 0.05f;
    static float JITTER = 0.125f;

    SpriteBatch batch = null;
    String text = "";
    TaskManager tasks;
    Texture mono;
    Sound type, typeLow;
    float alpha = 1;

    @Override
    public void onPreLoad(Context context) {
        tasks = new TaskManager();
        batch = new SpriteBatch(context.renderer);

        // load resources
        Game.getInstance().getResources().load("res/textures/mono.png", Texture.class);
        Game.getInstance().getResources().load("res/sfx/type_writer.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/type_writer_low.wav", Sound.class);
    }

    @Override
    public void onPostLoad(Context context) {
        type = Game.getInstance().getResources().get("res/sfx/type_writer.wav", Sound.class);
        typeLow = Game.getInstance().getResources().get("res/sfx/type_writer_low.wav", Sound.class);
        mono = Game.getInstance().getResources().get("res/textures/mono.png", Texture.class);
        mono.setMag(TextureFilter.Linear);
        mono.setMin(TextureFilter.Linear);
    }

    @Override
    public void onBegin(Context context) {
        int w = context.window.getWidth();
        int h = context.window.getHeight();
        batch.setProjection(new Matrix4f().setOrtho2D(0, w, h, 0));
        tasks.clear();

        alpha = 1;
        text = "";
        String show = "Some_location\n10:20AM\nHello_world...";
        blink = false;
        tasks.add(new DelayTask(2, context.time));
        for (int i = 0; i < 7; ++i) {
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
                    if (show.charAt(ind) != '\n')
                        context.audioRenderer.playSound(type, false);
                    else
                        context.audioRenderer.playSound(typeLow, false);
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });

            if (show.charAt(ind) != '\n')
                tasks.add(new DelayTask(DELAY + JITTER*(float)(Math.random()*Math.random()), context.time));
            else
                tasks.add(new DelayTask(0.9f, context.time));
        }

        for (int i = 0; i < 5; ++i) {
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
        tasks.add(new Task() {
            @Override
            public void onBegin() {
                context.audioRenderer.playSound(typeLow, false);
                blink = false;
            }

            @Override
            public boolean onUpdate() {
                return true;
            }
        });

        tasks.add(new Task() {
            @Override
            public void onBegin() {
                alpha = 1;
            }

            @Override
            public boolean onUpdate() {
                alpha -= context.time.getFrameTime();
                if (alpha < 0) {
                    alpha = 0;
                    return true;
                }
                return false;
            }
        });

        tasks.add(new DelayTask(2, context.time));
        /*tasks.add(new Task() {
            @Override
            public void onBegin() {
                context.audioRenderer.playSound(telf, true);
            }

            @Override
            public boolean onUpdate() {
                return true;
            }
        });
        tasks.add(new DelayTask(2, context.time));*/

        tasks.add(new Task() {
            @Override
            public void onBegin() {
                Game.getInstance().popActivity();
            }

            @Override
            public boolean onUpdate() {
                return true;
            }
        });
    }

    private boolean blink = true;

    @Override
    public void onUpdate(Context context) {
        tasks.update();
        context.renderer.clearColorBuffer();
        context.renderer.setClearColor(0, 0, 0, 1);
        batch.begin();
        batch.setColor(alpha, alpha, alpha, alpha);
        batch.addText(mono, 32, context.window.getHeight()-200, text+(blink?'_':""), 24);
        batch.end();
    }

    @Override
    public void onEnd(Context context) {

    }
}
