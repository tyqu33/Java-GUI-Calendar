import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import calendar.event.Event;
import calendar.util.CsvExporter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Test class for CSVExporter.
 */
public class CsvExporterTest {

  @Test
  public void testExportEvent() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .description("Team meeting")
        .location("Room A")
        .status("public")
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("Subject,Start Date,Start Time,End Date,End Time"));
    assertTrue(csv.contains("Meeting"));
    assertTrue(csv.contains("05/01/2025"));
    assertTrue(csv.contains("10:00 AM"));
    assertTrue(csv.contains("11:00 AM"));
    assertTrue(csv.contains("Team meeting"));
    assertTrue(csv.contains("Room A"));
  }

  @Test
  public void testExportAllDayEvent() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Holiday", LocalDateTime.parse("2025-05-01T08:00"))
        .setAllDayEvent()
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("Holiday"));
    assertTrue(csv.contains("True")); // All Day Event = True
  }

  @Test
  public void testExportPrivateEvent() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Personal", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .status("private")
        .build());

    String csv = CsvExporter.exportToCsv(events);

    String[] lines = csv.split("\n");
    assertTrue(lines[1].endsWith("True")); // Private = True
  }

  @Test
  public void testEscape1() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting, with \"quotes\"", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("\"Meeting, with \"\"quotes\"\"\""));
  }

  @Test
  public void testEscape2() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .description("Description with, commas and \"quotes\"")
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("\"Description with, commas and \"\"quotes\"\"\""));
  }

  @Test
  public void testExportEmptyList() {
    List<Event> events = new ArrayList<>();
    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("Subject,Start Date"));
    assertEquals(1, csv.split("\n").length);
  }

  @Test
  public void testExportMultiEvents() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Event1", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .build());
    events.add(Event.builder("Event2", LocalDateTime.parse("2025-05-02T14:00"))
        .end(LocalDateTime.parse("2025-05-02T15:00"))
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("Event1"));
    assertTrue(csv.contains("Event2"));
    String[] lines = csv.split("\n");
    assertEquals(3, lines.length);
  }

  @Test
  public void testExportNullFields() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Basic", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("Basic"));
    assertFalse(csv.contains("null"));
  }

  @Test
  public void testExportEventWithoutEnd() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting", LocalDateTime.parse("2025-05-01T10:00"))
        .description("Team meeting")
        .location("Room A")
        .status("public")
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("Subject,Start Date,Start Time"));
    assertTrue(csv.contains("Meeting"));
    assertTrue(csv.contains("05/01/2025"));
    assertTrue(csv.contains("10:00 AM"));
    assertTrue(csv.contains("Team meeting"));
    assertTrue(csv.contains("Room A"));
  }

  @Test
  public void testEscape3() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Multi-line\nTitle", LocalDateTime.parse("2025-05-01T10:00"))
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("\"Multi-line\nTitle\""));
  }

  @Test
  public void testEscape4() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting 1, room 1", LocalDateTime.parse("2025-05-01T10:00"))
        .build());

    String csv = CsvExporter.exportToCsv(events);

    assertTrue(csv.contains("\"Meeting 1, room 1\""));
    assertFalse(csv.contains("\"\""));
  }
}
