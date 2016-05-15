package activity;

import activity.tasks.Task;
import activity.tasks.TaskManager;
import game.Game;
import game.Values;
import rendering.*;
import rendering.materials.*;
import rendering.utils.RayPicker;
import rendering.utils.Raymarcher;
import tpa.application.Context;
import tpa.application.Window;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.*;
import tpa.graphics.texture.*;
import tpa.input.mouse.Cursor;
import tpa.input.mouse.MouseAdapter;
import tpa.joml.Matrix4f;
import tpa.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by germangb on 13/04/16.
 */
public abstract class LocationActivity extends Activity {
    
    private static final boolean DEBUG = true;

    /** low res scale */
    private static final int SCALE = 2;

    ///** resource manager for the location, to load textures, meshes, sound, etc */
    //protected ResourceManager resources = new SimpleResourceManager();

    /** Camera of the location */
    protected Camera camera = new Camera();

    /** Camera used for reflection stuff */
    protected Camera cameraReflect = new Camera();

    /** Geometry visible on the scene */
    private List<GeometryActor> geometry = new ArrayList<>();

    /** geometry that doesn't go into the depth pass */
    private List<GeometryActor> debugGeometry = new ArrayList<>();

    /** Decals visible on the scene */
    private List<DecalActor> decals = new ArrayList<>();

    /** Text in the scene */
    private List<TextActor> text = new ArrayList<>();

    /** Framebuffer for early Z pass */
    private Framebuffer zPass;

    /** Reflection framebuffer pass */
    private Framebuffer reflectPass;

    /** texture containing reflection */
    protected Texture reflectRender;

    /** depth texture from early z pass */
    protected Texture depth;

    /** text texture */
    private Texture font;

    /** material used to render geometry */
    private DepthMaterial depthMaterial = new DepthMaterial();

    /** framebuffer half as small as the window */
    private Framebuffer lowresPass;

    /** box for decals */
    private Mesh box;

    /** quad for rendering framebuffers */
    private Mesh quad;

    /** material for rendering framebuffer */
    protected CompositeMaterial composite;

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
        reflectPass = new Framebuffer(win.getWidth()/SCALE, win.getHeight()/SCALE, new TextureFormat[]{TextureFormat.Rgb, TextureFormat.Rgb}, true);
        reflectRender = reflectPass.getTargets()[0];

        Game.getInstance().getResources().load("res/models/box.json", Mesh.class);
        Game.getInstance().getResources().load("res/models/quad.json", Mesh.class);
        Game.getInstance().getResources().load("res/textures/ubuntu24.png", Texture.class);

        onRoomPreLoad(context);
    }

    @Override
    public void onPostLoad(Context context) {
        font = Game.getInstance().getResources().get("res/textures/ubuntu24.png", Texture.class);
        font.setMag(TextureFilter.Linear);
        box = Game.getInstance().getResources().get("res/models/box.json", Mesh.class);
        quad = Game.getInstance().getResources().get("res/models/quad.json", Mesh.class);
        composite = new CompositeMaterial(lowresPass.getTargets()[0], lowresPass.getTargets()[1], context.time);
        //wireframe = new WireframeMaterial();
        sprites = new SpriteBatch(context.renderer);
        onRoomPostLoad(context);
    }

    protected void addText (TextActor actor) {
        this.text.add(actor);
    }

    /**
     * Add a box for the 3d picker
     * @param pos box position
     * @param size box scale
     * @param data data associated with box
     */
    protected void addPickerBox (Vector3f pos, Vector3f size, Object data) {
        picker.addBox(pos, size, data);
        if (DEBUG) {
            WireframeMaterial wire = new WireframeMaterial();
            wire.setTint(1, 0.7f, 0.7f);
            GeometryActor debug = new GeometryActor(box, wire);
            debug.model.translate(pos).scale(size);
            debugGeometry.add(debug);
        }
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
        if (DEBUG) {
            WireframeMaterial wire = new WireframeMaterial();
            wire.setTint(0.7f, 1, 0.7f);
            GeometryActor debug = new GeometryActor(box, wire);
            debug.model.set(actor.model);
            debugGeometry.add(debug);
        }
    }

    /**
     * Set strength for screen noise effect
     * @param noise noise strength
     */
    protected void setNoise (float noise) {
        this.composite.setNoise(noise);
    }

    private TaskManager taks = new TaskManager();

    @Override
    public void onBegin(Context context) {
        onEntered(context);

        // set input listeners
        //composite.setTimer(Values.LOCATION_TRANSITION_ANIMATION?0:9999);

        taks.clear();
        if (Values.LOCATION_TRANSITION_ANIMATION) {
            composite.setTimer(0);
            taks.add(new Task() {
                float t = 0;
                @Override
                public void onBegin() {
                }

                @Override
                public boolean onUpdate() {
                    t += context.time.getFrameTime() * 0.25f;
                    composite.setTimer(Math.min(t, 1));
                    return t >= 1;
                }
            });
        } else {
            composite.setTimer(1);
        }
        context.mouse.setGrabbed(true);
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
        x = 0.5f;
        y = 0.5f;


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
        context.mouse.setGrabbed(false);
        geometry.clear();
        decals.clear();
        picker.clear();
        text.clear();
        debugGeometry.clear();
        context.mouse.setCursor(Cursor.Arrow);
        Values.LOCATION_TRANSITION_ANIMATION = true;
    }

    @Override
    public void onUpdate(Context context) {
        onTick(context);

        taks.update();

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

        // reflect pass
        renderer.setFramebuffer(reflectPass);
        renderer.setViewport(0, 0, reflectPass.getWidth(), reflectPass.getHeight());
        renderer.setClearColor(camera.clearColor.x, camera.clearColor.y, camera.clearColor.z, 1);
        renderer.clearBuffers();

        RendererState stateReflect = new RendererState();
        stateReflect.depthTest = true;
        renderer.setState(stateReflect);

        // render geometry
        for (GeometryActor actor : geometry) {
            Material mat = actor.getMaterial();
            if (mat instanceof TexturedMaterial) {
                if (((TexturedMaterial) mat).discardReflectPass)
                    continue;
            }
            mat.render(renderer, cameraReflect, actor.getMesh(), actor.model);
        }

        // render decal
        for (DecalActor decal : decals) {
            Material mat = decal.getMaterial();
            mat.render(context.renderer, cameraReflect, box, decal.model);
        }

        // render debug geometry
        for (GeometryActor actor : debugGeometry) {
            Material mat = actor.getMaterial();
            mat.render(context.renderer, cameraReflect, box, actor.model);
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

        // render text
        float scale = 0.005f;
        sprites.getState().depthTest = true;
        sprites.getState().depthMask = false;
        sprites.begin();
        sprites.setProjection(new Matrix4f(camera.viewProjection));

        for (TextActor act : text) {
            Vector3f pos = new Vector3f(3.5f, 0.675f + 0.5f, -1.5f);
            pos.set(act.position);
            sprites.setModel(act.model);
            sprites.addText(font, 0, 0 - 12 * scale, 0, act.getText(), 12, scale);
        }
        sprites.end();

        // window pass
        renderer.setFramebuffer(null);
        renderer.setViewport(0, 0, window.getWidth(), window.getHeight());
        composite.render(context.renderer, null, quad, null);
    }
}
