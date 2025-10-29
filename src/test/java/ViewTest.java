import static org.junit.Assert.assertTrue;

import calendar.event.Event;
import calendar.view.CalendarView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ViewTest {
  private StringBuilder output;
  private CalendarView view;

  @Before
  public void setUp() {
    output = new StringBuilder();
    view = new CalendarView(output);
  }

  @Test
  public void testDisplayWelcome() {
    view.displayWelcome();

    String result = output.toString();
    assertTrue(result.contains("Virtual Calendar Application"));
    assertTrue(result.contains("Type 'exit' to quit"));
    assertTrue(result.contains("================================="));
  }

  @Test
  public void testDisplayEventsOnDate() {
    List<Event> events = new ArrayList<>();
    events.add(Event.builder("Meeting", LocalDateTime.parse("2025-05-01T10:00"))
        .end(LocalDateTime.parse("2025-05-01T11:00"))
        .location("Room A")
        .build());
    events.add(Event.builder("Lunch", LocalDateTime.parse("2025-05-01T12:00"))
        .end(LocalDateTime.parse("2025-05-01T13:00"))
        .location("Room B")
        .build());

    view.displayEventsOnDate(events, LocalDate.parse("2025-05-01"));

    String result = output.toString();
    assertTrue(result.contains("Events on 2025-05-01:"));
    assertTrue(result.contains("Meeting"));
    assertTrue(result.contains("10:00"));
    assertTrue(result.contains("11:00"));
    assertTrue(result.contains("Room A"));
    assertTrue(result.contains("Lunch"));
    assertTrue(result.contains("12:00"));
    assertTrue(result.contains("Room B"));
  }
}
