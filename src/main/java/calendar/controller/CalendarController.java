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

  public CalendarController(Calendar calendar, CalendarView view) {
    this.calendar = new Calendar();
    this.view = view;
  }
  public void runInteractiveMode() {
    view.displayWelcome();

    Scanner consoleScanner = new Scanner(System.in);
    String commandLine;

    while (true) {
      System.out.print("> ");
      commandLine = consoleScanner.nextLine();

      if (commandLine.equalsIgnoreCase("exit")) {
        break;
      }

      processCommand(commandLine.trim());
    }
    consoleScanner.close();
  }

  public void runHeadlessMode(String fileName) {
    File commandFile = new File(fileName);
    try {
      Scanner fileScanner = new Scanner(commandFile);
      while (fileScanner.hasNextLine()) {
        String commandLine = fileScanner.nextLine();
        if (commandLine.equalsIgnoreCase("exit")) {
          break;
        }
        if (commandLine.trim().isEmpty()) {
          continue;
        }
        processCommand(commandLine.trim());
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
  }

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
        break;
      case "edit":
        CommandFactory editEvent = new EditCommand(commandLine, calendar);
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
