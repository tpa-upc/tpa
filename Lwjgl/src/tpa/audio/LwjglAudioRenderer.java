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
    ByteBuffer musicBuffer = ByteBuffer.allocateDirect(44100<<2).order(ByteOrder.nativeOrder());
    Music music[] = new Music[2];
    AudioInputStream[] musicIs = new AudioInputStream[2];
    int[] musicSource = new int[2];
    int[] musicFormat = new int[2];
    int[] musicSampling = new int[2];
    boolean[] musicLoop = new boolean[2];
    float[] musicGain = {1, 1};

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
        for (int i = 0; i < 2; ++i) {
            if (music[i] != null) {
                int processed = alGetSourcei(musicSource[i], AL_BUFFERS_PROCESSED);
                int queued = alGetSourcei(musicSource[i], AL_BUFFERS_QUEUED);
                //System.out.println("[MUSIC] ch="+i+" processed="+processed+" queued="+queued);

                for (int j = 0; j < processed; ++j) {
                    int buffer = alSourceUnqueueBuffers(musicSource[i]);

                    // refill buffer
                    try {
                        if (musicIs[i].available() > 0) {
                            getSamples(buffer, i);
                            alSourceQueueBuffers(musicSource[i], buffer);
                        } else {
                            if (musicLoop[i]) {
                                playMusic(music[i], true, i, 1);
                            } else {
                                alDeleteBuffers(buffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    private void playMusic (Music music, boolean loop, int channel, int numBuffer) {
        //TODO Error handling
        if (musicIs[channel] != null) {
            alSourceStop(musicSource[channel]);

            int processed = alGetSourcei(musicSource[channel], AL_BUFFERS_PROCESSED);
            int queued = alGetSourcei(musicSource[channel], AL_BUFFERS_QUEUED);

            for (int i = 0; i < processed+queued; ++i) {
                int buf = alSourceUnqueueBuffers(musicSource[channel]);
                alDeleteBuffers(buf);
            }

            try {
                musicIs[channel].close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            alDeleteSources(musicSource[channel]);
        }

        try {
            musicLoop[channel] = loop;
            musicIs[channel] = AudioSystem.getAudioInputStream(new File(music.getFile()));
            AudioFormat format = musicIs[channel].getFormat();
            musicSampling[channel] = (int) format.getSampleRate();
            //System.out.println("[MUSIC] SR="+musicSampling);
            //System.out.println("[MUSIC] DEPTH="+format.getSampleSizeInBits());
            //System.out.println("[MUSIC] CHANNELS="+format.getChannels());

            boolean error = false;
            if (format.getChannels() == 1) {
                if (format.getSampleSizeInBits() == 8) {
                    musicFormat[channel] = AL_FORMAT_MONO8;
                } else if (format.getSampleSizeInBits() == 16) {
                    musicFormat[channel] = AL_FORMAT_MONO16;
                } else {
                    System.err.println("Unsupported bit depth");
                }
            } else if (format.getChannels() == 2) {
                if (format.getSampleSizeInBits() == 8) {
                    musicFormat[channel] = AL_FORMAT_STEREO8;
                } else if (format.getSampleSizeInBits() == 16) {
                    musicFormat[channel] = AL_FORMAT_STEREO16;
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
                musicSource[channel] = alGenSources();

                // assign a few buffers
                for (int i = 0; i < 4; ++i) {
                    if (musicIs[channel].available() > 0) {
                        int buffer = alGenBuffers();
                        getSamples(buffer, channel);
                        alSourceQueueBuffers(musicSource[channel], buffer);
                    } else break;
                }

                System.out.println("asdasdasd");
                // play music
                if (alGetSourcei(musicSource[channel], AL_SOURCE_STATE) != AL_PLAYING) {
                    System.out.println("asdasdasdasdas34534535");
                    alSourcef(musicSource[channel], AL_GAIN, musicGain[channel]);
                    alSourcePlay(musicSource[channel]);
                }

                this.music[channel] = music;
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            this.music = null;
        } catch (IOException e) {
            e.printStackTrace();
            this.music = null;
        }
    }

    @Override
    public void playMusic(Music music, boolean loop, int channel) {
        playMusic(music, loop, channel, 4);
    }

    @Override
    public void setMusicGain(int channel, float gain) {
        musicGain[channel] = gain;
        alSourcef(musicSource[channel], AL_GAIN, gain);
    }

    /**
     * Read samples from music input stream and put them in a buffer
     * @param buffer
     */
    private void getSamples (int buffer, int channel) throws IOException {
        musicBuffer.clear();
        byte[] readBuffer = new byte[128];

        // read a chunk of the audio samples
        while (musicBuffer.remaining() > 0) {
            // read a bit of the file
            int toRead = Math.min(musicBuffer.remaining(), readBuffer.length);
            int read = musicIs[channel].read(readBuffer, 0, toRead);
            if (read <= 0)
                break;

            // slowly fill buffer
            musicBuffer.put(readBuffer, 0, read);
        }

        // upload samples to the audio buffer
        musicBuffer.flip();
        alBufferData(buffer, musicFormat[channel], musicBuffer, musicSampling[channel]);
    }

    @Override
    public void stopMusic(Music music) {

    }

    @Override
    public void stopEverything() {
        for (int i = 0; i < music.length; ++i)
            stopMusic(music[i]);
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
