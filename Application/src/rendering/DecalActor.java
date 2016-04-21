package rendering;

import rendering.materials.DecalMaterial;
import rendering.materials.Material;

/**
 * Created by german on 13/04/2016.
 */
public class DecalActor extends Actor {

    /** Decal material */
    private Material material;

    public DecalActor (Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(DecalMaterial material) {
        this.material = material;
    }
}
