package activity;

import activity.tasks.DelayTask;
import activity.tasks.PrintTask;
import activity.tasks.TaskManager;
import game.Game;
import rendering.SpriteBatch;
import tpa.application.Context;
import tpa.graphics.texture.Texture;

/**
 * Created by germangb on 20/04/16.
 */
public class DialogActivity extends Activity {

    private static SpriteBatch batch = null;

    /** Dialog file */
    private String file;

    /** Dialog */
    private Dialog dialog;

    /** Current dialog node */
    private Dialog.Node node;

    /** task manager */
    private TaskManager tasks;

    public DialogActivity (String file) {
        this.file = file;
    }

    @Override
    public void onPreLoad(Context context) {
        if (batch == null) {
            batch = new SpriteBatch(context.renderer);
        }

        tasks = new TaskManager();
        tasks.add(new PrintTask("Hello world 0"));
        tasks.add(new DelayTask(2, context.time));
        tasks.add(new PrintTask("Hello world 1"));
        tasks.add(new DelayTask(2, context.time));
        tasks.add(new PrintTask("Hello world 2"));

        Game.getInstance().getResources().load(file, Dialog.class);
    }

    @Override
    public void onPostLoad(Context context) {
        dialog = Game.getInstance().getResources().get(file, Dialog.class);
    }

    @Override
    public void onBegin(Context context) {
        context.keyboard.setKeyboardListener(null);
    }

    @Override
    public void onUpdate(Context context) {
        tasks.update();

        context.renderer.setClearColor(0, 0, 0, 1);
        context.renderer.clearBuffers();

        // render dialog
        batch.begin();
        batch.end();
    }

    @Override
    public void onEnd(Context context) {

    }
}
