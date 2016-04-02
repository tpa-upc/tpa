package test;

/**
 * Created by germangb on 01/04/16.
 */
public class StaticTextures {

    public static byte[] DEBUG = new byte[3*64*64];

    static {
        byte[] pal = {(byte)0xbb, (byte)0x88};
        for (int i = 0; i < 64; i++) {
            for (int x = 0; x < 64; ++x) {
                int ind = (64*i+x)*3;
                int gridX = i/16%2;
                int gridY = x/16%2;
                byte c = pal[(gridX+gridY)%2];
                if (i < 2 || i >= 62 || x < 2 || x >= 62) c = (byte)0x55;
                DEBUG[ind] = DEBUG[ind+1] = DEBUG[ind+2] = c;
            }
        }
    }

    private StaticTextures () {
    }
}
