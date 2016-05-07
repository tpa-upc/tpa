package tpa.audio;

/**
 * Created by germangb on 07/05/16.
 */
public class Music {

    /** file with the music */
    private String file;

    /**
     * Sound stream represents a music file to be streamed
     * @param file location of the file
     */
    public Music(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
