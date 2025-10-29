import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.event.EventSeries;
import calendar.model.EventKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.Test;

public class EventSeriesTest {

  @Test
  public void testCreateSeries1() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-05T11:00");

    EventSeries series = EventSeries.builder("Lecture", start, "MW")
        .end(end)
        .occurrences(6)
        .build();

    Set<EventKey> keys = series.getSeriesKeys();
    assertEquals(6, keys.size());
    assertEquals("MW", series.getWeekdays());
    assertEquals(Integer.valueOf(6), series.getOccurrences());
  }

  @Test
  public void testCreateSeries2() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-05T11:00");
    LocalDate endDate = LocalDate.parse("2025-05-19");

    EventSeries series = EventSeries.builder("Lecture", start, "MW")
        .end(end)
        .setEndDate(endDate)
        .build();

    Set<EventKey> keys = series.getSeriesKeys();
    assertFalse(keys.isEmpty());
    assertEquals(endDate, series.getEndDate());
  }

  @Test
  public void testSeriesAllDayEvents() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T00:00");

    EventSeries series = EventSeries.builder("Meeting", start, "MTWRF")
        .setAllDay()
        .occurrences(5)
        .build();

    assertTrue(series.isAllDay());
    Set<EventKey> keys = series.getSeriesKeys();

    for (EventKey key : keys) {
      assertEquals(8, key.getStartDateTime().getHour());
      assertEquals(17, key.getEndDateTime().getHour());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidWeekdays() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    EventSeries.builder("Test", start, "XYZ").build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidBuild() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-05T11:00");

    EventSeries.builder("Test", start, "M")
        .end(end)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEndBeforeStart() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-05T09:00");

    EventSeries.builder("Test", start, "M")
        .end(end)
        .occurrences(1)
        .build();
  }

  @Test
  public void testWeekdaysParsing() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00"); // Monday
    LocalDateTime end = LocalDateTime.parse("2025-05-05T11:00");

    EventSeries series = EventSeries.builder("Test", start, "MRF")
        .end(end)
        .occurrences(6)
        .build();

    Set<EventKey> keys = series.getSeriesKeys();
    assertEquals(6, keys.size());

    for (EventKey key : keys) {
      int dayOfWeek = key.getStartDateTime().getDayOfWeek().getValue();
      assertTrue(dayOfWeek == 1 || dayOfWeek == 4 || dayOfWeek == 5);
    }
  }

  @Test
  public void testMarkDateRemoved() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-05T11:00");

    EventSeries series = EventSeries.builder("Test", start, "M")
        .end(end)
        .occurrences(3)
        .build();

    LocalDate dateToRemove = LocalDate.parse("2025-05-12");
    series.markDateRemoved(dateToRemove);

    assertTrue(series.getRemovedDates().contains(dateToRemove));
  }

  @Test
  public void testEventKey() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-01T11:00");

    EventKey key1 = new EventKey("Meeting", start, end);
    EventKey key2 = new EventKey("Lunch", start, end);

    assertNotEquals(key1, key2);
  }

  @Test
  public void testNullEndDateTime() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    EventKey key = new EventKey("Meeting", start, null);

    assertNull(key.getEndDateTime());
  }

}
