package activity.tasks;

import tpa.timing.Time;

/**
 * Created by germangb on 25/04/2016.
 */
public class DelayTask implements Task {

    private Time time;
    private float seconds;
    private float start;

    public DelayTask (float seconds, Time time) {
        this.time = time;
        this.seconds = seconds;
    }

    @Override
    public void onBegin() {
        start = time.getTime();
    }

    @Override
    public boolean onUpdate() {
        return time.getTime() - start > seconds;
    }
}
