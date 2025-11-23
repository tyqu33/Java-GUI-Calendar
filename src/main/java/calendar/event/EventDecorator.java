package calendar.event;

import calendar.enums.EventStatus;
import java.time.LocalDateTime;

public class EventDecorator implements EventInterface{
  String calendarName;
  String calendarTimezone;
  EventInterface event;

  public EventDecorator(String calendarName, String calendarTimezone, EventInterface event) {
    this.calendarName = calendarName;
    this.calendarTimezone = calendarTimezone;
    this.event = event;
  }

  public EventInterface getEvent() {
    return this.event;
  }

  public String getCalendarName() {
    return this.calendarName;
  }

  @Override
  public String getSubject() {
    return "";
  }

  @Override
  public LocalDateTime getStartDateTime() {
    return null;
  }

  @Override
  public LocalDateTime getEndDateTime() {
    return null;
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public String getLocation() {
    return "";
  }

  @Override
  public EventStatus getEventStatus() {
    return null;
  }

  @Override
  public String getSeriesId() {
    return "";
  }

  @Override
  public void editDescription(String newDescription) {

  }

  @Override
  public void editLocation(String newLocation) {

  }

  @Override
  public void editEventStatus(String newEventStatus) {

  }
}
