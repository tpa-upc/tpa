package test;

import game.Game;
import game.GameActivity;
import tpa.application.Application;
import tpa.application.Context;

/**
 * Created by germangb on 12/04/16.
 */
public class GameTest implements Application {

    @Override
    public void onInit(Context context) {
        Game.getInstance().onInit(context);

        // start computer activity
        Game.getInstance().pushActivity(GameActivity.Monkey, null);
    }

    @Override
    public void onUpdate(Context context) {
        context.renderer.beginFrame();
        Game.getInstance().onUpdate(context);
        context.renderer.endFrame();
    }
}
