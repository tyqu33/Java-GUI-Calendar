import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import calendar.controller.CalendarController;
import calendar.model.CalendarInterface;
import calendar.model.MultiCalendarManager;
import calendar.model.MultiCalendarManagerInterface;
import calendar.view.CalendarView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for ControllerTest Class.
 */
public class ControllerTest {
  String premise;
  String use;

  /**
   * To set up the context of a calendar.
   */
  @Before
  public void setUp() {
    premise = "create calendar --name Meetings --timezone America/New_York\n";
    use = "use calendar --name Meetings\n";

  }

  @Test
  public void testGoExamineOutput() throws IOException {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
    System.setOut(out);
    try {
      Reader in = new StringReader(premise + use
          + "create event Meeting on 2025-10-27\nprint events on 2025-10-27\nexit\n");
      MultiCalendarManagerInterface manager = new MultiCalendarManager();
      CalendarView view = new CalendarView();
      CalendarController controller = new CalendarController(manager, view, in, out);
      controller.go();
      String allOuts = bytes.toString(StandardCharsets.UTF_8);
      String expectedOutput = "Success: Successfully created calendar 'Meetings'\n"
          + "Events on 2025-10-27:\n • Meeting from 8:00 AM to 5:00 PM\n";
      assertEquals(expectedOutput.trim(), allOuts.trim());
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testGoWithMockCreateEvent0() throws IOException {
    System.out.println("testGoWithMockCreateEvent0 STARTING=======\n");
    Reader in = new StringReader(
        "create event Meeting on 2025-10-27\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();
    CalendarView view = new CalendarView();

    CalendarInterface model = new MockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);

    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);

    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();
    System.out.println("testGoWithMockCreateEvent0 ENDING=======\n");
    String expectedOutput =
        "create event Meeting on 2025-10-27\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent1() throws IOException {
    Reader in = new StringReader(
        "create event Meeting on 2025-10-27 repeats M for 4 times \nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting on 2025-10-27 repeats M for 4 times\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent2()  throws IOException {
    Reader in = new StringReader(
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent3()  throws IOException {
    Reader in = new StringReader(
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "repeats M for 4 times\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00"
            + " repeats M for 4 times\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent4()  throws IOException {
    Reader in = new StringReader(
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "repeats M until 2025-11-17\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ThirdMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "repeats M until 2025-11-17\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockCreateEvent5()  throws IOException {
    Reader in = new StringReader(
        "create event Meeting on 2025-10-27 repeats M until 2025-11-17\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ForthMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "create event Meeting on 2025-10-27 repeats M until 2025-11-17\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }




  @Test
  public void testGoWithMockEditEvent0()  throws IOException {
    Reader in = new StringReader(
        "edit event subject Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "with Presentation\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit event subject Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "with Presentation\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent00()  throws IOException {
    Reader in = new StringReader(
        "edit event subject Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "with \"Long Long Meeting\"\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit event subject Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "with Long Long Meeting\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());

  }

  @Test
  public void testGoWithMockEditEvent1()  throws IOException {
    Reader in = new StringReader(
        "edit series description Meeting from 2025-10-27T09:00 "
            + "with \"Long Long Meeting\"\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new MockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit series description Meeting from 2025-10-27T09:00 "
            + "with Long Long Meeting\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent2()  throws IOException {
    Reader in = new StringReader(
        "edit events location Meeting from 2025-10-27T09:00 "
            + "with Boston\nprint events on 2025-10-27\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit events location Meeting from 2025-10-27T09:00 "
            + "with Boston\nprint events on 2025-10-27\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent3()  throws IOException {
    Reader in = new StringReader(
        "edit events start Meeting from 2025-10-27T09:00 with 2025-10-27T09:59\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ThirdMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit events start Meeting from 2025-10-27T09:00 with 2025-10-27T09:59\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent4()  throws IOException {
    Reader in = new StringReader(
        "edit events end Meeting from 2025-10-27T09:00 with 2025-10-27T12:59\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ForthMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit events end Meeting from 2025-10-27T09:00 with 2025-10-27T12:59\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent5()  throws IOException {
    Reader in = new StringReader(
        "edit event status Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "with private\nexit\n");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit event status Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with private\nexit\n";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent6()  throws IOException {
    Reader in = new StringReader(
        "edit event subject Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with Meeting \"");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    assertEquals(null, "New subject value is invalid\n", out.toString());
  }

  @Test
  public void testGoWithMockEditEvent7()  throws IOException {
    Reader in = new StringReader(
        "edit event location Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with Boston \"");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    assertEquals(null, "New location value is invalid\n", out.toString());
  }

  @Test
  public void testGoWithMockEditEvent8()  throws IOException {
    Reader in = new StringReader(
        "edit event description Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with tea \"");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    assertEquals(null, "New description value is invalid\n", out.toString());
  }

  @Test
  public void testGoWithMockEditEvent9()  throws IOException {
    Reader in = new StringReader(
        "edit event location Meeting from 2025-10-27T09:00 to 2025-10-27T12:00"
            + " with \"Boston Huntington Ave\"");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ThirdMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit event location Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 "
            + "with Boston Huntington Ave";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }

  @Test
  public void testGoWithMockEditEvent10()  throws IOException {
    Reader in = new StringReader(
        "edit event description Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with tea");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new ForthMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    String expectedOutput =
        "edit event description Meeting from 2025-10-27T09:00 to 2025-10-27T12:00 with tea";
    assertEquals(expectedOutput.trim(), log.toString().trim());
  }


  @Test
  public void testGoWithMockCreateEventExp0()  throws IOException {
    Reader in = new StringReader("create XXXX");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    assertEquals(null,
          "Create event failure. Wrong format: create XXXX\n", out.toString());

  }

  @Test
  public void testGoWithMockEditEventExp0()  throws IOException {
    Reader in = new StringReader("edit XXXX");
    StringBuilder log = new StringBuilder();
    String uniqueResult = "";
    StringBuffer out = new StringBuffer();

    CalendarInterface model = new SecondMockModel(log, uniqueResult);
    EntityMockModel entity = new EntityMockModel();
    entity.useThisCalendar(model);
    MultiCalendarManagerInterface manager = new MultiCalendarManagerMockModel();
    manager.useThisCalendarEntity(entity);
    CalendarView view = new CalendarView();
    CalendarController controller = new CalendarController(manager, view, in, out);
    controller.go();

    assertEquals(null,
        "Edit event failure. Wrong format: edit XXXX\n", out.toString());

  }
}
