package calendar.controller;

import calendar.model.MultiCalendarManager;
import calendar.view.CalendarView;
import java.util.regex.Pattern;

public class UseCalendarCommand extends CommandFactory{
  private static final Pattern USE_CALENDAR = Pattern.compile(
      "^use calendar --name (\\S+)$");

  private final MultiCalendarManager manager;
  private final CalendarView view;
  private final String commandLine;

  public UseCalendarCommand(String commandLine, MultiCalendarManager manager, CalendarView view) {
    this.commandLine = commandLine;
    this.manager = manager;
    this.view = view;
  }

  @Override
  protected void parseCommand(String commandLine) {

  }

  @Override
  protected void execute() {
    parseCommand(commandLine);
  }
}
