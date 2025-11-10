import calendar.calendarEntity.CalendarEntityInterface;
import calendar.model.MultiCalendarManagerInterface;
import java.time.ZoneId;

public class MultiCalendarManagerMockModel implements MultiCalendarManagerInterface {

  private CalendarEntityInterface mockEntity;

  public MultiCalendarManagerMockModel() {
    this.mockEntity = null;
  }

  @Override
  public CalendarEntityInterface createCalendar(String calendarName, String timeZone) {
    return this.mockEntity;
  }

  @Override
  public void editCalendar(String calendarName, String property, String propertyValue) {

  }

  @Override
  public CalendarEntityInterface getCurrentCalendarEntity() {
    return this.mockEntity;
  }

  @Override
  public CalendarEntityInterface getCalendarEntity(String calendarName) {
    return this.mockEntity;
  }

  @Override
  public ZoneId getCalendarTimeZone(String calendarName) {
    return null;
  }

  @Override
  public void useThisCalendarEntity(CalendarEntityInterface calendarEntity) {
    this.mockEntity = calendarEntity;
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
