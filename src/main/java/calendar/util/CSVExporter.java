package calendar.util;

import calendar.event.Event;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVExporter {
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("hh:mm a");

  public static String exportToCSV(List<Event> events) {
    StringBuilder csv = new StringBuilder();
    csv.append("Subject,Start Date,Start Time,End Date,End Time,");
    csv.append("All Day Event,Description,Location,Private\n");
    for (Event event : events) {
      csv.append(formatEventAsCSV(event));
    }

    return csv.toString();
  }

  private static String formatEventAsCSV(Event event) {
    StringBuilder line = new StringBuilder();
// Subject
    line.append(escapeCSV(event.getSubject())).append(",");

    // Start Date
    line.append(event.getStartDateTime().format(DATE_FORMATTER)).append(",");

    // Start Time
    if (event.isAllDayEvent()) {
      line.append(",");
    } else {
      line.append(event.getStartDateTime().format(TIME_FORMATTER)).append(",");
    }

    // End Date
    if (event.getEndDateTime() != null) {
      line.append(event.getEndDateTime().format(DATE_FORMATTER)).append(",");
    } else {
      line.append(",");
    }
    // End Time
    if (event.isAllDayEvent() || event.getEndDateTime() == null) {
      line.append(",");
    } else {
      line.append(event.getEndDateTime().format(TIME_FORMATTER)).append(",");
    }

    // All Day Event
    line.append(event.isAllDayEvent() ? "True" : "False").append(",");

    // Description
    line.append(escapeCSV(event.getDescription())).append(",");

    // Location
    line.append(escapeCSV(event.getLocation())).append(",");
    // Private
    line.append(event.getEventStatus().toString().equalsIgnoreCase("PRIVATE") ? "True" : "False");

    line.append("\n");
    return line.toString();
  }

  private static String escapeCSV(String value) {
    if (value == null || value.isEmpty()) {
      return "";
    }
    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }
}

