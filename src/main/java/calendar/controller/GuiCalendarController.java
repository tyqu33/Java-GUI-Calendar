package calendar.controller;

import calendar.calendarentity.CalendarEntityInterface;
import calendar.event.EventInterface;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarViewInterface;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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
  public void switchCalendar(String calendarName) {
    CalendarEntityInterface entity = model.getCalendarEntity(calendarName);
    if (entity != null) {
      model.useThisCalendarEntity(entity);
    }
  }

  @Override
  public void editCalendarTimezone(String calendarName, String timeZone) {

  }

  @Override
  public void editCalendarName(String calendarName, String timeZone) {

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
