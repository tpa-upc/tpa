package activity;

import game.Game;
import game.Values;
import rendering.SpriteBatch;
import tpa.application.Context;
import tpa.audio.Sound;
import tpa.graphics.render.RendererState;
import tpa.graphics.texture.Framebuffer;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFilter;
import tpa.graphics.texture.TextureFormat;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;
import tpa.input.mouse.MouseAdapter;
import tpa.joml.Matrix4f;
import tpa.joml.Vector3f;

/**
 * Created by germangb on 30/04/2016.
 */
public class ImageActivity extends Activity {

    private String path;
    private SpriteBatch drawer;
    private Texture texture;
    private Sound paper0;
    private Sound paper1;
    private Framebuffer fbo;
    private RendererState state = new RendererState();
    private float zoom = 1;
    private float offX = 0, offY = 0;

    public ImageActivity (String file) {
        this.path = file;
    }

    @Override
    public void onPreLoad(Context context) {
        Game.getInstance().getResources().load(path, Texture.class);
        Game.getInstance().getResources().load("res/sfx/paper0.wav", Sound.class);
        Game.getInstance().getResources().load("res/sfx/paper1.wav", Sound.class);

        drawer = new SpriteBatch(context.renderer);
        fbo = new Framebuffer(context.window.getWidth(), context.window.getHeight(), new TextureFormat[]{TextureFormat.Rgb}, false);
    }

    @Override
    public void onPostLoad(Context context) {
        texture = Game.getInstance().getResources().get(path, Texture.class);
        texture.setMag(TextureFilter.Linear);
        texture.setMin(TextureFilter.MipmapLinear);
        texture.setGenerateMipmaps(true);
        paper0 = Game.getInstance().getResources().get("res/sfx/paper0.wav", Sound.class);
        paper1 = Game.getInstance().getResources().get("res/sfx/paper1.wav", Sound.class);
    }

    @Override
    public void onBegin(Context context) {
        offX = 0;
        offY = 0;
        //context.audioRenderer.playSound(paper1, false);

        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyboardInput.KEY_ESCAPE)
                    Game.getInstance().popActivity();
            }
        });

        context.mouse.setMouseListener(new MouseAdapter() {
            /*@Override
            public void onMouseDown(int button) {
                Game.getInstance().popActivity();
            }*/

            @Override
            public void onMouseScroll(float xoff, float yoff) {
                zoom += yoff * 0.1f;
                if (zoom < 0.1f) zoom = 0.1f;
            }

            @Override
            public void onMouseDrag(int button) {
                float x = context.mouse.getCursorX();
                float y = context.mouse.getCursorY();
                float dx = context.mouse.getCursorDX();
                float dy = context.mouse.getCursorDY();
                int w = context.window.getWidth();
                int h = context.window.getHeight();
                Vector3f u = projection.unproject(x-dx, y-dy, 0, new int[]{0, 0, w, h}, new Vector3f());
                Vector3f v = projection.unproject(x, y, 0, new int[]{0, 0, w, h}, new Vector3f());
                offX += v.x - u.x;
                offY -= v.y - u.y;
            }
        });

        time = 0;
        rot = (float) Math.random()*2-1;
        rot *= 0.05f;
    }

    float time = 0;
    float rot = 0;

    @Override
    public void onUpdate(Context context) {
        context.renderer.clearBuffers();
        context.renderer.setClearColor(0, 0, 0, 1);
        float aspect = (float) context.window.getWidth()/context.window.getHeight();
        float s = 0.5f;
        time += context.time.getFrameTime()*16;

        float t = 1-(float)Math.exp(-time);
        projection.setOrtho2D(-aspect*s, +aspect*s, s, -s).rotateZ(rot).scale((1-t) * 0.65f + t * 1).scale((float)texture.getWidth()/512f).scale(zoom);

        drawer.begin();
        drawer.setProjection(projection);
        drawer.setColor(0,0,0, 0.025f);
        drawer.setColor(1,1,1,1);
        drawer.add(texture, offX-0.5f, offY-0.5f, 1, 1, 0, 0, 1, 1);
        drawer.end();
    }

    Matrix4f projection = new Matrix4f();

    @Override
    public void onEnd(Context context) {
        //context.audioRenderer.playSound(paper0, false);
        Values.LOCATION_TRANSITION_ANIMATION = false;
    }
}
