package activity.tasks;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by germangb on 25/04/2016.
 */
public class TaskManager {

    /** queue of tasks */
    private Queue<Task> queue = new LinkedList<>();
    private boolean started = false;

    /**
     * Add a new task
     * @param task new task to be added
     */
    public void add (Task task) {
        queue.add(task);
    }

    /** Clear tasks */
    public void clear () {
        queue.clear();
        started = false;
    }

    public int remaining () {
        return queue.size();
    }

    /** Update tasks */
    public void update () {
        if (queue.isEmpty()) return;

        // update task
        Task task = queue.peek();

        if (!started) {
            started = true;
            task.onBegin();
        }

        if (task.onUpdate()) {
            queue.poll();
            started = false;
        }
    }
}
