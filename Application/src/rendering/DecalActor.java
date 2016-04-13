package rendering;

/**
 * Created by german on 13/04/2016.
 */
public class DecalActor extends Actor {

    /** Decal material */
    private DecalMaterial material;

    public DecalActor (DecalMaterial material) {
        this.material = material;
    }

    public DecalMaterial getMaterial() {
        return material;
    }

    public void setMaterial(DecalMaterial material) {
        this.material = material;
    }
}
