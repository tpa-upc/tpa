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
import com.graphics.texture.Texture;
import com.graphics.texture.TextureFilter;
import com.graphics.texture.TextureFormat;
import com.graphics.texture.TextureWrap;
import com.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by germangb on 01/04/16.
 */
public class Tests extends Application {

    /** Projection matrix */
    Matrix4f projection = new Matrix4f();

    /** Camera transformation */
    Matrix4f view = new Matrix4f();

    /** model transformation */
    Matrix4f model = new Matrix4f();

    Mesh cube;
    Mesh plane;
    ShaderProgram wireframe;
    ShaderProgram diffuse;
    Texture debug;

    @Override
    public void onInit(Context context) {
        // create programs
        wireframe = new ShaderProgram(StaticPrograms.WIRE_VERT, StaticPrograms.WIRE_FRAG, Attribute.Position);
        diffuse = new ShaderProgram(StaticPrograms.DIFFUSE_VERT, StaticPrograms.DIFFUSE_FRAG, Attribute.Position, Attribute.Uv);

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
        float camX = (float) Math.cos(context.time.getTime()/4) * 6;
        float camZ = (float) Math.sin(context.time.getTime()/4) * 6;
        projection.setPerspective(50*3.1415f/180, 4f/3, 0.1f, 1000f);
        view.setLookAt(camX, 5, camZ, 0, 0, 0, 0, 1, 0);
        diffuse.setUniform("u_projection", UniformType.Matrix4, projection);
        diffuse.setUniform("u_view", UniformType.Matrix4, view);
        diffuse.setUniform("u_model", UniformType.Matrix4, model);
        diffuse.setUniform("u_texture", UniformType.Sampler2D, 0);

        Renderer renderer = context.renderer;
        renderer.beginFrame();
        renderer.viewport(0, 0, 640, 480);
        renderer.clearBuffers();
        renderer.setDepth(true);
        renderer.clearColor(0, 0, 0, 1);

        renderer.setShaderProgram(diffuse);
        renderer.setRenderMode(RenderMode.Fill);
        renderer.setCulling(Culling.BackFace);

        renderer.setTexture(0, debug);

        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().rotate(context.time.getTime()*1.167f, 0, 1, 0).rotate(context.time.getTime()*0.27f, 1, 0, 0));
        renderer.renderMesh(cube);

        diffuse.setUniform("u_model", UniformType.Matrix4, model.identity().translate(0, -1, 0));
        renderer.renderMesh(plane);

        renderer.endFrame();
    }
}
