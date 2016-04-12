package rendering;

import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.geometry.MeshUsage;
import tpa.graphics.geometry.Primitive;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by germangb on 12/04/16.
 */
public class FontMeshHelper {

    /** generated mesh */
    private Mesh mesh;

    // vertex buffers
    private FloatBuffer position;
    private FloatBuffer uv;
    private IntBuffer index;

    public FontMeshHelper () {
        createMesh();
    }

    private void createMesh() {
        position = ByteBuffer.allocateDirect(10000<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        uv = ByteBuffer.allocateDirect(10000<<2)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        index = ByteBuffer.allocateDirect(10000<<2)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();

        mesh = new Mesh(MeshUsage.Dynamic);
        mesh.setData(Attribute.Position, position);
        mesh.setData(Attribute.Uv, uv);
        mesh.setIndices(index);
        mesh.setPrimitive(Primitive.Triangles);
        mesh.setKeepData(true);
    }

    /**
     * Clear text
     */
    public void clear () {
        position.clear();
        uv.clear();
        index.clear();
        count = 0;
    }

    /** character count */
    private int count = 0;

    /**
     * Set text. This will create the mesh for the text
     * @param text text to be meshified
     */
    public void setText (String text) {
        clear();
        int offX = 0;
        int offY = 0;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            int row = c/16;
            int col = c%16;

            position.put(new float[] {
                    offX+0, offY+0, 0,
                    offX+8, offY+0, 0,
                    offX+8, offY+8, 0,
                    offX+0, offY+8, 0
            });

            uv.put(new float[] {
                    col/16f, row/16f,
                    (col+1)/16f, row/16f,
                    (col+1)/16f, (row+1)/16f,
                    col/16f, (row+1)/16f
            });

            int off = 4*count;
            index.put(new int[] {
                    off+0, off+1, off+2,
                    off+0, off+2, off+3
            });

            count++;
            offX += 8;
        }

        position.flip();
        uv.flip();
        index.flip();

        mesh.setOffset(0);
        mesh.setLength(6*count);
        mesh.setDirty(true);
    }

    /**
     * Get generated mesh
     * @return
     */
    public Mesh getMesh() {
        return mesh;
    }
}
