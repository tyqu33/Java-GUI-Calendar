import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import calendar.enums.EventStatus;
import calendar.event.Event;
import calendar.event.EventSeries;
import calendar.model.Calendar;
import org.junit.Test;

public class ModelTest {
  @Test
  public void testModelCreateSingleEvent() {
    Calendar calendar = new Calendar();
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", null );
    assertNotNull(event);
    assertEquals("Meeting", event.getSubject());
    assertEquals("2025-10-28T09:00", event.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", event.getEndDateTime().toString());
    assertEquals("", event.getDescription());
    assertEquals("", event.getLocation());
    assertEquals(EventStatus.PUBLIC, event.getEventStatus());
  }

  @Test
  public void testModelCreateEventSeries() {
    Calendar calendar = new Calendar();
    EventSeries
        series = calendar.createEventSeries("Meeting", "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "" );
    assertNotNull(series);
    assertEquals("Meeting", series.getSubject());
    assertEquals("2025-10-28T09:00", series.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", series.getEndDateTime().toString());
    assertEquals("", series.getDescription());
    assertEquals("", series.getLocation());
    assertEquals(EventStatus.PUBLIC, series.getEventStatus());
    assertEquals("T", series.getWeekdays());
    assertEquals(4, series.getOccurrences().intValue());
    assertEquals("", series.getEndDate() == null ? "" : series.getEndDate().toString());
  }

  @Test
  public void testModelEditSingleEvent() {
    Calendar calendar = new Calendar();
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", null );
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00", "2025-10-28T12:00", "Presentation", null, null, "", "", "");
    assertNotNull(updatedEvent);
    assertEquals("Presentation", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("", updatedEvent.getDescription());
    assertEquals("", updatedEvent.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditEventSeries() {
    Calendar calendar = new Calendar();
    EventSeries series = calendar.createEventSeries("Meeting", "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "" );
    EventSeries updatedSeries = calendar.editEventSeries("Meeting", "2025-10-28T09:00", "2025-10-28T12:00", "Presentation", null, null, "", "", "");
    assertNotNull(updatedSeries);
    assertEquals("Presentation", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:00", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedSeries.getEndDateTime().toString());
    assertEquals("", updatedSeries.getDescription());
    assertEquals("", updatedSeries.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedSeries.getEventStatus());
  }
}
