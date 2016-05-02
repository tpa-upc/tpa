package tpa.audio;

/**
 * Created by german on 27/03/2016.
 */
public interface AudioRenderer {

    /**
     * Play a sound
     * @param sound sound to be played
     */
    void playSound (Sound sound, boolean loop);

    /**
     * Stop a sound being played
     * @param sound sound to be stopped
     */
    void stopSound (Sound sound);
}
