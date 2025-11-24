import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventContext;
import calendar.event.EventSeries;
import calendar.model.CalendarInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

/**
 * This is a mock model to replace the Calendar for testing the controller in isolation.
 */
class MockModel implements CalendarInterface {
  private StringBuilder log;
  private final String uniqueResult;

  /**
   * Constructor for test class MockModel.
   *
   * @param log          the record to receive input arguments for commands
   * @param uniqueResult the result of output
   */
  public MockModel(StringBuilder log, String uniqueResult) {
    this.log = log;
    this.uniqueResult = uniqueResult;
  }

  @Override
  public Event createSingleEvent(EventContext context,
                                 String seriesId) {
    LocalDate start = LocalDate.parse(context.getStartDateTime());
    log.append("create event " + context.getSubject()
        + " on " + start.toString() + "\nprint events on " + start.toString() + "\nexit\n");
    return null;
  }

  public String getEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException {
    log.append("getSingleEvent subject " + subject + " startDateTime " + startDateTime + "\n");
    return this.uniqueResult;
  }

  @Override
  public EventSeries createEventSeries(EventContext context, String weekdays, int repeatTimes,
                                       String seriesEndDateTime) {
    LocalDate start = LocalDate.parse(context.getStartDateTime());
    log.append("create event " + context.getSubject()
        + " on " + start.toString() + " repeats " + weekdays
        + " for " + repeatTimes + " times" + "\nexit\n");
    return null;
  }

  @Override
  public Event getSingleEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException {
    log.append("getSingleEvent subject " + subject + " startDateTime " + startDateTime + "\n");
    return null;
  }

  public Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                               String newSubject, String newStartDateTime, String newEndDateTime,
                               String newDescription, String newLocation, String newEventStatus)
      throws IllegalArgumentException {
    EventContext newContext =
        new EventContext(newSubject, newStartDateTime, newEndDateTime, newDescription, newLocation,
            newEventStatus);
    editSingleEvent(subject, startDateTime, endDateTime, newContext);
    return null;
  }

  @Override
  public Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                               EventContext newContext) {
    log.append("edit event subject " + subject
        + " from " + startDateTime + " to " + endDateTime + " with " + newContext.getSubject()
        + "\nprint events on " + startDateTime.substring(0, 10) + "\nexit\n");
    return null;
  }

  public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                     String newSubject, String newStartDateTime,
                                     String newEndDateTime, String newDescription,
                                     String newLocation, String newEventStatus)
      throws IllegalArgumentException {
    EventContext newContext =
        new EventContext(newSubject, newStartDateTime, newEndDateTime, newDescription, newLocation,
            newEventStatus);
    editEventSeries(subject, startDateTime, endDateTime, newContext);
    return null;
  }

  @Override
  public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                     EventContext newContext)
      throws IllegalArgumentException {
    log.append("edit series description " + subject
        + " from " + startDateTime + " with " + newContext.getDescription()
        + "\nprint events on " + startDateTime.substring(0, 10) + "\nexit\n");
    return null;
  }

  @Override
  public List<Event> getEventsOnDate(LocalDate date) {
    return List.of();
  }

  @Override
  public List<Event> getEventsBetween(LocalDateTime start, LocalDateTime end) {
    return List.of();
  }

  @Override
  public UserStatus getUserStatus(LocalDateTime queryTime) {
    return null;
  }

  @Override
  public String exportToCsv() {
    return "";
  }

  @Override
  public String exportToIcal(String calendarName, ZoneId timezone) {
    return "";
  }

  @Override
  public Collection<Event> getEvents() {
    return List.of();
  }

  @Override
  public EventSeries getEventSeries(String seriesId) {
    return null;
  }
}