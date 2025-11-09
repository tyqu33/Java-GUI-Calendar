package calendar.controller;

import calendar.model.MultiCalendarManager;
import calendar.view.CalendarView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditCalendarCommand extends CommandFactory{
  private static final Pattern EDIT_CALENDAR = Pattern.compile(
      "^edit calendar --name (\\S+) --property (\\S+) (.+)$");

  private final MultiCalendarManager manager;
  private final CalendarView view;
  private final String commandLine;

  public EditCalendarCommand(String commandLine, MultiCalendarManager manager, CalendarView view) {
    this.commandLine = commandLine;
    this.manager = manager;
    this.view = view;
  }

  @Override
  protected void parseCommand(String commandLine) {
    Matcher matcher = EDIT_CALENDAR.matcher(commandLine);
    if (matcher.matches()) {
      String name = matcher.group(1).trim();
      String property = matcher.group(2).trim();
      String value = matcher.group(3).trim();
      try {
        manager.editCalendar(name, property, value);
        view.displaySuccess("Calendar " + name + " updated: " +
            property + " = " + value);
      } catch (IllegalArgumentException e) {
        view.displayError("Fail to edit calendar" + e.getMessage());
      }
    } else  {
      view.displayError("Invalid command");
    }
  }

  @Override
  protected void execute() {
    parseCommand(commandLine);
  }
}
