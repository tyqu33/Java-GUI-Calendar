package calendar.controller;

import calendar.calendarentity.CalendarEntity;
import calendar.calendarentity.CalendarEntityInterface;
import calendar.event.EventInterface;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarViewInterface;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GuiCalendarController implements Features {
  private MultiCalendarManagerInterface model;
  private CalendarViewInterface view;

  public GuiCalendarController(MultiCalendarManagerInterface model, CalendarViewInterface view) {
    this.model = model;
    this.view = view;
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
}
