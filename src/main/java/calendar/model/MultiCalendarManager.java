package calendar.model;

import calendar.calendarEntity.CalendarEntity;
import java.time.ZoneId;

public class MultiCalendarManager implements MultiCalendarManagerInterface {


  @Override
  public CalendarEntity createCalendar(String calendarName, ZoneId zoneId) {
    return null;
  }

  @Override
  public void editCalendar(String calendarName, String property, String propertyValue) {

  }

  @Override
  public CalendarEntity getCalendar(String calendarName) {
    return null;
  }

  @Override
  public ZoneId getCalendarTimeZone(String calendarName) {
    return null;
  }

  @Override
  public void copyEvent(String subject, String startDateTime, String targetCalendarName, String targetDateTime) {

  }

  @Override
  public void copyEventsOnThatDay(String specificDate, String targetCalendarName, String targetDay) {

  }

  @Override
  public void copyEventsBetweenDays(String startDate,  String endDate, String targetCalendarName, String targetDay) {

  }
}
