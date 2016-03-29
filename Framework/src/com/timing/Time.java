package com.timing;

/**
 * Created by german on 27/03/2016.
 */
public interface Time {

    /**
     * Get time in seconds since the beginning of the application
     * @return time in seconds
     */
    float getTime();

    /**
     * Get time between consecutive frames
     * @return time in seconds
     */
    float getFrameTime ();

    /**
     * Get frames per second rate
     * @return frames per second rate
     */
    int getFramesPerSecond ();

    /**
     * Get number of elapsed frames
     * @return number of elapsed frames
     */
    int elapsedFrames ();
}
