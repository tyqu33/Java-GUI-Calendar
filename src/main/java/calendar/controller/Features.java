package calendar.controller;

import java.util.Collection;

public interface Features {

  void createCalendar(String calendarName, String timeZone);

  void createEvent(String subject, String startDateTime, String endDateTime,
                   String description, String location, String eventStatus);

  Collection<String> getAllCalendarNames();

  void switchCalendar(String calendarName);

  void editCalendarProperty(String calendarName, String propertyName, String propertyValue);

  void manipulateCalendar();

  void backToCalendarToday();

  void searchAcrossCalendar(String keyword);
}
