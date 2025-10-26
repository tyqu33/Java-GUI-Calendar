package calendar.model;


import calendar.enums.EventStatus;
import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventSeries;

/**
 * This interface contains necessary operations that a calendar should support.
 */
public interface CalendarInterface {

  /**
   * To create a single event with necessary subject, startDateTime, endDateTime and optional
   * properties. If not specify endDateTime, the event will be an all-day event with
   * default time range from 8am to 5pm (even if the startDateTime is different from 8am).
   * A single event may span across days.
   * Two events cannot have the same subject, start date/time and end date/time.
   *
   * @param subject       the theme of the event on calendar (must specify)
   * @param startDateTime the start date and/or time of the event (must specify)
   * @param endDateTime   the end date and/or time of the event (must specify) if input is null,
   *                      note this event as an all-day event with default time range from 8am to 5pm
   * @param description   a longer description of the event (optional)
   * @param location      the location of the event (optional)
   * @param eventStatus   the eventStatus (public/private) (optional)
   * @param seriesId      null by default; if this event is a single independent event, then null; if belongs to a series, then not null
   * @return the single event that is created; if fail to create, return null
   * @throws IllegalArgumentException if inputs are not illegally formatted
   */
  Event createSingleEvent(String subject, String startDateTime, String endDateTime,
                          String description, String location, String eventStatus,
                          String seriesId)
      throws IllegalArgumentException;

  /**
   * To create an event series with necessary subject, startDateTime, endDateTime and optional
   * properties. All events in an event series are required to have the same start time.
   * An event series must specify its occurrences in which weekdays, and then specify
   * repeatTimes or until a specific seriesEndDateTime.
   * A single event in a series can only span one day (it must start and finish on the same day).
   * Two events in the calendar cannot have the same subject, start date/time and end date/time.
   *
   * @param subject           the theme of the event series on calendar (must specify)
   * @param startDateTime     the start date and/or time of the event series (must specify)
   * @param endDateTime       the end date and/or time of the event series (must specify)
   *                          if input is null, note every event in this series as an all-day event
   *                          with default time range from 8am to 5pm
   * @param description       a longer description of the event series (optional)
   * @param location          the location of the event series (optional)
   * @param eventStatus       the eventStatus (public/private) (optional)
   * @param weekdays          (optional) a sequence of characters where each character denotes a day of the
   *                          event's occurrence e.g., MRU. 'M' is Monday, 'T' is Tuesday, 'W' is Wednesday,
   *                          'R' is Thursday, 'F' is Friday, 'S' is Saturday, and 'U' is Sunday
   * @param repeatTimes       (optional) the int that denotes how many occurrences of the event on weekdays
   * @param seriesEndDateTime (optional) the end date and/or time of the series
   * @return the event series that is created; if fail to create, return null
   * @throws IllegalArgumentException if inputs are not illegally formatted
   */
  EventSeries createEventSeries(String subject, String startDateTime, String endDateTime,
                                String description, String location, String eventStatus,
                                String weekdays, int repeatTimes, String seriesEndDateTime)
      throws IllegalArgumentException;

  /**
   * To get a single event with subject, startDateTime and optional properties given.
   *
   * @param subject       the theme of the event series on calendar (must specify)
   * @param startDateTime the start date and/or time of the event series (must specify)
   * @param endDateTime   the end date and/or time of the event series (must specify) if input is null,
   *                      search for an all-day event
   * @return the single event that is created; if fail to create, return null
   * @throws IllegalArgumentException if inputs are not illegally formatted
   */
  Event getSingleEvent(String subject, String startDateTime, String endDateTime)
      throws IllegalArgumentException;

  /**
   * This method should only be used after getSingleEvent() to edit a single event.
   *
   * @param newSubject
   * @param newStartDateTime
   * @param newEndDateTime
   * @param newDescription
   * @param newLocation
   * @param newEventStatus
   * @return
   * @throws IllegalArgumentException if inputs are not illegally formatted
   */
  Event editSingleEvent(String subject, String startDateTime, String endDateTime,
                        String newSubject, String newStartDateTime, String newEndDateTime,
                        String newDescription, String newLocation, String newEventStatus)
      throws IllegalArgumentException;

  EventSeries editEventSeries(String subject, String startDateTime, String endDateTime,
                                      String newSubject, String newStartDateTime, String newEndDateTime,
                                      String newDescription, String newLocation, String newEventStatus) throws IllegalArgumentException;

  void printEventsOnSpecificDay(String startDateTime)
      throws IllegalArgumentException;

  void printEventsFromTimeToTime(String startDateTime, String endDateTime)
      throws IllegalArgumentException;

  /**
   * Show UserStatus on a given date and/or time.
   *
   * @return If there exists event on the given date and/or time, return BUSY; else, AVAILABLE
   * @throws IllegalArgumentException if inputs are not illegally formatted
   */
  UserStatus showUserStatusOnSpecificTime(String queryDateTime) throws IllegalArgumentException;

  void exportCalendarToFile(String fileName);
}
