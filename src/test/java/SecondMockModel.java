import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;
import calendar.model.CalendarInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class SecondMockModel implements CalendarInterface {
  private StringBuilder log;
  private final String uniqueResult;

  public SecondMockModel(StringBuilder log, String uniqueResult) {
    this.log = log;
    this.uniqueResult = uniqueResult;
  }

  @Override
  public Event createSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String description, String location, String eventStatus,
                                 String seriesId) throws IllegalArgumentException {
    LocalDateTime start = LocalDateTime.parse(startDateTime);
    LocalDateTime end = LocalDateTime.parse(endDateTime);
    log.append("create event " + subject
        + " from " + start.toString() + " to " + end.toString() + "\nexit\n");
    return null;
  }

  @Override
  public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                       String description, String location, String eventStatus,
                                       String weekdays, int repeatTimes, String seriesEndDateTime)
      throws IllegalArgumentException {
    log.append("create event " + subject
        + " from " + startDateTime + " to " + endDateTime + " repeats " + weekdays + " for " + repeatTimes + " times" + "\nexit\n");
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
    log.append("edit event status " + subject
        + " from " + startDateTime + " to " + endDateTime + " with " + newEventStatus
        + "\nexit\n");
    return null;
  }

  @Override
  public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                     String newSubject, String newStartDateTime,
                                     String newEndDateTime, String newDescription,
                                     String newLocation, String newEventStatus)
      throws IllegalArgumentException {
    log.append("edit events location " + subject
        + " from " + startDateTime + " with " + newLocation
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
  public String exportToCSV() {
    return "";
  }
}
