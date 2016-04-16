package tpa.graphics.render;

import tpa.graphics.geometry.MeshUsage;
import tpa.graphics.geometry.Primitive;
import tpa.graphics.texture.TextureFilter;
import tpa.graphics.texture.TextureFormat;
import tpa.graphics.texture.TextureWrap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

/**
 * Created by german on 26/03/2016.
 */
public class LwjglUtils {

    public static int TextureFormat2int (TextureFormat format) {
        switch (format) {
            case Red:
                return GL11.GL_RED;
            case Rgb:
                return GL11.GL_RGB;
            case Rgba:
                return GL11.GL_RGBA;
            case Depth:
                return GL11.GL_DEPTH_COMPONENT;
            default:
                throw new RuntimeException();
        }
    }

    public static int filter2int(TextureFilter filter) {
        switch (filter) {
            case Linear:
                return GL11.GL_LINEAR;
            case Nearest:
                return GL11.GL_NEAREST;
            case MipmapLinear:
                return GL11.GL_LINEAR_MIPMAP_LINEAR;
            case MipmapNearest:
                return GL11.GL_LINEAR_MIPMAP_NEAREST;
            default:
                throw new RuntimeException();
        }
    }

    public static int wrap2int(TextureWrap wrap) {
        switch (wrap) {
            case Clamp:
                return GL11.GL_CLAMP;
            case Repeat:
                return GL11.GL_REPEAT;
            default:
                throw new RuntimeException();
        }
    }

    public static int getShader (int type, String source) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);
        String log = GL20.glGetShaderInfoLog(shader);

        int status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (status == GL11.GL_FALSE)
            throw new RuntimeException(log);

        return shader;
    }

    public static int primitive2int (Primitive p) {
        switch (p) {
            case Triangles:
                return GL11.GL_TRIANGLES;
            case TriangleStrp:
                return GL11.GL_TRIANGLE_STRIP;
            default:
                throw new RuntimeException();
        }
    }

    public static int usage2int (MeshUsage u) {
        switch (u) {
            case Static:
                return GL15.GL_STATIC_DRAW;
            case Dynamic:
                return GL15.GL_DYNAMIC_DRAW;
            case Stream:
                return GL15.GL_STREAM_DRAW;
            default:
                throw new RuntimeException();
        }
    }
}
