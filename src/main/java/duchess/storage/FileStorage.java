package duchess.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import duchess.list.TodoList;
import duchess.task.DeadlineTask;
import duchess.task.EventTask;
import duchess.task.Task;
import duchess.task.TodoTask;

/**
 * Handles writing tasks to and fetching tasks from the hard disk.
 */
public class FileStorage {

    /**
     * Fetches tasks from the hard disk and loads them into a TodoList.
     * If no data file exists, an empty task list is returned.
     *
     * @return a TodoList containing all previously saved tasks.
     */
    public static TodoList fetchTasks() {
        TodoList todolist = new TodoList();
        File f = new File("data/duchess.txt");
        if (!f.exists()) {
            System.out.println("No existing data found. Starting with a fresh list, peasant!");
            return todolist;
        }

        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextLine()) {
                String next = scanner.nextLine();
                String[] parts = next.split(" \\| ");

                try {
                    switch (parts[0]) {
                    case "T":
                        if (parts.length < 3) throw new IllegalArgumentException("Corrupt data for TodoTask");
                        Task todoTask = new TodoTask(parts[2]);
                        if (parts[1].equals("1")) todoTask.mark();
                        todolist.addTask(todoTask);
                        break;
                    case "D":
                        if (parts.length < 4) throw new IllegalArgumentException("Corrupt data for DeadlineTask");
                        Task deadlineTask = new DeadlineTask(parts[2], parts[3]);
                        if (parts[1].equals("1")) deadlineTask.mark();
                        todolist.addTask(deadlineTask);
                        break;
                    case "E":
                        if (parts.length < 5) throw new IllegalArgumentException("Corrupt data for EventTask");
                        Task eventTask = new EventTask(parts[2], parts[3], parts[4]);
                        if (parts[1].equals("1")) eventTask.mark();
                        todolist.addTask(eventTask);
                        break;
                    default:
                        System.out.println("Skipping unknown task type in line: " + next);
                    }
                } catch (Exception e) {
                    System.out.println("Skipping corrupted line: \"" + next + "\". Reason: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read data file. Starting fresh, peasant!");
        }

        return todolist;
    }

    /**
     * Writes all tasks in the given TodoList to the data file.
     *
     * @param todolist the task list to be written to the data file
     */
    public static void writeTasks(TodoList todolist) {
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileWriter fw = new FileWriter("data/duchess.txt");
            for (int i = 0; i < todolist.size(); i++) {
                Task task = todolist.getTask(i);
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
                System.out.println("Couldn't save your royal ledger: " + e.getMessage());
        }
    }
}
