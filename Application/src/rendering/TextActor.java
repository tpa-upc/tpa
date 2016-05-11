package rendering;

/**
 * Created by germangb on 11/05/2016.
 */
public class TextActor extends Actor {

    /** 3d text */
    private String text;

    public TextActor (String text) {
        this.text = text;
    }

    public String getText () {
        return text;
    }

    public void billboard (Camera camera) {
        rotation.set(camera.rotation).invert();
        update();
    }
}
