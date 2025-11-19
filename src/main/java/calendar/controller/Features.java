package calendar.controller;

public interface Features {

  void createCalendar(String calendarName, String timeZone);

  void createEvent(String subject, String startDateTime, String endDateTime,
                   String description, String location, String eventStatus);

  void switchCalendar(String calendarName);

  void editCalendarTimezone(String calendarName, String timeZone);

  void editCalendarName(String calendarName, String timeZone);

  void manipulateCalendar();

  void backToCalendarToday();

  void searchAcrossCalendar(String keyword);
}
