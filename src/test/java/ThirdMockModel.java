import calendar.enums.UserStatus;
import calendar.event.Event;
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
   * @param log the record to receive input arguments for commands
   * @param uniqueResult the result of output
   */
  public ThirdMockModel(StringBuilder log, String uniqueResult) {
    this.log = log;
    this.uniqueResult = uniqueResult;
  }

  @Override
  public Event createSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String description, String location, String eventStatus,
                                 String seriesId) throws IllegalArgumentException {
    return null;
  }

  @Override
  public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                       String description, String location, String eventStatus,
                                       String weekdays, int repeatTimes, String seriesEndDateTime)
      throws IllegalArgumentException {
    log.append("create event " + subject
        + " from " + startDateTime + " to " + endDateTime + " repeats " + weekdays
        + " until " + seriesEndDateTime + "\nexit\n");

    return null;
  }

  @Override
  public Event getSingleEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException {
    return null;
  }

  @Override
  public Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                               String newSubject, String newStartDateTime, String newEndDateTime,
                               String newDescription, String newLocation, String newEventStatus)
      throws IllegalArgumentException {
    log.append("edit event location " + subject
        + " from " + startDateTime + " to " + endDateTime + " with " + newLocation);
    return null;
  }

  @Override
  public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                     String newSubject, String newStartDateTime,
                                     String newEndDateTime, String newDescription,
                                     String newLocation, String newEventStatus)
      throws IllegalArgumentException {
    log.append("edit events start " + subject
        + " from " + startDateTime + " with " + newStartDateTime
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
  public String exportToICal(String calendarName, ZoneId timezone) {
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
