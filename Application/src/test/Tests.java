package test;

import com.application.Application;
import com.application.Context;
import com.graphics.geometry.Attribute;
import com.graphics.geometry.Mesh;
import com.graphics.geometry.MeshUsage;
import com.graphics.geometry.Primitive;
import com.graphics.render.Culling;
import com.graphics.render.RenderMode;
import com.graphics.render.Renderer;
import com.graphics.shader.ShaderProgram;
import com.graphics.shader.UniformType;
import com.graphics.texture.*;
import com.joml.Matrix4f;
import com.joml.Vector2f;
import com.joml.Vector3f;

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

        // create texture
        debug = new Texture(128, 128, TextureFormat.Rgb);
        debug.setData((ByteBuffer) ByteBuffer.allocateDirect(128*128*3)
                .order(ByteOrder.nativeOrder())
                .put(StaticTextures.TEXTURE).flip());
        debug.setWrapU(TextureWrap.Repeat);
        debug.setWrapV(TextureWrap.Repeat);
        debug.setMin(TextureFilter.MipmapLinear);
        debug.setMag(TextureFilter.Linear);
        debug.setGenerateMipmaps(true);
        debug.setKeepData(false);

        texture2 = new Texture(128, 128, TextureFormat.Rgb);
        texture2.setData((ByteBuffer) ByteBuffer.allocateDirect(128*128*3)
                .order(ByteOrder.nativeOrder())
                .put(StaticTextures.TEXTURE2).flip());
        texture2.setWrapU(TextureWrap.Repeat);
        texture2.setWrapV(TextureWrap.Repeat);
        texture2.setMin(TextureFilter.MipmapLinear);
        texture2.setMag(TextureFilter.Linear);
        texture2.setGenerateMipmaps(true);
        texture2.setKeepData(false);

        // create models
        cube = new Mesh(MeshUsage.Static);
        cube.setData(Attribute.Position, ByteBuffer.allocateDirect(StaticGeometry.CUBE_POSITION.length<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(StaticGeometry.CUBE_POSITION)
                .flip());
        cube.setData(Attribute.Uv, ByteBuffer.allocateDirect(StaticGeometry.CUBE_UV.length<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(StaticGeometry.CUBE_UV)
                .flip());
        cube.setData(Attribute.Normal, ByteBuffer.allocateDirect(StaticGeometry.CUBE_NORMAL.length<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(StaticGeometry.CUBE_NORMAL)
                .flip());
        cube.setIndices(ByteBuffer.allocateDirect(StaticGeometry.CUBE_INDICES.length<<1)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(StaticGeometry.CUBE_INDICES)
                .flip());
        cube.setKeepData(false);
        cube.setLength(36);

        plane = new Mesh(MeshUsage.Static);
        plane.setPrimitive(Primitive.TriangleStrp);
        plane.setData(Attribute.Position, ByteBuffer.allocateDirect(StaticGeometry.PLANE_POSITION.length<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(StaticGeometry.PLANE_POSITION)
                .flip());
        plane.setData(Attribute.Uv, ByteBuffer.allocateDirect(StaticGeometry.PLANE_UV.length<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(StaticGeometry.PLANE_UV)
                .flip());
        plane.setData(Attribute.Normal, ByteBuffer.allocateDirect(StaticGeometry.PLANE_NORMAL.length<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(StaticGeometry.PLANE_NORMAL)
                .flip());
        plane.setIndices(ByteBuffer.allocateDirect(StaticGeometry.PLANE_INDICES.length<<1)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(StaticGeometry.PLANE_INDICES)
                .flip());
        plane.setKeepData(false);
        plane.setLength(6);

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
