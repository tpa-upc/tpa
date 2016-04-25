package resources;

import com.google.gson.Gson;
import tpa.audio.Sound;
import tpa.graphics.geometry.Attribute;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.geometry.MeshUsage;
import tpa.graphics.geometry.Primitive;
import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFormat;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

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

        int cmp = 3;
        if (img.getTransparency() == Transparency.TRANSLUCENT) cmp = 4;
        Texture texture = new Texture(img.getWidth(), img.getHeight(), cmp==3?TextureFormat.Rgb:TextureFormat.Rgba);
        ByteBuffer data = ByteBuffer.allocateDirect(img.getWidth()*img.getHeight()*cmp).order(ByteOrder.nativeOrder());

        // read pixel data
        for (int x = 0; x < img.getWidth(); ++x)
            for (int y = 0; y < img.getHeight(); ++y) {
                int argb = img.getRGB(x, y);
                data.put(new byte[] {
                        (byte) ((argb >> 16) & 0xff),
                        (byte) ((argb >> 8) & 0xff),
                        (byte) (argb & 0xff)
                });
                if (cmp == 4) {
                    data.put((byte)((argb >> 24)&0xff));
                }
            }

        // set texture data
        data.flip();
        texture.setData(data);

        // close file and return texture
        is.close();
        return texture;
    }

    public static Sound loadSound (String path) throws Exception {
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(path));
        AudioInputStream audio = AudioSystem.getAudioInputStream(is);

        ByteBuffer samples = ByteBuffer.allocateDirect(audio.available())
                .order(ByteOrder.nativeOrder());

        byte[] bytes = new byte[32<<1]; // 32 samples mono
        while (true) {
            int read = audio.read(bytes);
            if (read <= 0) break;
            samples.put(bytes, 0, read);
        }

        samples.flip();

        Sound sound = new Sound();
        sound.setSamplingRate((int)audio.getFormat().getSampleRate());
        sound.setStereo(audio.getFormat().getChannels()==2);
        sound.setSamples(samples.asShortBuffer());

        is.close();
        return sound;
    }
}
