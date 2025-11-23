package calendar.controller;

import calendar.event.EventDecorator;
import calendar.event.EventInterface;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * The features for GUI functions.
 */
public interface Features {

  /**
   * To create a calendar for GUI.
   *
   * @param calendarName the calendar name
   * @param timeZone the timezone of the calendar
   */
  void createCalendar(String calendarName, String timeZone);

  void createEvent(String subject, String startDateTime, String endDateTime,
                   String description, String location, String eventStatus);

  void createEventSeries(String subject, String startDateTime, String endDateTime,
                         String description, String location, String eventStatus,
                         String weekdays, int occurrences, String seriesEndDate);

  /**
   * To get a collection of all existing calendar names.
   *
   * @return the collection of all existing calendar names
   */
  Collection<String> getAllCalendarNames();

  /**
   * To switch to the designated calendar.
   *
   * @param calendarName the aimed calendar name
   */
  void switchCalendar(String calendarName);

  void editEvent(String subject, String startDateTime, String endDateTime,
                 String newSubject, String newStartDateTime, String newEndDateTime,
                 String newDescription, String newLocation,
                 String newEventStatus, String calendarName);

  void editCalendarProperty(String calendarName, String propertyName, String propertyValue);

  void manipulateCalendar();

  void backToCalendarToday();

  Collection<EventDecorator> getEventsAcrossCalendar(String keyword);

  /**
   * Navigate to a specific month and year.
   *
   * @param year  the year to navigate to
   * @param month the month to navigate to
   */
  void navigateToMonth(int year, int month);

  /**
   * Export the current calendar to a file.
   *
   * @param fileName the file name (with .csv or .ics extension)
   */
  void exportCalendar(String fileName);

  /**
   * View all events on a specific date.
   *
   * @param date the date to view events for
   */
  void viewEventsOnDate(LocalDate date);
}
