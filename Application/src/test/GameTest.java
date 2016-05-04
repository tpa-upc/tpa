package test;

import game.Game;
import game.GameActivity;
import rendering.materials.GrainMaterial;
import resources.ResourceManager;
import tpa.application.Application;
import tpa.application.Context;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Framebuffer;
import tpa.graphics.texture.TextureFormat;
import tpa.joml.Vector2f;

/**
 * Created by germangb on 12/04/16.
 */
public class GameTest implements Application {

    boolean loaded = false;

    // grain effect
    Mesh quad;
    Framebuffer fbo;
    GrainMaterial mat;

    @Override
    public void onInit(Context context) {
        // set context
        Game.getInstance().setContext(context);

        // load quad
        Game.getInstance().getResources().load("res/models/quad.json", Mesh.class);

        // create default Framebuffer & shader
        int w = context.window.getWidth();
        int h = context.window.getHeight();
        fbo = new Framebuffer(w, h, new TextureFormat[]{TextureFormat.Rgb}, true);
        context.renderer.setDefaultFramebuffer(fbo);
        mat = new GrainMaterial(fbo.getTargets()[0], context.time, new Vector2f(w, h));

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

                // grab quad
                quad = Game.getInstance().getResources().get("res/models/box.json", Mesh.class);

                for (GameActivity act : GameActivity.values())
                    act.getActivity().onPostLoad(context);

                // push some activity
                Game.getInstance().pushActivity(GameActivity.Amant);
                //Game.getInstance().pushActivity(GameActivity.Intro);
            }

            context.renderer.beginFrame();
            context.renderer.setDefaultFramebuffer(fbo);
            context.renderer.setFramebuffer(null);
            Game.getInstance().onUpdate(context);
            context.renderer.setDefaultFramebuffer(null);
            context.renderer.setFramebuffer(null);
            context.renderer.setViewport(0, 0, context.window.getWidth(), context.window.getHeight());
            mat.render(context.renderer, null, quad, null);
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
