package activity.tasks;

/**
 * Created by germangb on 25/04/2016.
 */
public class PrintTask implements Task {

    private Object line;

    public PrintTask (Object line) {
        this.line = line;
    }

    @Override
    public void onBegin() {
        System.out.println(line);
    }

    @Override
    public boolean onUpdate() {
        return true;
    }
}
