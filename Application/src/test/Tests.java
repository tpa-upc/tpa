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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by germangb on 01/04/16.
 */
public class Tests extends Application {

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
    Framebuffer shadowmap;

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
        debug = new Texture(64, 64, TextureFormat.Rgb);
        debug.setData((ByteBuffer) ByteBuffer.allocateDirect(64*64*3).order(ByteOrder.nativeOrder()).put(StaticTextures.DEBUG).flip());
        debug.setWrapU(TextureWrap.Repeat);
        debug.setWrapV(TextureWrap.Repeat);
        debug.setMin(TextureFilter.MipmapLinear);
        debug.setMag(TextureFilter.Linear);
        debug.setGenerateMipmaps(true);

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
    }

    @Override
    public void onUpdate(Context context) {
        float camX = (float) Math.cos(context.time.getTime()/4) * 8;
        float camZ = (float) Math.sin(context.time.getTime()/4) * 8;
        projection.setPerspective(50*3.1415f/180, 4f/3, 0.1f, 1000f);
        view.setLookAt(camX, 6, camZ, 0, 0, 0, 0, 1, 0);

        shadowProjection.setOrtho(-32, 32, -32, 32, -16, 16);
        shadowView.setLookAlong(-1f, -1, -1, 0, 1, 0);

        diffuse.setUniform("u_projection_shadow", UniformType.Matrix4, shadowProjection);
        diffuse.setUniform("u_view_shadow", UniformType.Matrix4, shadowView);
        diffuse.setUniform("u_projection", UniformType.Matrix4, projection);
        diffuse.setUniform("u_view", UniformType.Matrix4, view);
        diffuse.setUniform("u_model", UniformType.Matrix4, model);
        diffuse.setUniform("u_texture", UniformType.Sampler2D, 0);
        diffuse.setUniform("u_shadowmap", UniformType.Sampler2D, 1);

        depth.setUniform("u_projection", UniformType.Matrix4, shadowProjection);
        depth.setUniform("u_view", UniformType.Matrix4, shadowView);
        depth.setUniform("u_model", UniformType.Matrix4, model);

        Renderer renderer = context.renderer;
        renderer.beginFrame();
        // render shadowmap
        renderer.setFramebuffer(shadowmap);
        renderer.viewport(0, 0, shadowmap.getWidth(), shadowmap.getHeight());
        renderer.clearDepthBuffer();
        renderer.setDepth(true);

        renderer.setShaderProgram(depth);

        depth.setUniform("u_model", UniformType.Matrix4, model.identity().rotate(context.time.getTime()*1.167f, 0, 1, 0).rotate(context.time.getTime()*0.27f, 1, 0, 0));
        renderer.renderMesh(cube);
        depth.setUniform("u_model", UniformType.Matrix4, model.identity().translate(3, 2, 3f).scale(0.5f).rotate(context.time.getTime()*1.167f, 0, 1, 0).rotate(context.time.getTime()*0.27f, 1, 0, 0));
        renderer.renderMesh(cube);
        depth.setUniform("u_model", UniformType.Matrix4, model.identity().translate(0, -1.5f, 0));
        renderer.renderMesh(plane);

        // render scene
        renderer.setFramebuffer(null);
        renderer.viewport(0, 0, 640, 480);
        renderer.clearBuffers();
        renderer.setDepth(true);
        renderer.clearColor(0, 0, 0, 1);

        renderer.setShaderProgram(diffuse);
        renderer.setTexture(0, debug);
        renderer.setTexture(1, shadowmap.getDepth());
        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().rotate(context.time.getTime()*1.167f, 0, 1, 0).rotate(context.time.getTime()*0.27f, 1, 0, 0));
        renderer.renderMesh(cube);
        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().translate(3, 2, 3f).scale(0.5f).rotate(context.time.getTime()*1.167f, 0, 1, 0).rotate(context.time.getTime()*0.27f, 1, 0, 0));
        renderer.renderMesh(cube);
        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().translate(0, -1.5f, 0));
        renderer.renderMesh(plane);

        renderer.endFrame();
    }
}
