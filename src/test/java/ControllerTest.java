import static org.junit.Assert.assertEquals;

import calendar.controller.CalendarController;
import calendar.model.Calendar;
import calendar.model.CalendarInterface;
import calendar.view.CalendarView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Test for ControllerTest Class.
 */
public class ControllerTest {

  //  create event Meeting on 2025-10-27
  //  print events on 2025-10-27
  //  Events on 2025-10-27:
  //   • Meeting from 08:00 to 17:00
  @Test
  public void testGoCoupledWithView() {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    System.setOut(out);
    // Appendable out = new StringBuffer();
    try {
      Reader in = new StringReader(
          "create event Meeting on 2025-10-27\nprint events on 2025-10-27\nexit\n");
      Calendar model = new Calendar();
      CalendarView view = new CalendarView();
      CalendarController controller = new CalendarController(model, view, in, out);
      controller.go();
      String allOuts = bytes.toString();
      String expectedOutput = "Events on 2025-10-27:\n • Meeting from 08:00 to 17:00\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testGoWithMockCreateEvent() {
    Reader in = new StringReader(
        "create event Meeting on 2025-10-27\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting on 2025-10-27\nprint events on 2025-10-27\nexit\n";
    // "Events on 2025-10-27:\n • Meeting from 08:00 to 17:00\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());

  }



}
