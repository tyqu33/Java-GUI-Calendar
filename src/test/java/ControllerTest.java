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
  //   • Meeting from 8:00 AM to 5:00 PM
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
      String expectedOutput = "Events on 2025-10-27:\n • Meeting from 8:00 AM to 5:00 PM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testGoWithMockCreateEvent0() {
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

  @Test
  public void testGoWithMockCreateEvent1() {
    Reader in = new StringReader(
        "create event Meeting on 2025-10-27 repeats M for 4 times \nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting on 2025-10-27 repeats M for 4 times\nexit\n";
    // "Events on 2025-10-27:\n • Meeting from 08:00 to 17:00\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent2() {
    Reader in = new StringReader(
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00\nexit\n";
    // "Events on 2025-10-27:\n • Meeting from 08:00 to 17:00\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent3() {
    Reader in = new StringReader(
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 repeats M for 4 times\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 repeats M for 4 times\nexit\n";
    // "Events on 2025-10-27:\n • Meeting from 08:00 to 17:00\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent4() {
    Reader in = new StringReader(
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 repeats M until 2025-11-17\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ThirdMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 repeats M until 2025-11-17\nexit\n";
    // "Events on 2025-10-27:\n • Meeting from 08:00 to 17:00\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent5() {
    Reader in = new StringReader(
        "create event Meeting on 2025-10-27 repeats M until 2025-11-17\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ForthMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting on 2025-10-27 repeats M until 2025-11-17\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }




  @Test
  public void testGoWithMockEditEvent0() {
    Reader in = new StringReader(
        "edit event subject Meeting from 2025-10-27T09:00 to 2025-10-2712:00 with Presentation\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "edit event subject Meeting from 2025-10-27T09:00 to 2025-10-2712:00 with Presentation\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent1() {
    Reader in = new StringReader(
        "edit series description Meeting from 2025-10-27T09:00 with \"Long Long Meeting\"\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "edit series description Meeting from 2025-10-27T09:00 with Long Long Meeting\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent2() {
    Reader in = new StringReader(
        "edit events location Meeting from 2025-10-27T09:00 with Boston\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "edit events location Meeting from 2025-10-27T09:00 with Boston\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent3() {
    Reader in = new StringReader(
        "edit events start Meeting from 2025-10-27T09:00 with 2025-10-27T09:59\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ThirdMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "edit events start Meeting from 2025-10-27T09:00 with 2025-10-27T09:59\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent4() {
    Reader in = new StringReader(
        "edit events end Meeting from 2025-10-27T09:00 with 2025-10-27T12:59\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ForthMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "edit events end Meeting from 2025-10-27T09:00 with 2025-10-27T12:59\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent5() {
    Reader in = new StringReader(
        "edit event status Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with private\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(model, view, in, out);
    controller.go();

    String expectedOutput =
        "edit event status Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with private\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }


















}
