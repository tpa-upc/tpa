package tpa.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import tpa.utils.Destroyable;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL10.alGetString;

/**
 * TODO music
 *
 * Created by german on 27/03/2016.
 */
public class LwjglAudioRenderer implements AudioRenderer, Destroyable {

    /** openAL sound buffers */
    Map<Sound, Integer> buffers = new HashMap<>();

    /** OpenAL sound sources */
    Map<Sound, Integer> sources = new HashMap<>();

    /** Audio device pointer */
    long device;

    /** OpenAL context pointer */
    long context;

    public LwjglAudioRenderer () {
        // create OpenAL context
        device = ALC10.alcOpenDevice((String) null);
        context = ALC10.alcCreateContext(device, null);
        ALCCapabilities alcCaps = ALC.createCapabilities(device);
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(alcCaps);

        // output info
        System.err.println("AL_VENDOR: " + alGetString(AL_VENDOR));
        System.err.println("AL_RENDERER: " + alGetString(AL_RENDERER));
        System.err.println("AL_VERSION: " + alGetString(AL_VERSION));
    }

    @Override
    public void playSound(Sound sound, boolean loop) {
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
        alSourcei(source, AL_LOOPING, loop?AL_TRUE:AL_FALSE);
    }

    @Override
    public void stopSound(Sound sound) {
        Integer source = sources.get(sound);
        if (source != null) {
            alSourceStop(source);
        }
    }

    @Override
    public void destroy() {
        // destroy allocated sources & buffers
        sources.forEach((s, source) -> alDeleteSources(source));
        buffers.forEach((s, buffer) -> alDeleteBuffers(buffer));

        // destroy openAL context
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }
}
