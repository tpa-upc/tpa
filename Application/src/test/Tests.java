package test;

import com.application.Application;
import com.application.Context;
import com.graphics.geometry.Attribute;
import com.graphics.geometry.Mesh;
import com.graphics.geometry.MeshUsage;
import com.graphics.render.Culling;
import com.graphics.render.RenderMode;
import com.graphics.render.Renderer;
import com.graphics.shader.ShaderProgram;
import com.graphics.shader.UniformType;
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
    ShaderProgram wireframe;

    @Override
    public void onInit(Context context) {
        // create programs
        wireframe = new ShaderProgram(StaticPrograms.WIRE_VERT, StaticPrograms.WIRE_FRAG, Attribute.Position);

        // create models
        cube = new Mesh(MeshUsage.Static);
        cube.setData(Attribute.Position, ByteBuffer.allocateDirect(StaticGeometry.CUBE_POSITION.length<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(StaticGeometry.CUBE_POSITION)
                .flip());
        cube.setIndices(ByteBuffer.allocateDirect(StaticGeometry.CUBE_INDICES.length<<2)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(StaticGeometry.CUBE_INDICES)
                .flip());
        cube.setLength(36);
    }

    @Override
    public void onUpdate(Context context) {
        float camX = (float) Math.cos(context.time.getTime()) * 5;
        float camZ = (float) Math.sin(context.time.getTime()) * 5;
        projection.setPerspective(50*3.1415f/180, 4f/3, 0.1f, 100f);
        view.setLookAt(camX, 2, camZ, 0, 0, 0, 0, 1, 0);
        wireframe.setUniform("u_projection", UniformType.Matrix4, projection);
        wireframe.setUniform("u_view", UniformType.Matrix4, view);
        wireframe.setUniform("u_model", UniformType.Matrix4, model);

        Renderer renderer = context.renderer;
        renderer.beginFrame();
        renderer.viewport(0, 0, 640, 480);
        renderer.clearBuffers();
        renderer.setDepth(true);
        renderer.clearColor(0, 0, 0, 1);

        renderer.setShaderProgram(wireframe);
        renderer.setRenderMode(RenderMode.Wireframe);
        renderer.setCulling(Culling.BackFace);
        renderer.renderMesh(cube);

        renderer.endFrame();
    }
}
