import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import calendar.controller.ExportCommand;
import calendar.controller.PrintCommand;
import calendar.controller.ShowCommand;
import calendar.event.Event;
import calendar.event.EventContext;
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
  private LocalDateTime time;
  private EventKey key1;
  private EventKey key2;

  /**
   * Initialize a calendar and view.
   */
  @Before
  public void setUp() {
    calendar = new Calendar();
    time = LocalDateTime.now().withNano(0);
    key1 = new EventKey("Test Subject", time, time.plusHours(1));
    key2 = new EventKey("Test Subject", time, time.plusHours(1));
  }

  @Test
  public void testDummyCalendarName() {
    calendar.DummyCalendar calendar = new calendar.DummyCalendar();
    assertEquals("DummyCalendar", calendar.getName());
  }

  @Test
  public void testCreateEvent() {
    EventContext context = new EventContext(
        "Team Meeting",
        "2025-05-01T10:00",
        "2025-05-01T11:00",
        "Weekly sync meeting",
        "Conference Room A",
        "public"
    );
    Event event = calendar.createSingleEvent(context, null);
    assertNotNull(event);
    assertEquals("Team Meeting", event.getSubject());
    assertEquals(LocalDateTime.parse("2025-05-01T10:00"), event.getStartDateTime());
    assertEquals(LocalDateTime.parse("2025-05-01T11:00"), event.getEndDateTime());
    assertEquals("Weekly sync meeting", event.getDescription());
    assertEquals("Conference Room A", event.getLocation());
  }

  @Test
  public void testCeateEventSeries() {
    EventContext context = new EventContext(
        "Lecture",
        "2025-05-05T10:00",
        "2025-05-05T11:00",
        "Java",
        "Room 101",
        "public"
    );
    EventSeries series = calendar.createEventSeries(context, "MW", 6, "");
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

  @Test
  public void testEquals1() {
    assertEquals(key1, key1);
  }

  @Test
  public void testEquals2() {
    assertNotEquals(null, key1);
  }

  @Test
  public void testEquals3() {
    assertFalse(key1.equals("Not EventKey"));
  }

  @Test
  public void testEquals4() {
    assertEquals(key1, key2);
  }

  @Test
  public void testEquals5() {
    EventKey differentSubject = new EventKey("Other", time, time.plusHours(1));
    assertNotEquals(key1, differentSubject);
  }

  @Test
  public void testEquals6() {
    EventKey differentStart = new EventKey("Test Subject", time.plusMinutes(30), time.plusHours(1));
    assertNotEquals(key1, differentStart);
  }

  @Test
  public void testEquals7() {
    EventKey differentEnd = new EventKey("Test Subject", time, time.plusHours(2));
    assertNotEquals(key1, differentEnd);
  }

  @Test
  public void testEquals8() {
    assertFalse(key1.equals(null));
  }

  @Test
  public void testHashCode1() {
    assertEquals(key1.hashCode(), key2.hashCode());
  }

  @Test
  public void testHashCode2() {
    EventKey differentSubject = new EventKey("Other", time, time.plusHours(1));
    assertTrue(key1.hashCode() != differentSubject.hashCode());
  }

}
