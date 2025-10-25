package calendar.model;

import calendar.enums.EventStatus;
import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class Calendar implements CalendarInterface {

  Map<EventKey, Event> calendar;

  public Calendar() {
    calendar = new HashMap<>();
  }

  @Override
  public Event createSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String description, String location, String eventStatus)
      throws IllegalArgumentException {
    if (subject.isEmpty() || startDateTime.isEmpty()) {
      throw new IllegalArgumentException("subject or startDateTime cannot be empty");
    }
    LocalDateTime start;
    try {
      start = LocalDateTime.parse(startDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid start date/time: " + startDateTime);
    }
    LocalDateTime end = null;
    if (!endDateTime.isEmpty()) {
      try {
        end = LocalDateTime.parse(endDateTime);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid end date/time: " + endDateTime);
      }
    }
    //    LocalDateTime start = null;
    //    LocalDateTime end = null;
    //    try {
    //      verifyInputs(subject, startDateTime, endDateTime, start, end);
    //    } catch (Exception e){
    //      throw new IllegalArgumentException("the input parameters are invalid");
    //    }

    Event event = new Event.EventBuilder(subject, start)
        .end(end)
        .description(description)
        .location(location)
        .status(eventStatus)
        .build();
    EventKey key = new EventKey(subject, start, end);
    calendar.put(key, event);
    return event;
  }

  @Override
  public EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                       String description, String location, String eventStatus,
                                       String weekdays, int repeatTimes, String seriesEndDateTime)
      throws IllegalArgumentException {
    if (subject.isEmpty() || startDateTime.isEmpty()) {
      throw new IllegalArgumentException("subject or startDateTime cannot be empty");
    }
    LocalDateTime start;
    try {
      start = LocalDateTime.parse(startDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid start date/time: " + startDateTime);
    }
    LocalDateTime end = null;
    if (!endDateTime.isEmpty()) {
      try {
        end = LocalDateTime.parse(endDateTime);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid end date/time: " + endDateTime);
      }
    }

    EventSeries series = new EventSeries.EventSeriesBuilder(subject, start, weekdays)
        .end(end)
        .description(description)
        .location(location)
        .status(eventStatus)
        .allDay()
        .occurrences(repeatTimes)
        .build();

    return series;
  }

  @Override
  public Event getSingleEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException {
    if (subject.isEmpty() || startDateTime.isEmpty()) {
      throw new IllegalArgumentException("subject or startDateTime cannot be empty");
    }
    LocalDateTime start;
    try {
      start = LocalDateTime.parse(startDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid start date/time: " + startDateTime);
    }
    LocalDateTime end = null;
    if (!endDateTime.isEmpty()) {
      try {
        end = LocalDateTime.parse(endDateTime);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid end date/time: " + endDateTime);
      }
    }
    EventKey key = new EventKey(subject, start, end);
    return calendar.get(key);
  }

  @Override
  public Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                               String newSubject, String newStartDateTime, String newEndDateTime,
                               String newDescription, String newLocation,
                               String newEventStatus) throws IllegalArgumentException {
    if (newSubject.isEmpty() || newStartDateTime.isEmpty()) {
      throw new IllegalArgumentException("subject or startDateTime cannot be empty");
    }
    LocalDateTime start;
    try {
      start = LocalDateTime.parse(newStartDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid start date/time: " + newStartDateTime);
    }
    LocalDateTime end = null;
    if (!newEndDateTime.isEmpty()) {
      try {
        end = LocalDateTime.parse(newEndDateTime);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid end date/time: " + newEndDateTime);
      }
    }
    // The new inputs clash with existing event, the edition should not occur
    EventKey newKey = new EventKey(newSubject, start, end);
    if (calendar.get(newKey) != null) {
      throw new IllegalArgumentException(
          "Event on the new subject, start date/time, end date/time already exists");
    }
    // The old Event does not exist
    Event oldEvent = getSingleEvent(subject, startDateTime, endDateTime);
    if (oldEvent == null) {
      throw new IllegalArgumentException("Event does not exist, should create a new one");
    }

    Event newEvent = null;
    if (newSubject.equals(subject) && newStartDateTime.equals(startDateTime) &&
        newEndDateTime.equals(endDateTime)) {
      newEvent = getSingleEvent(newSubject, newStartDateTime, newEndDateTime);
      if (!newDescription.isEmpty()) {
        newEvent.editDescription(newDescription);
      }
      if (!newLocation.isEmpty()) {
        newEvent.editLocation(newLocation);
      }
      if (!newEventStatus.isEmpty()) {
        newEvent.editEventStatus(newEventStatus);
      }

    } else {
      newEvent = createSingleEvent(newSubject, newStartDateTime, newEndDateTime, newDescription,
          newLocation, newEventStatus);

      calendar.remove(new EventKey(oldEvent.getSubject(), oldEvent.getStartDateTime(),
          oldEvent.getEndDateTime()));
      calendar.put(newKey, newEvent);
    }


    return newEvent;
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

  private void verifyInputs(String subject, String startDateTime, String endDateTime,
                            LocalDateTime start, LocalDateTime end)
      throws IllegalArgumentException {
    if (subject.isEmpty() || startDateTime.isEmpty()) {
      throw new IllegalArgumentException("subject or startDateTime cannot be empty");
    }
    try {
      start = LocalDateTime.parse(startDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid start date/time: " + startDateTime);
    }
    if (!endDateTime.isEmpty()) {
      try {
        end = LocalDateTime.parse(endDateTime);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid end date/time: " + endDateTime);
      }
    }
  }
}
