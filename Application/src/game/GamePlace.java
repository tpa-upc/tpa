package game;

import places.Location;
import places.Room;

/**
 * Created by germangb on 13/04/16.
 */
public enum GamePlace {

    /** Computer activity */
    Computer (new Room());

    /** referenced game activity */
    private Location location;

    GamePlace(Location location) {
        this.location= location;
    }

    /**
     * Get eumarated location
     * @return game location
     */
    public Location getActivity() {
        return location;
    }
}
