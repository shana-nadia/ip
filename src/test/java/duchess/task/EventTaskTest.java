package duchess.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for the EventTask class.
 */
public class EventTaskTest {

    private EventTask event;

    /**
     * Sets up a fresh EventTask before each test.
     */
    @BeforeEach
    public void setUp() {
        event = new EventTask("Group meeting", "2026-02-01 1600", "2026-02-01 1700");
    }

    /**
     * Tests that toFileFormat() return the correct string when the task is not done.
     */
    @Test
    public void toFileFormatNotDone() {
        String expected = "E | 0 | Group meeting | 2026-02-01 1600 | 2026-02-01 1700";
        assertEquals(expected, event.toFileFormat());
    }

    /**
     * Tests that toFileFormat() returns the correct string when the task is done.
     */
    @Test
    public void toFileFormatDone() {
        event.mark();
        String expected = "E | 1 | Group meeting | 2026-02-01 1600 | 2026-02-01 1700";
        assertEquals(expected, event.toFileFormat());
    }

    /**
     * Tests that toString() returns a correct and user-friendly representation including dates and mark.
     */
    @Test
    public void testToString() {
        String expected = "[E][ ] Group meeting (from: Feb 1 2026, 4 pm to: Feb 1 2026, 5 pm)";
        assertEquals(expected, event.toString());
    }
}

