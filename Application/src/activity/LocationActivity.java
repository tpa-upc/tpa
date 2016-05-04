package activity;

import game.Game;
import game.Values;
import rendering.*;
import rendering.materials.CompositeMaterial;
import rendering.materials.DepthMaterial;
import rendering.materials.Material;
import rendering.materials.WireframeMaterial;
import rendering.utils.RayPicker;
import rendering.utils.Raymarcher;
import tpa.application.Context;
import tpa.application.Window;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.*;
import tpa.graphics.texture.*;
import tpa.input.mouse.Cursor;
import tpa.input.mouse.MouseAdapter;
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

    /** geometry that doesn't go into the depth pass */
    private List<GeometryActor> debugGeometry = new ArrayList<>();

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

    /** Wireframe material for debugging */
    //private WireframeMaterial wireframe;

    /** Ray picker */
    private RayPicker picker = new Raymarcher();

    /** 3d rendering helper */
    private SpriteBatch sprites;

    /** load location specific resources here */
    public abstract void onRoomPreLoad(Context context);

    /** loaded stuff */
    public abstract void onRoomPostLoad(Context context);

    /** Called when the scene is entered */
    public abstract void onEntered (Context context);

    /** called once per frame */
    public abstract void onTick (Context context);

    /** Called when something is 3D-picked */
    public abstract void onSelected(Context context, Object data);

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
        //wireframe = new WireframeMaterial();
        sprites = new SpriteBatch(context.renderer);
        onRoomPostLoad(context);
    }

    /**
     * Add a box for the 3d picker
     * @param pos box position
     * @param size box scale
     * @param data data associated with box
     */
    protected void addPickerBox (Vector3f pos, Vector3f size, Object data) {
        picker.addBox(pos, size, data);
        WireframeMaterial wire = new WireframeMaterial();
        wire.setTint(1, 0.7f, 0.7f);
        GeometryActor debug = new GeometryActor(box, wire);
        debug.model.translate(pos).scale(size);
        debugGeometry.add(debug);
    }

    /**
     * Add a geometry actor to the scene
     * @param actor geometry actor
     */
    protected void addGeometry (GeometryActor actor) {
        this.geometry.add(actor);
    }

    /**
     * Add a decal to the scene
     * @param actor decal actor
     */
    protected void addDecal (DecalActor actor) {
        this.decals.add(actor);
        WireframeMaterial wire = new WireframeMaterial();
        wire.setTint(0.7f, 1, 0.7f);
        GeometryActor debug = new GeometryActor(box, wire);
        debug.model.set(actor.model);
        debugGeometry.add(debug);
    }

    @Override
    public void onBegin(Context context) {
        onEntered(context);

        // set input listeners
        composite.setTimer(Values.LOCATION_TRANSITION_ANIMATION?0:9999);
        context.keyboard.setKeyboardListener(null);
        context.mouse.setMouseListener(new MouseAdapter() {
            @Override
            public void onMouseDown(int button) {
                Object hit = pick(context);

                if (hit != null) {
                    onSelected(context, hit);
                }
            }
        });
    }

    private Object pick (Context context) {
        float x = (float) context.mouse.getCursorX()/context.window.getWidth();
        float y = 1 - (float) context.mouse.getCursorY()/context.window.getHeight();

        Vector3f ro = new Vector3f();
        Vector3f rd = new Vector3f();
        camera.ray(x, y, ro, rd);

        return picker.query(ro, rd);
    }

    @Override
    public void onEnd(Context context) {
        onLeft(context);
        context.keyboard.setKeyboardListener(null);
        context.mouse.setMouseListener(null);
        geometry.clear();
        decals.clear();
        picker.clear();
        debugGeometry.clear();
        context.mouse.setCursor(Cursor.Arrow);
        Values.LOCATION_TRANSITION_ANIMATION = true;
    }

    @Override
    public void onUpdate(Context context) {
        onTick(context);

        if (pick(context) != null) context.mouse.setCursor(Cursor.Hand);
        else context.mouse.setCursor(Cursor.Arrow);

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

        // render debug geometry
        for (GeometryActor actor : debugGeometry) {
            Material mat = actor.getMaterial();
            mat.render(context.renderer, camera, box, actor.model);
        }

        // window pass
        renderer.setFramebuffer(null);
        renderer.setViewport(0, 0, window.getWidth(), window.getHeight());
        composite.render(context.renderer, null, quad, null);
    }
}
