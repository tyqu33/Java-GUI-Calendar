package calendar.model;

import calendar.calendarEntity.CalendarEntity;
import calendar.calendarEntity.CalendarEntityInterface;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements interface MultiCalendarManagerInterface and offers create, edit a calendar
 * and copy its event(s) to another calendar.
 */
public class MultiCalendarManager implements MultiCalendarManagerInterface {
  private Map<String, CalendarEntityInterface> calendarManager;
  private Calendar calendar;

  /**
   * Constructor for MultiCalendarManager.
   */
  public MultiCalendarManager() {
    this.calendarManager = new HashMap<>();
    this.calendar = null;
  }

  @Override
  public CalendarEntityInterface createCalendar(String calendarName, String timeZone)
      throws IllegalArgumentException {
    if (calendarName == null || calendarName.isEmpty() || timeZone == null || timeZone.isEmpty()) {
      throw new IllegalArgumentException("calendar name and timezone cannot be empty");
    }
    ZoneId zoneId = null;
    try {
      zoneId = ZoneId.of(timeZone);
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("Invalid time zone: " + timeZone);
    }
    if (this.calendarManager.containsKey(calendarName)) {
      throw new IllegalArgumentException("Calendar with name already exists: " + calendarName);
    }
    Calendar calendar = new Calendar();
    CalendarEntityInterface entity = new CalendarEntity.CalendarEntityBuilder()
        .calendarName(calendarName)
        .timezone(zoneId)
        .calendar(calendar)
        .build();
    this.calendarManager.put(calendarName, entity);
    return entity;
  }

  @Override
  public void editCalendar(String calendarName, String property, String propertyValue) {
    if (calendarName == null || calendarName.isEmpty()) {
      throw new IllegalArgumentException("calendar name cannot be empty");
    }
    if (property == null || property.isEmpty() || propertyValue == null
        || propertyValue.isEmpty()) {
      throw new IllegalArgumentException("property to be updated and value cannot be empty");
    }
    if (!this.calendarManager.containsKey(calendarName)) {
      throw new IllegalArgumentException("Calendar with name does not exist: " + calendarName);
    }
    if (property.equals("name")) {
      CalendarEntityInterface oldEntity = this.calendarManager.get(calendarName);

      CalendarEntityInterface newEntity = new CalendarEntity.CalendarEntityBuilder()
          .calendarName(propertyValue.trim())
          .timezone(oldEntity.getTimeZone())
          .calendar(oldEntity.getCalendar())
          .build();
      this.calendarManager.put(propertyValue.trim(), newEntity);
      this.calendarManager.remove(calendarName);

    } else if (property.equals("timezone")) {
      ZoneId newZoneId = null;
      try {
        newZoneId = ZoneId.of(propertyValue.trim());
      } catch (DateTimeException e) {
        throw new IllegalArgumentException("Invalid time zone: " + propertyValue);
      }
      CalendarEntityInterface oldEntity = this.calendarManager.get(calendarName);

      CalendarEntityInterface newEntity = new CalendarEntity.CalendarEntityBuilder()
          .calendarName(calendarName)
          .timezone(newZoneId)
          .calendar(oldEntity.getCalendar())
          .build();

      this.calendarManager.replace(calendarName, newEntity);

    } else {
      throw new IllegalArgumentException("Invalid property input: " + property);
    }

  }

  @Override
  public CalendarEntityInterface getCalendar(String calendarName) {
    if (this.calendarManager.containsKey(calendarName.trim())) {
      return this.calendarManager.get(calendarName.trim());
    }
    return null;
  }

  @Override
  public ZoneId getCalendarTimeZone(String calendarName) {
    if (!this.calendarManager.containsKey(calendarName.trim())) {
      throw new IllegalArgumentException("Calendar with name does not exist: " + calendarName);
    }
    return this.calendarManager.get(calendarName.trim()).getTimeZone();
  }

  @Override
  public void copyEvent(String subject, String startDateTime, String targetCalendarName,
                        String targetDateTime) {

  }

  @Override
  public void copyEventsOnThatDay(String specificDate, String targetCalendarName,
                                  String targetDay) {

  }

  @Override
  public void copyEventsBetweenDays(String startDate, String endDate, String targetCalendarName,
                                    String targetDay) {

  }
}
