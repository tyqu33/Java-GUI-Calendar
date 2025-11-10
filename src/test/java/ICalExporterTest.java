import static org.junit.Assert.assertTrue;

import calendar.event.Event;
import calendar.util.ICalExporter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ICalExporterTest {
  @Test
  public void testExportToICalBasic() {
    List<Event> events = new ArrayList<>();
    Event event = Event.builder("Meeting", LocalDateTime.parse("2025-12-01T10:00"))
        .end(LocalDateTime.parse("2025-12-01T11:00"))
        .description("Team meeting")
        .location("Office")
        .status("public")
        .build();
    events.add(event);
    String ical = ICalExporter.exportToICal(events, "Work Calendar",
        ZoneId.of("America/New_York"));

    assertTrue(ical.contains("BEGIN:VCALENDAR"));
    assertTrue(ical.contains("END:VCALENDAR"));
    assertTrue(ical.contains("BEGIN:VEVENT"));
    assertTrue(ical.contains("END:VEVENT"));
    assertTrue(ical.contains("SUMMARY:Meeting"));
    assertTrue(ical.contains("DESCRIPTION:Team meeting"));
    assertTrue(ical.contains("LOCATION:Office"));
    assertTrue(ical.contains("CLASS:PUBLIC"));
  }

  @Test
  public void testExportToICalAllDay() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Holiday", LocalDateTime.parse("2025-12-01T08:00"))
        .setAllDayEvent()
        .build());
    String ical = ICalExporter.exportToICal(events, "Cal", ZoneId.of("America/New_York"));
    assertTrue(ical.contains("DTSTART;VALUE=DATE:20251201"));
    assertTrue(ical.contains("DTEND;VALUE=DATE:20251202"));
  }

  @Test
  public void testExportToICalMulti() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Event1", LocalDateTime.parse("2025-12-01T10:00"))
        .end(LocalDateTime.parse("2025-12-01T11:00"))
        .build());
    events.add(Event.builder("Event2", LocalDateTime.parse("2025-12-02T14:00"))
        .end(LocalDateTime.parse("2025-12-02T15:00"))
        .build());
    String ical = ICalExporter.exportToICal(events, "Cal", ZoneId.of("UTC"));

    assertTrue(ical.contains("SUMMARY:Event1"));
    assertTrue(ical.contains("SUMMARY:Event2"));
  }

  @Test
  public void testExportToICalEscape() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Event", LocalDateTime.parse("2025-12-01T10:00"))
        .end(LocalDateTime.parse("2025-12-01T11:00"))
        .description("Text with,;\n")
        .location("Office")
        .build());
    String ical = ICalExporter.exportToICal(events, "Cal", ZoneId.of("UTC"));

    assertTrue(ical.contains("\\,"));
    assertTrue(ical.contains("\\;"));
    assertTrue(ical.contains("\\n"));
  }
}
