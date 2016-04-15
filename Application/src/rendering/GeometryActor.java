package rendering;

import rendering.materials.Material;
import tpa.graphics.geometry.Mesh;

/**
 * Created by germangb on 13/04/16.
 */
public class GeometryActor extends Actor {

    /** Actor geometry */
    private Mesh mesh;

    /** Geometry materuak */
    private Material material;

    public GeometryActor (Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    /**
     * Get mesh from actor
     * @return mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Set geometry mesh
     * @param mesh mesh
     */
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    /**
     * Get geometry material
     * @return material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set geometry material
     * @param material material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}
