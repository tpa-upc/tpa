package activity;

import rendering.Camera;
import rendering.GeometryActor;
import rendering.Material;
import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Context;
import tpa.application.Window;
import tpa.graphics.render.Renderer;
import tpa.graphics.texture.Framebuffer;
import tpa.graphics.texture.TextureFormat;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by germangb on 13/04/16.
 */
public abstract class LocationActivity extends Activity {

    /** resource manager for the location, to load textures, meshes, sound, etc */
    protected ResourceManager resources = new SimpleResourceManager();

    /** Camera of the location */
    protected Camera camera = new Camera();

    /** Geometry visible on the scene */
    private Set<GeometryActor> geometry = new HashSet<>();

    /** Framebuffer for early Z pass */
    private Framebuffer zPass;

    /** load location (load resources or something!) */
    public abstract void loadLocation ();

    /** Called when the scene is entered */
    public abstract void onEntered ();

    /** Called when the scene is left */
    public abstract void onLeft ();

    @Override
    public void onInit(Context context) {
        loadLocation();

        Window win = context.window;
        zPass = new Framebuffer(win.getWidth(), win.getHeight(), new TextureFormat[]{}, true);
    }

    /**
     * Add a geometry actor to the scene
     * @param actor geometry actor
     */
    public void addGeometry (GeometryActor actor) {
        this.geometry.add(actor);
    }

    @Override
    public void onBegin(Context context) {
        onEntered();
    }

    @Override
    public void onEnd(Context context) {
        onLeft();
        geometry.clear();
    }

    @Override
    public void onUpdate(Context context) {
        Window window = context.window;
        Renderer renderer = context.renderer;

        // perfom z pass
        renderer.setFramebuffer(zPass);
        renderer.viewport(0, 0, zPass.getWidth(), zPass.getHeight());
        renderer.setDepth(true);
        renderer.setColorMask(false, false, false, false);
        for (GeometryActor actor : geometry) {
            Material mat = actor.getMaterial();
            mat.render(renderer, camera, actor);
        }

        // final pass
        renderer.setFramebuffer(null);
        renderer.viewport(0, 0, window.getWidth(), window.getHeight());
        renderer.clearColor(camera.clearColor.x, camera.clearColor.y, camera.clearColor.z, 1);
        renderer.setDepth(true);
        renderer.clearBuffers();
        for (GeometryActor actor : geometry) {
            Material mat = actor.getMaterial();
            mat.render(renderer, camera, actor);
        }
    }

}
