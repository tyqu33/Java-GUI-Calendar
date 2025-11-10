import calendar.calendarEntity.CalendarEntityInterface;
import calendar.model.CalendarInterface;
import java.time.ZoneId;

/**
 * This is a mock model to replace the CalendarEntity for testing the controller in isolation.
 */
public class EntityMockModel implements CalendarEntityInterface {

  private CalendarInterface calendar;

  /**
   * Constructor for test class MultiCalendarManagerMockModel.
   *
   * @param calendar the calendar context
   */
  public void useThisCalendar(CalendarInterface calendar) {
    this.calendar = calendar;
  }

  @Override
  public String getCalendarName() {
    return "";
  }

  @Override
  public ZoneId getTimeZone() {
    return null;
  }

  @Override
  public CalendarInterface getCalendar() {
    return this.calendar;
  }
}
