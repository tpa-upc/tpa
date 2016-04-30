package activity;

import game.Game;
import rendering.*;
import rendering.materials.CompositeMaterial;
import rendering.materials.DepthMaterial;
import rendering.materials.Material;
import tpa.application.Context;
import tpa.application.Window;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.*;
import tpa.graphics.texture.*;
import tpa.joml.Vector2f;
import tpa.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by germangb on 13/04/16.
 */
public abstract class LocationActivity extends Activity {

    /** low res scale */
    private static int SCALE = 2;

    ///** resource manager for the location, to load textures, meshes, sound, etc */
    //protected ResourceManager resources = new SimpleResourceManager();

    /** Camera of the location */
    protected Camera camera = new Camera();

    /** Geometry visible on the scene */
    private List<GeometryActor> geometry = new ArrayList<>();

    /** Decals visible on the screen */
    private List<DecalActor> decals = new ArrayList<>();

    /** Framebuffer for early Z pass */
    private Framebuffer zPass;

    /** depth texture from early z pass */
    protected Texture depth;

    /** material used to render geometry */
    private DepthMaterial depthMaterial = new DepthMaterial();

    /** framebuffer half as small as the window */
    private Framebuffer lowresPass;

    /** box for decals */
    private Mesh box;

    /** quad for rendering framebuffers */
    private Mesh quad;

    /** material for rendering framebuffer */
    private CompositeMaterial composite;

    /** load location specific resources here */
    public abstract void onRoomPreLoad(Context context);

    /** loaded stuff */
    public abstract void onRoomPostLoad(Context context);

    /** Called when the scene is entered */
    public abstract void onEntered (Context context);

    /** called once per frame */
    public abstract void onTick (Context context);

    /** Called when the scene is left */
    public abstract void onLeft (Context context);

    @Override
    public void onPreLoad(Context context) {
        // create needed resources
        Window win = context.window;
        zPass = new Framebuffer(win.getWidth()/SCALE, win.getHeight()/SCALE, new TextureFormat[]{}, true);
        depth = zPass.getDepth();
        lowresPass = new Framebuffer(win.getWidth()/SCALE, win.getHeight()/SCALE, new TextureFormat[]{TextureFormat.Rgb, TextureFormat.Rgb}, true);

        Game.getInstance().getResources().load("res/models/box.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/quad.json", Mesh.class);

        onRoomPreLoad(context);
    }

    @Override
    public void onPostLoad(Context context) {
        box = Game.getInstance().getResources().get("res/models/box.json", Mesh.class);
        quad = Game.getInstance().getResources().get("res/models/quad.json", Mesh.class);
        composite = new CompositeMaterial(lowresPass.getTargets()[0], lowresPass.getTargets()[1], context.time);
        onRoomPostLoad(context);
    }

    /**
     * Add a geometry actor to the scene
     * @param actor geometry actor
     */
    void addGeometry (GeometryActor actor) {
        this.geometry.add(actor);
    }

    /**
     * Add a decal to the scene
     * @param actor decal actor
     */
    void addDecal (DecalActor actor) {
        this.decals.add(actor);
    }

    @Override
    public void onBegin(Context context) {
        composite.setTimer(1000000);
        context.keyboard.setKeyboardListener(null);
        onEntered(context);
    }

    @Override
    public void onEnd(Context context) {
        onLeft(context);
        context.keyboard.setKeyboardListener(null);
        geometry.clear();
        decals.clear();
    }

    @Override
    public void onUpdate(Context context) {
        onTick(context);

        // update camera
        camera.update();

        Window window = context.window;
        Renderer renderer = context.renderer;

        // perfom early z pass
        renderer.setFramebuffer(zPass);
        renderer.clearDepthBuffer();
        renderer.setViewport(0, 0, zPass.getWidth(), zPass.getHeight());

        // set state for z pass
        RendererState stateZpass = new RendererState();
        stateZpass.depthTest = true;
        stateZpass.redMask = stateZpass.greenMask = stateZpass.blueMask = stateZpass.alphaMask = false;
        renderer.setState(stateZpass);

        for (GeometryActor actor : geometry) {
            depthMaterial.render(renderer, camera, actor.getMesh(), actor.model);
        }

        // lowres pass
        renderer.setFramebuffer(lowresPass);
        renderer.setViewport(0, 0, lowresPass.getWidth(), lowresPass.getHeight());
        renderer.setClearColor(camera.clearColor.x, camera.clearColor.y, camera.clearColor.z, 1);
        renderer.clearBuffers();

        // set low res state
        RendererState stateLow = new RendererState();
        stateLow.depthTest = true;
        renderer.setState(stateLow);

        // render geometry
        for (GeometryActor actor : geometry) {
            Material mat = actor.getMaterial();
            mat.render(renderer, camera, actor.getMesh(), actor.model);
        }

        // render decals
        for (DecalActor decal : decals) {
            Material mat = decal.getMaterial();
            mat.render(context.renderer, camera, box, decal.model);
        }

        // window pass
        renderer.setFramebuffer(null);
        renderer.setViewport(0, 0, window.getWidth(), window.getHeight());
        composite.render(context.renderer, null, quad, null);
    }
}
