package duchess.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import duchess.task.Task;
import duchess.task.TodoTask;

/**
 * JUnit tests for the deleteTask method in the TodoList class.
 */
public class TodoListTest {
    private TodoList todoList;

    /**
     * Sets up a fresh TodoList with 3 tasks before each test.
     */
    @BeforeEach
    public void setUp() {
        this.todoList = new TodoList();
        this.todoList.addTask(new TodoTask("Eat lunch"));
        this.todoList.addTask(new TodoTask("Go for class"));
        this.todoList.addTask(new TodoTask("Do your assignments"));
    }

    /**
     * Tests that deleting a task at a certain valid index returns the correct task.
     */
    @Test
    public void deleteCorrectTask() {
        Task deleted = this.todoList.deleteTask(1);
        assertEquals("Go for class", deleted.getDescription());
    }

    /**
     * Tests that deleting a task at a valid index reduces the size of the TodoList by 1.
     */
    @Test
    public void deleteReducesSize() {
        this.todoList.deleteTask(0);
        assertEquals(2, this.todoList.size());
    }

    /**
     * Tests that deleting a task at an invalid index throws an IndexOutOfBoundsException.
     */
    @Test
    public void deleteTaskInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.todoList.deleteTask(5);
        });
    }

    /**
     * Tests that deleting a task at a valid index shifts the remaining tasks correctly.
     */
    @Test
    public void deleteShiftsTasks() {
        this.todoList.deleteTask(0);
        assertEquals("Go for class", this.todoList.getTask(0).getDescription());
    }
}
