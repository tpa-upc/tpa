package resources;

import com.google.gson.Gson;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.geometry.MeshUsage;
import tpa.graphics.geometry.Primitive;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFormat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by germangb on 10/04/16.
 */
public class ResourceUtils {

    private static Gson gson = new Gson();

    private class AttributeJson {
        String name;
        float[] data;
    }

    private class IndicesJson {
        String type;
        int[] data;
    }

    private class MeshJson {
        String primitive;
        AttributeJson[] attributes;
        IndicesJson indices;
    }

    /**
     * Load a mesh.
     * @param path mesh path
     * @return loaded mesh with all buffers
     * @throws Exception error
     */
    public static Mesh loadMesh (String path) throws Exception {
        FileReader reader = new FileReader(path);
        Mesh mesh = new Mesh(MeshUsage.Static);
        MeshJson meshJson = gson.fromJson(reader, MeshJson.class);

        // read primitive
        Primitive primitive = null;
        switch (meshJson.primitive) {
            case "triangles":
                primitive = Primitive.Triangles;
                break;
            case "triangle_strip":
                primitive = Primitive.TriangleStrp;
                break;
        }

        if (primitive == null) {
            reader.close();
            throw new IllegalArgumentException("Invalid primitive type \""+meshJson.primitive+"\"");
        } else {
            mesh.setPrimitive(primitive);
        }

        // read attributes
        for (int i = 0; i < meshJson.attributes.length; ++i) {
            AttributeJson attr = meshJson.attributes[i];
            Attribute attribute = null;
            Buffer data = null;
            switch (attr.name) {
                case "position":
                    attribute = Attribute.Position;
                    data = ByteBuffer.allocateDirect(attr.data.length*3<<2)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer()
                            .put(attr.data)
                            .flip();
                    break;
                case "normal":
                    attribute = Attribute.Normal;
                    data = ByteBuffer.allocateDirect(attr.data.length*3<<2)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer()
                            .put(attr.data)
                            .flip();
                    break;
                case "uv":
                    attribute = Attribute.Uv;
                    data = ByteBuffer.allocateDirect(attr.data.length*2<<2)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer()
                            .put(attr.data)
                            .flip();
                    break;
            }

            if (attribute == null) {
                reader.close();
                throw new IllegalArgumentException("Invalid vertex attribute \""+attr.name+"\"");
            } else {
                mesh.setData(attribute, data);
            }
        }

        // read indices
        Buffer indices = null;
        switch (meshJson.indices.type) {
            case "uint":    // unsigned int
                indices = ByteBuffer.allocateDirect(meshJson.indices.data.length<<2)
                        .order(ByteOrder.nativeOrder())
                        .asIntBuffer()
                        .put(meshJson.indices.data)
                        .flip();
                break;
        }

        if (indices == null) {
            reader.close();
            throw new IllegalArgumentException("Invalid indices type \""+meshJson.indices.type+"\"");
        } else {
            mesh.setIndices(indices);
            mesh.setLength(meshJson.indices.data.length);
            mesh.setOffset(0);
        }

        // close file and return mesh
        reader.close();
        return mesh;
    }

    /**
     * Load a texture from a file
     * @param path file path
     * @return texture
     * @throws Exception error
     */
    public static Texture loadTexture (String path) throws Exception {
        // open image file
        FileInputStream is = new FileInputStream(path);
        BufferedImage img = ImageIO.read(is);

        Texture texture = new Texture(img.getWidth(), img.getHeight(), TextureFormat.Rgb);
        ByteBuffer data = ByteBuffer.allocateDirect(img.getWidth()*img.getHeight()*3).order(ByteOrder.nativeOrder());

        // read pixel data
        for (int x = 0; x < img.getWidth(); ++x)
            for (int y = 0; y < img.getHeight(); ++y) {
                int argb = img.getRGB(y, x);
                data.put(new byte[] {
                        (byte) ((argb >> 16) & 0xff),
                        (byte) ((argb >> 8) & 0xff),
                        (byte) (argb & 0xff)
                });
            }

        // set texture data
        data.flip();
        texture.setData(data);

        // close file and return texture
        is.close();
        return texture;
    }
}
