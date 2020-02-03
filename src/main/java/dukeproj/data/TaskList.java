package dukeproj.data;

import dukeproj.tasks.Task;

import java.util.ArrayList;

/**
 * Represents a list of tasks using ArrayList.
 */
public class TaskList {
    /** Data structure used to represent list. */
    private ArrayList<Task> tasks;

    /**
     * @return size of task list.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * @param task task to add into list.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * @param idx index of task to get.
     * @return task from list.
     */
    public Task getTask(int idx) {
        return tasks.get(idx);
    }

    /**
     * @param idx index of task to remove.
     */
    public void removeTask(int idx) {
        tasks.remove(idx);
    }

    /**
     * @return ArrayList of tasks.
     */
    public ArrayList<Task> getList() {
        return tasks;
    }

    /**
     * Checks if the TaskList contains a specified Task.
     * @param task the task to be checked for.
     * @return boolean whether the list has the specified Task.
     */
    public boolean hasTask(Task task) {
        return tasks.contains(task);
    }

    /**
     * Prints the list of task systematically.
     * Includes index of task based on (array index + 1).
     */
    public void printTask() {
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(i + "." + tasks.get(i - 1));
        }
    }

    public ArrayList<Task> find(String ... keywords) {
        ArrayList<Task> outputList = new ArrayList<>();
        for (Task task: tasks) {
            for (String str: keywords) {
                if (task.getTask().contains(str) && !outputList.contains(task)) {
                    outputList.add(task);
                }
            }
        }
        return outputList;
    }

    /**
     * Constructs a TaskList based on an existing ArrayList.
     * @param tasks an ArrayList of tasks to be added into TaskList.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs an empty list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }
}
