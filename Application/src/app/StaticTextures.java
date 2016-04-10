package app;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by germangb on 01/04/16.
 */
public class StaticTextures {

    public static byte[] DEBUG = new byte[3*128*128];

    public static byte[] TEXTURE = null;

    public static byte[] TEXTURE2 = null;

    static {
        TEXTURE = texture("res/texture.png");
        TEXTURE2 = texture("res/texture2.png");

        byte[] pal = {(byte)0xbb, (byte)0x88};
        for (int i = 0; i < 128; i++) {
            for (int x = 0; x < 128; ++x) {
                int ind = (64*i+x)*3;
                int gridX = i/16%2;
                int gridY = x/16%2;
                byte c = pal[(gridX+gridY)%2];
                if (i < 2 || i >= 62 || x < 2 || x >= 62) c = (byte)0x55;
                DEBUG[ind] = DEBUG[ind+1] = DEBUG[ind+2] = c;
            }
        }
    }

    private static byte[] texture (String res) {
        byte[] data = null;
        try {
            int j = 0;
            BufferedImage img = ImageIO.read(new File(res));
            data = new byte[img.getWidth()*img.getHeight()*3];
            for (int i = 0; i < img.getWidth(); ++i) {
                for (int x = 0; x < img.getWidth(); ++x) {
                    int argb = img.getRGB(i, x);
                    data[j++] = (byte) ((argb & 0xff0000) >> 16);
                    data[j++] = (byte) ((argb & 0x00ff00) >> 8);
                    data[j++] = (byte) (argb & 0x0000ff);
                    //data[j++] = (byte) 0x00;
                    //data[j++] = (byte) 0xff;
                    //data[j++] = (byte) 0xff;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            return data;
        }
    }

    private StaticTextures () {
    }
}
