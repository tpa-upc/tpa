package resources;

import tpa.graphics.texture.Texture;
import tpa.graphics.texture.TextureFormat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by germangb on 10/04/16.
 */
public class ResourceUtils {

    /**
     * Load a texture from a file
     * @param path file path
     * @return texture
     * @throws IOException I/O error
     */
    public static Texture loadTexture (String path) throws IOException {
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
