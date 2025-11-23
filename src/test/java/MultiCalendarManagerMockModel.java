import calendar.calendarentity.CalendarEntityInterface;
import calendar.event.EventDecorator;
import calendar.event.EventInterface;
import calendar.model.MultiCalendarManagerInterface;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * This is a mock model to replace the MultiCalendarManager for testing the controller in isolation.
 */
public class MultiCalendarManagerMockModel implements MultiCalendarManagerInterface {

  private CalendarEntityInterface mockEntity;

  /**
   * Constructor for test class MultiCalendarManagerMockModel.
   */
  public MultiCalendarManagerMockModel() {
    this.mockEntity = null;
  }

  @Override
  public CalendarEntityInterface createCalendar(String calendarName, String timeZone) {
    return this.mockEntity;
  }

  @Override
  public CalendarEntityInterface editCalendar(String calendarName, String property,
                                              String propertyValue) {
    return this.mockEntity;
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

  @Override
  public Collection<CalendarEntityInterface> getAllCalendars() {
    return List.of();
  }

  @Override
  public Collection<EventDecorator> getEventsAcrossCalendar(String keyword) {
    return null;
  }
}
