import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
            return todolist;
        }
        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextLine()) {
                String next = scanner.nextLine();
                String[] parts = next.split(" \\| ");
                switch (parts[0]) {
                case "T":
                    Task todoTask = new TodoTask(parts[2]);
                    if (parts[1].equals("1")) {
                        todoTask.mark();
                    }
                    todolist.addTask(todoTask);
                    break;
                case "D":
                    Task deadlineTask = new DeadlineTask(parts[2], parts[3]);
                    if (parts[1].equals("1")) {
                        deadlineTask.mark();
                    }
                    todolist.addTask(deadlineTask);
                    break;
                case "E":
                    Task eventTask = new EventTask(parts[2], parts[3], parts[4]);
                    if (parts[1].equals("1")) {
                        eventTask.mark();
                    }
                    todolist.addTask(eventTask);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("You don't have any existing data file, so we're starting with an empty list! :(");
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
            System.out.println("Couldn't save your data: " + e.getMessage() + " :(");
        }

    }
}
