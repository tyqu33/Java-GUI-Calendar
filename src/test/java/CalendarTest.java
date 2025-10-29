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

}
