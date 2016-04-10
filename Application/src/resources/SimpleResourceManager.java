package resources;

import tpa.audio.Sound;
import tpa.graphics.geometry.Mesh;
import tpa.graphics.texture.Texture;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by germangb on 10/04/16.
 */
public class SimpleResourceManager implements ResourceManager {

    class Pair {
        String path;
        Class<?> type;
    }

    ResourceManagerListener listener = null;
    Queue<Pair> queued = new LinkedList<>();
    Map<String, Object> loaded = new HashMap<>();

    @Override
    public void setListener(ResourceManagerListener listener) {
        this.listener = listener;
    }

    @Override
    public ResourceManagerListener getListener() {
        return listener;
    }

    @Override
    public void load(String file, Class<?> type) {
        Pair p = new Pair();
        p.path = file;
        p.type = type;
        queued.add(p);
    }

    @Override
    public <T> T get(String file, Class<T> type) {
        return (T) loaded.get(file);
    }

    @Override
    public void update() {
        // load one resource
        Pair p = queued.poll();
        if (p.type == Texture.class) {
            try {
                Texture tex = ResourceUtils.loadTexture(p.path);
                loaded.put(p.path, tex);
            } catch (Exception e) {
                if (listener != null)
                    listener.onFailed(p.path, p.type, e);
            }
        } else if (p.type == Mesh.class) {
            throw new UnsupportedOperationException();
        } else if (p.type == Sound.class) {
            throw new UnsupportedOperationException();
        } else {
            if (listener != null)
                listener.onFailed(p.path, p.type, new IllegalArgumentException("Invalid resource type"));
        }
    }

    @Override
    public boolean isFinishedLoading() {
        return queued.isEmpty();
    }

    @Override
    public void finishLoading() {
        while (!isFinishedLoading())
            update();
    }
}
