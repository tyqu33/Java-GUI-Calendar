import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.enums.EventStatus;
import calendar.event.Event;
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28",
        "", "", "", "", null);
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
      Event event0 = calendar.createSingleEvent(null, "2025-10-28",
          "", "", "", "", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event0 = calendar.createSingleEvent("", "2025-10-28",
          "", "", "", "", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event1 = calendar.createSingleEvent("Meeting", null,
          "", "", "", "", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event1 = calendar.createSingleEvent("Meeting", "",
          "", "", "", "", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event2 = calendar.createSingleEvent("Meeting", "20251028t0900",
          "", "", "", "", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event event2 = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
          "20251028t1700", "", "", "", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCreateSingleEventExp1() {
    Calendar calendar = new Calendar();
    Event event0 = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    try {
      Event event1 = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
          "2025-10-28T12:00", "", "", "", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCreateEventSeries0() {
    Calendar calendar = new Calendar();
    EventSeries
        series = calendar.createEventSeries("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", "T", 4, "");
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
    EventSeries
        series = calendar.createEventSeries("Meeting", "2025-10-28",
        "", "", "", "", "T", 4, "");
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
    EventSeries
        series = calendar.createEventSeries("Meeting", "2025-10-28",
        "", "", "", "", "T", 0, "2025-11-18");
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
      EventSeries series = calendar.createEventSeries("", "2025-10-28T09:00",
          "2025-10-28T12:00", "", "", "", "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries(null, "2025-10-28T09:00",
          "2025-10-28T12:00", "", "", "", "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries("Meeting", "",
          "2025-10-28T12:00", "", "", "", "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries("Meeting", null,
          "2025-10-28T12:00", "", "", "", "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries("Meeting",
          "20251028t0900", "2025-10-28T12:00", "", "", "", "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries("Meeting",
          "2025-10-28T09:00", "20251028t1700", "", "", "", "T", 4, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 0, "20251118");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 0, "2025");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries series = calendar.createEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 0, "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditSingleEvent0() {
    Calendar calendar = new Calendar();
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "Presentation", null, null, "", "", "");
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
    Event event1 = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent1 = calendar.editSingleEvent("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "2025-10-28T09:59", null, "", "", "");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "2025-10-28T12:59", "", "", "");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", "Long Long Meeting", "", "");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", null, "", "", "Long Long Meeting", "", "");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", "", "Boston", "");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", null, "", "", "Boston", "");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", "", "", "private");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event updatedEvent = calendar.editSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", null, "", "", "private");
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
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    try {
      Event updatedEvent = calendar.editSingleEvent("", "2025-10-28T09:00",
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent(null, "2025-10-28T09:00",
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting", "",
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting", null,
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting", "20251028t0900",
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "20251028t1200", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditSingleEventExp1() {
    Calendar calendar = new Calendar();
    try {
      Event updatedEvent = calendar.editSingleEvent("", "2025-10-28T09:00",
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditSingleEventExp2() {
    Calendar calendar = new Calendar();
    Event event = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "20251028t0959", "", "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "20251028t1259", "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      Event updatedEvent = calendar.editSingleEvent("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "", "", "Invalid Status");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditEventSeries0() {
    Calendar calendar = new Calendar();
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "Presentation", null, null, "", "", "");
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
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "2025-10-28T09:59", null, "", "", "");
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
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "2025-10-28T12:59", "", "", "");
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
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "Long Long Meeting", "", "");
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
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "", "Boston", "");
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
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "", "", "private");
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
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "", "", "public");
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
    Event event0 = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    Event event1 = calendar.createSingleEvent("Meeting", "2025-11-04T09:00",
        "2025-11-04T12:00", "", "", "", null);

    EventSeries updatedSeries = calendar.editEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "No Meeting", "", "", "", "", "");
    assertNull(updatedSeries);
  }


  @Test
  public void testModelEditEventSeriesExp0() {
    Calendar calendar = new Calendar();
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    try {
      EventSeries updatedSeries = calendar.editEventSeries("",
          "2025-10-28T09:00", "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries(null,
          "2025-10-28T09:00", "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting", "",
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting", null,
          "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "20251028t0900", "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "20251028t1200", "Presentation", null, null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "20251028t1200", "Presentation", "", null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "20251028t0959", null, "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "", "", "20251028t1259", "", "", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditEventSeriesExp1() {
    Calendar calendar = new Calendar();
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00", "Presentation", null, null, "", "", "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditEventSeriesExp2() {
    Calendar calendar = new Calendar();
    EventSeries series = calendar.createEventSeries("Meeting",
        "2025-10-28T09:00", "2025-10-28T12:00", "", "", "", "T", 4, "");
    try {
      EventSeries updatedSeries = calendar.editEventSeries("Meeting",
          "2025-10-28T09:00", "2025-10-28T12:00",
          "", "", "", "", "", "Invalid Status");
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals(null,
          "Invalid event series status: Invalid Status", e.getMessage());
    }
  }

  @Test
  public void testModelGetEvent0() {
    Calendar calendar = new Calendar();
    Event event0 = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
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
    Event event0 = calendar.createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
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
