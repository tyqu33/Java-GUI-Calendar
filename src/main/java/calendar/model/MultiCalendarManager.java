package calendar.model;

import calendar.calendarEntity.CalendarEntity;
import calendar.calendarEntity.CalendarEntityInterface;
import calendar.event.Event;
import calendar.event.EventSeries;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This class implements interface MultiCalendarManagerInterface and offers create, edit a calendar
 * and copy its event(s) to another calendar.
 */
public class MultiCalendarManager implements MultiCalendarManagerInterface {
  private Map<String, CalendarEntityInterface> calendarManager;
  private CalendarEntityInterface calendarEntity;

  /**
   * Constructor for MultiCalendarManager.
   */
  public MultiCalendarManager() {
    this.calendarManager = new HashMap<>();
    this.calendarEntity = null;
  }

  @Override
  public CalendarEntityInterface createCalendar(String calendarName, String timeZone)
      throws IllegalArgumentException {
    if (calendarName == null || calendarName.isEmpty() || timeZone == null || timeZone.isEmpty()) {
      throw new IllegalArgumentException("calendar name and timezone cannot be empty");
    }
    ZoneId zoneId = null;
    try {
      zoneId = ZoneId.of(timeZone);
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("Invalid time zone: " + timeZone);
    }
    if (this.calendarManager.containsKey(calendarName)) {
      throw new IllegalArgumentException("Calendar with name already exists: " + calendarName);
    }
    Calendar calendar = new Calendar();
    CalendarEntityInterface entity = new CalendarEntity.CalendarEntityBuilder()
        .calendarName(calendarName)
        .timezone(zoneId)
        .calendar(calendar)
        .build();
    this.calendarManager.put(calendarName, entity);
    // this.calendarEntity = entity;
    return entity;
  }

  @Override
  public void editCalendar(String calendarName, String property, String propertyValue) {
    if (calendarName == null || calendarName.isEmpty()) {
      throw new IllegalArgumentException("calendar name cannot be empty");
    }
    if (property == null || property.isEmpty() || propertyValue == null
        || propertyValue.isEmpty()) {
      throw new IllegalArgumentException("property to be updated and value cannot be empty");
    }
    if (!this.calendarManager.containsKey(calendarName)) {
      throw new IllegalArgumentException("Calendar with name does not exist: " + calendarName);
    }
    if (property.equals("name")) {
      CalendarEntityInterface oldEntity = this.calendarManager.get(calendarName);

      CalendarEntityInterface newEntity = new CalendarEntity.CalendarEntityBuilder()
          .calendarName(propertyValue.trim())
          .timezone(oldEntity.getTimeZone())
          .calendar((Calendar)oldEntity.getCalendar())
          .build();
      this.calendarManager.put(propertyValue.trim(), newEntity);
      this.calendarManager.remove(calendarName);
      this.calendarEntity = newEntity;

    } else if (property.equals("timezone")) {
      ZoneId newZoneId = null;
      try {
        newZoneId = ZoneId.of(propertyValue.trim());
      } catch (DateTimeException e) {
        throw new IllegalArgumentException("Invalid time zone: " + propertyValue);
      }
      CalendarEntityInterface oldEntity = this.calendarManager.get(calendarName);

      CalendarEntityInterface newEntity = new CalendarEntity.CalendarEntityBuilder()
          .calendarName(calendarName)
          .timezone(newZoneId)
          .calendar((Calendar)oldEntity.getCalendar())
          .build();

      this.calendarManager.replace(calendarName, newEntity);
      // this.calendarEntity = newEntity;

    } else {
      throw new IllegalArgumentException("Invalid property input: " + property);
    }

  }

  @Override
  public CalendarEntityInterface getCurrentCalendarEntity() {
    return this.calendarEntity;
  }

  @Override
  public CalendarEntityInterface getCalendarEntity(String calendarName) {
    if (this.calendarManager.containsKey(calendarName.trim())) {
      return this.calendarManager.get(calendarName.trim());
    }
    return null;
  }

  @Override
  public ZoneId getCalendarTimeZone(String calendarName) {
    if (!this.calendarManager.containsKey(calendarName.trim())) {
      throw new IllegalArgumentException("Calendar with name does not exist: " + calendarName);
    }
    return this.calendarManager.get(calendarName.trim()).getTimeZone();
  }

  @Override
  public void useThisCalendarEntity(CalendarEntityInterface calendarEntity) {
    this.calendarEntity = calendarEntity;
  }

  @Override
  public void copyEvent(String subject, String startDateTime, String targetCalendarName,
                        String targetDateTime) throws IllegalArgumentException {
    if (subject == null || startDateTime == null || targetCalendarName == null
        || targetDateTime == null || subject.isEmpty() || startDateTime.isEmpty()
        || targetCalendarName.isEmpty() || targetDateTime.isEmpty()) {
      throw new IllegalArgumentException("event name, event start date/time, target calendar name,"
          + " and target start date/time cannot be empty");
    }
    if (this.getCalendarEntity(targetCalendarName) == null) {
      throw new IllegalArgumentException(
          "Calendar with name does not exist: " + targetCalendarName);
    }
    if (this.calendarEntity == null || this.calendarEntity.getCalendar() == null) {
      throw new IllegalArgumentException("Current calendar does not exist");
    }

    LocalDateTime targetStart = LocalDateTime.parse(targetDateTime);

    for (Event event : this.calendarEntity.getCalendar().getEvents()) {
      if (event.getSubject().equals(subject)
          && event.getStartDateTime().toString().equals(startDateTime)) {
        Duration duration = Duration.between(event.getStartDateTime(), event.getEndDateTime());
        LocalDateTime targetEnd = targetStart.plus(duration);

        try {
          this.getCalendarEntity(targetCalendarName).getCalendar().createSingleEvent(
              subject.trim(),
              targetStart.toString(),
              targetEnd.toString(),
              event.getDescription(),
              event.getLocation(),
              event.getEventStatus().toString(),
              null);
        } catch (IllegalArgumentException e) {
          if (e.getMessage().equals("Event already exists")) {
            throw new IllegalArgumentException("Calendar " + targetCalendarName
                + " already has an event with the name " + subject.trim()
                + ", the start date/time " + targetStart.toString()
                + ", the end date/time " + targetEnd.toString() + " existed ");
          } else {
            throw e;
          }
        }
      }
    }

  }

  @Override
  public void copyEventsOnThatDay(String specificDate, String targetCalendarName,
                                  String targetDay) {
    if (specificDate == null || targetCalendarName == null || targetDay == null
        || specificDate.isEmpty() || targetCalendarName.isEmpty() || targetDay.isEmpty()) {
      throw new IllegalArgumentException("events day, target calendar name and target day"
          + " cannot be empty");
    }
    if (this.getCalendarEntity(targetCalendarName) == null) {
      throw new IllegalArgumentException(
          "Calendar with name does not exist: " + targetCalendarName);
    }
    if (this.calendarEntity == null || this.calendarEntity.getCalendar() == null) {
      throw new IllegalArgumentException("Current calendar does not exist");
    }

    ZoneId oldZoneId = this.calendarEntity.getTimeZone();
    ZoneId newZoneId = this.getCalendarTimeZone(targetCalendarName);
    CalendarInterface newCalendar = this.getCalendarEntity(targetCalendarName).getCalendar();
    LocalDateTime oldStart = LocalDateTime.parse(specificDate + "T00:00");
    LocalDateTime oldEnd = LocalDateTime.parse(specificDate + "T23:59");

    for (Event event : this.calendarEntity.getCalendar().getEvents()) { // 20251107T19:00
      LocalDateTime eventStart = event.getStartDateTime();
      LocalDateTime eventEnd = event.getEndDateTime();

      if (!(eventEnd.isBefore(oldStart) || eventStart.isAfter(oldEnd))) {
        LocalDateTime newStart = event.getStartDateTime().atZone(oldZoneId)
            .withZoneSameInstant(newZoneId).toLocalDateTime();
        LocalDateTime newEnd = event.getEndDateTime().atZone(oldZoneId)
            .withZoneSameInstant(newZoneId).toLocalDateTime();

        try {
          newCalendar.createSingleEvent(
              event.getSubject(),
              newStart.toString(),
              newEnd.toString(),
              event.getDescription(),
              event.getLocation(),
              event.getEventStatus().toString(),
              null
          );
        } catch (IllegalArgumentException e) {
          if (e.getMessage().equals("Event already exists")) {
            throw new IllegalArgumentException("Calendar " + targetCalendarName
                + " already has an event with the name " + event.getSubject()
                + ", the start date/time " + newStart.toString()
                + ", the end date/time " + newEnd.toString() + " existed ");
          } else {
            throw e;
          }
        }
      }
    }

  }

  @Override
  public void copyEventsBetweenDays(String startDate, String endDate, String targetCalendarName,
                                    String targetDay) {
    if (startDate == null || endDate == null || targetCalendarName == null || targetDay == null
        || startDate.isEmpty() || endDate.isEmpty() || targetCalendarName.isEmpty()
        || targetDay.isEmpty()) {
      throw new IllegalArgumentException("events start day, events end day, target calendar name"
          + " and target day cannot be empty");
    }
    if (this.getCalendarEntity(targetCalendarName) == null) {
      throw new IllegalArgumentException(
          "Calendar with name does not exist: " + targetCalendarName);
    }
    if (this.calendarEntity == null || this.calendarEntity.getCalendar() == null) {
      throw new IllegalArgumentException("Current calendar does not exist");
    }

    ZoneId oldZoneId = this.calendarEntity.getTimeZone();
    ZoneId newZoneId = this.getCalendarTimeZone(targetCalendarName);
    CalendarInterface newCalendar = this.getCalendarEntity(targetCalendarName).getCalendar();
    LocalDateTime oldStart = LocalDateTime.parse(startDate + "T00:00");
    LocalDateTime oldEnd = LocalDateTime.parse(endDate + "T23:59");
    Map<String, String> mappedSeriesIds = new HashMap<>();
    for (Event event : this.calendarEntity.getCalendar().getEvents()) {
      LocalDateTime eventStart = event.getStartDateTime();
      LocalDateTime eventEnd = event.getEndDateTime();

      if (!(eventEnd.isBefore(oldStart) || eventStart.isAfter(oldEnd))) {
        LocalDateTime newStart = eventStart.atZone(oldZoneId)
            .withZoneSameInstant(newZoneId).toLocalDateTime();
        LocalDateTime newEnd = eventEnd.atZone(oldZoneId)
            .withZoneSameInstant(newZoneId).toLocalDateTime();
        // single event that doesn't belong to a series
        if (event.getSeriesId() == null) {
          try {
            newCalendar.createSingleEvent(
                event.getSubject(),
                newStart.toString(),
                newEnd.toString(),
                event.getDescription(),
                event.getLocation(),
                event.getEventStatus().toString(),
                null
            );
          } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Event already exists")) {
              throw new IllegalArgumentException("Calendar " + targetCalendarName
                  + " already has an event with the name " + event.getSubject()
                  + ", the start date/time " + newStart.toString()
                  + ", the end date/time " + newEnd.toString() + " existed ");
            } else {
              throw e;
            }
          }
        } else {

          String oldSeriesId = event.getSeriesId();
          EventSeries oldSeries = this.calendarEntity.getCalendar().getEventSeries(oldSeriesId);
          // invalid old series id
          if (oldSeries == null) {
            try {
              newCalendar.createSingleEvent(
                  event.getSubject(),
                  newStart.toString(),
                  newEnd.toString(),
                  event.getDescription(),
                  event.getLocation(),
                  event.getEventStatus().toString(),
                  null
              );
            } catch (IllegalArgumentException e) {
              if (e.getMessage().equals("Event already exists")) {
                throw new IllegalArgumentException("Calendar " + targetCalendarName
                    + " already has an event with the name " + event.getSubject()
                    + ", the start date/time " + newStart.toString()
                    + ", the end date/time " + newEnd.toString() + " existed ");
              } else {
                throw e;
              }
            }
          } else {
            if (newStart.isBefore(
                LocalDateTime.parse(newStart.toString().substring(0, 8) + "T00:00"))
                || newEnd.isAfter(
                LocalDateTime.parse(newEnd.toString().substring(0, 8) + "T23:59"))) {
              throw new IllegalArgumentException(
                  "New event in a series should not cover more than one day "
                      + "after being copied to the new calendar");
            }
            //            if (mappedSeriesIds.containsKey(oldSeriesId)) {
            //              String newSeriesId = mappedSeriesIds.get(oldSeriesId);
            //              newCalendar.createSingleEvent(
            //                  event.getSubject(),
            //                  newStart.toString(),
            //                  newEnd.toString(),
            //                  event.getDescription(),
            //                  event.getLocation(),
            //                  event.getEventStatus().toString(),
            //                  newSeriesId
            //              );
            //            } else {
            //              // first time encounter this series in this interval
            //              String newSeriesId = UUID.randomUUID().toString();
            //              newCalendar.createSingleEvent(
            //                  event.getSubject(),
            //                  newStart.toString(),
            //                  newEnd.toString(),
            //                  event.getDescription(),
            //                  event.getLocation(),
            //                  event.getEventStatus().toString(),
            //                  newSeriesId
            //              );
            //              mappedSeriesIds.put(oldSeriesId, newSeriesId);
            //            }

            LocalDateTime oldSeriesPrunedStart =
                oldSeries.getStartDateTime().isBefore(oldStart) ? oldStart :
                    oldSeries.getStartDateTime();
            LocalDateTime oldSeriesPrunedEnd =
                oldSeries.getEndDateTime().isAfter(oldEnd) ? oldEnd : oldSeries.getEndDateTime();

            LocalDateTime newSeriesStart = oldSeriesPrunedStart.atZone(oldZoneId)
                .withZoneSameInstant(newZoneId).toLocalDateTime();
            LocalDateTime newSeriesEnd = oldSeriesPrunedEnd.atZone(oldZoneId)
                .withZoneSameInstant(newZoneId).toLocalDateTime();
            int newOccurrence = calOccurrenceBetweenDays(oldSeries.getStartDateTime(),
                oldSeries.getEndDateTime(), oldStart, oldEnd, oldSeries.getWeekdays());
            try {
              newCalendar.createEventSeries(
                  oldSeries.getSubject(),
                  newSeriesStart.toString(),
                  newSeriesEnd.toString(),
                  oldSeries.getDescription(),
                  oldSeries.getLocation(),
                  oldSeries.getEventStatus().toString(),
                  oldSeries.getWeekdays(),
                  newOccurrence,
                  null
              );
            } catch (IllegalArgumentException e) {
              if (e.getMessage().equals("Event already exists")) {
                throw new IllegalArgumentException("Calendar " + targetCalendarName
                    + " already has an event with the name " + oldSeries.getSubject()
                    + " in conflict with events to be copied ");
              } else {
                throw e;
              }
            }

          }

        }
      }


    }


  }


  private int calOccurrenceBetweenDays(LocalDateTime originalStart, LocalDateTime originalEnd,
                                       LocalDateTime newStart, LocalDateTime newEnd,
                                       String weekdays) {
    if (newStart.isAfter(newEnd) || originalStart.isAfter(originalEnd)) {
      return 0;
    }
    int newOccurrence = 0;
    Set<DayOfWeek> targetDays = parseWeekdays(weekdays);
    if (!originalStart.isAfter(newStart) && !originalEnd.isBefore(newEnd)) { // wrap up
      newOccurrence = countOccurrencesWithTime(newStart, newEnd, targetDays);
    } else if (originalEnd.isAfter(newStart) && originalEnd.isBefore(newEnd)) {
      newOccurrence = countOccurrencesWithTime(newStart, originalEnd, targetDays);
    } else { // else if (originalStart.isAfter(newStart) && originalStart.isBefore(newEnd))
      newOccurrence = countOccurrencesWithTime(originalStart, newEnd, targetDays);
    }
    //    int originalOccurrence = originalOccurrences > 0 ? originalOccurrences :
    //    calOccurrence(originalStart, originalEnd, targetDays);
    //    int newOccurrence = calOccurrence(newStart, newEnd, targetDays);
    return newOccurrence;
  }

  private int countOccurrencesWithTime(LocalDateTime start, LocalDateTime end,
                                       Set<DayOfWeek> targetDays) {
    int count = 0;
    LocalDate current = start.toLocalDate();
    LocalDate target = end.toLocalDate();

    while (!current.isAfter(target)) {
      if (targetDays.contains(current.getDayOfWeek())) {
        count++;
      }
      current = current.plusDays(1);
    }
    return count;
  }

  private Set<DayOfWeek> parseWeekdays(String weekdays) {
    Set<DayOfWeek> targetDays = new HashSet<>();
    for (char c : weekdays.toUpperCase().toCharArray()) {
      switch (c) {
        case 'M':
          targetDays.add(DayOfWeek.MONDAY);
          break;
        case 'T':
          targetDays.add(DayOfWeek.TUESDAY);
          break;
        case 'W':
          targetDays.add(DayOfWeek.WEDNESDAY);
          break;
        case 'R':
          targetDays.add(DayOfWeek.THURSDAY);
          break;
        case 'F':
          targetDays.add(DayOfWeek.FRIDAY);
          break;
        case 'S':
          targetDays.add(DayOfWeek.SATURDAY);
          break;
        case 'U':
          targetDays.add(DayOfWeek.SUNDAY);
          break;
        default:
          break;
      }
    }
    return targetDays;
  }
}
