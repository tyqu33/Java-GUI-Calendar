import calendar.calendarentity.CalendarEntity;
import calendar.calendarentity.CalendarEntityInterface;
import calendar.event.EventWrapper;
import calendar.model.MultiCalendarManagerInterface;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

/**
 * This is a mock model to replace the MultiCalendarManager for testing the controller in isolation.
 */
public class GuiMultiCalendarManagerMockModel implements MultiCalendarManagerInterface {
  private StringBuilder log;
  private CalendarEntityInterface entity;

  /**
   * Constructor for test class GuiMultiCalendarManagerMockModel.
   *
   * @param log   the logging
   * @param local the flag means call of this Constructor to make entity null
   */
  public GuiMultiCalendarManagerMockModel(StringBuilder log, int local) {
    this.log = log;
    this.entity = null;
  }

  /**
   * Constructor for test class GuiMultiCalendarManagerMockModel.
   *
   * @param log the logging
   */
  public GuiMultiCalendarManagerMockModel(StringBuilder log) {
    this.log = log;
    ZoneId zoneId = ZoneId.systemDefault();
    this.entity = new CalendarEntity.CalendarEntityBuilder()
        .calendarName("Default").timezone(zoneId).build();
  }

  @Override
  public CalendarEntityInterface createCalendar(String calendarName, String timeZone) {
    log.append("createCalendar: " + calendarName + ", " + timeZone + "\n");

    return null;
  }

  @Override
  public CalendarEntityInterface editCalendar(String calendarName, String property,
                                              String propertyValue) {
    log.append("editCalendar: " + calendarName + ", " + property + ", " + propertyValue + "\n");
    return null;
  }

  @Override
  public CalendarEntityInterface getCurrentCalendarEntity() {
    log.append("getCurrentCalendarEntity\n");
    return this.entity;
  }

  @Override
  public CalendarEntityInterface getCalendarEntity(String calendarName) {
    log.append("getCalendarEntity: " + calendarName + "\n");
    return this.entity;
  }

  @Override
  public ZoneId getCalendarTimeZone(String calendarName) {
    return null;
  }

  @Override
  public void useThisCalendarEntity(CalendarEntityInterface calendarEntity) {
    log.append("useThisCalendarEntity: \n"); // " + calendarEntity.getCalendarName() + "
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
    log.append("getAllCalendars\n");
    return List.of();
  }

  @Override
  public Collection<EventWrapper> getEventsAcrossCalendar(String keyword) {
    log.append("getEventsAcrossCalendar: " + keyword + "\n");
    return List.of();
  }
}
