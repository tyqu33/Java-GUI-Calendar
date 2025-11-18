import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import calendar.enums.EventStatus;
import calendar.enums.UserStatus;
import calendar.event.Event;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Test;

/**
 * Test class for Event.
 */
public class EventTest {
  @Test
  public void testEventCreation() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-01T11:00");

    Event event = Event.builder("Meeting", start)
        .end(end)
        .location("Office")
        .description("Project meeting")
        .status("private")
        .build();

    assertNotNull(event);
    assertEquals("Meeting", event.getSubject());
    assertEquals(start, event.getStartDateTime());
    assertEquals(end, event.getEndDateTime());
    assertEquals("Office", event.getLocation());
    assertEquals("Project meeting", event.getDescription());
    assertEquals(EventStatus.PRIVATE, event.getEventStatus());
  }

  @Test
  public void testCreateAllDayEvent() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T08:00");

    Event event = Event.builder("Meeting", start)
        .setAllDayEvent()
        .build();

    assertTrue(event.isAllDayEvent());
    assertEquals(8, event.getStartDateTime().getHour());
    assertEquals(17, event.getEndDateTime().getHour());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEndBeforeStart() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-01T09:00");

    Event.builder("Invalid", start)
        .end(end)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullSubject() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    Event.builder(null, start).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNullStart() {
    Event.builder("Meeting", null).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateEmptySubject() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    Event.builder("   ", start).build();
  }

  @Test
  public void testEventInequality() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-01T11:00");

    Event event1 = Event.builder("Meeting", start).end(end).build();
    Event event2 = Event.builder("Lunch", start).end(end).build();

    assertNotEquals(event1, event2);
  }

  @Test
  public void testEditDescription() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    Event event = Event.builder("Meeting", start)
        .description("Old description")
        .build();

    event.editDescription("New description");
    assertEquals("New description", event.getDescription());
  }

  @Test
  public void testEditLocation() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    Event event = Event.builder("Meeting", start)
        .location("Room A")
        .build();

    event.editLocation("Room B");
    assertEquals("Room B", event.getLocation());
  }

  @Test
  public void testEditStatus1() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    Event event = Event.builder("Meeting", start)
        .status("public")
        .build();

    event.editEventStatus("private");
    assertEquals("PRIVATE", event.getEventStatus().toString());
  }

  @Test
  public void testEditStatus2() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    Event event = Event.builder("Meeting", start)
        .status("private")
        .build();

    event.editEventStatus("public");
    assertEquals("PUBLIC", event.getEventStatus().toString());
  }

  @Test
  public void testPrivateEvent() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    Event event = Event.builder("Meeting", start)
        .status("private")
        .build();

    assertEquals("PRIVATE", event.getEventStatus().toString());
  }

  @Test
  public void testSpanningMultipleDays() {
    LocalDateTime start = LocalDateTime.parse("2025-05-01T10:00");
    LocalDateTime end = LocalDateTime.parse("2025-05-03T17:00");

    Event event = Event.builder("Conference", start)
        .end(end)
        .build();

    assertEquals(start, event.getStartDateTime());
    assertEquals(end, event.getEndDateTime());
    assertFalse(event.isAllDayEvent());
  }
}
