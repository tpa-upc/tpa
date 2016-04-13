package activity;

import rendering.*;
import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Context;
import tpa.application.Window;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.geometry.MeshUsage;
import tpa.graphics.geometry.Primitive;
import tpa.graphics.render.Culling;
import tpa.graphics.render.RenderMode;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.*;
import tpa.joml.Vector2f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by germangb on 13/04/16.
 */
public abstract class LocationActivity extends Activity {

    private static int SCALE = 2;

    private static String quadVert = "#version 130\n" +
            "\n" +
            "in vec3 a_position;\n" +
            "\n" +
            "out vec2 v_uv;\n" +
            "\n" +
            "void main () {\n" +
            "    gl_Position = vec4(a_position, 1.0);\n" +
            "    v_uv = a_position.xy*0.5+0.5;\n" +
            "}";

    private static String quadFrag = "#version 130\n" +
            "\n" +
            "in vec2 v_uv;\n" +
            "\n" +
            "out vec4 frag_color;\n" +
            "\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform sampler2D u_dither;\n" +
            "\n" +
            "uniform vec2 u_resolution;\n" +
            "\n" +
            "float rand(vec2 co){\n" +
            "    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
            "}\n" +
            "\n" +
            "void main () {\n" +
            "    float noise = rand(floor(gl_FragCoord.xy/2));\n" +
            "    vec3 dither = texture2D(u_dither, gl_FragCoord.xy/vec2(8.0*2)).rrr;\n" +
            "    vec3 color = texture2D(u_texture, v_uv).rgb;\n" +
            "    vec3 dithered = step(dither, color);\n" +
            "    frag_color = vec4(mix(color, dithered, 0.05), 1.0);\n" +
            "}";

    private static Mesh quadMesh = new Mesh(MeshUsage.Static);
    private static ShaderProgram quadProgram = new ShaderProgram(quadVert, quadFrag, Attribute.Position);
    static {
        float[] quadPos = {-1, -1, 0, +1, -1, 0, +1, +1, 0, -1, +1, 0};
        int[] quadInd = {0, 1, 2, 0, 2, 3};

        quadMesh.setData(Attribute.Position, ByteBuffer.allocateDirect(12<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(quadPos)
                .flip());

        quadMesh.setIndices(ByteBuffer.allocateDirect(6<<2)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(quadInd)
                .flip());

        quadMesh.setLength(6);
        quadMesh.setKeepData(false);
        quadMesh.setPrimitive(Primitive.Triangles);
    }

    /** resource manager for the location, to load textures, meshes, sound, etc */
    protected ResourceManager resources = new SimpleResourceManager();

    /** Camera of the location */
    protected Camera camera = new Camera();

    /** Geometry visible on the scene */
    private Set<GeometryActor> geometry = new HashSet<>();

    /** Decals visible on the screen */
    private Set<DecalActor> decals = new HashSet<>();

    /** Framebuffer for early Z pass */
    private Framebuffer zPass;

    /** framebuffer half as small as the window */
    private Framebuffer lowresPass;

    /** dithering texture */
    private Texture dither;

    /** box for decals */
    private Mesh box;

    /** load location (load resources or something!) */
    public abstract void loadLocation (Context context);

    /** Called when the scene is entered */
    public abstract void onEntered (Context context);

    /** called once per frame */
    public abstract void onTick (Context context);

    /** Called when the scene is left */
    public abstract void onLeft (Context context);

    @Override
    public void onInit(Context context) {
        // create needed resources
        Window win = context.window;
        zPass = new Framebuffer(win.getWidth()/SCALE, win.getHeight()/SCALE, new TextureFormat[]{}, true);
        lowresPass = new Framebuffer(win.getWidth()/SCALE, win.getHeight()/SCALE, new TextureFormat[]{TextureFormat.Rgb}, true);
        lowresPass.getTargets()[0].setMag(TextureFilter.Nearest);
        resources.load("res/dither.png", Texture.class);
        resources.load("res/box.json", Mesh.class);
        resources.finishLoading();
        box = resources.get("res/box.json", Mesh.class);
        dither = resources.get("res/dither.png", Texture.class);
        dither.setMag(TextureFilter.Nearest);
        dither.setWrapU(TextureWrap.Repeat);
        dither.setWrapV(TextureWrap.Repeat);

        // location specific stuff
        loadLocation(context);
    }

    /**
     * Add a geometry actor to the scene
     * @param actor geometry actor
     */
    public void addGeometry (GeometryActor actor) {
        this.geometry.add(actor);
    }

    /**
     * Add a decal to the scene
     * @param actor decal actor
     */
    public void addDecal (DecalActor actor) {
        this.decals.add(actor);
    }

    @Override
    public void onBegin(Context context) {
        onEntered(context);
    }

    @Override
    public void onEnd(Context context) {
        onLeft(context);
        geometry.clear();
        decals.clear();
    }

    @Override
    public void onUpdate(Context context) {
        onTick(context);

        Window window = context.window;
        Renderer renderer = context.renderer;

        // perfom z pass
        renderer.setFramebuffer(zPass);
        renderer.clearDepthBuffer();
        renderer.viewport(0, 0, zPass.getWidth(), zPass.getHeight());
        renderer.setDepth(true);
        renderer.setColorMask(false, false, false, false);

        for (GeometryActor actor : geometry) {
            Material mat = actor.getMaterial();
            mat.render(renderer, camera, actor.getMesh(), actor.model);
        }

        // lowres pass
        renderer.setFramebuffer(lowresPass);
        renderer.viewport(0, 0, lowresPass.getWidth(), lowresPass.getHeight());
        renderer.clearColor(camera.clearColor.x, camera.clearColor.y, camera.clearColor.z, 1);
        renderer.setDepth(true);
        renderer.clearBuffers();

        for (GeometryActor actor : geometry) {
            Material mat = actor.getMaterial();
            mat.render(renderer, camera, actor.getMesh(), actor.model);
        }

        for (DecalActor decal : decals) {
            Material mat = decal.getMaterial();
            mat.render(context.renderer, camera, box, decal.model);
        }

        // window pass
        renderer.setFramebuffer(null);
        renderer.viewport(0, 0, window.getWidth(), window.getHeight());
        renderer.setDepth(false);
        renderer.setRenderMode(RenderMode.Fill);
        renderer.setCulling(Culling.Disabled);
        renderer.setColorMask(true, true, true, true);
        renderer.setShaderProgram(quadProgram);
        quadProgram.setUniform("u_texture", UniformType.Sampler2D, 0);
        quadProgram.setUniform("u_dither", UniformType.Sampler2D, 1);
        quadProgram.setUniform("u_resolution", UniformType.Vector2, new Vector2f(window.getWidth(), window.getHeight()));
        renderer.setTexture(0, lowresPass.getTargets()[0]);
        renderer.setTexture(1, dither);
        renderer.renderMesh(quadMesh);
    }

    /**
     * Returns a decal material. This requires a depth texture
     * @param diffuse diffuse texture map
     * @return decal material ready to use
     */
    public DecalMaterial getDecalMaterial (Texture diffuse) {
        DecalMaterial mat = new DecalMaterial(diffuse, zPass.getDepth());
        return mat;
    }

}
