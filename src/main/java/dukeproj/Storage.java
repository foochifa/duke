package dukeproj;

import dukeproj.data.Schedule;
import dukeproj.exception.BadDateException;
import dukeproj.tasks.Deadline;
import dukeproj.tasks.Event;
import dukeproj.tasks.Task;
import dukeproj.tasks.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the storage file for DukeProject.
 */
public class Storage {
    /** File object that connects to Duke's storage file on the hard drive. */
    private File file;

    /**
     * Prints the file in storage into an ArrayList.
     * Takes in a calender to update the calender with the tasks in the file.
     *
     * @param schedule calender to be updated by tasks in storage file.
     * @return ArrayList made from tasks in storage file.
     */
    public ArrayList<Task> printFileIntoList(Schedule schedule) {
        try {
            Scanner sc = new Scanner(file);
            ArrayList<Task> tasks = new ArrayList<>();
            while (sc.hasNext()) {
                String str = sc.nextLine();
                String[] parts = str.split("\\|");
                switch (parts[0]) {
                case "T": {
                    Task task = new Todo(Boolean.parseBoolean(parts[1]), parts[2]);
                    tasks.add(task);
                    schedule.addDate(task);
                    break;
                }
                case "E": {
                    Task task = new Event(Boolean.parseBoolean(parts[1]), parts[2], parts[3]);
                    tasks.add(task);
                    schedule.addDate(task);
                    break;
                }
                case "D": {
                    Task task = new Deadline(Boolean.parseBoolean(parts[1]), parts[2], parts[3]);
                    tasks.add(task);
                    schedule.addDate(task);
                    break;
                }
                default:
                    System.err.println("Error in task type in file");
                    break;
                }
            }
            return tasks;
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            return null;
        } catch (BadDateException e) {
            System.err.println("Bad dates in File\n Please correct it and reload");
            return null;
        }
    }

    /**
     * Writes the tasks in an ArrayList into the storage file.
     *
     * @param tasks ArrayList of tasks to be written into the storage file.
     */
    public void writeListIntoFile(ArrayList<Task> tasks) {
        try {
            FileWriter fw = new FileWriter(file);
            for (Task task : tasks) {
                switch (task.getType()) {
                case TODO:
                    fw.write("T|" + task.getDone() + "|" + task.getTask()
                            + System.lineSeparator());
                    break;
                case EVENT:
                    fw.write("E|" + task.getDone() + "|" + task.getTask() + "|"
                            + task.getDate().format(Parser.DATE_WRITE_FORMATTER)
                            + System.lineSeparator());
                    break;
                case DEADLINE:
                    fw.write("D|" + task.getDone() + "|" + task.getTask() + "|"
                            + task.getDate().format(Parser.DATE_WRITE_FORMATTER)
                            + System.lineSeparator());
                    break;
                default:
                    System.err.println("Error in TaskList");
                    break;
                }
            }
            fw.close();
        } catch (IOException e) {
            System.err.println("Unable to write");
        }
    }

    /**
     * Constructs a Storage with the file pointing to the filepath provided.
     * Will make a directory if parent directory (default: data directory) is missing.
     *
     * @param filepath filepath to be assigned to the storage.
     */
    public Storage(String filepath) {
        file = new File(filepath);
        try {
            file.getParentFile().mkdir(); //makes data directory if does not exists
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Unable to create file");
        }
    }
}
