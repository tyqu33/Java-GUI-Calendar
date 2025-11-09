package calendar.controller;

import calendar.calendarEntity.CalendarEntityInterface;
import calendar.model.MultiCalendarManager;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UseCalendarCommand extends CommandFactory {
  private static final Pattern USE_CALENDAR = Pattern.compile(
      "^use calendar --name (?:(\\S+)|\"([^\"]*)\")$");

  private final MultiCalendarManagerInterface manager;
  private final CalendarView view;
  private final String commandLine;

  public UseCalendarCommand(String commandLine, MultiCalendarManagerInterface manager, CalendarView view) {
    this.commandLine = commandLine;
    this.manager = manager;
    this.view = view;
  }

  @Override
  protected void parseCommand(String commandLine) {
    Matcher matcher = USE_CALENDAR.matcher(commandLine);
    if (matcher.matches()) {
      String calendarName =
          ((matcher.group(1) != null) ? matcher.group(1).trim() : matcher.group(2)).trim();

      CalendarEntityInterface entity = manager.getCalendarEntity(calendarName);
      manager.useThisCalendarEntity(entity);
    }
  }

  @Override
  protected void execute() {
    parseCommand(commandLine);
  }
}
