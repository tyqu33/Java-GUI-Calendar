import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.enums.EventStatus;
import calendar.event.Event;
import calendar.event.EventContext;
import calendar.event.EventSeries;
import calendar.model.Calendar;
import org.junit.Test;

/**
 * Test for Calendar Class.
 */
public class ModelTest {
  @Test
  public void testModelCreateSingleEvent0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);
    assertNotNull(event);
    assertEquals("Meeting", event.getSubject());
    assertEquals("2025-10-28T09:00", event.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", event.getEndDateTime().toString());
    assertEquals("", event.getDescription());
    assertEquals("", event.getLocation());
    assertEquals(EventStatus.PUBLIC, event.getEventStatus());
  }

  @Test
  public void testModelCreateSingleEvent1() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28",
        "", "", "", "");
    Event event = calendar.createSingleEvent(context, null);
    assertNotNull(event);
    assertEquals("Meeting", event.getSubject());
    assertEquals("2025-10-28T08:00", event.getStartDateTime().toString());
    assertEquals("2025-10-28T17:00", event.getEndDateTime().toString());
    assertEquals("", event.getDescription());
    assertEquals("", event.getLocation());
    assertEquals(EventStatus.PUBLIC, event.getEventStatus());
  }

  @Test
  public void testModelCreateSingleEventExp0() {
    Calendar calendar = new Calendar();
    try {
      // Test null subject
      EventContext context = new EventContext(null, "2025-10-28", "", "", "", "");
      Event event0 = calendar.createSingleEvent(context, null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      // Test empty subject
      EventContext context = new EventContext("", "2025-10-28", "", "", "", "");
      Event event0 = calendar.createSingleEvent(context, null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      // Test null start time
      EventContext context = new EventContext("Meeting", null, "", "", "", "");
      Event event1 = calendar.createSingleEvent(context, null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      // Test empty start time
      EventContext context = new EventContext("Meeting", "", "", "", "", "");
      Event event1 = calendar.createSingleEvent(context, null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      // Test invalid start time format
      EventContext context = new EventContext("Meeting", "20251028t0900", "", "", "", "");
      Event event2 = calendar.createSingleEvent(context, null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      // Test invalid end time format
      EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
          "20251028t1700", "", "", "");
      Event event2 = calendar.createSingleEvent(context, null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCreateSingleEventExp1() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event0 = calendar.createSingleEvent(context, null);
    try {
      // Create duplicate event
      Event event1 = calendar.createSingleEvent(context, null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCreateEventSeries0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");
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
  public void testModelCreateEventSeries1() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28",
        "", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");
    assertNotNull(series);
    assertEquals("Meeting", series.getSubject());
    assertEquals("2025-10-28T08:00", series.getStartDateTime().toString());
    assertEquals("2025-10-28T17:00", series.getEndDateTime().toString());
    assertEquals("", series.getDescription());
    assertEquals("", series.getLocation());
    assertEquals(EventStatus.PUBLIC, series.getEventStatus());
    assertEquals("T", series.getWeekdays());
    assertEquals(4, series.getOccurrences().intValue());
    assertEquals("", series.getEndDate() == null ? "" : series.getEndDate().toString());
  }

  @Test
  public void testModelCreateEventSeries2() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28",
        "", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 0, "2025-11-18");
    assertNotNull(series);
    assertEquals("Meeting", series.getSubject());
    assertEquals("2025-10-28T08:00", series.getStartDateTime().toString());
    assertEquals("2025-10-28T17:00", series.getEndDateTime().toString());
    assertEquals("", series.getDescription());
    assertEquals("", series.getLocation());
    assertEquals(EventStatus.PUBLIC, series.getEventStatus());
    assertEquals("T", series.getWeekdays());
    assertEquals(0, series.getOccurrences() == null ? 0 : series.getOccurrences().intValue());
    assertEquals("2025-11-18", series.getEndDate() == null ? "" : series.getEndDate().toString());
  }

  @Test
  public void testModelCreateEventSeriesExp0() {
    Calendar calendar = new Calendar();
    try {
      EventContext context = new EventContext("", "2025-10-28T09:00",
          "2025-10-28T12:00", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    //    try {
    //      EventSeries series = calendar.createEventSeries(null, "T", 4, "");
    //      assert false;
    //    } catch (IllegalArgumentException e) {
    //      assertTrue(true);
    //    }
    try {
      EventContext context = new EventContext("Meeting", "",
          "2025-10-28T12:00", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext context = new EventContext("Meeting", null,
          "2025-10-28T12:00", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext context = new EventContext("Meeting",
          "20251028t0900", "2025-10-28T12:00", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext context = new EventContext("Meeting",
          "2025-10-28T09:00", "20251028t1700", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext context = new EventContext("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 0, "20251118");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext context = new EventContext("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 0, "2025");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext context = new EventContext("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "");
      EventSeries series = calendar.createEventSeries(context, "T", 0, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditSingleEvent0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("Presentation", null, null, "", "", "");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Presentation", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("", updatedEvent.getDescription());
    assertEquals("", updatedEvent.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent1() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event1 = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("", "2025-10-28T09:59", null, "", "", "");
    Event updatedEvent1 = calendar.editSingleEvent("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent1);
    assertEquals("Meeting", updatedEvent1.getSubject());
    assertEquals("2025-10-28T09:59", updatedEvent1.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent1.getEndDateTime().toString());
    assertEquals("", updatedEvent1.getDescription());
    assertEquals("", updatedEvent1.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent1.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent2() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("", "", "2025-10-28T12:59", "", "", "");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Meeting", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:59", updatedEvent.getEndDateTime().toString());
    assertEquals("", updatedEvent.getDescription());
    assertEquals("", updatedEvent.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent3() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("", "", "", "Long Long Meeting", "", "");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Meeting", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("Long Long Meeting", updatedEvent.getDescription());
    assertEquals("", updatedEvent.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent4() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext(null, "", "", "Long Long Meeting", "", "");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Meeting", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("Long Long Meeting", updatedEvent.getDescription());
    assertEquals("", updatedEvent.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent5() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("", "", "", "", "Boston", "");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Meeting", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("", updatedEvent.getDescription());
    assertEquals("Boston", updatedEvent.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent6() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("", null, "", "", "Boston", "");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Meeting", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("", updatedEvent.getDescription());
    assertEquals("Boston", updatedEvent.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent7() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("", "", "", "", "", "private");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Meeting", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("", updatedEvent.getDescription());
    assertEquals("", updatedEvent.getLocation());
    assertEquals(EventStatus.PRIVATE, updatedEvent.getEventStatus());
  }

  @Test
  public void testModelEditSingleEvent8() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    EventContext newContext = new EventContext("", "", null, "", "", "private");
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", newContext);
    assertNotNull(updatedEvent);
    assertEquals("Meeting", updatedEvent.getSubject());
    assertEquals("2025-10-28T09:00", updatedEvent.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedEvent.getEndDateTime().toString());
    assertEquals("", updatedEvent.getDescription());
    assertEquals("", updatedEvent.getLocation());
    assertEquals(EventStatus.PRIVATE, updatedEvent.getEventStatus());
  }


  @Test
  public void testModelEditSingleEventExp0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    // Shared newContext for reuse in invalid lookups (since the context itself is valid here)
    EventContext newContext = new EventContext("Presentation", null, null, "", "", "");

    try {
      Event updatedEvent = calendar.editSingleEvent("", "2025-10-28T09:00",
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent(null, "2025-10-28T09:00",
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting", "",
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting", null,
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting", "20251028t0900",
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "20251028t1200", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditSingleEventExp1() {
    Calendar calendar = new Calendar();
    EventContext newContext = new EventContext("Presentation", null, null, "", "", "");
    try {
      Event updatedEvent = calendar.editSingleEvent("", "2025-10-28T09:00",
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditSingleEventExp2() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event = calendar.createSingleEvent(context, null);

    try {
      EventContext invalidContext = new EventContext("", "20251028t0959", "", "", "", "");
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", invalidContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext invalidContext = new EventContext("", "", "20251028t1259", "", "", "");
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", invalidContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext invalidContext = new EventContext("", "", "", "", "", "Invalid Status");
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", invalidContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditEventSeries0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("Presentation", null, null, "", "", "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedSeries);
    assertEquals("Presentation", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:00", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedSeries.getEndDateTime().toString());
    assertEquals("", updatedSeries.getDescription());
    assertEquals("", updatedSeries.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedSeries.getEventStatus());
  }

  @Test
  public void testModelEditEventSeries1() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("", "2025-10-28T09:59", null, "", "", "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedSeries);
    assertEquals("Meeting", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:59", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedSeries.getEndDateTime().toString());
    assertEquals("", updatedSeries.getDescription());
    assertEquals("", updatedSeries.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedSeries.getEventStatus());

  }

  @Test
  public void testModelEditEventSeries2() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("", "", "2025-10-28T12:59", "", "", "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedSeries);
    assertEquals("Meeting", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:00", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:59", updatedSeries.getEndDateTime().toString());
    assertEquals("", updatedSeries.getDescription());
    assertEquals("", updatedSeries.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedSeries.getEventStatus());
  }

  @Test
  public void testModelEditEventSeries3() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("", "", "", "Long Long Meeting", "", "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedSeries);
    assertEquals("Meeting", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:00", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedSeries.getEndDateTime().toString());
    assertEquals("Long Long Meeting", updatedSeries.getDescription());
    assertEquals("", updatedSeries.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedSeries.getEventStatus());
  }

  @Test
  public void testModelEditEventSeries4() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("", "", "", "", "Boston", "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedSeries);
    assertEquals("Meeting", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:00", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedSeries.getEndDateTime().toString());
    assertEquals("", updatedSeries.getDescription());
    assertEquals("Boston", updatedSeries.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedSeries.getEventStatus());
  }

  @Test
  public void testModelEditEventSeries5() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("", "", "", "", "", "private");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedSeries);
    assertEquals("Meeting", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:00", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedSeries.getEndDateTime().toString());
    assertEquals("", updatedSeries.getDescription());
    assertEquals("", updatedSeries.getLocation());
    assertEquals(EventStatus.PRIVATE, updatedSeries.getEventStatus());
  }

  @Test
  public void testModelEditEventSeries7() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("", "", "", "", "", "public");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNotNull(updatedSeries);
    assertEquals("Meeting", updatedSeries.getSubject());
    assertEquals("2025-10-28T09:00", updatedSeries.getStartDateTime().toString());
    assertEquals("2025-10-28T12:00", updatedSeries.getEndDateTime().toString());
    assertEquals("", updatedSeries.getDescription());
    assertEquals("", updatedSeries.getLocation());
    assertEquals(EventStatus.PUBLIC, updatedSeries.getEventStatus());
  }

  @Test
  public void testModelEditEventSeries6() {
    Calendar calendar = new Calendar();
    EventContext context0 = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event0 = calendar.createSingleEvent(context0, null);

    EventContext context1 = new EventContext("Meeting", "2025-11-04T09:00",
        "2025-11-04T12:00", "", "", "");
    Event event1 = calendar.createSingleEvent(context1, null);

    EventContext newContext = new EventContext("No Meeting", "", "", "", "", "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", newContext);
    assertNull(updatedSeries);
  }


  @Test
  public void testModelEditEventSeriesExp0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext newContext = new EventContext("Presentation", null, null, "", "", "");

    try {
      EventSeries updatedSeries = calendar.editEventSeries("",
          "2025-10-28T09:00", "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries(null,
          "2025-10-28T09:00", "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting", "",
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting", null,
          "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "20251028t0900", "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "20251028t1200", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "20251028t1200", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    // Test invalid values inside newContext
    try {
      EventContext invalidContext = new EventContext("", "20251028t0959", null, "", "", "");
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", invalidContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventContext invalidContext = new EventContext("", "", "20251028t1259", "", "", "");
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", invalidContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditEventSeriesExp1() {
    Calendar calendar = new Calendar();
    EventContext newContext = new EventContext("Presentation", null, null, "", "", "");
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", newContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditEventSeriesExp2() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    EventSeries series = calendar.createEventSeries(context, "T", 4, "");

    EventContext invalidContext = new EventContext("", "", "", "", "", "Invalid Status");
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", invalidContext);
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid event series status: Invalid Status", e.getMessage());
    }
  }

  @Test
  public void testModelGetEvent0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event0 = calendar.createSingleEvent(context, null);
    Event event1 = calendar.getSingleEvent("Meeting", "2025-10-28T09:00", "2025-10-28T12:00");
    assertNotNull(event0);
    assertNotNull(event1);
    assertEquals(event1.getSubject(), event0.getSubject());
    assertEquals(event1.getStartDateTime().toString(), event0.getStartDateTime().toString());
    assertEquals(event1.getEndDateTime().toString(), event0.getEndDateTime().toString());
    assertEquals(event1.getDescription(), event0.getDescription());
    assertEquals(event1.getLocation(), event0.getLocation());
    assertEquals(event1.getEventStatus(), event0.getEventStatus());
  }

  @Test
  public void testModelGetEventExp0() {
    Calendar calendar = new Calendar();
    EventContext context = new EventContext("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "");
    Event event0 = calendar.createSingleEvent(context, null);
    try {
      Event event1 = calendar.getSingleEvent("", "2025-10-28T09:00", "2025-10-28T12:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event1 = calendar.getSingleEvent(null, "2025-10-28T09:00", "2025-10-28T12:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event1 = calendar.getSingleEvent("Meeting", null, "2025-10-28T12:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event1 = calendar.getSingleEvent("Meeting", "", "2025-10-28T12:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event1 = calendar.getSingleEvent("Meeting", "20251028t0900", "2025-10-28T12:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event1 = calendar.getSingleEvent("Meeting", "2025-10-28T09:00", "20251028t1200");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
}