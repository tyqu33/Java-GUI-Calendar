import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import calendar.controller.ExportCommand;
import calendar.controller.PrintCommand;
import calendar.controller.ShowCommand;
import calendar.event.Event;
import calendar.event.EventSeries;
import calendar.model.Calendar;
import calendar.model.EventKey;
import calendar.view.CalendarView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A placeholder test class. You must definitely not use this.
 */
public class CalendarTest {
  private Calendar calendar;
  private CalendarView view;
  private ByteArrayOutputStream outContent;
  private ByteArrayOutputStream errContent;
  private PrintStream originalOut;
  private PrintStream originalErr;

  @Before
  public void setUp() {
    calendar = new Calendar();
    view = new CalendarView();
    outContent = new ByteArrayOutputStream();
    errContent = new ByteArrayOutputStream();
    originalOut = System.out;
    originalErr = System.err;
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void tearDown() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testDummyCalendarName() {
    calendar.DummyCalendar calendar = new calendar.DummyCalendar();
    assertEquals("DummyCalendar", calendar.getName());
  }

  @Test
  public void testCreateEvent() {
    Event event = calendar.createSingleEvent(
        "Team Meeting",
        "2025-05-01T10:00",
        "2025-05-01T11:00",
        "Weekly sync meeting",
        "Conference Room A",
        "public",
        null
    );
    assertNotNull(event);
    assertEquals("Team Meeting", event.getSubject());
    assertEquals(LocalDateTime.parse("2025-05-01T10:00"), event.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-05-01T11:00"), event.getEndDateTime());
    assertEquals("Weekly sync meeting", event.getDescription());
    assertEquals("Conference Room A", event.getLocation());
  }

  @Test
  public void testCeateEventSeries() {
    EventSeries series = calendar.createEventSeries(
        "Lecture",
        "2025-05-05T10:00",
        "2025-05-05T11:00",
        "Java Programming",
        "Room 101",
        "public",
        "MW",
        6,
        ""
    );
    Set<EventKey> keys = series.getSeriesKeys();
    assertEquals(6, keys.size());
    List<LocalDate> dates = keys.stream()
        .map(k -> k.getStartDateTime().toLocalDate())
        .sorted()
        .collect(Collectors.toList());
    assertEquals(LocalDate.parse("2025-05-05"), dates.get(0));
    assertEquals(LocalDate.parse("2025-05-07"), dates.get(1));
    assertEquals(LocalDate.parse("2025-05-12"), dates.get(2));
    assertEquals(LocalDate.parse("2025-05-14"), dates.get(3));
    assertEquals(LocalDate.parse("2025-05-19"), dates.get(4));
    assertEquals(LocalDate.parse("2025-05-21"), dates.get(5));
    for (EventKey key : keys) {
      assertEquals(LocalTime.of(10, 0), key.getStartDateTime().toLocalTime());
      assertEquals(LocalTime.of(11, 0), key.getEndDateTime().toLocalTime());
    }
  }

  //Print and export
  @Test
  public void testPrintEventOnDate() {
    calendar.createSingleEvent(
        "Meeting",
        "2025-05-01T10:00",
        "2025-05-01T11:00",
        "Team meeting",
        "Room A",
        "public",
        null
    );
    calendar.createSingleEvent(
        "Lunch",
        "2025-05-01T12:00",
        "2025-05-01T13:00",
        null,
        "Cafeteria",
        "public",
        null
    );
    new PrintCommand("print events on 2025-05-01", calendar, view);
    String output = outContent.toString();
    assertTrue(output.contains("Events on 2025-05-01:"));
    assertTrue(output.contains("Meeting"));
    assertTrue(output.contains("10:00"));
    assertTrue(output.contains("11:00"));
    assertTrue(output.contains("Room A"));
    assertTrue(output.contains("Lunch"));
    assertTrue(output.contains("12:00"));
    assertTrue(output.contains("Cafeteria"));
  }

  @Test
  public void testPrintNoEvents() {
    new PrintCommand("print events on 2025-05-01", calendar, view);

    String output = outContent.toString();
    assertTrue(output.contains("No events on"));
    assertTrue(output.contains("2025-05-01"));
  }

  @Test
  public void testPrintAllDayEvent() {
    calendar.createSingleEvent(
        "Meeting",
        "2025-05-01",
        null,
        "Team meeting",
        null,
        "public",
        null
    );
    new PrintCommand("print events on 2025-05-01", calendar, view);

    String output = outContent.toString();
    assertTrue(output.contains("Meeting"));
    assertTrue(output.contains("08:00"));
    assertTrue(output.contains("17:00"));
  }

  @Test
  public void testPrintInvalid() {
    new PrintCommand("print events on 20250501", calendar, view);

    String errorOutput = errContent.toString();
    assertTrue(errorOutput.contains("Error"));
  }

  @Test
  public void testPrintEventsBetween1() {
    calendar.createSingleEvent(
        "Meeting",
        "2025-05-01T10:00",
        "2025-05-01T11:00",
        null,
        "Room A",
        "public",
        null
    );
    calendar.createSingleEvent(
        "Lecture",
        "2025-05-02T14:00",
        "2025-05-02T16:00",
        null,
        "Room B",
        "public",
        null
    );
    new PrintCommand("print events from 2025-05-01T09:00 to 2025-05-02T17:00", calendar, view);

    String output = outContent.toString();
    assertTrue(output.contains("Events from"));
    assertTrue(output.contains("Meeting"));
    assertTrue(output.contains("Lecture"));
    assertTrue(output.contains("2025-05-01"));
    assertTrue(output.contains("2025-05-02"));
  }

  @Test
  public void testPrintEventsBetween2() {
    calendar.createSingleEvent(
        "Long Meeting",
        "2025-05-01T10:00",
        "2025-05-01T15:00",
        null,
        null,
        "public",
        null
    );

    new PrintCommand("print events from 2025-05-01T12:00 to 2025-05-01T13:00", calendar, view);

    String output = outContent.toString();
    assertTrue(output.contains("Long Meeting"));
  }

  @Test
  public void testPrintEventsMultiDays() {
    calendar.createSingleEvent(
        "Long meeting",
        "2025-05-01T09:00",
        "2025-05-03T17:00",
        null,
        "Convention Center",
        "public",
        null
    );

    new PrintCommand("print events from 2025-05-01T00:00 to 2025-05-04T00:00", calendar, view);

    String output = outContent.toString();
    assertTrue(output.contains("Long meeting"));
  }

  @Test
  public void testShowStatus1() {
    calendar.createSingleEvent(
        "Meeting",
        "2025-05-01T10:00",
        "2025-05-01T11:00",
        null,
        null,
        "public",
        null
    );

    new ShowCommand("show status on 2025-05-01T10:30", calendar, view);

    String output = outContent.toString();
    assertTrue(output.contains("busy"));
  }

  @Test
  public void testShowStatus2() {
    calendar.createSingleEvent(
        "Meeting",
        "2025-05-01T10:00",
        "2025-05-01T11:00",
        null,
        null,
        "public",
        null
    );

    new ShowCommand("show status on 2025-05-01T09:00", calendar, view);

    String output = outContent.toString();
    assertTrue(output.contains("available"));
  }
  @Test
  public void testShowStatus_InvalidFormat() {
    new ShowCommand("show status on 202505010900", calendar, view);

    String errorOutput = errContent.toString();
    assertTrue(errorOutput.contains("Error"));
  }

  @Test
  public void testExport() {
    String fileName = "test_export.csv";

    try {
      calendar.createSingleEvent(
          "Meeting",
          "2025-05-01T10:00",
          "2025-05-01T11:00",
          "Team sync",
          "Room A",
          "public",
          null
      );
      new ExportCommand("export cal " + fileName, calendar, view);
      Path filePath = Paths.get(fileName);
      assertTrue(Files.exists(filePath));
      String content = new String(Files.readAllBytes(filePath));
      assertTrue(content.contains("Subject,Start Date,Start Time,End Date,End Time"));
      assertTrue(content.contains("All Day Event,Description,Location,Private"));
      assertTrue(content.contains("Meeting"));
      assertTrue(content.contains("05/01/2025"));
      assertTrue(content.contains("10:00 AM"));
      assertTrue(content.contains("11:00 AM"));
      assertTrue(content.contains("Team sync"));
      assertTrue(content.contains("Room A"));
      assertTrue(content.contains("False"));
      String output = outContent.toString();
      assertTrue(output.contains("Exported to"));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        Files.deleteIfExists(Paths.get(fileName));
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  }

}
