package test;

import game.Game;
import game.GameActivity;
import resources.ResourceManager;
import tpa.application.Application;
import tpa.application.Context;

/**
 * Created by germangb on 12/04/16.
 */
public class GameTest implements Application {

    private boolean loaded = false;

    @Override
    public void onInit(Context context) {
        // set context
        Game.getInstance().setContext(context);

        // load activities
        for (GameActivity act : GameActivity.values())
            act.getActivity().onPreLoad(context);

        // set resources listener
        Game.getInstance().getResources().setListener(new ResourceManager.ResourceManagerListener() {
            @Override
            public void onLoaded(String string, Class<?> type) {
                System.out.println("[OK] "+string);
            }

            @Override
            public void onFailed(String string, Class<?> type, Exception e) {
                System.out.println("[ERR] "+string);
                if (e != null) e.printStackTrace();
            }
        });
    }

    @Override
    public void onUpdate(Context context) {
        ResourceManager res = Game.getInstance().getResources();

        if (res.isFinishedLoading()) {
            if (!loaded) {
                loaded = true;
                for (GameActivity act : GameActivity.values())
                    act.getActivity().onPostLoad(context);

                // push some activity
                Game.getInstance().pushActivity(GameActivity.Intro, null);
            }

            context.renderer.beginFrame();
            Game.getInstance().onUpdate(context);
            context.renderer.endFrame();
        } else {
            // load resources
            res.update();

            context.renderer.beginFrame();
            context.renderer.clearBuffers();
            context.renderer.setClearColor(1, 0, 1, 1);
            context.renderer.endFrame();
        }
    }
}
