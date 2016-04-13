package rendering;

import tpa.graphics.geometry.Mesh;
import tpa.graphics.render.Renderer;
import tpa.graphics.shader.ShaderProgram;

/**
 * Created by germangb on 13/04/16.
 */
public abstract class Material {

    /** Shader program asociated with this material */
    protected ShaderProgram program;

    /**
     * Create a material asociated with a shader program
     * @param program shader program
     */
    public Material (ShaderProgram program) {
        this.program = program;
    }

    /**
     * Use material to render specific mesh
     * @param renderer renderer implementation
     * @param camera projection & view transformation
     * @param actor mesh to be rendered
     */
    public abstract void render (Renderer renderer, Camera camera, GeometryActor actor);
}
