package calendar.controller;

import java.time.LocalDate;
import java.util.Collection;

public interface Features {

  void createCalendar(String calendarName, String timeZone);

  void createEvent(String subject, String startDateTime, String endDateTime,
                   String description, String location, String eventStatus);

  void createEventSeries(String subject, String startDateTime, String endDateTime,
                         String description, String location, String eventStatus,
                         String weekdays, int occurrences, String seriesEndDate);

  Collection<String> getAllCalendarNames();

  void switchCalendar(String calendarName);

  void editCalendarProperty(String calendarName, String propertyName, String propertyValue);

  void manipulateCalendar();

  void backToCalendarToday();

  void searchAcrossCalendar(String keyword);

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
