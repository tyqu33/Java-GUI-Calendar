import calendar.controller.CalendarController;
import calendar.model.Calendar;
import calendar.view.CalendarView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    Readable input;
    Appendable output = System.out;

    try {
      input = verifyAndGetInput(args);
      if (input == null) {
        return;
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      return;
    }
    //    if (args.length == 0 || !args[0].equals("--mode") || args.length < 2) {
    //      System.out.println("Error: Wrong usage of running Calendar. Usage: java CalendarRunner --mode [interactive|headless] [command.txt]");
    //      return;
    //    }
    //    String mode = args[1];
    //    if (mode.equals("interactive")) {
    //      // calendarController.runInteractiveMode();
    //      input = new InputStreamReader(System.in);
    //
    //    } else if (mode.equals("headless")) {
    //      if (args.length != 3) {
    //        System.out.println("Error: Headless mode needs a third argument [command.txt].");
    //        return;
    //      }
    //      String fileName = args[2];
    //      // calendarController.runHeadlessMode(fileName);
    //      try {
    //        input = new FileReader(fileName);
    //      } catch (FileNotFoundException e) {
    //        throw new RuntimeException(e);
    //      }
    //    } else {
    //      System.out.println("Error: Unknown mode " + mode);
    //      return;
    //    }
    if (args[1].equals("interactive")) {
      calendarView.displayWelcome();
    }
    CalendarController calendarController = new CalendarController(calendarModel, calendarView, input, output);
    calendarController.go();
    if (input instanceof java.io.FileReader) {
      try {
        ((FileReader) input).close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }

  private static Readable verifyAndGetInput(String[] args) throws FileNotFoundException {
    if (args.length == 0 || !args[0].equals("--mode") || args.length < 2) {
      System.out.println("Error: Wrong usage of running Calendar. Usage: java CalendarRunner --mode [interactive|headless] [command.txt]");
      return null;
    }

    String mode = args[1];
    if (mode.equals("interactive")) {

      // calendarController.runInteractiveMode();
      return new InputStreamReader(System.in);

    } else if (mode.equals("headless")) {
      if (args.length != 3) {
        System.out.println("Error: Headless mode needs a third argument [command.txt].");
        return null;
      }
      String fileName = args[2];
      // calendarController.runHeadlessMode(fileName);
      return new FileReader(fileName);
    } else {
      System.out.println("Error: Unknown mode " + mode);
      return null;
    }
  }
}
