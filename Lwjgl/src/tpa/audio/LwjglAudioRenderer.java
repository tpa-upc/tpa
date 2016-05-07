package tpa.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import tpa.utils.Destroyable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.*;
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

    /** Music playing */
    Music music = null;
    AudioInputStream musicIs = null;
    ByteBuffer musicBuffer = ByteBuffer.allocateDirect(44100<<2).order(ByteOrder.nativeOrder());
    int musicSource;
    int musicFormat;
    int musicSampling;

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

    public void update () {
        if (music != null) {
            int processed = alGetSourcei(musicSource, AL_BUFFERS_PROCESSED);
            int queued = alGetSourcei(musicSource, AL_BUFFERS_QUEUED);
            //System.out.println("[MUSIC] processed="+processed+" queued="+queued);

            for (int i = 0; i < processed; ++i) {
                int buffer = alSourceUnqueueBuffers(musicSource);

                // refill buffer
                try {
                    if (musicIs.available() > 0) {
                        getSamples(buffer);
                        alSourceQueueBuffers(musicSource, buffer);
                    } else {
                        alDeleteBuffers(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
    public void playMusic(Music music) {
        //TODO Error handling

        if (musicIs != null) {

            int processed = alGetSourcei(musicSource, AL_BUFFERS_PROCESSED);
            int queued = alGetSourcei(musicSource, AL_BUFFERS_QUEUED);

            for (int i = 0; i < processed+queued; ++i) {
                int buf = alSourceUnqueueBuffers(musicSource);
                alDeleteBuffers(buf);
            }

            try {
                musicIs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            musicIs = AudioSystem.getAudioInputStream(new File(music.getFile()));
            AudioFormat format = musicIs.getFormat();
            musicSampling = (int) format.getSampleRate();
            //System.out.println("[MUSIC] SR="+musicSampling);
            //System.out.println("[MUSIC] DEPTH="+format.getSampleSizeInBits());
            //System.out.println("[MUSIC] CHANNELS="+format.getChannels());

            boolean error = false;
            if (format.getChannels() == 1) {
                if (format.getSampleSizeInBits() == 8) {
                    musicFormat = AL_FORMAT_MONO8;
                } else if (format.getSampleSizeInBits() == 16) {
                    musicFormat = AL_FORMAT_MONO16;
                } else {
                    System.err.println("Unsupported bit depth");
                }
            } else if (format.getChannels() == 2) {
                if (format.getSampleSizeInBits() == 8) {
                    musicFormat = AL_FORMAT_STEREO8;
                } else if (format.getSampleSizeInBits() == 16) {
                    musicFormat = AL_FORMAT_STEREO16;
                } else {
                    System.err.println("Unsupported bit depth");
                }
            } else {
                // TODO ERROR or something
                error = true;
                System.err.println("Unsupported number of channels.");
            }

            if (!error) {
                // generate a source
                musicSource = alGenSources();

                // assign a few buffers
                for (int i = 0; i < 4; ++i) {
                    if (musicIs.available() > 0) {
                        int buffer = alGenBuffers();
                        getSamples(buffer);
                        alSourceQueueBuffers(musicSource, buffer);
                    } else break;
                }

                // play music
                alSourcePlay(musicSource);

                this.music = music;
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            this.music = null;
        } catch (IOException e) {
            e.printStackTrace();
            this.music = null;
        }
    }

    /**
     * Read samples from music input stream and put them in a buffer
     * @param buffer
     */
    private void getSamples (int buffer) throws IOException {
        musicBuffer.clear();
        byte[] readBuffer = new byte[128];

        // read a chunk of the audio samples
        while (musicBuffer.remaining() > 0) {
            // read a bit of the file
            int toRead = Math.min(musicBuffer.remaining(), readBuffer.length);
            int read = musicIs.read(readBuffer, 0, toRead);
            if (read <= 0)
                break;

            // slowly fill buffer
            musicBuffer.put(readBuffer, 0, read);
        }

        // upload samples to the audio buffer
        musicBuffer.flip();
        alBufferData(buffer, musicFormat, musicBuffer, musicSampling);
    }

    @Override
    public void stopMusic(Music music) {

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
