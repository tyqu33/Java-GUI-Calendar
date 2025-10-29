import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;
import calendar.model.CalendarInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class MockModel implements CalendarInterface {
  private StringBuilder log;
  private final String uniqueResult;

  public MockModel(StringBuilder log, String uniqueResult) {
    this.log = log;
    this.uniqueResult = uniqueResult;
  }

  @Override
  public Event createSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String description, String location, String eventStatus,
                                 String seriesId) {
    LocalDate start = LocalDate.parse(startDateTime);
    log.append("create event " + subject
        + " on " + start.toString() + "\nprint events on " + start.toString() + "\nexit\n");
    return null;
  }

  public String getEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException {
    log.append("getSingleEvent subject " + subject + " startDateTime " + startDateTime + "\n");
    return this.uniqueResult;
  }

  @Override
  public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                       String description, String location, String eventStatus,
                                       String weekdays, int repeatTimes, String seriesEndDateTime) {
    return null;
  }

  @Override
  public Event getSingleEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException {
    log.append("getSingleEvent subject " + subject + " startDateTime " + startDateTime + "\n");
    return null;
  }

  @Override
  public Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                               String newSubject, String newStartDateTime, String newEndDateTime,
                               String newDescription, String newLocation, String newEventStatus) {
    return null;
  }

  @Override
  public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                     String newSubject, String newStartDateTime,
                                     String newEndDateTime, String newDescription,
                                     String newLocation, String newEventStatus)
      throws IllegalArgumentException {
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