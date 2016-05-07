package activity;

import game.Game;
import rendering.SpriteBatch;
import resources.ResourceManager;
import resources.ResourceUtils;
import tpa.application.Context;
import tpa.graphics.texture.Texture;
import tpa.joml.Matrix4f;

import java.util.LinkedList;

/**
 * Created by germangb on 07/05/16.
 */
public class LoadingActivity extends Activity {

    Texture loading, ubuntu;
    SpriteBatch batch;
    LinkedList<String> lines = new LinkedList<>();
    LinkedList<String> errors = new LinkedList<>();

    @Override
    public void onPreLoad(Context context) {}

    @Override
    public void onPostLoad(Context context) {}

    @Override
    public void onBegin(Context context) {
        batch = new SpriteBatch(context.renderer);
        try {
            //loading = ResourceUtils.loadTexture("res/textures/loading.png");
            ubuntu = ResourceUtils.loadTexture("res/textures/ubuntu24.png");
        } catch (Exception e) {
            e.printStackTrace();
        }


        // set resources listener
        Game.getInstance().getResources().setListener(new ResourceManager.ResourceManagerListener() {
            @Override
            public void onLoaded(String string, Class<?> type) {
                //System.out.println("[OK] "+string);
                lines.add("[OK] "+string);
            }

            @Override
            public void onFailed(String string, Class<?> type, Exception e) {
                //System.out.println("[ERR] "+string);
                lines.add("[ERR] "+string);
                errors.add("[ERR] "+string);
                //if (e != null) e.printStackTrace();
            }
        });
    }

    @Override
    public void onUpdate(Context context) {
        context.renderer.setClearColor(0, 0, 0, 1);
        context.renderer.clearColorBuffer();

        int w = context.window.getWidth();
        int h = context.window.getHeight();

        int progress = (int) (100 * Game.getInstance().getResources().getProgress());

        // render dialog
        batch.begin();
        batch.setProjection(new Matrix4f().setOrtho2D(0, w, h, 0));
        batch.setColor(1, 1, 1, 1);
        batch.addText(ubuntu, 32, 32, "Loading ... "+progress+"%", 12);
        int inc = 0;
        for (int i = Math.max(lines.size()-6, 0); i < lines.size(); ++i) {
            String line = lines.get(i);
            if (line.startsWith("[ERR]")) batch.setColor(1, 0.5f, 0.5f, 1);
            else batch.setColor(0.5f, 1, 0.5f, 1);
            batch.addText(ubuntu, 32, 64+16 + 24*inc, line, 12);
            inc++;
        }

        inc = 0;
        batch.setColor(1, 0.5f, 0.5f, 1);
        for (int i = Math.max(errors.size()-6, 0); i < errors.size(); ++i) {
            String line = errors.get(i);
            batch.addText(ubuntu, 32, 64+32 + 24*(inc+6), line, 12);
            inc++;
        }
        batch.end();
    }

    @Override
    public void onEnd(Context context) {

    }
}
