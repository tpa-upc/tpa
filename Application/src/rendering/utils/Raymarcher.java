package rendering.utils;

import tpa.joml.Vector3f;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by germangb on 30/04/2016.
 */
public class Raymarcher implements RayPicker {

    List<Vector3f> poss = new ArrayList<>();
    List<Vector3f> scas = new ArrayList<>();
    List<Object> ids = new ArrayList<>();

    @Override
    public void addBox(Vector3f position, Vector3f scale, Object id) {
        poss.add(position);
        scas.add(scale);
        ids.add(id);
    }

    @Override
    public void clear() {
        poss.clear();
        scas.clear();
        ids.clear();
    }

    private class IdWrap {
        Object id = null;
    }

    @Override
    public Object query(Vector3f ro, Vector3f rd) {
        float h = 0;
        Vector3f p = new Vector3f();
        Object ret = null;
        for (int i = 0; i < 16; ++i) {
            p.set(rd).mul(h).add(ro);
            IdWrap wr = new IdWrap();
            float dist = distance(p, wr);
            //System.out.println("[RAYM] it="+i+" h="+h+" dist="+dist+" p="+p.toString(NumberFormat.getInstance()));
            if (dist < 0.01f) {
                return wr.id;
            }
            h += Math.max(dist, 0.01f);
        }
        return null;
    }

    /*
     * @param p 3d point
     * @return distance to the objects
     */
    private float distance (Vector3f p, IdWrap out) {
        float min = 9999999;
        Object obj = null;
        for (int i = 0; i < poss.size(); ++i) {
            float d = boxDistance(i, p);
            if (d < min) {
                min = d;
                out.id = ids.get(i);
            }
        }
        return min;
    }

    /**
     * ref: http://iquilezles.org/www/articles/distfunctions/distfunctions.htm
     *
     * float sdBox( vec3 p, vec3 b )
     * {
     *  vec3 d = abs(p) - b;
     *  return min(max(d.x,max(d.y,d.z)),0.0) + length(max(d,0.0));
     * }
     */
    private float boxDistance (int i, Vector3f p) {
        Vector3f pos = poss.get(i);
        Vector3f b = scas.get(i);
        Vector3f d = new Vector3f(Math.abs(p.x-pos.x), Math.abs(p.y-pos.y), Math.abs(p.z-pos.z)).sub(b);
        return Math.min(0, Math.max(d.x, Math.max(d.y, d.z))) + new Vector3f(d).max(new Vector3f(0)).length();
    }
}
