import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import calendar.controller.CalendarController;
import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;
import calendar.model.Calendar;
import calendar.model.CalendarInterface;
import calendar.model.MultiCalendarManager;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarView;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for view command such as PrintCommand, ExportCommand, ShowCommand.
 */
public class ViewCommandsTest {
  private Calendar calendar;
  private StringBuilder output;
  private CalendarView view;
  private MultiCalendarManager manager;

  /**
   * Set up a new Calendar.
   */
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

    CalendarController controller = new CalendarController(manager, view, readable, output);
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

    CalendarController controller = new CalendarController(manager, view, readable, output);
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

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("No events on 2025-05-01"));
  }

  @Test
  public void testPrintCommandInvalid() throws IOException {
    String input = "print events invalid format\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
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

    CalendarController controller = new CalendarController(manager, view, readable, output);
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

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("available"));
  }

  @Test
  public void testShowCommandInvalid() throws IOException {
    String input = "show status on datetime\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
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

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("Exported to"));

    java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(tempFile));
  }

  @Test
  public void testExportCommandInvalid() throws IOException {
    String input = "export cal\nexit\n";  // Missing filename
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue(result.contains("Error") || result.contains("Invalid"));
  }

  @Test
  public void testExportCommandNoFileName() throws IOException {
    String input = "export cal\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error for missing filename",
        result.contains("Error") || result.contains("Invalid"));
  }

  @Test
  public void testExportCommandInvalid2() throws IOException {
    String input = "export calendar test.csv\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display 'Invalid command' error",
        result.contains("Invalid command") || result.contains("Error"));
  }

  @Test
  public void testExportCommandInvalid3() throws IOException {
    String input = "export cal test file.csv\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error for filename with spaces",
        result.contains("Error") || result.contains("Invalid"));
  }

  @Test
  public void testShowCommandInvalid2() throws IOException {
    String input = "show status 2025-05-01T10:00\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display 'Invalid command' error",
        result.contains("Show status failed. Invalid command:"));
    assertTrue("Should include the invalid command line",
        result.contains("show status 2025-05-01T10:00"));
  }

  @Test
  public void testShowCommandInvalid3() throws IOException {
    String input = "show status at 2025-05-01T10:00\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display 'Invalid command' error",
        result.contains("Show status failed. Invalid command:"));
    assertTrue("Should include the full command",
        result.contains("show status at 2025-05-01T10:00"));
  }

  /**
   * A Calendar spy for testing.
   */
  private static class RuntimeThrowingCalendar implements CalendarInterface {
    private final Calendar realCalendar = new Calendar();

    @Override
    public String exportToCsv() {
      throw new RuntimeException("Force failure for catch coverage.");
    }

    @Override
    public String exportToICal(String calendarName, ZoneId timezone) {
      return "";
    }

    @Override
    public Event createSingleEvent(String subject, String start, String end, String desc,
                                   String loc, String status, String seriesId) {
      return null;
    }

    @Override
    public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                         String description, String location, String eventStatus,
                                         String weekdays, int repeatTimes, String seriesEndDateTime)
        throws IllegalArgumentException {
      return null;
    }

    @Override
    public Event getSingleEvent(String subject, String startDateTime, String endDateTime)
        throws IllegalArgumentException {
      return null;
    }

    @Override
    public Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String newSubject, String newStartDateTime, String newEndDateTime,
                                 String newDescription, String newLocation, String newEventStatus)
        throws IllegalArgumentException {
      return null;
    }

    @Override
    public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                       String newSubject, String newStartDateTime,
                                       String newEndDateTime, String newDescription,
                                       String newLocation, String newEventStatus)
        throws IllegalArgumentException {
      return null;
    }

    @Override
    public List<Event> getEventsOnDate(LocalDate date) {
      throw new RuntimeException("Force failure for PrintCommand coverage.");
    }

    @Override
    public List<Event> getEventsBetween(LocalDateTime start, LocalDateTime end) {
      return List.of();
    }

    @Override
    public UserStatus getUserStatus(LocalDateTime queryTime) {
      return null;
    }

  }

  @Test
  public void testExportCommandException() throws IOException {
    CalendarInterface errorCalendar = new RuntimeThrowingCalendar();
    String input = "export cal test_runtime_fail.csv\nexit\n";
    Readable readable = new StringReader(input);
    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();
    String result = output.toString();
    assertTrue("Should contain 'Export failed.' message from the catch block.",
        result.contains("Export failed."));
    assertTrue("Should contain the custom exception message.",
        result.contains("Force failure for catch coverage."));
    assertFalse("Success message 'Exported to' should NOT be present.",
        result.contains("Exported to"));
  }

  @Test
  public void testPrintCommandException() throws IOException {
    CalendarInterface errorCalendar = new RuntimeThrowingCalendar();
    String input = "print events on 2025-05-01\nexit\n";
    Readable readable = new StringReader(input);
    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();
    String result = output.toString();
    assertTrue("Should contain 'Print failed.' message from the catch block.",
        result.contains("Print failed."));
    assertTrue("Should contain the custom exception message.",
        result.contains("Force failure for PrintCommand coverage."));
  }
}
