import calendar.controller.CalendarController;
import calendar.model.Calendar;
import calendar.view.CalendarView;

/**
 * Program runner.
 */
public class CalendarRunner {

  public CalendarRunner() {
  }
  /**
   * The main method placeholder.
   */
  public static void main(String[] args) {
    Calendar calendarModel = new Calendar();
    CalendarView calendarView = new CalendarView();
    CalendarController calendarController = new CalendarController(calendarModel, calendarView);

    if (args.length == 0 || !args[0].equals("--mode") || args.length < 2) {
      System.out.println("Error: Wrong usage of running Calendar. Usage: java CalendarRunner --mode [interactive|headless] [command.txt]");
      return;
    }
    String mode = args[1];
    if (mode.equals("interactive")) {
      calendarController.runInteractiveMode();

    } else if (mode.equals("headless")) {
      if (args.length != 3) {
        System.out.println("Error: Headless mode needs a third argument [command.txt].");
        return;
      }
      String fileName = args[2];
      calendarController.runHeadlessMode(fileName);
    } else {
      System.out.println("Error: Unknown mode " + mode);
      return;
    }







  }
}
