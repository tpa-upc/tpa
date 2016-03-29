package com.audio;

import com.utils.Destroyable;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL10.alGetString;

/**
 * Very WIP
 *
 * Created by german on 27/03/2016.
 */
public class LwjglAudioRenderer implements AudioRenderer, Destroyable {

    Map<Sound, Integer> buffers = new HashMap<>();
    Map<Sound, Integer> sources = new HashMap<>();

    public LwjglAudioRenderer () {
        System.err.println("AL_VENDOR: " + alGetString(AL_VENDOR));
        System.err.println("AL_RENDERER: " + alGetString(AL_RENDERER));
        System.err.println("AL_VERSION: " + alGetString(AL_VERSION));
    }

    @Override
    public void playSound(Sound sound) {
        Integer source = sources.get(sound);
        if (source == null || sound.isDirty()) {
            int buffer = source == null ? alGenBuffers() : buffers.get(sound);
            Buffer samples = sound.getSamples();
            int format = -1;
            if (sound.isStereo()) {
                if (samples instanceof ByteBuffer)
                    format = AL_FORMAT_STEREO8;
                else if (samples instanceof ShortBuffer)
                    format = AL_FORMAT_STEREO16;
                else {
                    // handle error? fallback to something?
                }
            } else {
                if (samples instanceof ByteBuffer)
                    format = AL_FORMAT_MONO8;
                else if (samples instanceof ShortBuffer)
                    format = AL_FORMAT_MONO16;
            }

            if (samples instanceof ByteBuffer)
                alBufferData(buffer, format, (ByteBuffer) samples, sound.getSamplingRate());
            else if (samples instanceof ShortBuffer)
                alBufferData(buffer, format, (ShortBuffer) samples, sound.getSamplingRate());

            sound.setDirty(false);

            if (source == null) {
                source = alGenSources();
                alSourcei(source, AL_BUFFER, buffer);
                sources.put(sound, source);
                buffers.put(sound, buffer);
            }
        }

        alSourcePlay(source);
    }

    @Override
    public void destroy() {
        sources.forEach((s, source) -> alDeleteSources(source));
        buffers.forEach((s, buffer) -> alDeleteBuffers(buffer));
    }
}
