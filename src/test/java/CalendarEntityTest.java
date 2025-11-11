import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import calendar.calendarentity.CalendarEntity;
import calendar.model.Calendar;
import java.time.ZoneId;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CalendarEntity.
 */
public class CalendarEntityTest {
  private Calendar calendar;

  /**
   * Set up a new Calendar.
   */
  @Before
  public void setUp() {
    calendar = new Calendar();
  }

  @Test
  public void testCreation() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("TestCalendar")
        .timezone(ZoneId.of("America/New_York"))
        .calendar(calendar)
        .build();

    assertNotNull(entity);
    assertEquals("TestCalendar", entity.getCalendarName());
    assertEquals("America/New_York", entity.getTimeZone().toString());
    assertEquals(calendar, entity.getCalendar());
  }

  @Test
  public void testWithoutCalendar() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    assertNotNull(entity);
    assertNotNull(entity.getCalendar());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    CalendarEntity.builder()
        .calendarName(null)
        .timezone(ZoneId.of("America/New_York"))
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyName() {
    CalendarEntity.builder()
        .calendarName("")
        .timezone(ZoneId.of("America/New_York"))
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhitespaceName() {
    CalendarEntity.builder()
        .calendarName("   ")
        .timezone(ZoneId.of("America/New_York"))
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullTimezone() {
    CalendarEntity.builder()
        .calendarName("Test")
        .timezone(null)
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingName() {
    CalendarEntity.builder()
        .timezone(ZoneId.of("America/New_York"))
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingTimezone() {
    CalendarEntity.builder()
        .calendarName("Test")
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCalendar() {
    CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .calendar(null)
        .build();
  }

  @Test
  public void testEquals() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    assertEquals(entity, entity);
  }

  @Test
  public void testSameName() {
    CalendarEntity entity1 = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    CalendarEntity entity2 = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/Chicago"))
        .build();

    assertEquals(entity1, entity2);
  }

  @Test
  public void testDifferentName() {
    CalendarEntity entity1 = CalendarEntity.builder()
        .calendarName("Test1")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    CalendarEntity entity2 = CalendarEntity.builder()
        .calendarName("Test2")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    assertNotEquals(entity1, entity2);
  }

  @Test
  public void testEqualsNull() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    assertFalse(entity.equals(null));
  }

  @Test
  public void testDifferentClass() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    assertFalse(entity.equals("Not a CalendarEntity"));
    assertFalse(entity.equals(new Object()));
    assertFalse(entity.equals(Integer.valueOf(123)));
  }

  @Test
  public void testHashCode() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    int hash1 = entity.hashCode();
    int hash2 = entity.hashCode();

    assertEquals(hash1, hash2);
  }

  @Test
  public void testHashCodeEqual() {
    CalendarEntity entity1 = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    CalendarEntity entity2 = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("America/Chicago"))
        .build();

    assertEquals(entity1.hashCode(), entity2.hashCode());
  }

  @Test
  public void testHashCodeDifferent() {
    CalendarEntity entity1 = CalendarEntity.builder()
        .calendarName("Test1")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    CalendarEntity entity2 = CalendarEntity.builder()
        .calendarName("Test2")
        .timezone(ZoneId.of("America/New_York"))
        .build();

    assertNotEquals(entity1.hashCode(), entity2.hashCode());
  }

  @Test
  public void testToString() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("MyCalendar")
        .timezone(ZoneId.of("America/Los_Angeles"))
        .build();

    String str = entity.toString();

    assertNotNull(str);
    assertTrue("Should contain class name", str.contains("CalendarEntity"));
    assertTrue("Should contain calendar name", str.contains("MyCalendar"));
    assertTrue("Should contain timezone", str.contains("America/Los_Angeles"));
  }

  @Test
  public void testToStringFormat() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("Test")
        .timezone(ZoneId.of("UTC"))
        .build();

    String str = entity.toString();

    assertTrue(str.matches(".*name='.*'.*"));
    assertTrue(str.matches(".*timezone=.*"));
  }

  @Test
  public void testDifferentTimezones() {
    String[] timezones = {
        "America/New_York",
        "Europe/London",
        "Asia/Tokyo",
        "UTC"
    };

    for (String tz : timezones) {
      CalendarEntity entity = CalendarEntity.builder()
          .calendarName("Test")
          .timezone(ZoneId.of(tz))
          .build();

      assertEquals(tz, entity.getTimeZone().toString());
    }
  }

  @Test
  public void testProvidedCalendar() {
    Calendar customCalendar = new Calendar();
    customCalendar.createSingleEvent("Test Event", "2025-11-01T10:00",
        "2025-11-01T11:00", "", "", "public", null);

    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("TestCal")
        .timezone(ZoneId.of("UTC"))
        .calendar(customCalendar)
        .build();

    assertNotNull(entity.getCalendar());
    assertEquals(1, entity.getCalendar().getEvents().size());
  }

  @Test
  public void testBuilderChaining() {
    CalendarEntity entity = CalendarEntity.builder()
        .calendarName("Chain")
        .timezone(ZoneId.of("UTC"))
        .calendar(calendar)
        .build();

    assertNotNull(entity);
    assertEquals("Chain", entity.getCalendarName());
  }
}