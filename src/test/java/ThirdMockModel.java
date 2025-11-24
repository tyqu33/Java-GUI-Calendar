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
class ThirdMockModel implements CalendarInterface {
  private StringBuilder log;
  private final String uniqueResult;

  /**
   * Constructor for test class ThirdMockModel.
   *
   * @param log          the record to receive input arguments for commands
   * @param uniqueResult the result of output
   */
  public ThirdMockModel(StringBuilder log, String uniqueResult) {
    this.log = log;
    this.uniqueResult = uniqueResult;
  }

  @Override
  public Event createSingleEvent(EventContext context, String seriesId)
      throws IllegalArgumentException {
    return null;
  }

  public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                       String description, String location, String eventStatus,
                                       String weekdays, int repeatTimes, String seriesEndDateTime)
      throws IllegalArgumentException {
    EventContext context =
        new EventContext(subject, startDateTime, endDateTime, description, location, eventStatus);
    createEventSeries(context, weekdays, repeatTimes, seriesEndDateTime);
    return null;
  }

  @Override
  public EventSeries createEventSeries(EventContext context, String weekdays, int repeatTimes,
                                       String seriesEndDateTime) {
    log.append("create event " + context.getSubject()
        + " from " + context.getStartDateTime() + " to " + context.getEndDateTime() + " repeats "
        + weekdays + " until " + seriesEndDateTime + "\nexit\n");
    return null;
  }

  @Override
  public Event getSingleEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException {
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
                               EventContext newContext)
      throws IllegalArgumentException {
    log.append("edit event location " + subject
        + " from " + startDateTime + " to " + endDateTime + " with " + newContext.getLocation());
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
    log.append("edit events start " + subject
        + " from " + startDateTime + " with " + newContext.getStartDateTime()
        + "\nexit\n");
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
