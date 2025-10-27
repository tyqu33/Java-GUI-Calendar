package calendar.controller;

import calendar.enums.UserStatus;
import calendar.model.CalendarInterface;
import calendar.view.CalendarView;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowCommand extends CommandFactory{

  private static final Pattern SHOW_STATUS =  Pattern.compile("^show status on (\\S+)$");

  private final CalendarInterface calendar;
  private final CalendarView view;

  public ShowCommand(String commandline, CalendarInterface calendar, CalendarView view) {
    this.calendar = calendar;
    this.view = view;
    parseCommand(commandline);
  }

  @Override
  protected void parseCommand(String commandLine) {
    Matcher matcher = SHOW_STATUS.matcher(commandLine);
    try {
      if (matcher.matches()) {
        String queryDateTime = matcher.group(1).trim();
        LocalDateTime queryTime = LocalDateTime.parse(queryDateTime);
        UserStatus status = calendar.getUserStatus(queryTime);
        view.displayUserStatus(status);
        return;
      }
      view.displayError("Show status failed. Invalid command: " + commandLine);
    } catch (Exception e) {
      view.displayError("Show status failed. " + e.getMessage());
    }
  }
}
