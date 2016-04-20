package activity;

import tpa.application.Context;
import tpa.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by germangb on 20/04/16.
 */
public abstract class InteractiveLocationActivity extends LocationActivity {

    /**
     * Sensors trigger events
     */
    protected static class Sensor {

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
        public final SensorListener listener;

        public Sensor (Vector3f position, float radius, Object data, SensorListener listener) {
            this.position = position;
            this.radius = radius;
            this.data = data;
            this.listener = listener;
        }
    }

    /** Sensors in the scene */
    private List<Sensor> sensors = new ArrayList<>();

    /** current sensor player is standing in */
    private Sensor currentSensor = null;

    /** current position of the player */
    protected final Vector3f player = new Vector3f();

    /**
     * Add a sensor to the scene
     * @param sensor sensor to be added
     */
    protected void addSensor (Sensor sensor) {
        sensors.add(sensor);
    }

    @Override
    public void onUpdate(Context context) {
        super.onUpdate(context);

        // assume sensors do not overlap
        Sensor newSensor = null;
        for (Sensor s : sensors)
            if (s.position.distanceSquared(player) < s.radius*s.radius)
                newSensor = s;

        if (newSensor != currentSensor) {
            if (currentSensor != null && currentSensor.listener != null)
                currentSensor.listener.onLeft(currentSensor);

            // new sensor
            currentSensor = newSensor;
            if (currentSensor != null && currentSensor.listener != null)
                currentSensor.listener.onEntered(currentSensor);
        }
    }
}
