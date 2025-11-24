import calendar.controller.Features;
import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.view.CalendarViewInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This is a mock view to replace the JframeCalendarView for testing the controller in isolation.
 */
public class MockGuiView implements CalendarViewInterface {
  private StringBuilder log;

  /**
   * Constructor for MockGuiView.
   *
   * @param log the logging
   */
  public MockGuiView(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void displayMonthView(int year, int month, Map<LocalDate, List<Event>> events) {
    log.append("displayMonthView: " + year + ", " + month + ", " + events.toString() + "\n");
  }

  @Override
  public void displayCurrentCalendar(String calendarName, String timezone) {
    log.append("displayCurrentCalendar: " + calendarName + ", " + timezone + "\n");
  }

  @Override
  public void displayAvailableCalendars(List<String> calendarNames) {
    log.append("displayAvailableCalendars: " + calendarNames.toString() + "\n");
  }

  @Override
  public void displayEventsOnDate(List<Event> events, LocalDate date) {
    log.append("displayEventsOnDate: " + date.toString() + "\n");
  }

  @Override
  public void displayEventsBetween(List<Event> events, LocalDateTime start, LocalDateTime end) {

  }

  @Override
  public void displayUserStatus(UserStatus status) {

  }

  @Override
  public void exportCalendar(String content, String fileName) {

  }

  @Override
  public void displaySuccess(String message) {
    log.append("displaySuccess: " + message + "\n");

  }

  @Override
  public void displayError(String error) {
    log.append("displayError: " + error + "\n");
  }

  @Override
  public void displayWarning(String warning) {

  }

  @Override
  public void displayWelcome() {

  }

  @Override
  public void makeVisible() {
    log.append("makeVisible\n");
  }

  @Override
  public void refresh() {
    log.append("refresh\n");
  }

  @Override
  public void addFeatures(Features feature) {

  }
}
