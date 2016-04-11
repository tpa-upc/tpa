package test;

import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Application;
import tpa.application.Context;
import tpa.application.Window;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Culling;
import tpa.graphics.render.RenderMode;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.*;
import tpa.input.keyboard.KeyboardAdapter;
import tpa.input.keyboard.KeyboardListener;
import tpa.input.mouse.MouseAdapter;
import tpa.joml.Matrix4f;
import tpa.joml.Vector2f;
import tpa.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by germangb on 01/04/16.
 */
public class Tests extends Application {

    Matrix4f projection = new Matrix4f();
    Matrix4f shadowProjection = new Matrix4f();
    Matrix4f view = new Matrix4f();
    Matrix4f shadowView = new Matrix4f();
    Matrix4f model = new Matrix4f();

    Texture debug;
    Texture texture2;

    Mesh cube;
    Mesh plane;
    Mesh dragon;
    Mesh armadillo;

    ShaderProgram wireframe;
    ShaderProgram diffuse;
    ShaderProgram depth;

    Framebuffer shadowmap;

    List<Matrix4f> cubes = new ArrayList<>();

    ResourceManager manager = new SimpleResourceManager();

    @Override
    public void onInit(Context context) {
        // listen to keyboard input
        context.keyboard.setKeyboardListener(new KeyboardAdapter() {
            @Override
            public void onKeyDown(int key) {
                System.out.println("[KEYBOARD] "+key+" DOWN");
            }
        });

        // listen to mouse input
        context.mouse.setMouseListener(new MouseAdapter() {
            @Override
            public void onMouseDown(int button) {
                System.out.println("[MOUSE] "+button+" DOWN");
            }

            @Override
            public void onMouseScroll(int xoff, int yoff) {
                System.out.println("[SCROLL] dx="+xoff+" dy="+yoff);
            }
        });

        // create shadowmap
        shadowmap = new Framebuffer(2048, 2048, new TextureFormat[] {}, true);
        shadowmap.getDepth().setMin(TextureFilter.Linear);
        shadowmap.getDepth().setMag(TextureFilter.Linear);

        // create programs
        wireframe = new ShaderProgram(StaticPrograms.WIRE_VERT, StaticPrograms.WIRE_FRAG, Attribute.Position);
        diffuse = new ShaderProgram(StaticPrograms.DIFFUSE_VERT, StaticPrograms.DIFFUSE_FRAG, Attribute.Position, Attribute.Uv, Attribute.Normal);
        depth = new ShaderProgram(StaticPrograms.SHADOWMAP_VERT, StaticPrograms.SHADOWMAP_FRAG, Attribute.Position);

        manager.setListener(new ResourceManager.ResourceManagerListener() {
            @Override
            public void onLoaded(String string, Class<?> type) {
                System.out.println("[LOADED] "+string+" ("+type+")");
            }

            @Override
            public void onFailed(String string, Class<?> type, Exception e) {
                System.out.println("[FAILED] "+string+" ("+type+")");
                e.printStackTrace(System.out);
            }
        });

        // load resources
        manager.load("res/texture.png", Texture.class);
        manager.load("res/texture2.png", Texture.class);
        manager.load("res/cube.json", Mesh.class);
        manager.load("res/ground.json", Mesh.class);
        manager.load("res/dragon.json", Mesh.class);
        manager.load("res/armadillo.json", Mesh.class);
        manager.finishLoading();

        // grab textures
        debug = manager.get("res/texture.png", Texture.class);
        texture2 = manager.get("res/texture2.png", Texture.class);

        // grab meshes
        cube = manager.get("res/cube.json", Mesh.class);
        plane = manager.get("res/ground.json", Mesh.class);
        dragon = manager.get("res/dragon.json", Mesh.class);
        armadillo = manager.get("res/armadillo.json", Mesh.class);
        dragon.setKeepData(false);
        armadillo.setKeepData(false);

        Random rand = new Random(42);
        for (int i = 0; i < 16; ++i) {
            Vector3f axis = new Vector3f(rand.nextFloat()*2-1, rand.nextFloat()*2-1, rand.nextFloat()*2-1).normalize();
            Matrix4f mod = new Matrix4f()
                    .translate(rand.nextFloat()*42-21, rand.nextFloat()*16, rand.nextFloat()*42-21)
                    .rotate(context.time.getTime() + i, axis);
            cubes.add(mod);
        }
    }

    @Override
    public void onUpdate(Context context) {
        Random rand = new Random(42);
        for (int i = 0; i < cubes.size(); ++i) {
            Vector3f axis = new Vector3f(rand.nextFloat()*2-1, rand.nextFloat()*2-1, rand.nextFloat()*2-1).normalize();
            Matrix4f mod = cubes.get(i)
                    .identity()
                    .translate(rand.nextFloat()*42-21, rand.nextFloat()*16, rand.nextFloat()*42-21)
                    .rotate(context.time.getTime() + i, axis);
        }

        float camX = (float) Math.cos(context.time.getTime()/2) * 32;
        float camZ = (float) Math.sin(context.time.getTime()/2) * 32;
        projection.setPerspective(50*3.1415f/180, (float)context.window.getWidth()/context.window.getHeight(), 0.1f, 1000f);
        view.setLookAt(camX, 16 + 10*(float)Math.sin(context.time.getTime()*0.5), camZ, 0, 8, 0, 0, 1, 0);

        float s = 128;
        shadowProjection.setOrtho(-s, s, -s, s, -s, s);
        shadowView.setLookAlong(-1f, -1, -1, 0, 1, 0);

        depth.setUniform("u_projection", UniformType.Matrix4, shadowProjection);
        depth.setUniform("u_view", UniformType.Matrix4, shadowView);
        depth.setUniform("u_model", UniformType.Matrix4, model);

        Vector2f[] samples = new Vector2f[8];
        for (int i = 0; i < 8; ++i) {
            float a = (float) Math.random() * 3.1415f*2;
            samples[i] = new Vector2f((float)Math.sin(a), (float)Math.cos(a));
        }
        depth.setUniform("u_samples", UniformType.Vector2Array, samples);

        Renderer renderer = context.renderer;
        renderer.beginFrame();
        //renderer.setCulling(Culling.BackFace);
        renderer.setRenderMode(RenderMode.Fill);

        // render shadowmap
        renderer.setFramebuffer(shadowmap);
        renderer.viewport(0, 0, shadowmap.getWidth(), shadowmap.getHeight());
        renderer.clearDepthBuffer();
        renderer.setDepth(true);

        renderer.setShaderProgram(depth);

        cubes.forEach(mod -> {
            depth.setUniform("u_model", UniformType.Matrix4, mod);
            renderer.renderMesh(cube);
        });

        depth.setUniform("u_model", UniformType.Matrix4, model.identity().scale(1.5f));
        renderer.renderMesh(armadillo);

        depth.setUniform("u_model", UniformType.Matrix4, model.identity().translate(8,0,12).scale(1.5f));
        renderer.renderMesh(dragon);

        depth.setUniform("u_model", UniformType.Matrix4, model.identity().translate(0, -1.5f, 0));
        renderer.renderMesh(plane);

        // render scene
        renderer.setFramebuffer(null);
        renderer.viewport(0, 0, context.window.getWidth(), context.window.getHeight());
        renderer.clearBuffers();
        renderer.setDepth(true);
        renderer.clearColor(1, 1, 1, 1);

        renderer.setShaderProgram(diffuse);
        diffuse.setUniform("u_projection_shadow", UniformType.Matrix4, shadowProjection);
        diffuse.setUniform("u_view_shadow", UniformType.Matrix4, shadowView);
        diffuse.setUniform("u_projection", UniformType.Matrix4, projection);
        diffuse.setUniform("u_view", UniformType.Matrix4, view);
        diffuse.setUniform("u_model", UniformType.Matrix4, model);
        diffuse.setUniform("u_texture", UniformType.Sampler2D, 0);
        diffuse.setUniform("u_shadowmap", UniformType.Sampler2D, 1);

        renderer.setTexture(0, debug);
        renderer.setTexture(1, shadowmap.getDepth());

        cubes.forEach(mod -> {
            diffuse.setUniform("u_model", UniformType.Matrix4, mod);
            renderer.renderMesh(cube);
        });

        renderer.setTexture(0, texture2);
        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().scale(1.5f));
        renderer.renderMesh(armadillo);

        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().translate(8,0,12).scale(1.5f));
        renderer.renderMesh(dragon);

        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().translate(0, 0, 0));
        renderer.renderMesh(plane);

        renderer.endFrame();
    }
}
