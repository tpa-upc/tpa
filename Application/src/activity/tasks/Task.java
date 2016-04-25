package activity.tasks;

/**
 * Created by germangb on 25/04/2016.
 */
public interface Task {

    /** called when task is begin */
    void onBegin ();

    /**
     * Called to update task
     * @return if task is finished, returns true
     */
    boolean onUpdate ();
}
