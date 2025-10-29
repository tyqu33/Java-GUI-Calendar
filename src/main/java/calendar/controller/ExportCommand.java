package calendar.controller;

import calendar.model.CalendarInterface;
import calendar.view.CalendarView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportCommand extends CommandFactory{
  private static final Pattern EXPORT = Pattern.compile("^export cal (\\S+)$");
  private final CalendarInterface calendar;
  private final CalendarView view;
  private final String commandLine;

  public ExportCommand(String commandLine, CalendarInterface calendar, CalendarView view) {
    this.calendar = calendar;
    this.view = view;
    this.commandLine = commandLine;
  }

  @Override
  protected void parseCommand(String commandLine) {
    Matcher matcher = EXPORT.matcher(commandLine);
    try {
      if (matcher.matches()) {
        String fileName = matcher.group(1).trim();
        String csvContent = calendar.exportToCSV();
        view.exportCalendar(csvContent, fileName);
        return;
      }
      view.displayError("Export failed. Invalid command: " + commandLine);
    } catch (Exception e) {
      view.displayError("Export failed." + e.getMessage());
    }
  }

  @Override
  protected void execute() {
    parseCommand(commandLine);
  }
}
