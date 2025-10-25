import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import calendar.event.Event;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Test;

public class EventTest {
  private final LocalDateTime start = LocalDateTime.of(2025, Month.OCTOBER, 25, 10, 0);
  private final LocalDateTime end = LocalDateTime.of(2025, Month.OCTOBER, 25, 11, 30);

  @Test
  public void testEventCreation() {
    Event event = Event.builder("Meeting", start)
        .end(end)
        .location("Office")
        .description("Project review")
        .status("private")
        .build();

    assertNotNull(event);
    assertEquals("Meeting", event.getSubject());
    assertEquals(start, event.getStartDateTime());
    assertEquals(end, event.getEndDateTime());
    assertEquals("Office", event.getLocation());
    assertEquals("Project review", event.getDescription());
    assertEquals("private", event.getEventStatus());
  }
}
