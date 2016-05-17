package activity;

import game.Game;
import game.Values;
import rendering.SpriteBatch;
import tpa.application.Context;
import tpa.audio.Sound;
import tpa.graphics.render.RendererState;
import tpa.graphics.texture.Framebuffer;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFormat;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardInput;
import tpa.input.mouse.MouseAdapter;
import tpa.joml.Matrix4f;

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
        paper0 = Game.getInstance().getResources().get("res/sfx/paper0.wav", Sound.class);
        paper1 = Game.getInstance().getResources().get("res/sfx/paper1.wav", Sound.class);
    }

    @Override
    public void onBegin(Context context) {
        context.audioRenderer.playSound(paper1, false);

        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyboardInput.KEY_ESCAPE)
                    Game.getInstance().popActivity();
            }
        });

        context.mouse.setMouseListener(new MouseAdapter() {
            @Override
            public void onMouseDown(int button) {
                Game.getInstance().popActivity();
            }

            @Override
            public void onMouseScroll(int xoff, int yoff) {
                zoom += yoff * 0.1f;
                if (zoom < 0.1f) zoom = 0.1f;
            }
        });

        time = 0;
        rot = (float) Math.random()*2-1;
        rot *= 0.1f;
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

        drawer.begin();
        //drawer.setProjection(new Matrix4f());
        //drawer.add(fbo.getTargets()[0], -1, -1, 2, 2, 0, 0, 1, 1);
        float t = 1-(float)Math.exp(-time);
        //drawer.setProjection(new Matrix4f());
        //drawer.setColor(0, 0, 0, 1/60f*0.35f);
        //drawer.add(fbo.getTargets()[0], -1, -1, 2, 2, 0, 0, 1, 1);
        drawer.setProjection(new Matrix4f().setOrtho2D(-aspect*s, +aspect*s, s, -s).rotateZ(rot).scale((1-t) * 0.65f + t * 1).scale((float)texture.getWidth()/512f).scale(zoom));
        drawer.setColor(0,0,0, 0.025f);
        float off = 0 * (1-t) + 0.0125f * t;
        drawer.add(texture, -0.5f+off, -0.5f+off, 1, 1, 0, 0, 1, 1);
        drawer.setColor(1,1,1,1);
        drawer.add(texture, -0.5f, -0.5f, 1, 1, 0, 0, 1, 1);
        drawer.end();
    }

    @Override
    public void onEnd(Context context) {
        context.audioRenderer.playSound(paper0, false);
        Values.LOCATION_TRANSITION_ANIMATION = false;
    }
}
