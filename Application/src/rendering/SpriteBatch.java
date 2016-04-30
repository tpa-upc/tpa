package rendering;

import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.geometry.MeshUsage;
import tpa.graphics.geometry.Primitive;
import tpa.graphics.render.Blending;
import tpa.graphics.render.Renderer;
import tpa.graphics.render.RendererState;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.shader.UniformType;
import tpa.graphics.texture.Texture;
import tpa.joml.Matrix4f;
import tpa.joml.Vector3f;
import tpa.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by germangb on 25/04/2016.
 */
public class SpriteBatch {

    private RendererState state = new RendererState();
    private Renderer renderer;
    private ShaderProgram program;
    private Mesh mesh;
    private Texture used;

    private FloatBuffer position;
    private FloatBuffer color;
    private FloatBuffer uv;
    private IntBuffer indices;

    private boolean drawing = false;
    private Matrix4f projection = new Matrix4f();
    private int quads = 0;

    private Vector4f tint = new Vector4f(1);

    public SpriteBatch (Renderer renderer) {
        this.renderer = renderer;
        this.state.blending = Blending.Alpha;
        createProgram();
        createMesh();
    }

    public void setProjection (Matrix4f prj) {
        if (drawing) flush();
        program.setUniform("u_projection", UniformType.Matrix4, projection.set(prj));
    }

    public void setColor (float r, float g, float b, float a) {
        tint.set(r, g, b, a);
    }

    public void begin () {
        if (drawing) throw new IllegalStateException();
        drawing = true;
        quads = 0;

        // set up renderer
        renderer.setState(state);
        renderer.setShaderProgram(program);
        program.setUniform("u_projection", UniformType.Matrix4, projection);
        program.setUniform("u_texture", UniformType.Sampler2D, 0);
    }

    /**
     *
     * @param texture text texture
     * @param text text to be renderer
     */
    public void addText (Texture texture, int x, int y, String text, int textSize) {
        int tw = texture.getWidth();
        int th = texture.getHeight();
        int cw = tw/16;
        int ch = th/16;
        int offx = 0;
        int offy = 0;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '\n') {
                offx = 0;
                offy += ch;
                continue;
            }
            int row = c/16;
            int col = c%16;
            if (row < 16) {
                float u = row/16f;
                float v = col/16f;
                add(texture, x+offx, y+offy, cw, ch, u, v, 1/16f, 1/16f);
                offx += textSize;
            }
        }
    }

    public void add (Texture texture, float x, float y, float width, float height, float u, float v, float du, float dv) {
        if (texture != used && used != null)
            flush();

        // don't overflow the nio buffers!
        if (position.remaining() < 12 || color.remaining() < 16 || uv.remaining() < 8 || indices.remaining() < 6)
            flush();

        used = texture;
        position.put(new float[] {
                x, y, 0,
                x+width, y, 0,
                x+width, y+height, 0,
                x, y+height, 0
        });
        color.put(new float[] {
                tint.x, tint.y, tint.z, tint.w,
                tint.x, tint.y, tint.z, tint.w,
                tint.x, tint.y, tint.z, tint.w,
                tint.x, tint.y, tint.z, tint.w
        });
        uv.put(new float[]{
                u, v,
                u, v+dv,
                u+du, v+dv,
                u+du, v
        });
        int off = 4*quads;
        indices.put(new int[] {
                off+0, off+1, off+2,
                off+0, off+2, off+3
        });
        quads++;
    }

    public void flush () {
        if (!drawing) throw new IllegalStateException();
        if (quads == 0) return;

        position.rewind();
        color.rewind();
        uv.rewind();
        indices.rewind();

        // update mesh
        mesh.setData(Attribute.Position, position);
        mesh.setData(Attribute.Color, color);
        mesh.setData(Attribute.Uv, uv);
        mesh.setIndices(indices);
        mesh.setLength(6*quads);
        mesh.setOffset(0);

        // render & clear data
        if (used != null) {
            renderer.setTexture(0, used);
        }
        renderer.renderMesh(mesh);

        position.clear();
        uv.clear();
        indices.clear();
        quads = 0;
    }

    public void end () {
        if (!drawing) throw new IllegalStateException();
        flush();
        drawing = false;
        quads = 0;
        position.clear();
        uv.clear();
        indices.clear();
        used = null;
    }

    private void createMesh() {
        // create buffers
        position = ByteBuffer.allocateDirect(1000<<2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        color = ByteBuffer.allocateDirect(1000<<2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        uv = ByteBuffer.allocateDirect(1000<<2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        indices = ByteBuffer.allocateDirect(1000<<2).order(ByteOrder.nativeOrder()).asIntBuffer();

        // create mesh
        mesh = new Mesh(MeshUsage.Stream);
        mesh.setData(Attribute.Position, position);
        mesh.setData(Attribute.Color, color);
        mesh.setData(Attribute.Uv, position);
        mesh.setIndices(indices);
        mesh.setKeepData(true);
        mesh.setPrimitive(Primitive.Triangles);
        mesh.setOffset(0);
    }

    private void createProgram() {
        //language=GLSL
        String vert = "#version 120\n" +
                "\n" +
                "attribute vec3 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_uv;\n" +
                "\n" +
                "varying vec2 v_uv;\n" +
                "varying vec4 v_color;\n" +
                "\n" +
                "uniform mat4 u_projection;\n" +
                "\n" +
                "void main () {\n" +
                "    gl_Position = u_projection * vec4(a_position, 1.0);\n" +
                "    v_uv = a_uv;\n" +
                "    v_color = a_color;\n" +
                "}";
        //language=GLSL
        String frag = "#version 120\n" +
                "\n" +
                "varying vec2 v_uv;\n" +
                "varying vec4 v_color;\n" +
                "\n" +
                "uniform sampler2D u_texture;\n" +
                "\n" +
                "void main () {\n" +
                "    vec4 text = texture2D(u_texture, v_uv);\n" +
                "    gl_FragColor = text * v_color;\n" +
                "}";
        program = new ShaderProgram(vert, frag, Attribute.Position, Attribute.Uv, Attribute.Color);
    }
}
