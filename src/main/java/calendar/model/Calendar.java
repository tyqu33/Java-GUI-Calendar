package calendar.model;

import calendar.enums.EventStatus;
import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;

public class Calendar implements CalendarInterface{
  @Override
  public Event createSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String description, String location, EventStatus eventStatus)
      throws IllegalArgumentException {
    return null;
  }

  @Override
  public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                       String description, String location, EventStatus eventStatus,
                                       String weekdays, int repeatTimes, String seriesEndDateTime)
      throws IllegalArgumentException {
    return null;
  }

  @Override
  public Event getSingleEvent(String subject, String startDateTime, String endDateTime,
                              String description, String location, EventStatus eventStatus)
      throws IllegalArgumentException {
    return null;
  }

  @Override
  public Event editSingleEvent(String newSubject, String newStartDateTime, String newEndDateTime,
                               String newDescription, String newLocation,
                               EventStatus newEventStatus) throws IllegalArgumentException {
    return null;
  }

  @Override
  public Event editEventSeries() throws IllegalArgumentException {
    return null;
  }

  @Override
  public void queryForEvent(String startDateTime, String endDateTime)
      throws IllegalArgumentException {

  }

  @Override
  public UserStatus queryForUserStatus(String queryDateTime) throws IllegalArgumentException {
    return null;
  }

  @Override
  public void exportCalendarToFile(String fileName) {

  }
}
