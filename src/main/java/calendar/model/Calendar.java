package calendar.model;

import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;
import calendar.util.CSVExporter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Calendar implements CalendarInterface {

  Map<EventKey, Event> calendar;
  Map<String, EventSeries> seriesManager;

  public Calendar() {
    calendar = new HashMap<>();
    seriesManager = new HashMap<>();
  }

  @Override
  public Event createSingleEvent(String subject, String startDateTime, String endDateTime,
                                 String description, String location, String eventStatus,
                                 String seriesId)
      throws IllegalArgumentException {
    if (subject.isEmpty() || startDateTime.isEmpty()) {
      throw new IllegalArgumentException("subject or startDateTime cannot be empty");
    }
    LocalDateTime start;
    if (startDateTime.length() <= 10) {
      startDateTime = startDateTime + "T00:00";
    }
    try {
      start = LocalDateTime.parse(startDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid start date/time: " + startDateTime);
    }
    LocalDateTime end = null;
    boolean isAllDay = false;
    if (endDateTime == null || endDateTime.isEmpty()) {
      isAllDay = true;
      LocalDate date = start.toLocalDate();
      start = LocalDateTime.of(date, LocalTime.of(8, 0));
      end = LocalDateTime.of(date, LocalTime.of(17, 0));
    } else {
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
        .seriesId(seriesId)
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

    LocalDate seriesEndDate = null;
    if (!seriesEndDateTime.isEmpty()) {
      try {
        seriesEndDate = LocalDate.parse(seriesEndDateTime);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid series end date/time: " + seriesEndDateTime);
      }
    }

    EventSeries.EventSeriesBuilder seriesBuilder = EventSeries.builder(subject, start, weekdays)
        .description(description)
        .location(location)
        .status(eventStatus);
    if (end != null) {
      seriesBuilder.end(end);
    }
    if (repeatTimes > 0) {
      seriesBuilder.occurrences(repeatTimes);
    } else if (seriesEndDate != null) {
      seriesBuilder.setEndDate(seriesEndDate);
    } else {
      throw new IllegalArgumentException("Must specify either occurrences or end date");
    }

    EventSeries newSeries = seriesBuilder.build();
    String seriesId = newSeries.getSeriesId();
    Set<EventKey> keys = newSeries.getSeriesKeys();

    for (EventKey key : keys) {
      Event eventInstance = new Event.EventBuilder(key.getSubject(), key.getStartDateTime())
          .end(key.getEndDateTime())
          .description(description)
          .location(location)
          .status(eventStatus)
          .seriesId(seriesId)
          .build();
      calendar.put(key, eventInstance);
    }

    seriesManager.put(seriesId, newSeries); // put this series into Manager
    return newSeries;

    // THERE SHOULD BE A HELPER METHOD, CALCULATING EVERY START AND END IN THIS SERIES FOR EVERY EVENT
    // IN A LOOP:
    //{
    //  EventKey key = new EventKey(subject, start, end);
    //  Event singleEvent =
    //      createSingleEvent(subject, startDateTime, endDateTime, description, location, eventStatus,
    //          seriesId);
    //  calendar.put(key, singleEvent);

    // PUT EVERY KEY OF CURRENT EVENT IN THIS SERIES
    //  series.getEventKeys().add(key);
    //}

    //    EventSeries series = new EventSeries.EventSeriesBuilder(subject, start, weekdays)
    //        .end(end)
    //        .description(description)
    //        .location(location)
    //        .status(eventStatus)
    //        .allDay()
    //        .occurrences(repeatTimes)
    //        .build();
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
    if (subject == null || startDateTime == null) {
      throw new IllegalArgumentException("subject or startDateTime cannot be empty");
    }
    LocalDateTime oldStart;
    try {
      oldStart = LocalDateTime.parse(startDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid start date/time: " + startDateTime);
    }


    LocalDateTime oldEnd = null;
    try {
      oldEnd = LocalDateTime.parse(endDateTime);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid end date/time: " + endDateTime);
    }

    EventKey oldKey = new EventKey(subject, oldStart, oldEnd);
    Event oldEvent = calendar.get(oldKey);
    // The old Event does not exist
    if (oldEvent == null) {
      throw new IllegalArgumentException("Event does not exist, should create a new one");
    }
    boolean isKeyChanged = (newSubject != null) || (newStartDateTime != null) || (newEndDateTime != null);

    if (!isKeyChanged) {
      if (newDescription != null && !newDescription.isEmpty()) {
        oldEvent.editDescription(newDescription);
      }
      if (newLocation != null && !newLocation.isEmpty()) {
        oldEvent.editLocation(newLocation);
      }
      if (newEventStatus != null && !newEventStatus.isEmpty()) {
        oldEvent.editEventStatus(newEventStatus);
      }
      return oldEvent;

    } else {

      String finalNewSubject = (newSubject != null) ? newSubject : oldEvent.getSubject();
      LocalDateTime finalNewStart;
      if (newStartDateTime != null) {
        try {
          finalNewStart = LocalDateTime.parse(newStartDateTime);
        } catch (DateTimeParseException e) {
          throw new IllegalArgumentException("Invalid new start date/time: " + newStartDateTime);
        }
      } else {
        finalNewStart = oldEvent.getStartDateTime();
      }
      LocalDateTime finalNewEnd;
      if (newEndDateTime != null) {
        try {
          finalNewEnd = LocalDateTime.parse(newEndDateTime);
        } catch (DateTimeParseException e) {
          throw new IllegalArgumentException("Invalid new end date/time: " + newEndDateTime);
        }
      } else {
        finalNewEnd = oldEvent.getEndDateTime();
      }
      // The new inputs clash with existing event, the edition should not occur
      EventKey newKey = new EventKey(finalNewSubject, finalNewStart, finalNewEnd);
      if (calendar.get(newKey) != null) { // !newKey.equals(oldKey) && calendar.containsKey(newKey)
        throw new IllegalArgumentException(
            "Event on the new subject, start date/time, end date/time already exists");
      }

      Event newEvent = createSingleEvent(finalNewSubject, finalNewStart.toString(), finalNewEnd.toString(), newDescription,
          newLocation, newEventStatus, null);
      calendar.remove(oldKey);
      calendar.put(newKey, newEvent);

      return newEvent;
    }

  }

  @Override
  public EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                     String newSubject, String newStartDateTime,
                                     String newEndDateTime,
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

    EventKey key = new EventKey(subject, start, end);
    Event event = calendar.get(key);
    if (event == null) {
      throw new IllegalArgumentException("Event does not exist");
    }
    // This event does not belong to any series
    if (event.getSeriesId() == null) {
      editSingleEvent(subject, startDateTime, endDateTime, newSubject, newStartDateTime,
          newEndDateTime,
          newDescription, newLocation, newEventStatus);
    } else {
      // PRIMAL KEY NOT CHANGED
      if (newSubject.equals(subject) && newStartDateTime.equals(startDateTime) &&
          newEndDateTime.equals(endDateTime)) {
        // THERE SHOULD BE A HELPER METHOD, CALCULATING EVERY START AND END IN THIS SERIES FOR EVERY EVENT
        // IN A LOOP:
        {
          editSingleEvent(subject, startDateTime, endDateTime, newSubject, newStartDateTime,
              newEndDateTime,
              newDescription, newLocation, newEventStatus);
        }
      } else {

        Event oldEvent = calendar.remove(key);
        String seriesId = oldEvent.getSeriesId();
        EventSeries series = seriesManager.get(seriesId);
        if (series != null) {
          series.removeEventKey(key);
        }

        EventKey newKey = new EventKey(newSubject, start, end);
        String newSeriesId = null;
        String newWeekdays = null;
        int newRepeatTimes = 0;
        String newSeriesEndDate = null;
        EventSeries newSeries = createEventSeries(newSubject,
            newStartDateTime, newEndDateTime, newDescription, newLocation, newEventStatus,
            newWeekdays, newRepeatTimes, newSeriesEndDate);


      }

    }


    return null;
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

  @Override
  public List<Event> getEventsOnDate(LocalDate date) {
    List<Event> result = new ArrayList<>();
    for (Event event : calendar.values()) {
      LocalDate eventStartDate = event.getStartDateTime().toLocalDate();
      LocalDate eventEndDate = event.getEndDateTime() != null ?
          event.getEndDateTime().toLocalDate() : eventStartDate;
      boolean isOnDate = (date.isEqual(eventStartDate) || date.isAfter(eventStartDate))
          && (date.isEqual(eventEndDate) || date.isBefore(eventEndDate) || date.isEqual(eventEndDate));
      if (isOnDate) {
        result.add(event);
      }
    }
    Collections.sort(result, new Comparator<Event>() {
      @Override
      public int compare(Event e1, Event e2) {
        return e1.getStartDateTime().compareTo(e2.getStartDateTime());
      }
    });
    return result;
  }

  @Override
  public List<Event> getEventsBetween(LocalDateTime start, LocalDateTime end) {
    List<Event> result = new ArrayList<>();

    for (Event event : calendar.values()) {
      LocalDateTime eventStart = event.getStartDateTime();
      LocalDateTime eventEnd = event.getEndDateTime();
      if (!(eventEnd.isBefore(start) || eventStart.isAfter(end))) {
        result.add(event);
      }
    }
    Collections.sort(result, new Comparator<Event>() {
      @Override
      public int compare(Event e1, Event e2) {
        return e1.getStartDateTime().compareTo(e2.getStartDateTime());
      }
    });

    return result;
  }

  @Override
  public UserStatus getUserStatus(LocalDateTime queryTime) {
    for (Event event : calendar.values()) {
      LocalDateTime eventStart = event.getStartDateTime();
      LocalDateTime eventEnd = event.getEndDateTime();
      if (!queryTime.isBefore(eventStart) && !queryTime.isAfter(eventEnd)) {
        return UserStatus.BUSY;
      }
    }
    return UserStatus.AVAILABLE;
  }

  @Override
  public String exportToCSV() {
    List<Event> sortedEvents = new ArrayList<>(calendar.values());
    Collections.sort(sortedEvents, new Comparator<Event>() {
      @Override
      public int compare(Event e1, Event e2) {
        return e1.getStartDateTime().compareTo(e2.getStartDateTime());
      }
    });
    return CSVExporter.exportToCSV(sortedEvents);
  }
}
