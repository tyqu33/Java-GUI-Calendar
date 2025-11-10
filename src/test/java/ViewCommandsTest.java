import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import calendar.calendarEntity.CalendarEntityInterface;
import calendar.controller.CalendarController;
import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;
import calendar.model.Calendar;
import calendar.model.CalendarInterface;
import calendar.model.MultiCalendarManager;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarView;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for view command such as PrintCommand, ExportCommand, ShowCommand.
 */
public class ViewCommandsTest {
  private StringBuilder output;
  private CalendarView view;
  private CalendarInterface calendar;
  private MultiCalendarManager manager;

  /**
   * Set up a new Calendar.
   */
  @Before
  public void setUp() {
    manager = new MultiCalendarManager();
    output = new StringBuilder();
    view = new CalendarView(output);
    CalendarEntityInterface entity = manager.createCalendar("TestCalendar", "America/New_York");
    manager.useThisCalendarEntity(entity);
    calendar = entity.getCalendar();
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
    assertTrue("Should contain 'Events on 2025-05-01:', but got: " + result,
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
    assertTrue("Should contain 'Events from', but got: " + result,
        result.contains("Events from"));
    assertTrue("Should contain 'Event1'", result.contains("Event1"));
    assertTrue("Should contain 'Event2'", result.contains("Event2"));
  }

  @Test
  public void testPrintCommandEmpty() throws IOException {
    String input = "print events on 2025-05-01\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should contain 'No events on 2025-05-01', but got: " + result,
        result.contains("No events on 2025-05-01"));
  }

  @Test
  public void testPrintCommandInvalid() throws IOException {
    String input = "print events invalid format\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should contain 'Error' or 'Invalid', but got: " + result,
        result.contains("Error") || result.contains("Invalid"));
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
    assertTrue("Should contain 'busy', but got: " + result,
        result.contains("busy"));
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
    assertTrue("Should contain 'available', but got: " + result,
        result.contains("available"));
  }

  @Test
  public void testShowCommandInvalid() throws IOException {
    String input = "show status on datetime\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should contain 'Error' or 'Invalid', but got: " + result,
        result.contains("Error") || result.contains("Invalid") || result.contains("failed"));
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
    assertTrue("Should contain 'Exported to', but got: " + result,
        result.contains("Exported to"));

    // Clean up
    try {
      java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(tempFile));
    } catch (Exception e) {
      // Ignore cleanup errors
    }
  }

  @Test
  public void testExportCommandInvalid() throws IOException {
    String input = "export cal\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error for missing filename, but got: " + result,
        result.contains("Error") || result.contains("Invalid") || result.contains("failed"));
  }

  @Test
  public void testExportCommandNoFileName() throws IOException {
    String input = "export cal\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error for missing filename, but got: " + result,
        result.contains("Error") || result.contains("Invalid") || result.contains("failed"));
  }

  @Test
  public void testExportCommandInvalid2() throws IOException {
    String input = "export calendar test.csv\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error, but got: " + result,
        result.contains("Invalid command") || result.contains("Error") || result.contains("Invalid"));
  }

  @Test
  public void testExportCommandInvalid3() throws IOException {
    String input = "export cal test file.csv\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error for filename with spaces, but got: " + result,
        result.contains("Error") || result.contains("Invalid") || result.contains("failed"));
  }

  @Test
  public void testShowCommandInvalid2() throws IOException {
    String input = "show status 2025-05-01T10:00\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error, but got: " + result,
        result.contains("Show status failed") || result.contains("Invalid command")
            || result.contains("Error"));
  }

  @Test
  public void testShowCommandInvalid3() throws IOException {
    String input = "show status at 2025-05-01T10:00\nexit\n";

    CalendarController controller = new CalendarController(
        manager, view, new StringReader(input), output);
    controller.go();

    String result = output.toString();
    assertTrue("Should display error, but got: " + result,
        result.contains("Show status failed") || result.contains("Invalid command")
            || result.contains("Error"));
  }

  /**
   * A Calendar spy for testing exception handling.
   */
  private static class RuntimeThrowingCalendar implements CalendarInterface {

    @Override
    public String exportToCsv() {
      throw new RuntimeException("Force failure for catch coverage.");
    }

    @Override
    public String exportToICal(String calendarName, ZoneId timezone) {
      return "";
    }

    @Override
    public Collection<Event> getEvents() {
      return List.of();
    }

    @Override
    public EventSeries getEventSeries(String seriesId) {
      return null;
    }

    @Override
    public Event createSingleEvent(String subject, String start, String end, String desc,
                                   String loc, String status, String seriesId) {
      return null;
    }

    @Override
    public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                         String description, String location, String eventStatus,
                                         String weekdays, int repeatTimes, String seriesEndDateTime) {
      return null;
    }

    @Override
    public Event getSingleEvent(String subject, String startDateTime, String endDateTime) {
      return null;
    }

    @Override
    public Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String newSubject, String newStartDateTime, String newEndDateTime,
                                 String newDescription, String newLocation, String newEventStatus) {
      return null;
    }

    @Override
    public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                       String newSubject, String newStartDateTime,
                                       String newEndDateTime, String newDescription,
                                       String newLocation, String newEventStatus) {
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

  /**
   * A MultiCalendarManager that returns a calendar throwing exceptions.
   */
  private static class RuntimeThrowingManager extends MultiCalendarManager {
    private final CalendarInterface throwingCalendar = new RuntimeThrowingCalendar();
    private CalendarEntityInterface entity;

    @Override
    public CalendarEntityInterface createCalendar(String calendarName, String timeZone) {
      entity = new CalendarEntityInterface() {
        @Override
        public String getCalendarName() {
          return calendarName;
        }

        @Override
        public ZoneId getTimeZone() {
          return ZoneId.of(timeZone);
        }

        @Override
        public CalendarInterface getCalendar() {
          return throwingCalendar;
        }
      };
      return entity;
    }

    @Override
    public CalendarEntityInterface getCurrentCalendarEntity() {
      return entity;
    }
  }

  @Test
  public void testExportCommandException() throws IOException {
    MultiCalendarManagerInterface errorManager = new RuntimeThrowingManager();
    errorManager.createCalendar("TestCalendar", "America/New_York");
    CalendarEntityInterface entity = errorManager.getCurrentCalendarEntity();
    errorManager.useThisCalendarEntity(entity);

    StringBuilder errorOutput = new StringBuilder();
    CalendarView errorView = new CalendarView(errorOutput);

    String input = "export cal test_runtime_fail.csv\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(errorManager, errorView, readable, errorOutput);
    controller.go();

    String result = errorOutput.toString();
    assertTrue("Should contain 'Export failed.' message, but got: " + result,
        result.contains("Export failed."));
    assertTrue("Should contain the custom exception message, but got: " + result,
        result.contains("Force failure for catch coverage."));
    assertFalse("Success message 'Exported to' should NOT be present, but got: " + result,
        result.contains("Exported to"));
  }

  @Test
  public void testPrintCommandException() throws IOException {
    MultiCalendarManagerInterface errorManager = new RuntimeThrowingManager();
    errorManager.createCalendar("TestCalendar", "America/New_York");
    CalendarEntityInterface entity = errorManager.getCurrentCalendarEntity();
    errorManager.useThisCalendarEntity(entity);

    StringBuilder errorOutput = new StringBuilder();
    CalendarView errorView = new CalendarView(errorOutput);

    String input = "print events on 2025-05-01\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(errorManager, errorView, readable, errorOutput);
    controller.go();

    String result = errorOutput.toString();
    assertTrue("Should contain 'Print failed.' message, but got: " + result,
        result.contains("Print failed."));
    assertTrue("Should contain the custom exception message, but got: " + result,
        result.contains("Force failure for PrintCommand coverage."));
  }

  @Test
  public void testExportCommandICal() throws IOException {
    calendar.createSingleEvent("Meeting", "2025-05-01T10:00", "2025-05-01T11:00",
        "Description", "Location", "public", null);
    String tempFile = "test" + System.currentTimeMillis() + ".ical";
    String input = "export cal " + tempFile + "\nexit\n";
    Readable readable = new StringReader(input);
    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should contain 'Exported to'", result.contains("Exported to"));
    try {
      java.nio.file.Path path = java.nio.file.Paths.get(tempFile);
      String content = new String(java.nio.file.Files.readAllBytes(path));
      assertTrue("File should contain iCal format", content.contains("BEGIN:VCALENDAR"));
      assertTrue("File should contain event", content.contains("SUMMARY:Meeting"));
      java.nio.file.Files.deleteIfExists(path);
    } catch (Exception e) {
      // Ignore cleanup errors
    }
  }

  @Test
  public void testExportCommandInvalid4() throws IOException {
    String input = "export cal test.txt\nexit\n";
    Readable readable = new StringReader(input);
    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should contain error",
        result.contains("Invalid file name") || result.contains("Error"));
  }

  @Test
  public void testExportEmptyCalendar() throws IOException {
    String tempFile = "test_export_empty_" + System.currentTimeMillis() + ".csv";
    String input = "export cal " + tempFile + "\nexit\n";
    Readable readable = new StringReader(input);

    CalendarController controller = new CalendarController(manager, view, readable, output);
    controller.go();

    String result = output.toString();
    assertTrue("Should successfully export empty calendar", result.contains("Exported to"));

    try {
      Files.deleteIfExists(Paths.get(tempFile));
    } catch (Exception e) {
      // Ignore
    }
  }
}