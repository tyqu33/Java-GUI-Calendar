package calendar.controller;

import calendar.model.Calendar;
import calendar.model.CalendarInterface;
import calendar.view.CalendarView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CalendarController implements CalendarControllerInterface{
  private final CalendarInterface calendar;
  private final CalendarView view;
  private final Readable input;
  private final Appendable output;

  public CalendarController(Calendar calendar, CalendarView calendarView, Readable input, Appendable output) {
    this.calendar = calendar;
    this.view = calendarView;
    this.input = input;
    this.output = output;
  }

  public void go() {
    Scanner scanner = new Scanner(this.input);
    String commandLine;
    boolean exitFlag = true;
    while (scanner.hasNextLine() && exitFlag) { // java cant judge int as bool
      commandLine = scanner.nextLine();
      if (commandLine.trim().isEmpty()) {
        continue;
      }
      if (commandLine.equalsIgnoreCase("exit")) {
        exitFlag = false;
        break;
      }
      processCommand(commandLine.trim());
    }
  }
  //  public void runInteractiveMode() {
  //    view.displayWelcome();
  //
  //    Scanner consoleScanner = new Scanner(System.in); // bad design! coupled with specific input source
  //    String commandLine;
  //
  //    while (true) {
  //      System.out.print("> ");
  //      commandLine = consoleScanner.nextLine();
  //
  //      if (commandLine.equalsIgnoreCase("exit")) {
  //        break;
  //      }
  //
  //      processCommand(commandLine.trim());
  //    }
  //    consoleScanner.close();
  //  }
  //
  //  public void runHeadlessMode(String fileName) {
  //    File commandFile = new File(fileName);
  //    try {
  //      Scanner fileScanner = new Scanner(commandFile); // bad design! coupled with specific input source
  //      while (fileScanner.hasNextLine()) {
  //        String commandLine = fileScanner.nextLine();
  //        if (commandLine.equalsIgnoreCase("exit")) {
  //          break;
  //        }
  //        if (commandLine.trim().isEmpty()) {
  //          continue;
  //        }
  //        processCommand(commandLine.trim());
  //      }
  //    } catch (FileNotFoundException e) {
  //      System.out.println("File not found");
  //    }
  //  }

  private void processCommand(String commandLine) {
    String commandType = "";
    int indexFirstSpace = commandLine.indexOf(' ');
    if (indexFirstSpace != -1) {
      commandType = commandLine.substring(0, indexFirstSpace);
    } else {
      commandType = commandLine;
    }
    switch (commandType) {
      case "create":
        CommandFactory createEvent = new CreateCommand(commandLine, calendar);
        createEvent.execute();
        break;
      case "edit":
        CommandFactory editEvent = new EditCommand(commandLine, calendar);
        editEvent.execute();
        break;
      case "print":
        CommandFactory printEvent = new PrintCommand(commandLine, calendar, view);
        break;
      case "export":
        CommandFactory exportEvent = new ExportCommand(commandLine, calendar, view);
        break;
      case "show":
        CommandFactory shoeEvent = new ExportCommand(commandLine, calendar, view);
        break;
      case "exit":
        break;
      default:
        System.out.println("Invalid command line");
    }
  }
}
