package calendar.controller;

import calendar.model.CalendarInterface;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateCommand extends CommandFactory {

  private static final Pattern C_FROM_TO_REPEATS_N = Pattern.compile(
      "^create event (.*?) from (\\S+) to (\\S+) repeats (\\S+) for (\\d+) times$");

  private static final Pattern C_FROM_TO_REPEATS_UNTIL = Pattern.compile(
      "^create event (.*?) from (\\S+) to (\\S+) repeats (\\S+) until (\\S+)$");

  private static final Pattern C_FROM_TO = Pattern.compile(
      "^create event (.*?) from (\\S+) to (\\S+)$");

  private static final Pattern C_ON_REPEATS_N = Pattern.compile(
      "^create event (.*?) on (\\S+) repeats (\\S+) for (\\d+) times$");

  private static final Pattern C_ON_REPEATS_UNTIL = Pattern.compile(
      "^create event (.*?) on (\\S+) repeats (\\S+) until (\\S+)$");

  private static final Pattern C_ON = Pattern.compile(
      "^create event (.*?) on (\\S+)$");

  private final CalendarInterface calendar;

  public CreateCommand(String commandLine, CalendarInterface calendar) {
    this.calendar = calendar;
    parseCommand(commandLine);
  }

  @Override
  public void parseCommand(String commandLine) {
    Matcher matcher;

    matcher = C_FROM_TO_REPEATS_N.matcher(commandLine);
    if (matcher.matches()) {
      subject = matcher.group(1);
      startDateTime = matcher.group(2);
      endDateTime = matcher.group(3);
      weekdays = matcher.group(4);
      repeatTimes = Integer.parseInt(matcher.group(5));

      this.calendar.createEventSeries(subject, startDateTime, endDateTime,
          description, location, eventStatus,
          weekdays, repeatTimes, null);
      return;
    }

    matcher = C_FROM_TO_REPEATS_UNTIL.matcher(commandLine);
    if (matcher.matches()) {
      subject = matcher.group(1);
      startDateTime = matcher.group(2);
      endDateTime = matcher.group(3);
      weekdays = matcher.group(4);
      seriesEndDateTime =  matcher.group(5);

      this.calendar.createEventSeries(subject, startDateTime, endDateTime,
          description, location, eventStatus,
          weekdays, 0, seriesEndDateTime);
      return;
    }

    matcher = C_FROM_TO.matcher(commandLine);
    if (matcher.matches()) {
      subject = matcher.group(1);
      startDateTime = matcher.group(2);
      endDateTime = matcher.group(3);

      this.calendar.createSingleEvent(subject, startDateTime, endDateTime,
          description, location, eventStatus, null);
      return;
    }

    matcher = C_ON_REPEATS_N.matcher(commandLine);
    if (matcher.matches()) {
      subject = matcher.group(1);
      startDateTime = matcher.group(2);
      weekdays = matcher.group(3);
      repeatTimes = Integer.parseInt(matcher.group(4));

      this.calendar.createEventSeries(subject, startDateTime, null,
          description, location, eventStatus,
          weekdays, repeatTimes, null);
      return;
    }

    matcher = C_ON_REPEATS_UNTIL.matcher(commandLine);
    if (matcher.matches()) {
      subject = matcher.group(1);
      startDateTime = matcher.group(2);
      weekdays = matcher.group(3);
      seriesEndDateTime =  matcher.group(4);

      this.calendar.createEventSeries(subject, startDateTime, null,
          description, location, eventStatus,
          weekdays, 0, seriesEndDateTime);
      return;
    }

    matcher = C_ON.matcher(commandLine);
    if (matcher.matches()) {
      subject = matcher.group(1);
      startDateTime = matcher.group(2);
      this.calendar.createSingleEvent(subject, startDateTime, null,
          description, location, eventStatus, null);
    }
    System.err.println("Create event failure. Wrong format: " + commandLine);
  }

}
