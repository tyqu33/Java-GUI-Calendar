import static org.junit.Assert.assertTrue;

import calendar.controller.CalendarController;
import calendar.model.Calendar;
import calendar.view.CalendarView;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;

public class ViewCommandsTest {
  private Calendar calendar;
  private StringBuilder output;
  private CalendarView view;

  @Before
  public void setUp() {
    calendar = new Calendar();
    output = new StringBuilder();
    view = new CalendarView(output);
  }

  @Test
  public void testPrintCommandOnDate() throws IOException {
    calendar.createSingleEvent("Meeting", "2025-05-01T10:00", "2025-05-01T11:00",
        "Team meeting", "Room A", "public", null);

    String input = "print events on 2025-05-01\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should contain 'Events on 2025-05-01:'",
        result.contains("Events on 2025-05-01:"));
    assertTrue("Should contain 'Meeting'", result.contains("Meeting"));
    assertTrue("Should contain '10:00'", result.contains("10:00"));
    assertTrue("Should contain 'Room A'", result.contains("Room A"));
  }

  @Test
  public void testPrintCommandBetween() throws IOException {
    calendar.createSingleEvent("Event1", "2025-05-01T10:00", "2025-05-01T11:00",
        null, null, "public", null);
    calendar.createSingleEvent("Event2", "2025-05-02T14:00", "2025-05-02T15:00",
        null, null, "public", null);

    String input = "print events from 2025-05-01T00:00 to 2025-05-03T00:00\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("Events from"));
    assertTrue(result.contains("Event1"));
    assertTrue(result.contains("Event2"));
  }

  @Test
  public void testPrintCommandEmpty() throws IOException {
    String input = "print events on 2025-05-01\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("No events on 2025-05-01"));
  }

  @Test
  public void testPrintCommandInvalid() throws IOException {
    String input = "print events invalid format\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("Error") || result.contains("Invalid"));
  }

  @Test
  public void testShowCommand1() throws IOException {
    calendar.createSingleEvent("Meeting", "2025-05-01T10:00", "2025-05-01T11:00",
        null, null, "public", null);

    String input = "show status on 2025-05-01T10:30\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("busy"));
  }

  @Test
  public void testShowCommand2() throws IOException {
    calendar.createSingleEvent("Meeting", "2025-05-01T10:00", "2025-05-01T11:00",
        null, null, "public", null);

    String input = "show status on 2025-05-01T09:00\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("available"));
  }

  @Test
  public void testShowCommandInvalid() throws IOException {
    String input = "show status on datetime\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("Error") || result.contains("Invalid"));
  }

  @Test
  public void testExportCommand() throws IOException {
    calendar.createSingleEvent("Meeting", "2025-05-01T10:00", "2025-05-01T11:00",
        "Description", "Location", "public", null);

    String tempFile = "test_export_" + System.currentTimeMillis() + ".csv";
    String input = "export cal " + tempFile + "\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("Exported to"));

    java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(tempFile));
  }

  @Test
  public void testExportCommandInvalid() throws IOException {
    String input = "export cal\nexit\n";  // Missing filename
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(calendar, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("Error") || result.contains("Invalid"));
  }

}
