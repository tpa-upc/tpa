package resources;

import activity.Dialog;
import tpa.audio.Music;
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
    Map<String, Boolean> lol = new HashMap<>();

    public SimpleResourceManager () {
        last = System.currentTimeMillis();
    }

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
        if (lol.containsKey(file)) return;
        lol.put(file, true);

        Pair p = new Pair();
        p.path = file;
        p.type = type;
        queued.add(p);

        /*if (Math.random() < 0.1f) {
            String fakeFile = System.currentTimeMillis()+".lol";
            lol.put(fakeFile, true);
            p = new Pair();
            p.path = fakeFile;
            p.type = type;
            queued.add(p);
        }*/
    }

    @Override
    public <T> T get(String file, Class<T> type) {
        Object obj = loaded.get(file);
        if (obj == null)
            return null;

        return (T) obj;
    }

    private void process () {
        // load one resource
        Pair p = queued.poll();
        if (p.type == Texture.class) {
            try {
                Texture tex = ResourceUtils.loadTexture(p.path);
                loaded.put(p.path, tex);
                if (listener != null)
                    listener.onLoaded(p.path, p.type);
            } catch (Exception e) {
                lol.remove(p.path);
                if (listener != null)
                    listener.onFailed(p.path, p.type, e);
            }
        } else if (p.type == Mesh.class) {
            try {
                Mesh mesh = ResourceUtils.loadMesh(p.path);
                loaded.put(p.path, mesh);
                if (listener != null)
                    listener.onLoaded(p.path, p.type);
            } catch (Exception e) {
                lol.remove(p.path);
                if (listener != null)
                    listener.onFailed(p.path, p.type, e);
            }
        } else if (p.type == Sound.class) {
            try {
                Sound sound = ResourceUtils.loadSound(p.path);
                loaded.put(p.path, sound);
                if (listener != null)
                    listener.onLoaded(p.path, p.type);
            } catch (Exception e) {
                lol.remove(p.path);
                if (listener != null)
                    listener.onFailed(p.path, p.type, e);
            }
        } else if (p.type == Dialog.class) {
            try {
                Dialog dialog = ResourceUtils.loadDialog(p.path);
                loaded.put(p.path, dialog);
                if (listener != null)
                    listener.onLoaded(p.path, p.type);
            } catch (Exception e) {
                lol.remove(p.path);
                if (listener != null)
                    listener.onFailed(p.path, p.type, e);
            }
        } else if (p.type == Music.class) {
            try {
                Music mus = ResourceUtils.loadMusic(p.path);
                loaded.put(p.path, mus);
                if (listener != null)
                    listener.onLoaded(p.path, p.type);
            } catch (Exception e) {
                lol.remove(p.path);
                if (listener != null)
                    listener.onFailed(p.path, p.type, e);
            }
        } else {
            if (listener != null)
                listener.onFailed(p.path, p.type, new IllegalArgumentException("Invalid resource type"));
        }
    }

    private long last = 0;

    @Override
    public void update() {
        //System.out.println("queue: "+ queued.size());
        if (queued.isEmpty() || System.currentTimeMillis() - last < (long)(500 * Math.random()))
            return;

        last = System.currentTimeMillis();
        //System.out.println("begin loading");
        process();
        //System.out.println("finish loading");
    }

    @Override
    public boolean isFinishedLoading() {
        return queued.isEmpty();
    }

    @Override
    public float getProgress() {
        int total = lol.size();
        return (float)loaded.size()/total;
    }

    @Override
    public void finishLoading() {
        while (!isFinishedLoading())
            process();
    }
}
