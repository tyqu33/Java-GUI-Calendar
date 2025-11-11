import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.calendarEntity.CalendarEntity;
import calendar.calendarEntity.CalendarEntityInterface;
import calendar.controller.CalendarController;
import calendar.enums.EventStatus;
import calendar.event.Event;
import calendar.model.MultiCalendarManager;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for MultiCalendarManager class.
 */
public class ManagerModelTest {
  String premise;
  String use;

  /**
   * To set up the context of a calendar.
   */
  @Before
  public void setUp() {
    premise = "create calendar --name Meetings --timezone America/New_York\n";
    use = "use calendar --name Meetings\n";

  }

  @Test
  public void testModelCreateCalendar0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface calendar = manager.createCalendar("Meetings", "America/New_York");
    assertNotNull(calendar);
    assertEquals("Meetings", calendar.getCalendarName());
    assertEquals("America/New_York", calendar.getTimeZone().toString());
  }

  @Test
  public void testModelCreateCalendarExp() {
    MultiCalendarManager manager = new MultiCalendarManager();
    try {
      CalendarEntityInterface calendar = manager.createCalendar(null, "America/New_York");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar = manager.createCalendar("", "America/New_York");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar = manager.createCalendar("Meetings", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar = manager.createCalendar("Meetings", "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar = manager.createCalendar("Meetings", "Middle-earth/Shire");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    CalendarEntityInterface calendar0 = manager.createCalendar("Meetings", "America/New_York");
    try {
      CalendarEntityInterface calendar1 = manager.createCalendar("Meetings", "America/New_York");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelEditCalendar0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface calendar0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface calendar1 = manager.editCalendar("Meetings", "name", "Lectures");
    assertNotNull(calendar1);
    assertEquals("Lectures", calendar1.getCalendarName());
    assertEquals("America/New_York", calendar1.getTimeZone().toString());
  }

  @Test
  public void testModelEditCalendar1() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface calendar0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface calendar1 = manager.editCalendar("Meetings",
        "timezone", "America/Chicago");
    assertNotNull(calendar1);
    assertEquals("Meetings", calendar1.getCalendarName());
    assertEquals("America/Chicago", calendar1.getTimeZone().toString());
  }

  @Test
  public void testModelEditCalendarExp() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface calendar0 = manager.createCalendar("Meetings", "America/New_York");
    try {
      CalendarEntityInterface calendar1 = manager.editCalendar(null, "name", "Lectures");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar1 = manager.editCalendar("", "name", "Lectures");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar1 = manager.editCalendar("Meetings", null, "Lectures");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar1 = manager.editCalendar("Meetings", "", "Lectures");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar1 = manager.editCalendar("Meetings", "name", null);
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar1 = manager.editCalendar("Meetings", "name", "");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      CalendarEntityInterface calendar1 = manager.editCalendar("Lectures", "name", "Presentation");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      CalendarEntityInterface calendar3 = manager.editCalendar("Meetings",
          "timezone", "Middle-earth/Shire");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      CalendarEntityInterface calendar3 = manager.editCalendar("Meetings",
          "location", "Middle-earth/Shire");
      assert false;
    }  catch (IllegalArgumentException e) {
      assertTrue(true);
    }

  }

  @Test
  public void testModelUseCalendar0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    assertNull(manager.getCalendarEntity("Meetings"));
    CalendarEntityInterface calendar = manager.createCalendar("Meetings", "America/New_York");
    assertNotNull(calendar);
    manager.useThisCalendarEntity(calendar);
    assertNotNull(manager.getCalendarEntity("Meetings"));
    assertEquals("America/New_York", calendar.getTimeZone().toString());

  }

  @Test
  public void testModelGetTimezone() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface calendar = manager.createCalendar("Meetings", "America/New_York");
    try {
      manager.getCalendarTimeZone("Lectures");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    assertEquals("America/New_York", manager.getCalendarTimeZone("Meetings").toString());
  }

  @Test
  public void testModelCopyEvent0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Chicago");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting2", "2025-10-28T13:00",
        "2025-10-28T15:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T13:00",
        "2025-10-28T17:00", "", "", "", null);
    manager.copyEvent("Meeting", "2025-10-28T09:00", "Lectures", "2025-10-28T09:00");
    assertNotNull(entity1.getCalendar().getEvents());
    for (Event event : entity1.getCalendar().getEvents()) {
      assertNotNull(event);
      assertEquals("Meeting", event.getSubject());
      assertEquals("2025-10-28T09:00", event.getStartDateTime().toString());
      assertEquals("2025-10-28T12:00", event.getEndDateTime().toString());
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }
  }

  @Test
  public void testModelCopyEventExp0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Chicago");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    try {
      manager.copyEvent(null, "2025-10-28T09:00", "Lectures", "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("", "2025-10-28T09:00", "Lectures", "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", null, "Lectures", "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", "", "Lectures", "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", null, "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", "", "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", "Lectures", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", "Lectures", "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", "Presentations", "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCopyEventExp1() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Chicago");
    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", "Lectures", "2025-10-28T09:00");
      assert false;
    } catch (IllegalArgumentException e) {
      if (e.getMessage().equals("Current calendar does not exist")) {
        assertTrue(true);
      } else {
        assert false;
      }
    }
  }

  @Test
  public void testModelCopyEventExp2() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity1.getCalendar().createSingleEvent("Meeting", "2025-10-28T06:00",
        "2025-10-28T09:00", "", "", "", null);
    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", "Lectures", "2025-10-28T06:00");
      //      for(Event e : manager.getCalendarEntity("Lectures").getCalendar().getEvents()) {
      //        System.out.println(e.getSubject() + " " + e.getStartDateTime() + " " + e.getEndDateTime());
      //      }
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", "2025-10-28T09:00", "Lectures", "20251028t0600");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEvent("Meeting", "20251028t0900", "Lectures", "2025-10-28T06:00");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCopyEventOnThatDay0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T18:00",
        "2025-10-28T20:00", "", "", "", null);
    manager.copyEventsOnThatDay("2025-10-28", "Lectures", "2025-10-28");
    assertNotNull(entity1.getCalendar());
    assertNotNull(entity1.getCalendar().getEvents());
    assertEquals(2, entity1.getCalendar().getEvents().size());
    for (Event event : entity1.getCalendar().getEvents()) {
      assertNotNull(event);
      assertEquals("Meeting", event.getSubject());
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T06:00")
          || event.getStartDateTime().toString().equals("2025-10-28T15:00") );
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T09:00")
          || event.getEndDateTime().toString().equals("2025-10-28T17:00") );
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }
  }


  @Test
  public void testModelCopyEventOnThatDayExp0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T06:00",
        "2025-10-28T09:00", "", "", "", null);
    try {
      manager.copyEventsOnThatDay(null, "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsOnThatDay("", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsOnThatDay("2025-10-28", null, "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsOnThatDay("2025-10-28", "", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsOnThatDay("2025-10-28", "Lectures", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsOnThatDay("2025-10-28", "Lectures", "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      manager.copyEventsOnThatDay("2025-10-28", "Presentations", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCopyEventOnThatDayExp1() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Chicago");
    try {
      manager.copyEventsOnThatDay("2025-10-28", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      if (e.getMessage().equals("Current calendar does not exist")) {
        assertTrue(true);
      } else {
        assert false;
      }
    }
  }

  @Test
  public void testModelCopyEventOnThatDayExp2() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T18:00",
        "2025-10-28T20:00", "", "", "", null);
    entity1.getCalendar().createSingleEvent("Meeting", "2025-10-28T06:00",
        "2025-10-28T09:00", "", "", "", null);
    entity1.getCalendar().createSingleEvent("Meeting", "2025-10-28T15:00",
        "2025-10-28T17:00", "", "", "", null);
    try {
      manager.copyEventsOnThatDay("2025-10-28", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCopyEventsBetweenDays0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T18:00",
        "2025-10-28T20:00", "", "", "", null);
    assertEquals(2, entity0.getCalendar().getEvents().size());


    manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", "2025-10-28");
    assertNotNull(entity1.getCalendar().getEvents());
    manager.useThisCalendarEntity(entity1);
    assertEquals(2, entity1.getCalendar().getEvents().size());
    for (Event event : entity1.getCalendar().getEvents()) {
      assertNotNull(event);
      assertEquals("Meeting", event.getSubject());
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T06:00")
          || event.getStartDateTime().toString().equals("2025-10-28T15:00") );
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T09:00")
          || event.getEndDateTime().toString().equals("2025-10-28T17:00") );
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDays1() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T18:00",
        "2025-10-28T20:00", "", "", "", null);
    entity0.getCalendar().createEventSeries("Business Meeting",
        "2025-10-29T08:00","2025-10-29T10:00",
        "", "", "", "WR", 4, null);
    assertEquals(6, entity0.getCalendar().getEvents().size());

    for (Event event : entity0.getCalendar().getEvents()) {
      assertNotNull(event);
      assertTrue(event.getSubject().equals("Meeting") || event.getSubject().equals("Business Meeting"));
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T09:00")
          || event.getStartDateTime().toString().equals("2025-10-28T18:00")
          || event.getStartDateTime().toString().equals("2025-10-29T08:00")
          || event.getStartDateTime().toString().equals("2025-10-30T08:00")
          || event.getStartDateTime().toString().equals("2025-11-05T08:00")
          || event.getStartDateTime().toString().equals("2025-11-06T08:00"));
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T12:00")
          || event.getEndDateTime().toString().equals("2025-10-28T20:00")
          || event.getEndDateTime().toString().equals("2025-10-29T10:00")
          || event.getEndDateTime().toString().equals("2025-10-30T10:00")
          || event.getEndDateTime().toString().equals("2025-11-05T10:00")
          || event.getEndDateTime().toString().equals("2025-11-06T10:00"));
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }

    manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", "2025-10-28");
    assertNotNull(entity1.getCalendar().getEvents());
    manager.useThisCalendarEntity(entity1);
    System.out.println(entity1.getCalendar().getEvents().size());
    assertEquals(6, entity1.getCalendar().getEvents().size());
    for (Event event : entity1.getCalendar().getEvents()) {
      assertNotNull(event);
      assertTrue(event.getSubject().equals("Meeting") || event.getSubject().equals("Business Meeting"));
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T06:00")
          || event.getStartDateTime().toString().equals("2025-10-28T15:00")
          || event.getStartDateTime().toString().equals("2025-10-29T05:00")
          || event.getStartDateTime().toString().equals("2025-10-30T05:00")
          || event.getStartDateTime().toString().equals("2025-11-05T05:00")
          || event.getStartDateTime().toString().equals("2025-11-06T05:00")
      );
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T09:00")
          || event.getEndDateTime().toString().equals("2025-10-28T17:00")
          || event.getEndDateTime().toString().equals("2025-10-29T07:00")
          || event.getEndDateTime().toString().equals("2025-10-30T07:00")
          || event.getEndDateTime().toString().equals("2025-11-05T07:00")
          || event.getEndDateTime().toString().equals("2025-11-06T07:00")
      );
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDays2() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T18:00",
        "2025-10-28T20:00", "", "", "", null);
    entity0.getCalendar().createEventSeries("Business Meeting",
        "2025-10-27T08:00","2025-10-27T10:00",
        "", "", "", "MT", 0, "2025-11-07");

    for (Event event : entity0.getCalendar().getEvents()) {
      assertNotNull(event);
      assertTrue(event.getSubject().equals("Meeting") || event.getSubject().equals("Business Meeting"));
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T09:00")
          || event.getStartDateTime().toString().equals("2025-10-28T18:00")
          || event.getStartDateTime().toString().equals("2025-10-27T08:00")
          || event.getStartDateTime().toString().equals("2025-10-28T08:00")
          || event.getStartDateTime().toString().equals("2025-11-03T08:00")
          || event.getStartDateTime().toString().equals("2025-11-04T08:00"));
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T12:00")
          || event.getEndDateTime().toString().equals("2025-10-28T20:00")
          || event.getEndDateTime().toString().equals("2025-10-27T10:00")
          || event.getEndDateTime().toString().equals("2025-10-28T10:00")
          || event.getEndDateTime().toString().equals("2025-11-03T10:00")
          || event.getEndDateTime().toString().equals("2025-11-04T10:00"));
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }

    manager.copyEventsBetweenDays("2025-10-27", "2025-11-07", "Lectures", "2025-10-27");
    assertNotNull(entity1.getCalendar().getEvents());
    manager.useThisCalendarEntity(entity1);
    for (Event event : entity1.getCalendar().getEvents()) {
      assertNotNull(event);
      assertTrue(event.getSubject().equals("Meeting") || event.getSubject().equals("Business Meeting"));
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T06:00")
          || event.getStartDateTime().toString().equals("2025-10-28T15:00")
          || event.getStartDateTime().toString().equals("2025-10-27T05:00")
          || event.getStartDateTime().toString().equals("2025-10-28T05:00")
          || event.getStartDateTime().toString().equals("2025-11-03T05:00")
          || event.getStartDateTime().toString().equals("2025-11-04T05:00"));
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T09:00")
          || event.getEndDateTime().toString().equals("2025-10-28T17:00")
          || event.getEndDateTime().toString().equals("2025-10-27T07:00")
          || event.getEndDateTime().toString().equals("2025-10-28T07:00")
          || event.getEndDateTime().toString().equals("2025-11-03T07:00")
          || event.getEndDateTime().toString().equals("2025-11-04T07:00"));
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDays3() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T18:00",
        "2025-10-28T20:00", "", "", "", null);
    entity0.getCalendar().createEventSeries("Business Meeting",
        "2025-10-31T08:00","2025-10-31T10:00",
        "", "", "", "FSU", 2, null);

    for (Event event : entity0.getCalendar().getEvents()) {
      assertNotNull(event);
      assertTrue(event.getSubject().equals("Meeting") || event.getSubject().equals("Business Meeting"));
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T09:00")
          || event.getStartDateTime().toString().equals("2025-10-28T18:00")
          || event.getStartDateTime().toString().equals("2025-10-31T08:00")
          || event.getStartDateTime().toString().equals("2025-11-01T08:00")
          || event.getStartDateTime().toString().equals("2025-11-02T08:00")
          || event.getStartDateTime().toString().equals("2025-11-07T08:00")
          || event.getStartDateTime().toString().equals("2025-11-08T08:00")
          || event.getStartDateTime().toString().equals("2025-11-09T08:00"));
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T12:00")
          || event.getEndDateTime().toString().equals("2025-10-28T20:00")
          || event.getEndDateTime().toString().equals("2025-10-31T10:00")
          || event.getEndDateTime().toString().equals("2025-11-01T10:00")
          || event.getEndDateTime().toString().equals("2025-11-02T10:00")
          || event.getEndDateTime().toString().equals("2025-11-07T10:00")
          || event.getEndDateTime().toString().equals("2025-11-08T10:00")
          || event.getEndDateTime().toString().equals("2025-11-09T10:00"));
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }

    manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", "2025-10-28");
    assertNotNull(entity1.getCalendar().getEvents());
    manager.useThisCalendarEntity(entity1);
    for (Event event : entity1.getCalendar().getEvents()) {
      assertNotNull(event);
      assertTrue(event.getSubject().equals("Meeting") || event.getSubject().equals("Business Meeting"));
      assertTrue(event.getStartDateTime().toString().equals("2025-10-28T06:00")
          || event.getStartDateTime().toString().equals("2025-10-28T15:00")
          || event.getStartDateTime().toString().equals("2025-10-31T05:00")
          || event.getStartDateTime().toString().equals("2025-11-01T05:00")
          || event.getStartDateTime().toString().equals("2025-11-02T05:00")
          || event.getStartDateTime().toString().equals("2025-11-07T05:00"));
      assertTrue(event.getEndDateTime().toString().equals("2025-10-28T09:00")
          || event.getEndDateTime().toString().equals("2025-10-28T17:00")
          || event.getEndDateTime().toString().equals("2025-10-31T07:00")
          || event.getEndDateTime().toString().equals("2025-11-01T07:00")
          || event.getEndDateTime().toString().equals("2025-11-02T07:00")
          || event.getEndDateTime().toString().equals("2025-11-07T07:00"));
      assertEquals("", event.getDescription());
      assertEquals("", event.getLocation());
      assertEquals(EventStatus.PUBLIC, event.getEventStatus());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDaysExp0() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    try {
      manager.copyEventsBetweenDays(null, "2025-11-07", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsBetweenDays("", "2025-11-07", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", null, "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", "", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", null, "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", null);
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", "");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Presentation", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      manager.copyEventsBetweenDays("2025--10--28", "2025-11-07", "Meetings", "2025-10-31");
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format: 2025--10--28", e.getMessage());
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", "20251107", "Meetings", "2025-10-31");
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format: 20251107", e.getMessage());
    }
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Meetings", "20251031");
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date format: 20251031", e.getMessage());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDaysExp1() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testModelCopyEventsBetweenDaysExp2() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createSingleEvent("Meeting", "2025-10-28T09:00",
        "2025-10-28T12:00", "", "", "", null);
    entity1.getCalendar().createSingleEvent("Meeting", "2025-10-28T06:00",
        "2025-10-28T09:00", "", "", "", null);
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals("Calendar Lectures already has an event with the name Meeting, the start date/time 2025-10-28T06:00, the end date/time 2025-10-28T09:00 existed ", e.getMessage());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDaysExp3() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createEventSeries("Meeting",
        "2025-10-29T08:00","2025-10-29T10:00",
        "", "", "", "WR", 2, null);
    entity1.getCalendar().createEventSeries("Meeting",
        "2025-10-29T05:00","2025-10-29T07:00",
        "", "", "", "WR", 2, null);
    try {
      manager.copyEventsBetweenDays("2025-10-28", "2025-11-07", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals("Calendar Lectures already has an event with the name Meeting in conflict with events to be copied ", e.getMessage());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDaysExp4() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/New_York");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/Los_Angeles");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createEventSeries("Meeting",
        "2025-10-29T01:00","2025-10-29T04:00",
        "", "", "", "WR", 2, null);
    try {
      manager.copyEventsBetweenDays("2025-10-29", "2025-11-06", "Lectures", "2025-10-28");
      assert false;
    } catch (IllegalArgumentException e) {
      assertEquals("New event in a series should not cover more than one day after being copied to the new calendar", e.getMessage());
    }
  }

  @Test
  public void testModelCopyEventsBetweenDaysExp5() {
    MultiCalendarManager manager = new MultiCalendarManager();
    CalendarEntityInterface entity0 = manager.createCalendar("Meetings", "America/Los_Angeles");
    CalendarEntityInterface entity1 = manager.createCalendar("Lectures", "America/New_York");
    assertNotNull(entity0);
    assertNotNull(entity1);
    manager.useThisCalendarEntity(entity0);
    assertNotNull(entity0.getCalendar());
    entity0.getCalendar().createEventSeries("Meeting",
        "2025-10-29T20:00","2025-10-29T23:00",
        "", "", "", "WR", 4, null);
    try {
      manager.copyEventsBetweenDays("2025-10-29", "2025-11-06", "Lectures", "2025-10-29");
      assert false;
    } catch (IllegalArgumentException e) {
      assertTrue(true);
      assertEquals("New event in a series should not cover more than one day after being copied to the new calendar", e.getMessage());
    }
  }





  @Test
  public void testWithoutUseCalendarExp0() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader( "create event Meeting on 2025-10-27\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      String expectedOutput =
          "No current calendar. Please specify which calendar you want to use\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testWithoutUseCalendarExp1() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create event Meeting on 2025-10-27\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "No current calendar. Please specify which calendar you want to use\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyOneEventAcrossDays0() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Meeting from 2025-10-27T01:00 to 2025-10-27T02:00\n"
          + "print events on 2025-10-27\n"
          + "copy event Meeting on 2025-10-27T01:00 --target Lectures to 2025-10-26T22:00\n"
          + "use calendar --name Lectures\n"
          + "print events on 2025-10-26\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events on 2025-10-27:\n • Meeting from 1:00 AM to 2:00 AM\n"
          + "Events on 2025-10-26:\n • Meeting from 10:00 PM to 11:00 PM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyOneEventAcrossDays1() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Meeting from 2025-10-27T01:00 to 2025-10-27T02:00\n"
          + "print events on 2025-10-27\n"
          + "copy event Meeting on 2025-10-27T01:00 --target Lectures to 2025-10-28T13:00\n"
          + "use calendar --name Lectures\n"
          + "print events on 2025-10-28\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events on 2025-10-27:\n • Meeting from 1:00 AM to 2:00 AM\n"
          + "Events on 2025-10-28:\n • Meeting from 1:00 PM to 2:00 PM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyOneEventAcrossDays2() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Meeting from 2025-10-27T01:00 to 2025-10-29T09:00\n"
          + "print events on 2025-10-27\n"
          + "copy event Meeting on 2025-10-27T01:00 --target Lectures to 2025-10-28T17:00\n"
          + "use calendar --name Lectures\n"
          + "print events on 2025-10-28\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events on 2025-10-27:\n • Meeting from 1:00 AM to 9:00 AM\n"
          + "Events on 2025-10-28:\n • Meeting from 5:00 PM to 1:00 AM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyPartOfEventsBetweenDays0() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Meeting from 2025-10-29T09:00 to 2025-10-29T10:00 repeats WR for 4 times\n"
          + "copy events between 2025-10-28 and 2025-11-07 --target Lectures to 2025-10-29\n"
          + "use calendar --name Lectures\n"
          + "print events from 2025-10-29T00:00 to 2025-11-06T23:59\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events from 2025-10-29T00:00 to 2025-11-06T23:59:\n"
          + " • Meeting starting on 2025-10-29 at 6:00 AM, ending on 2025-10-29 at 7:00 AM\n"
          + " • Meeting starting on 2025-10-30 at 6:00 AM, ending on 2025-10-30 at 7:00 AM\n"
          + " • Meeting starting on 2025-11-05 at 6:00 AM, ending on 2025-11-05 at 7:00 AM\n"
          + " • Meeting starting on 2025-11-06 at 6:00 AM, ending on 2025-11-06 at 7:00 AM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyPartOfEventsBetweenDays1() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Chat on 2025-10-28\n"
          + "create event Meeting from 2025-10-29T09:00 to 2025-10-29T10:00 repeats WR for 4 times\n"
          + "copy events between 2025-10-28 and 2025-11-05 --target Lectures to 2025-10-28\n"
          + "use calendar --name Lectures\n"
          + "print events from 2025-10-28T00:00 to 2025-11-06T23:59\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events from 2025-10-28T00:00 to 2025-11-06T23:59:\n"
          + " • Chat starting on 2025-10-28 at 5:00 AM, ending on 2025-10-28 at 2:00 PM\n"
          + " • Meeting starting on 2025-10-29 at 6:00 AM, ending on 2025-10-29 at 7:00 AM\n"
          + " • Meeting starting on 2025-10-30 at 6:00 AM, ending on 2025-10-30 at 7:00 AM\n"
          + " • Meeting starting on 2025-11-05 at 6:00 AM, ending on 2025-11-05 at 7:00 AM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyPartOfEventsBetweenDays2() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Chat on 2025-11-01\n"
          + "create event Meeting from 2025-10-29T09:00 to 2025-10-29T10:00 repeats WR for 4 times\n"
          + "copy events between 2025-10-29 and 2025-11-07 --target Lectures to 2025-10-30\n"
          + "use calendar --name Lectures\n"
          + "print events from 2025-10-28T00:00 to 2025-11-07T23:59\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events from 2025-10-28T00:00 to 2025-11-07T23:59:\n"
          + " • Meeting starting on 2025-10-30 at 6:00 AM, ending on 2025-10-30 at 7:00 AM\n"
          + " • Chat starting on 2025-11-02 at 5:00 AM, ending on 2025-11-02 at 2:00 PM\n"
          + " • Meeting starting on 2025-11-05 at 6:00 AM, ending on 2025-11-05 at 7:00 AM\n"
          + " • Meeting starting on 2025-11-06 at 6:00 AM, ending on 2025-11-06 at 7:00 AM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyPartOfEventsBetweenDays3() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Meeting from 2025-09-05T09:50 to 2025-09-05T11:30 repeats TF for 8 times\n"
          + "copy events between 2025-09-01 and 2025-09-30 --target Lectures to 2026-01-01\n"
          + "use calendar --name Lectures\n"
          + "print events from 2026-01-01T00:00 to 2026-01-31T23:59\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events from 2026-01-01T00:00 to 2026-01-31T23:59:\n"
          + " • Meeting starting on 2026-01-02 at 6:50 AM, ending on 2026-01-02 at 8:30 AM\n"
          + " • Meeting starting on 2026-01-06 at 6:50 AM, ending on 2026-01-06 at 8:30 AM\n"
          + " • Meeting starting on 2026-01-09 at 6:50 AM, ending on 2026-01-09 at 8:30 AM\n"
          + " • Meeting starting on 2026-01-13 at 6:50 AM, ending on 2026-01-13 at 8:30 AM\n"
          + " • Meeting starting on 2026-01-16 at 6:50 AM, ending on 2026-01-16 at 8:30 AM\n"
          + " • Meeting starting on 2026-01-20 at 6:50 AM, ending on 2026-01-20 at 8:30 AM\n"
          + " • Meeting starting on 2026-01-23 at 6:50 AM, ending on 2026-01-23 at 8:30 AM\n"
          + " • Meeting starting on 2026-01-27 at 6:50 AM, ending on 2026-01-27 at 8:30 AM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyPartOfEventsBetweenDays4() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      // The date string in the target calendar corresponds to the start of the interval
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Chat on 2025-11-01\n"
          + "create event Meeting from 2025-10-29T09:00 to 2025-10-29T10:00\n"
          + "create event Presentation from 2025-10-29T10:30 to 2025-10-29T11:30\n"
          + "copy events between 2025-10-29 and 2025-11-02 --target Lectures to 2025-12-01\n"
          + "use calendar --name Lectures\n"
          + "print events from 2025-12-01T00:00 to 2025-12-15T23:59\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events from 2025-12-01T00:00 to 2025-12-15T23:59:\n"
          + " • Meeting starting on 2025-12-01 at 6:00 AM, ending on 2025-12-01 at 7:00 AM\n"
          + " • Presentation starting on 2025-12-01 at 7:30 AM, ending on 2025-12-01 at 8:30 AM\n"
          + " • Chat starting on 2025-12-04 at 5:00 AM, ending on 2025-12-04 at 2:00 PM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testCopyPartOfEventsBetweenDays5() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    MultiCalendarManagerInterface manager = new MultiCalendarManager();
    CalendarView view = new CalendarView();
    try {
      // The date string in the target calendar corresponds to the start of the interval
      Reader in = new StringReader(premise
          + "create calendar --name Lectures --timezone America/Los_Angeles\n"
          + use
          + "create event Chat on 2025-11-01\n"
          + "create event Meeting from 2025-10-29T09:00 to 2025-10-29T10:00\n"
          + "create event Presentation from 2025-10-29T10:30 to 2025-10-29T11:30\n"
          + "copy events between 2025-10-27 and 2025-11-01 --target Lectures to 2025-12-01\n"
          + "use calendar --name Lectures\n"
          + "print events from 2025-12-01T00:00 to 2025-12-15T23:59\nexit\n");
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      System.out.println(allOuts.trim());
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Success: Successfully created calendar 'Lectures'\n"
          + "Events from 2025-12-01T00:00 to 2025-12-15T23:59:\n"
          + " • Meeting starting on 2025-12-03 at 6:00 AM, ending on 2025-12-03 at 7:00 AM\n"
          + " • Presentation starting on 2025-12-03 at 7:30 AM, ending on 2025-12-03 at 8:30 AM\n"
          + " • Chat starting on 2025-12-06 at 5:00 AM, ending on 2025-12-06 at 2:00 PM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

}
