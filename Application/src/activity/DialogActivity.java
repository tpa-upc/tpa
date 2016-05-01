package activity;

import activity.tasks.DelayTask;
import activity.tasks.PrintTask;
import activity.tasks.Task;
import activity.tasks.TaskManager;
import game.Game;
import rendering.SpriteBatch;
import tpa.application.Context;
import tpa.graphics.texture.Texture;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;
import tpa.joml.Matrix4f;

/**
 * Created by germangb on 20/04/16.
 */
public class DialogActivity extends Activity {

    private SpriteBatch batch;

    /** Dialog file */
    private String file;

    /** Dialog */
    private Dialog dialog;

    /** Current dialog node */
    private Dialog.Node node;

    private Texture font;

    /** task manager */
    private TaskManager tasks;

    private String text = "";

    private Context context;

    /**
     * 0 -> display questions
     * 1 -> animate question
     * 2 -> animate response
     */
    private int state = 0;
    private int line = 0;
    private String[] lines;

    public DialogActivity (String file) {
        this.file = file;
    }

    @Override
    public void onPreLoad(Context context) {
        this.context = context;
        batch = new SpriteBatch(context.renderer);
        Game.getInstance().getResources().load(file, Dialog.class);
        Game.getInstance().getResources().load("res/textures/ubuntu24.png", Texture.class);
        tasks = new TaskManager();
    }

    @Override
    public void onPostLoad(Context context) {
        dialog = Game.getInstance().getResources().get(file, Dialog.class);
        font = Game.getInstance().getResources().get("res/textures/ubuntu24.png", Texture.class);

        node = dialog.dialog[0];
        state = 0;
        line = 0;
    }

    @Override
    public void onBegin(Context context) {
        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (state == 0) {
                    int id = key - KeyboardInput.KEY_1;
                    if (id >= 0 && id < node.questions.length) {
                        onSelectQuestion(id);
                    }
                }
            }
        });
        context.mouse.setMouseListener(null);
    }

    @Override
    public void onUpdate(Context context) {
        tasks.update();

        context.renderer.setClearColor(0, 0, 0, 1);
        context.renderer.clearBuffers();

        int w = context.window.getWidth();
        int h = context.window.getHeight();

        // render dialog
        batch.begin();
        batch.setProjection(new Matrix4f().setOrtho2D(0, w, h, 0));

        if (state == 0) {
            for (int i = 0; i < node.questions.length; ++i) {
                String text = node.questions[i].text;
                if (node.questions[i].preview != null)
                    text = node.questions[i].preview;
                batch.addText(font, 16, h - 24*(i+1) - 16, "["+(i+1)+"] "+text, 12);
            }
        } else if (state == 1) {
            batch.addText(font, 16, h - 24 - 16, text, 12);
        }
        batch.end();
    }

    private void onSelectQuestion (int id) {
        System.out.println("asdasdasd "+id);
        state = 1;
        line = 0;

        Dialog.Answer ans = node.answers[node.questions[id].answer];
        Dialog.Question que = node.questions[id];
        lines = que.text.split(";");
        text = "";

        tasks.add(new DelayTask(0.5f, context.time));
        for (int i = 0; i < lines.length; ++i) {
            String str = lines[i];
            for (int x = 0; x < str.length(); ++x) {
                String sub = str.substring(0, x+1);
                tasks.add(new Task() {
                    @Override
                    public void onBegin() {
                        text = sub;
                    }

                    @Override
                    public boolean onUpdate() {
                        return true;
                    }
                });
                tasks.add(new DelayTask(0.05f, context.time));
            }

            tasks.add(new DelayTask(0.5f, context.time));
            tasks.add(new Task() {
                @Override
                public void onBegin() {
                    text = "";
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });
        }

        tasks.add(new DelayTask(1, context.time));
        tasks.add(new Task() {
            @Override
            public void onBegin() {
                text = "";
            }

            @Override
            public boolean onUpdate() {
                return true;
            }
        });
        lines = ans.text.split(";");

        for (int i = 0; i < lines.length; ++i) {
            String str = lines[i];
            for (int x = 0; x < str.length(); ++x) {
                String sub = str.substring(0, x+1);
                tasks.add(new Task() {
                    @Override
                    public void onBegin() {
                        text = sub;
                    }

                    @Override
                    public boolean onUpdate() {
                        return true;
                    }
                });
                tasks.add(new DelayTask(0.05f, context.time));
            }

            tasks.add(new DelayTask(0.5f, context.time));
        }

        // report data
        if (ans.data != null) {
            tasks.add(new Task() {
                @Override
                public void onBegin() {
                    report(ans.data);
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });
        }

        tasks.add(new Task() {
            @Override
            public void onBegin() {
                if (ans.jump >= 0) {
                    state = 0;
                    node = dialog.dialog[ans.jump];
                } else Game.getInstance().popActivity();
            }

            @Override
            public boolean onUpdate() {
                return true;
            }
        });
    }

    @Override
    public void onEnd(Context context) {

    }
}
