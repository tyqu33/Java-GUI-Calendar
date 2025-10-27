package calendar.view;

import calendar.enums.UserStatus;
import calendar.event.Event;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarView {
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("HH:mm");

  public void displayEventsOnDate(List<Event> events, LocalDate Date) {
    if (events == null || events.isEmpty()) {
      System.out.println("No events on" + DATE_FORMATTER.format(Date));
      return;
    }
    System.out.println("Events on " + DATE_FORMATTER.format(Date) + ":");
    for (Event event : events) {
      System.out.print(" • " + event.getSubject());
      System.out.print(" from " + event.getStartDateTime().format(TIME_FORMATTER));
      if (event.getEndDateTime() != null) {
        System.out.print(" to " + event.getEndDateTime().format(TIME_FORMATTER));
      }
      if (event.getLocation() != null && !event.getLocation().isEmpty()) {
        System.out.print(" at " + event.getLocation());
      }
      System.out.println();
    }
  }

  public void displayEventsBetween(List<Event> events, LocalDateTime start, LocalDateTime end) {
    if (events == null || events.isEmpty()) {
      System.out.println("No events between " + TIME_FORMATTER.format(start) + " and " + TIME_FORMATTER.format(end));
      return;
    }
    System.out.println("Events from " + TIME_FORMATTER.format(start) + " to " + TIME_FORMATTER.format(end) + ":");
    for (Event event : events) {
      System.out.print(" • " + event.getSubject());
      System.out.print(" starting on " + event.getStartDateTime().toLocalDate().format(DATE_FORMATTER));
      System.out.print(" at " + event.getEndDateTime().format(TIME_FORMATTER));
      if (event.getEndDateTime() != null) {
        System.out.print(", ending on " + event.getEndDateTime().toLocalDate().format(DATE_FORMATTER));
        System.out.print(" at " + event.getEndDateTime().format(TIME_FORMATTER));
      }
      if (event.getLocation() != null && !event.getLocation().isEmpty()) {
        System.out.print(" at " + event.getLocation());
      }
      System.out.println();
    }
  }

  public void displayUserStatus(UserStatus status) {
    if (status == UserStatus.BUSY) {
      System.out.println("busy");
    } else {
      System.out.println("available");
    }
  }

  public void exportCalendar(String content, String fileName) {
    try {
      Path filePath  = Paths.get(fileName);
      Files.write(filePath, content.getBytes());
      System.out.println("Exported to" + filePath.toAbsolutePath());
    } catch (IOException e) {
      displayError("Export failed: " + e.getMessage());
    }
  }

  public void displaySuccess(String message) {
    System.out.println("Suceess:" + message);
  }

  public void displayError(String error) {
    System.err.println("Error" + error);
  }

  public void displayWarning(String warning) {
    System.out.println("Warning:" + warning);
  }

  public void displayWelcome() {
    System.out.println("=================================");
    System.out.println("  Virtual Calendar Application  ");
    System.out.println("=================================");
    System.out.println("Type 'exit' to quit\n");
  }
}
