package tpa.graphics.render;

import tpa.graphics.geometry.Mesh;
import tpa.graphics.shader.ShaderProgram;
import tpa.graphics.texture.Framebuffer;
import tpa.graphics.texture.Texture;

/**
 * Renderer is a low level rendering wrapper that handles the rendering commands without you dealing
 * with annoying binding dependencies such as LWJGL. The class is a working progress and is very likely
 * that it'll change constantly.
 *
 * Created by german on 26/03/2016.
 */
public interface Renderer {

    /**
     * Interface used to probe the renderer
     */
    interface RendererListener {

        /**
         * Called when the rendering begins
         * @param renderer renderer
         */
        void onRenderBegin(Renderer renderer);

        /**
         * Called when the renderer ends
         * @param renderer renderer
         */
        void onRenderEnd(Renderer renderer);
    }

    /**
     * Set listener so that things can be logged, for example
     * @param listener new listener or null
     */
    void setListener (RendererListener listener);

    /**
     * Get current listener or null if none is set
     * @return current listener or null if none is set
     */
    RendererListener getListener ();

    /**
     * Get render statistics
     * @return render statistics
     */
    RenderStats getStatistics ();

    /** Begin rendering frame */
    void beginFrame();

    /**
     * Clear framebuffer color
     * @param r
     * @param g
     * @param b
     * @param a
     */
    void setClearColor(float r, float g, float b, float a);

    /**
     * Clear framebuffer buffers
     */
    void clearBuffers ();

    /**
     * clear color buffer bit
     * */
    void clearColorBuffer ();

    /**
     * clear depth buffer bit
     * */
    void clearDepthBuffer ();

    /**
     * Set the renderer state
     * @param state new state to be applied
     */
    void setState (RendererState state);

    /**
     * Set setViewport
     * @param x position x
     * @param y position y
     * @param with setViewport width
     * @param height setViewport height
     */
    void setViewport(int x, int y, int with, int height);

    /**
     * Bind a texture to a specific unit. If the texture is dirty, upload it
     * to the GPU
     * @param unit texture unit
     * @param texture texture to be bound. set to null to disable texture
     */
    void setTexture (int unit, Texture texture);

    /**
     * Set shader program to be used. If the program is not on the GPU, it will be
     * uploaded. Uniforms are not uploaded immediately.
     * @param program shader program to be used
     */
    void setShaderProgram (ShaderProgram program);

    /**
     * Set framebuffer
     * @param fbo framebuffer, null to go to default framebuffer
     */
    void setFramebuffer (Framebuffer fbo);

    /**
     * Render a mesh using the current state of the renderer. Data will be uploaded to the GPU if needed
     * Uniforms from shader will be set before rendering the mesh
     * @param mesh mesh to be rendered.
     */
    void renderMesh (Mesh mesh);

    /**
     * End frame rendering
     * */
    void endFrame ();
}
