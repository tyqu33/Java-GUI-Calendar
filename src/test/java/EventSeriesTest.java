import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import calendar.event.EventSeries;
import calendar.model.EventKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.Test;

/**
 * Test class for EventSeries.
 */
public class EventSeriesTest {
  private final LocalDateTime start = LocalDateTime.of(2025, 10, 30, 10, 0);
  private final LocalDate firstOccurrence = start.toLocalDate();
  private final String weekdays = "MWF";
  private final String subject = "Subject";

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

  @Test
  public void testToBuilder() {
    LocalDateTime start = LocalDateTime.parse("2025-06-02T09:00");
    LocalDateTime end = LocalDateTime.parse("2025-06-02T12:00");
    EventSeries originalSeries = EventSeries.builder("Meeting", start, "MWF")
        .end(end)
        .occurrences(5)
        .description("Meeting")
        .location("Location")
        .status("private")
        .build();
    LocalDate removedDate = LocalDate.parse("2025-06-04");
    originalSeries.markDateRemoved(removedDate);
    originalSeries.generateKeys();
    EventSeries newSeries = originalSeries.toBuilder().build();

    assertEquals(originalSeries.getSeriesId(), newSeries.getSeriesId());
    assertTrue(originalSeries.equals(newSeries));
    assertEquals(originalSeries.getEndDateTime(), newSeries.getEndDateTime());
    assertEquals(originalSeries.getOccurrences(), newSeries.getOccurrences());
    assertNull(newSeries.getEndDate());
    assertEquals(originalSeries.getDescription(), newSeries.getDescription());
    assertEquals(originalSeries.getLocation(), newSeries.getLocation());
    assertEquals(originalSeries.getEventStatus(), newSeries.getEventStatus());
    assertTrue(newSeries.getRemovedDates().contains(removedDate));
    assertEquals(originalSeries.getSeriesKeys(), newSeries.getSeriesKeys());
  }

  @Test
  public void testToBuilderAllDay() {
    LocalDateTime start = LocalDateTime.parse("2025-06-01T08:00");
    LocalDate endDate = LocalDate.parse("2025-06-15");
    EventSeries originalSeries = EventSeries.builder("Subject", start, "U")
        .setAllDay()
        .setEndDate(endDate)
        .status("public")
        .build();
    EventSeries newSeries = originalSeries.toBuilder().build();

    assertEquals(originalSeries.getSeriesId(), newSeries.getSeriesId());
    assertEquals(originalSeries, newSeries);
    assertTrue(newSeries.isAllDay());
    assertEquals(originalSeries.getEndDate(), newSeries.getEndDate());
    assertNull(newSeries.getOccurrences());
    assertEquals(originalSeries.getEventStatus(), newSeries.getEventStatus());
  }

  @Test
  public void testEquals() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-05T11:00");
    EventSeries series1 = EventSeries.builder("Test A", start, "M")
        .end(end)
        .occurrences(1)
        .build();
    EventSeries series2 = series1.toBuilder().build();
    assertEquals(series1, series1);
    assertEquals(series1, series2);
    assertEquals(series2, series1);
    EventSeries series3 = EventSeries.builder("Test A", start, "M")
        .end(end)
        .occurrences(1)
        .build();
    EventSeries series4 = EventSeries.builder("Test B", start, "T")
        .end(end)
        .occurrences(2)
        .build();
    assertNotEquals(series1, series3);
    assertNotEquals(series1, series4);
  }

  @Test
  public void testHashCode() {
    LocalDateTime start = LocalDateTime.parse("2025-05-05T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-05T11:00");
    EventSeries series1 = EventSeries.builder("Test A", start, "M")
        .end(end)
        .occurrences(1)
        .build();
    EventSeries series2 = series1.toBuilder().build();
    EventSeries series3 = EventSeries.builder("Test A", start, "M")
        .end(end)
        .occurrences(1)
        .build();
    EventSeries series4 = EventSeries.builder("Test B", start, "S")
        .end(end)
        .occurrences(2)
        .build();
    assertEquals(series1.hashCode(), series2.hashCode());
    assertNotEquals(series1.hashCode(), series3.hashCode());
    assertNotEquals(series1.hashCode(), series4.hashCode());
  }

  @Test
  public void testConstructorException() {
    assertThrows(IllegalArgumentException.class, () -> {
      EventSeries.builder(null, start, weekdays);
    });
  }

  @Test
  public void testConstructorException2() {
    assertThrows(IllegalArgumentException.class, () -> {
      EventSeries.builder("   ", start, weekdays);
    });
  }

  @Test
  public void testConstructorException3() {
    assertThrows(IllegalArgumentException.class, () -> {
      EventSeries.builder(subject, null, weekdays);
    });
  }

  @Test
  public void testConstructorException4() {
    assertThrows(IllegalArgumentException.class, () -> {
      EventSeries.builder(subject, start, null);
    });
  }

  @Test
  public void testConstructorException5() {
    assertThrows(IllegalArgumentException.class, () -> {
      EventSeries.builder(subject, start, "123");
    });
  }

  @Test
  public void testConstructorException6() {
    assertThrows(IllegalArgumentException.class, () -> {
      EventSeries.builder(subject, start, "  ");
    });
  }

  @Test
  public void testSetEndDateException() {
    EventSeries.EventSeriesBuilder builder = EventSeries.builder(subject, start, weekdays);

    assertThrows(IllegalArgumentException.class, () -> {
      builder.setEndDate(null);
    });
  }

  @Test
  public void testSetEndDateException2() {
    EventSeries.EventSeriesBuilder builder = EventSeries.builder(subject, start, weekdays);
    LocalDate dateBeforeStart = firstOccurrence.minusDays(1);

    assertThrows(IllegalArgumentException.class, () -> {
      builder.setEndDate(dateBeforeStart);
    });
  }

  @Test
  public void testOccurrencesException() {
    EventSeries.EventSeriesBuilder builder = EventSeries.builder(subject, start, weekdays);

    assertThrows(IllegalArgumentException.class, () -> {
      builder.occurrences(0);
    });
  }

  @Test
  public void testOccurrencesException2() {
    EventSeries.EventSeriesBuilder builder = EventSeries.builder(subject, start, weekdays);

    assertThrows(IllegalArgumentException.class, () -> {
      builder.occurrences(-5);
    });
  }

  @Test
  public void testEqualsFalse() {
    LocalDateTime start = LocalDateTime.parse("2025-11-01T10:00");
    EventSeries series1 = EventSeries.builder("Test", start, "M")
        .occurrences(1).setAllDay().build();

    Object differentObject = new Object();

    assertNotEquals(series1, differentObject);
  }
}
