package com.audio;

import com.graphics.geometry.Attribute;
import com.sun.istack.internal.NotNull;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.function.Function;

/**
 * Created by german on 28/03/2016.
 */
public class Sound {

    private static ShortBuffer sine = null;
    static {
        int sr = 16000;
        int dir = 2;
        sine = ByteBuffer.allocateDirect(sr*dir<<1)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();

        int freq = 540/2;
        float edge0 = sr*dir;
        float edge1 = sr*dir/2;

        for (int i = 0; i < sr*dir; ++i) {
            float t = (float)(i - edge0) / (edge1 - edge0);
            if (t<0)t=0;else if (t>1)t=1;
            float gain =  t * t * (3.0f - 2.0f * t);
            float mod = (float) Math.sin(2*Math.PI*freq * 1/sr*i);
            float sample = (float) Math.sin(2*Math.PI*freq/sr*i + mod*8) * 0.3f;
            sample += (float) Math.sin(2*Math.PI*freq*0.5f/sr*i + mod*4) * 0.7f * (mod*0.5+0.5);
            if (sample > 1) sample = 1;
            else if (sample < -1) sample = -1;
            sine.put((short)(Short.MAX_VALUE*sample*gain));
        }
        sine.flip();
    }

    /** sample data */
    private Buffer samples = null;

    /** sampling frequency */
    private int samplingRate = 16000;

    /** stereo sound flag */
    private boolean stereo = false;

    /** state */
    private boolean dirty = true;

    public Sound () {
        samples = sine;
    }

    /**
     * Set to true to tell renderer this sound is stereo
     * @param b true if sound is stereo
     */
    public void setStereo (boolean b) {
        stereo = b;
        setDirty(true);
    }

    /**
     * Wether the sound is stereo or not
     * @return true if sound is stereo, false is it is mono
     */
    public boolean isStereo () {
        return stereo;
    }

    /**
     * Set samples
     * @param samples
     */
    public void setSamples (@NotNull Buffer samples) {
        if (samples == null)
            throw new IllegalArgumentException();
        this.samples = samples;
        setDirty(true);
    }

    /**
     * Get typed samples
     * @param type sample type
     * @param <T> type declaration
     * @return samples
     */
    public <T extends Buffer> T getSamples (Class<T> type) {
        return (T) samples;
    }


    /**
     * Get buffer containing samples
     * @return sound samples
     */
    public Buffer getSamples () {
        return samples;
    }

    /**
     * Get state
     * @return
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Set state to signal sound renderer
     * @param dirty new state
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * Get sampling rate in Hz
     * @return sampling rate
     */
    public int getSamplingRate() {
        return samplingRate;
    }

    /**
     * Set sampling rate
     * @param samplingRate Hz
     */
    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
        setDirty(true);
    }
}
