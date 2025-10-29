import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.view.CalendarView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CalendarView.
 */
public class ViewTest {
  private StringBuilder output;
  private CalendarView view;

  @Before
  public void setUp() {
    output = new StringBuilder();
    view = new CalendarView(output);
  }

  @Test
  public void testDisplayWelcome() {
    view.displayWelcome();

    String result = output.toString();
    assertTrue(result.contains("Virtual Calendar Application"));
    assertTrue(result.contains("Type 'exit' to quit"));
    assertTrue(result.contains("================================="));
  }

  @Test
  public void testDisplayEventsOnDate() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .location("Room A")
        .build());
    events.add(Event.builder("Lunch", LocalDateTime.parse("2025-05-01T12:00"))
        .end(LocalDateTime.parse("2025-05-01T13:00"))
        .location("Room B")
        .build());

    view.displayEventsOnDate(events, LocalDate.parse("2025-05-01"));

    String result = output.toString();
    assertTrue(result.contains("Events on 2025-05-01:"));
    assertTrue(result.contains("Meeting"));
    assertTrue(result.contains("10:00"));
    assertTrue(result.contains("11:00"));
    assertTrue(result.contains("Room A"));
    assertTrue(result.contains("Lunch"));
    assertTrue(result.contains("12:00"));
    assertTrue(result.contains("Room B"));
  }

  @Test
  public void testDisplayEventsNull() {
    view.displayEventsOnDate(null, LocalDate.parse("2025-05-01"));

    String result = output.toString();
    assertTrue(result.contains("No events on 2025-05-01"));
  }

  @Test
  public void testDisplayEventsBetween() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Event1", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .build());

    LocalDateTime start = LocalDateTime.parse("2025-05-01T00:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-02T00:00");

    view.displayEventsBetween(events, start, end);

    String result = output.toString();
    assertTrue(result.contains("Events from"));
    assertTrue(result.contains("Event1"));
    assertTrue(result.contains("2025-05-01"));
  }

  @Test
  public void testDisplayEventsEmpty() {
    List<Event> events = new ArrayList<>();
    LocalDateTime start = LocalDateTime.parse("2025-05-01T00:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-02T00:00");

    view.displayEventsBetween(events, start, end);

    String result = output.toString();
    assertTrue(result.contains("No events between"));
  }

  @Test
  public void testDisplayUserStatus1() {
    view.displayUserStatus(UserStatus.BUSY);

    assertEquals("busy\n", output.toString());
  }

  @Test
  public void testDisplayUserStatus2() {
    view.displayUserStatus(UserStatus.AVAILABLE);

    assertEquals("available\n", output.toString());
  }

  @Test
  public void testDisplaySuccess() {
    view.displaySuccess("Operation completed");

    String result = output.toString();
    assertTrue(result.contains("Success"));
    assertTrue(result.contains("Operation completed"));
  }

  @Test
  public void testDisplayError() {
    view.displayError("Something went wrong");

    String result = output.toString();
    assertTrue(result.contains("Error"));
    assertTrue(result.contains("Something went wrong"));
  }

  @Test
  public void testDisplayWarning() {
    view.displayWarning("This is a warning");

    String result = output.toString();
    assertTrue(result.contains("Warning"));
    assertTrue(result.contains("This is a warning"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullOutput() {
    new CalendarView(null);
  }

  @Test
  public void testDefaultConstructor() {
    CalendarView defaultView = new CalendarView();
    assertNotNull(defaultView);
  }

  @Test
  public void testDisplayEventsWithoutEndTime() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("AllDay", LocalDateTime.parse("2025-05-01T08:00"))
        .setAllDayEvent()
        .build());

    view.displayEventsOnDate(events, LocalDate.parse("2025-05-01"));

    String result = output.toString();
    assertTrue(result.contains("AllDay"));
    assertTrue(result.contains("8:00"));
  }

  @Test
  public void testDisplayEventsWithoutLocation() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .build());

    view.displayEventsOnDate(events, LocalDate.parse("2025-05-01"));

    String result = output.toString();
    assertTrue(result.contains("Meeting"));
    assertTrue(result.contains("10:00"));
    assertFalse(result.contains(" at null"));
  }

  @Test
  public void testExportCalendar() throws IOException {
    String csvContent = "Subject,Start Date,Start Time\nMeeting,05/01/2025,10:00 AM\n";
    String fileName = "test_export.csv";

    view.exportCalendar(csvContent, fileName);

    String result = output.toString();
    assertTrue(result.contains("Exported to"));
    assertTrue(result.contains(fileName));

    Path filePath = Paths.get(fileName);
    assertTrue(Files.exists(filePath));
    String fileContent = new String(Files.readAllBytes(filePath));
    assertEquals(csvContent, fileContent);

    Files.deleteIfExists(filePath);
  }
}

