package activity;

import activity.tasks.*;
import game.Game;
import game.Values;
import rendering.SpriteBatch;
import tpa.application.Context;
import tpa.audio.Sound;
import tpa.graphics.texture.Texture;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;
import tpa.input.mouse.Cursor;
import tpa.input.mouse.MouseAdapter;
import tpa.joml.Matrix4f;

import java.io.BufferedInputStream;

/**
 * Created by germangb on 20/04/16.
 */
public class DialogActivity extends Activity {

    private static SpriteBatch batch = null;

    /** DialoguePhone file */
    private String file;

    /** DialoguePhone */
    private Dialog dialog;

    /** Current dialog node */
    private Dialog.Node node;

    private Texture font, pixel;

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
    private int selected = 0;
    private String[] lines;

    public DialogActivity (String file) {
        this.file = file;
    }

    @Override
    public void onPreLoad(Context context) {
        this.context = context;
        if (batch == null)
            batch = new SpriteBatch(context.renderer);
        Game.getInstance().getResources().load(file, Dialog.class);
        Game.getInstance().getResources().load("res/textures/ubuntu24.png", Texture.class);
        Game.getInstance().getResources().load("res/textures/pixel.png", Texture.class);
        tasks = new TaskManager();
    }

    @Override
    public void onPostLoad(Context context) {
        dialog = Game.getInstance().getResources().get(file, Dialog.class);
        font = Game.getInstance().getResources().get("res/textures/ubuntu24.png", Texture.class);
        pixel = Game.getInstance().getResources().get("res/textures/pixel.png", Texture.class);

        node = dialog.dialog[0];
        state = 0;
        selected = 0;
    }

    @Override
    public void onBegin(Context context) {
        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                //if (key == KeyboardInput.KEY_ESCAPE) Game.getInstance().popActivity();
                /*if (state == 0) {
                    int id = key - KeyboardInput.KEY_1;
                    if (id >= 0 && id < node.questions.length) {
                        onSelectQuestion(id);
                    }
                }*/
            }
        });
        context.mouse.setMouseListener(new MouseAdapter() {
            @Override
            public void onMouseDown(int button) {
                if (state == 0) {
                    int id = selected;
                    if (id >= 0 && id < node.questions.length) {
                        onSelectQuestion(id);
                    }
                }
            }
        });

        tasks.clear();
        node = dialog.dialog[0];
        state = 0;
        selected = 0;

        if (node.questions.length == 1) {
            onSelectQuestion(0);
        }
    }

    @Override
    public void onUpdate(Context context) {
        tasks.update();

        //context.renderer.setClearColor(0.5f, 0.5f, 0.5f, 1);
        //context.renderer.clearBuffers();

        int w = context.window.getWidth();
        int h = context.window.getHeight();

        // render dialog
        batch.begin();
        batch.setProjection(new Matrix4f().setOrtho2D(0, w, h, 0));

        //batch.setColor(0, 0, 0, 1/60f * 0.35f);
        //batch.add(pixel, 0, 0, w, h, 0, 0, 1, 1);

        batch.setColor(0, 0, 0, 1);
        batch.add(pixel, 0, h-70, w, 70, 0, 0, 1, 1);

        context.mouse.setCursor(Cursor.Arrow);
        if (state == 0) {
            int my = h-context.mouse.getCursorY();
            selected = my/32;
            if (selected < node.questions.length)
                context.mouse.setCursor(Cursor.Hand);

            for (int i = 0; i < node.questions.length; ++i) {
                String text = node.questions[i].text;
                if (node.questions[i].preview != null)
                    text = node.questions[i].preview;
                batch.setColor(0.25f, 0.25f, 0.25f, 1);
                if (selected == i)
                    batch.setColor(1, 1, 1, 1);
                batch.addText(font, 16, h - 24*(i+1) - 12, "> "+text, 12);
            }
        } else if ((state == 1 || state == 2) && text.length() > 0) {
            if (state == 1) batch.setColor(0, 0, 0, 1);
            else batch.setColor(1, 1, 1, 1);

            //batch.add(pixel, 0, h-42, 12*text.length()+8, 42, 0, 0, 1, 1);

            if (state == 2) {
                float r = ((Values.TEXT_COLOR >> 16) & 0xff)/255f;
                float g = ((Values.TEXT_COLOR >> 8) & 0xff)/255f;
                float b = (Values.TEXT_COLOR & 0xff)/255f;
                batch.setColor(r, g, b, 1);
            }
            else batch.setColor(1, 1, 1, 1);

            batch.addText(font, 16, h - 46, text, 12);
        }
        batch.end();
    }

    private void onSelectQuestion (int id) {
        //System.out.println("asdasdasd "+id);
        state = 1;
        selected = 0;

        int ansId = node.questions[id].answer;
        Dialog.Answer ans = null;
        if (ansId >= 0) ans = node.answers[ansId];
        Dialog.Question que = node.questions[id];
        lines = que.text.split(";");
        text = "";

        //tasks.add(new DelayTask(0.25f, context.time));
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
                tasks.add(new DelayTask(0.035f, context.time));
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

        tasks.add(new DoSomethingTask(() -> {
            if (que.data != null)
                report(que.data);
        }));

        //tasks.add(new DelayTask(1, context.time));
        tasks.add(new DelayTask(0.75f, context.time));

        if (ans != null) {
            tasks.add(new Task() {
                @Override
                public void onBegin() {
                    text = "";
                    state = 2;
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });
            lines = ans.text.split(";");

            for (int i = 0; i < lines.length; ++i) {
                String str = lines[i];
                tasks.add(new DelayTask(0.5f, context.time));
                for (int x = 0; x < str.length(); ++x) {
                    String sub = str.substring(0, x + 1);
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
                    tasks.add(new DelayTask(0.035f, context.time));
                }

                tasks.add(new DelayTask(0.5f, context.time));
            }

            final Dialog.Answer ansf = ans;

            // report data
            if (ans.data != null) {
                tasks.add(new Task() {
                    @Override
                    public void onBegin() {
                        report(ansf.data);
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
                    if (ansf.jump >= 0) {
                        state = 0;
                        node = dialog.dialog[ansf.jump];

                        if (node.questions.length == 1) {
                            // go straight to the text
                            onSelectQuestion(0);
                        }
                    } else Game.getInstance().popActivity();
                }

                @Override
                public boolean onUpdate() {
                    return true;
                }
            });
        } else {
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
    }

    @Override
    public void onEnd(Context context) {
        Values.LOCATION_TRANSITION_ANIMATION = false;
    }
}
