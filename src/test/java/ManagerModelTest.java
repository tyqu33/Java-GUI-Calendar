import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.calendarEntity.CalendarEntityInterface;
import calendar.model.MultiCalendarManager;
import org.junit.Test;

/**
 * Test for MultiCalendarManager class.
 */
public class ManagerModelTest {

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


}
