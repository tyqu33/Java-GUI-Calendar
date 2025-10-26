package calendar.controller;

import calendar.model.CalendarInterface;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditCommand extends CommandFactory {
  private String oldProperty;
  private String newPropertyValue;
  private String newSubject = null;
  private String newStartDateTime = null;
  private String newEndDateTime = null;
  private String newDescription = null;
  private String newLocation = null;
  private String newEventStatus = null;


  private static final Pattern E_P_FROM_TO_WNP = Pattern.compile(
      "^edit event (\\S+) (?:(\\S+)|\"([^\"]*)\") from (\\S+) to (\\S+) with (.*?)$");

  private static final Pattern ES_P_FROM_WNP = Pattern.compile(
      "^edit events (\\S+) (?:(\\S+)|\"([^\"]*)\") from (\\S+) with (.*?)$");

  private static final Pattern S_P_FROM_WNP = Pattern.compile(
      "^edit series (\\S+) (?:(\\S+)|\"([^\"]*)\") from (\\S+) with (.*?)$");


  private static final Pattern NEW_VALUE_PARSER = Pattern.compile("^(?:(\\S+)|\"([^\"]*)\")$");

  private final CalendarInterface calendar;

  public EditCommand(String commandLine, CalendarInterface calendar) {
    this.calendar = calendar;
    parseCommand(commandLine);
  }

  @Override
  protected void parseCommand(String commandLine) {
    Matcher matcher;

    matcher = E_P_FROM_TO_WNP.matcher(commandLine);
    if (matcher.matches()) {
      oldProperty = matcher.group(1).trim();
      subject = ((matcher.group(2) != null) ? matcher.group(2) : matcher.group(3)).trim();
      startDateTime = matcher.group(4).trim();
      endDateTime = matcher.group(5).trim();
      newPropertyValue = matcher.group(6).trim();

      getNewPropertyValue(oldProperty, newPropertyValue);
      this.calendar.editSingleEvent(subject, startDateTime, endDateTime, newSubject, newStartDateTime, newEndDateTime, newDescription, newLocation, newEventStatus);
      return;
    }

    matcher = ES_P_FROM_WNP.matcher(commandLine);
    if (matcher.matches()) {
      oldProperty = matcher.group(1).trim();
      subject = ((matcher.group(2) != null) ? matcher.group(2) : matcher.group(3)).trim();
      startDateTime = matcher.group(4).trim();
      newPropertyValue = matcher.group(5).trim();

      getNewPropertyValue(oldProperty, newPropertyValue);
      this.calendar.editEventSeries(subject, startDateTime, endDateTime, newSubject, newStartDateTime, newEndDateTime, newDescription, newLocation, newEventStatus);
      return;
    }

    matcher = S_P_FROM_WNP.matcher(commandLine);
    if (matcher.matches()) {
      oldProperty = matcher.group(1).trim();
      subject = ((matcher.group(2) != null) ? matcher.group(2) : matcher.group(3)).trim();
      startDateTime = matcher.group(4).trim();
      newPropertyValue = matcher.group(5).trim();

      getNewPropertyValue(oldProperty, newPropertyValue);
      this.calendar.editEventSeries(subject, startDateTime, endDateTime, newSubject, newStartDateTime, newEndDateTime, newDescription, newLocation, newEventStatus);
      return;
    }

  }

  private void getNewPropertyValue(String oldProperty, String newPropertyValue)
    throws IllegalArgumentException {
    switch (oldProperty) {
      case "subject": {
        Matcher mValue = NEW_VALUE_PARSER.matcher(newPropertyValue);
        if (mValue.find()) {
          newSubject = (mValue.group(1) != null ? mValue.group(1) : mValue.group(2)).trim();
        } else {
          throw new IllegalArgumentException("New property value is invalid");
        }
      }
      break;
      case "location": {
        Matcher mValue = NEW_VALUE_PARSER.matcher(newPropertyValue);
        if (mValue.find()) {
          newLocation = (mValue.group(1) != null ? mValue.group(1) : mValue.group(2)).trim();
        } else {
          throw new IllegalArgumentException("New property value is invalid");
        }
      }
      break;
      case "description": {
        Matcher mValue = NEW_VALUE_PARSER.matcher(newPropertyValue);
        if (mValue.find()) {
          newDescription = (mValue.group(1) != null ? mValue.group(1) : mValue.group(2)).trim();
        } else {
          throw new IllegalArgumentException("New property value is invalid");
        }
      }
      break;

      case "start": newStartDateTime = newPropertyValue; break;
      case "end": newEndDateTime = newPropertyValue; break;
      case "status": newEventStatus = newPropertyValue; break;
      default:
    }
  }

}
