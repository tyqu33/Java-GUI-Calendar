package calendar.controller;

import calendar.calendarentity.CalendarEntity;
import calendar.calendarentity.CalendarEntityInterface;
import calendar.event.Event;
import calendar.event.EventInterface;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarViewInterface;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiCalendarController implements Features {
  private MultiCalendarManagerInterface model;
  private CalendarViewInterface view;
  private int currentYear;
  private int currentMonth;

  public GuiCalendarController(MultiCalendarManagerInterface model, CalendarViewInterface view) {
    this.model = model;
    this.view = view;
    LocalDate today = LocalDate.now();
    this.currentYear = today.getYear();
    this.currentMonth = today.getMonthValue();
    view.addFeatures(this);
  }

  /**
   * The executioner of setting default calendar.
   */
  public void go() throws IOException {
    String defaultTimeZone = ZoneId.systemDefault().getId();
    CalendarEntityInterface entity = model.createCalendar("Default", defaultTimeZone);
    model.useThisCalendarEntity(model.getCalendarEntity("Default"));

    LocalDate today = LocalDate.now();
    view.displayMonthView(today.getYear(), today.getMonthValue(), new HashMap<>());
    view.displayCurrentCalendar("Default", defaultTimeZone);
    view.makeVisible();
  }

  @Override
  public void createCalendar(String calendarName, String timeZone) {
    model.createCalendar(calendarName, timeZone);
  }

  @Override
  public void createEvent(String subject, String startDateTime, String endDateTime,
                          String description, String location, String eventStatus) {
    EventInterface event = model.getCurrentCalendarEntity().getCalendar().createSingleEvent(
        subject, startDateTime, endDateTime, description, location, eventStatus, null);
    event.editDescription(description);
    event.editLocation(location);
    event.editEventStatus(eventStatus);
  }

  @Override
  public void createEventSeries(String subject, String startDateTime, String endDateTime,
                                String description, String location, String eventStatus,
                                String weekdays, int occurrences, String seriesEndDate) {
    try {
      CalendarEntityInterface currentEntity = model.getCurrentCalendarEntity();

      if (currentEntity == null) {
        view.displayError("No calendar selected. Please select or create a calendar first.");
        return;
      }
      currentEntity.getCalendar().createEventSeries(
          subject, startDateTime, endDateTime,
          description, location, eventStatus,
          weekdays, occurrences, seriesEndDate
      );

      view.displaySuccess("Event series '" + subject + "' created successfully!");

      // Refresh current month view
      refreshCurrentMonth();

    } catch (IllegalArgumentException e) {
      view.displayError("Failed to create event series: " + e.getMessage());
    } catch (Exception e) {
      view.displayError("Unexpected error: " + e.getMessage());
    }
  }

  @Override
  public Collection<String> getAllCalendarNames() {
    Collection<String> calendarNames = new ArrayList<>();
    for (CalendarEntityInterface entity : model.getAllCalendars()) {
      calendarNames.add(entity.getCalendarName());
    }
    return calendarNames;
  }

  @Override
  public void switchCalendar(String calendarName) {
    CalendarEntityInterface entity = model.getCalendarEntity(calendarName);
    if (entity != null) {
      model.useThisCalendarEntity(entity);
      LocalDate oldToday = LocalDate.now();
      ZoneId oldZoneId = ZoneId.systemDefault();
      LocalDate today = oldToday.atStartOfDay(oldZoneId)
          .withZoneSameInstant(entity.getTimeZone()).toLocalDate();
      view.displayMonthView(today.getYear(), today.getMonthValue(), new HashMap<>());
      view.displayCurrentCalendar(entity.getCalendarName(), entity.getTimeZone().toString());
    }
  }

  @Override
  public void editCalendarProperty(String calendarName, String propertyName, String propertyValue) {
    model.editCalendar(calendarName, propertyName, propertyValue);
  }

  @Override
  public void manipulateCalendar() {

  }

  @Override
  public void backToCalendarToday() {

  }

  @Override
  public void searchAcrossCalendar(String keyword) {

  }

  @Override
  public void navigateToMonth(int year, int month) {
    this.currentYear = year;
    this.currentMonth = month;
    refreshCurrentMonth();
  }

  /**
   * Refresh the current month view with events from the model.
   */
  private void refreshCurrentMonth() {
    try {
      CalendarEntityInterface currentEntity = model.getCurrentCalendarEntity();
      if (currentEntity == null) {
        view.displayMonthView(currentYear, currentMonth, new HashMap<>());
        return;
      }
      Map<LocalDate, List<Event>> eventsMap = new HashMap<>();

      LocalDate firstDay = LocalDate.of(currentYear, currentMonth, 1);
      LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);

      for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
        List<Event> eventsOnDate = currentEntity.getCalendar().getEventsOnDate(date);
        if (eventsOnDate != null && !eventsOnDate.isEmpty()) {
          eventsMap.put(date, eventsOnDate);
        }
      }
      view.displayMonthView(currentYear, currentMonth, eventsMap);
    } catch (Exception e) {
      view.displayError("Failed to refresh calendar: " + e.getMessage());
      view.displayMonthView(currentYear, currentMonth, new HashMap<>());
    }
  }

  @Override
  public void viewEventsOnDate(LocalDate date) {
    try {
      CalendarEntityInterface currentEntity = model.getCurrentCalendarEntity();

      if (currentEntity == null) {
        view.displayError("No calendar selected!");
        return;
      }

      List<Event> events = currentEntity.getCalendar().getEventsOnDate(date);
      view.displayEventsOnDate(events, date);

    } catch (Exception e) {
      view.displayError("Failed to load events: " + e.getMessage());
    }
  }

  @Override
  public void exportCalendar(String fileName) {
    try {
      CalendarEntityInterface currentEntity = model.getCurrentCalendarEntity();

      if (currentEntity == null) {
        view.displayError("No calendar selected!");
        return;
      }
      String content;
      if (fileName.toLowerCase().endsWith(".csv")) {
        content = currentEntity.getCalendar().exportToCsv();
      } else if (fileName.toLowerCase().endsWith(".ical")) {
        content = currentEntity.getCalendar().exportToIcal(
            currentEntity.getCalendarName(),
            currentEntity.getTimeZone());
      } else {
        view.displayError("Invalid file format. Use .csv or .ical");
        return;
      }
      view.exportCalendar(content, fileName);

    } catch (Exception e) {
      view.displayError("Failed to export calendar: " + e.getMessage());
    }
  }

}
