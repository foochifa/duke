package main.duke;

import main.duke.data.Calender;
import main.duke.data.TaskList;
import main.duke.enums.Command;
import main.duke.exception.BadDateException;
import main.duke.exception.BadDescriptionException;
import main.duke.exception.DukeDescriptionException;
import main.duke.exception.InvalidCommandException;
import main.duke.tasks.Deadline;
import main.duke.tasks.Event;
import main.duke.tasks.Task;
import main.duke.tasks.Todo;

import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;

public class Main {
    private TaskList tasklist;
    private Storage storage;
    private Calender calender;
    private Ui ui;
    private Scanner sc;

    public void readCommand(Command command) throws DukeDescriptionException,
            BadDescriptionException, BadDateException {
        switch (command) {
            case LIST:
                System.out.println("Here are all your tasks:");
                tasklist.printTask();
                break;
            case DONE:
                try {
                    String strDone = sc.nextLine();
                    if (strDone.isEmpty()) throw new DukeDescriptionException("Empty Description");
                    int done = Integer.parseInt(strDone.substring(1)); //there must be a space between command and input
                    if (done <= 0 || done > tasklist.getSize())
                        throw new BadDescriptionException("" + done);
                    tasklist.getTask(done - 1).setDone(true);
                    System.out.println("Nice! I've marked this task as done: \n" +
                            "  " + tasklist.getTask(done - 1));
                    storage.writeListIntoFile(tasklist.getList());
                } catch (NumberFormatException e) {
                    throw new BadDescriptionException("Non-Integer");
                }
                break;
            case TODO:
                String todo = sc.nextLine();
                if (todo.isEmpty()) throw new DukeDescriptionException("Empty Description");
                Task taskToDo = new Todo(todo.substring(1));
                tasklist.addTask(taskToDo);
                System.out.println("I've added this task: \n" +
                        "  " + taskToDo + "\nNow you have " +
                        tasklist.getSize() + " tasks in the list." );
                storage.writeListIntoFile(tasklist.getList());
                break;
            case EVENT:
                String event = sc.nextLine();
                if (event.isEmpty()) throw new DukeDescriptionException("Empty Description");
                int eventDate = event.indexOf("/");
                Task taskEvent = new Event(event.substring(1, eventDate),
                        event.substring(eventDate + 4));
                tasklist.addTask(taskEvent);
                calender.addDate(taskEvent);
                System.out.println("I've added this task: \n" +
                        "  " + taskEvent + "\nNow you have " +
                        tasklist.getSize() + " tasks in the list." );
                storage.writeListIntoFile(tasklist.getList());
                break;
            case DEADLINE:
                String deadline = sc.nextLine();
                if (deadline.isEmpty()) throw new DukeDescriptionException("Empty Description");
                int dLineDate = deadline.indexOf("/");
                Task taskDLine = new Deadline(deadline.substring(1, dLineDate),
                        deadline.substring(dLineDate + 4));
                tasklist.addTask(taskDLine);
                calender.addDate(taskDLine);
                System.out.println("I've added this task: \n" +
                        "  " + taskDLine + "\nNow you have " +
                        tasklist.getSize() + " tasks in the list." );
                storage.writeListIntoFile(tasklist.getList());
                break;
            case DELETE:
                try {
                    String strDelete = sc.nextLine();
                    if (strDelete.isEmpty()) throw new DukeDescriptionException("Empty Description");
                    int delete = Integer.parseInt(strDelete.substring(1)); //there must be a space between command and input
                    if (delete <= 0 || delete > tasklist.getSize())
                        throw new BadDescriptionException("" + delete);
                    Task deletedTask = tasklist.getTask(delete - 1);
                    tasklist.removeTask(delete - 1);
                    calender.removeTask(deletedTask);
                    System.out.println("Okay! I have deleted this task:\n" +
                            "  " + deletedTask + "\nNow you have " +
                            tasklist.getSize() + " tasks in the list.");
                    storage.writeListIntoFile(tasklist.getList());
                } catch (NumberFormatException e) {
                    throw new BadDescriptionException("Non-Integer");
                }
                break;
            case SEARCH:
                String search = sc.nextLine();
                if (search.isEmpty()) throw new DukeDescriptionException("Empty Description");
                LocalDate date = Parser.dateParser(search.substring(1));
                System.out.println("Here are the events on " +
                        date.format(Parser.DATE_FORMATTER) + ":");
                calender.searchDate(date);
                break;
            default:
                break;
        }
    }

    public void run() {
        ui.introduction();
        while (sc.hasNext()) {
            String next = sc.next();
            if (next.equals("bye")) break;
            else {
                try {
                    ui.lineBreak();
                    Command command = Parser.commandParser(next);
                    readCommand(command);
                } catch (InvalidCommandException e) {
                    System.out.println("Sorry I do not know what that means!");
                } catch (DukeDescriptionException e) {
                    System.out.println("OOPS! You forgot to include a description!");
                } catch (BadDescriptionException e) {
                    System.out.println("OOPS! The number input for " +
                            "done/delete cannot be " + e.getMessage());
                } catch (BadDateException e) {
                    System.out.println("Sorry I don't recognise this date format!\n" +
                            "Please make sure the format is: dd mm yy");
                } finally {
                    ui.lineBreak();
                }
            }
        }
        ui.exit();
    }

    public Main(String filepath) {
        ui = new Ui();
        calender = new Calender();
        storage = new Storage(filepath);
        tasklist = new TaskList(storage.printFileIntoList(calender));
        sc = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Main("." + File.separator + "data" +
                File.separator + "Task.txt").run();
    }
}