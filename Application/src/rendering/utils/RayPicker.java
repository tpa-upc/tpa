package rendering.utils;

import tpa.joml.Vector3f;

/**
 * Created by germangb on 30/04/2016.
 */
public interface RayPicker {

    /**
     * Add a box to the ray picker
     * @param position box position
     * @param scale box scale (1,1,1 will give a unit 2x2x2 cube)
     * @param id id of the box so that it can be identified later
     */
    void addBox (Vector3f position, Vector3f scale, Object id);

    /**
     * Query the scene with a ray
     * @param ro ray origin
     * @param rd ray direction
     * @return intersected object. Null if none is
     */
    Object query (Vector3f ro, Vector3f rd);
}
