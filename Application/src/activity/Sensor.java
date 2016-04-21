package activity;

/**
 * Created by germangb on 20/04/16.
 */

import tpa.joml.Vector3f;

public class Sensor {

    /** Monitoring interface */
    interface SensorListener {

        /** Called when something enters a sensor */
        void onEntered (Sensor sensor);

        /** Called when there is an interaction with a sensor */
        void onAction (Sensor sensor);

        /** Called when something leaves a sensor */
        void onLeft (Sensor sensor);
    }

    /** sensor position */
    public final Vector3f position;

    /** sensor radius */
    public final float radius;

    /** data to identify sensor */
    public final Object data;

    /** sensor listener */
    public final Sensor.SensorListener listener;

    public Sensor (Vector3f position, float radius, Object data, Sensor.SensorListener listener) {
        this.position = position;
        this.radius = radius;
        this.data = data;
        this.listener = listener;
    }
}