package test;

import resources.ResourceManager;
import resources.SimpleResourceManager;
import tpa.application.Application;
import tpa.application.Context;
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
import tpa.joml.Matrix4f;
import tpa.joml.Vector2f;
import tpa.joml.Vector3f;

import javax.xml.soap.Text;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by germangb on 01/04/16.
 */
public class Tests extends Application {

    static final int WIDTH = 640;
    static final int HEIGHT = 480;

    /** Projection matrix */
    Matrix4f projection = new Matrix4f();
    Matrix4f shadowProjection = new Matrix4f();

    /** Camera transformation */
    Matrix4f view = new Matrix4f();

    /** view matrix shadowmap */
    Matrix4f shadowView = new Matrix4f();

    /** model transformation */
    Matrix4f model = new Matrix4f();

    Mesh cube;
    Mesh plane;
    ShaderProgram wireframe;
    ShaderProgram diffuse;
    ShaderProgram depth;
    Texture debug;
    Texture texture2;
    Framebuffer shadowmap;

    List<Matrix4f> cubes = new ArrayList<>();

    ResourceManager manager = new SimpleResourceManager();

    @Override
    public void onInit(Context context) {
        // create shadowmap
        shadowmap = new Framebuffer(2048, 2048, new TextureFormat[] {}, true);
        shadowmap.getDepth().setMin(TextureFilter.Linear);
        shadowmap.getDepth().setMag(TextureFilter.Linear);

        // create programs
        wireframe = new ShaderProgram(StaticPrograms.WIRE_VERT, StaticPrograms.WIRE_FRAG, Attribute.Position);
        diffuse = new ShaderProgram(StaticPrograms.DIFFUSE_VERT, StaticPrograms.DIFFUSE_FRAG, Attribute.Position, Attribute.Uv, Attribute.Normal);
        depth = new ShaderProgram(StaticPrograms.SHADOWMAP_VERT, StaticPrograms.SHADOWMAP_FRAG, Attribute.Position);

        // load textures
        manager.setListener(new ResourceManager.ResourceManagerListener() {
            @Override
            public void onLoaded(String string, Class<?> type) {
                System.out.println("[LOADED] "+string+" ("+type+")");
            }

            @Override
            public void onFailed(String string, Class<?> type, Exception e) {
                System.out.println("[FAILED] "+string+" ("+type+")");
            }
        });
        manager.load("res/texture.png", Texture.class);
        manager.load("res/texture2.png", Texture.class);
        manager.load("res/cube.json", Mesh.class);
        manager.load("res/ground.json", Mesh.class);
        manager.finishLoading();

        // grab textures
        debug = manager.get("res/texture.png", Texture.class);
        texture2 = manager.get("res/texture2.png", Texture.class);

        // grab meshes
        cube = manager.get("res/cube.json", Mesh.class);
        plane = manager.get("res/ground.json", Mesh.class);

        Random rand = new Random(42);
        for (int i = 0; i < 64; ++i) {
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

        float camX = (float) Math.cos(context.time.getTime()/4) * 32;
        float camZ = (float) Math.sin(context.time.getTime()/4) * 32;
        projection.setPerspective(50*3.1415f/180, 4f/3, 0.1f, 1000f);
        view.setLookAt(camX, 12 + 10*(float)Math.sin(context.time.getTime()*0.5), camZ, 0, 0, 0, 0, 1, 0);

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
        renderer.setCulling(Culling.BackFace);
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

        depth.setUniform("u_model", UniformType.Matrix4, model.identity().translate(0, -1.5f, 0));
        renderer.renderMesh(plane);

        // render scene
        renderer.setFramebuffer(null);
        renderer.viewport(0, 0, WIDTH, HEIGHT);
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
        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().translate(0, -1, 0));
        renderer.renderMesh(plane);

        renderer.setShaderProgram(wireframe);
        renderer.setRenderMode(RenderMode.Wireframe);
        wireframe.setUniform("u_projection", UniformType.Matrix4, projection);
        wireframe.setUniform("u_view", UniformType.Matrix4, view);
        wireframe.setUniform("u_model", UniformType.Matrix4, model.identity());

        renderer.endFrame();
    }
}
