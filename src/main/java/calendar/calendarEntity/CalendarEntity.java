package calendar.calendarEntity;

import calendar.model.Calendar;
import java.time.ZoneId;

public class CalendarEntity implements CalendarEntityInterface {
  private String calendarName;
  private ZoneId zoneId;
  private Calendar calendar;

  CalendarEntity(String calendarName, ZoneId zoneId) {
    this.calendarName = calendarName;
    this.zoneId = zoneId;
    this.calendar = new Calendar();
  }

  @Override
  public String getCalendarName() {
    return calendarName;
  }

  @Override
  public ZoneId getTimeZone() {
    return zoneId;
  }

  @Override
  public Calendar getCalendar() {
    return calendar;
  }

  /**
   * To update the name of the calendar entity.
   *
   * @param calendarName the new name of the calendar entity
   */
  void editCalendarName(String calendarName) {

  }

  /**
   * To update the timezone of the calendar entity.
   *
   * @param zoneId the new timezone of the calendar entity
   */
  void editTimeZone(ZoneId zoneId) {

  }
}
