package activity.tasks;

/**
 * Created by germangb on 04/05/16.
 */
public class DoSomethingTask implements Task {

    private Runnable run;

    public DoSomethingTask (Runnable run) {
        this.run = run;
    }

    @Override
    public void onBegin() {
        run.run();
    }

    @Override
    public boolean onUpdate() {
        return true;
    }
}
