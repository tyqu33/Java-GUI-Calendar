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

    Event event = Event.builder("Meeting", LocalDateTime.parse("2024-12-01T10:00"))
        .end(LocalDateTime.parse("2024-12-01T11:00"))
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
}
