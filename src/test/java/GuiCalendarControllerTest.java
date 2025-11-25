import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import calendar.controller.GuiCalendarController;
import calendar.event.EventContext;
import calendar.model.MultiCalendarManagerInterface;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.Before;
import org.junit.Test;

/**
 * The test for GuiCalendarController.
 */
public class GuiCalendarControllerTest {
  private GuiCalendarController controller;
  private MockGuiView view;
  private MultiCalendarManagerInterface model;
  private StringBuilder log;
  private String expectedDefault;

  /**
   * To set up the model, view, controller, and log.
   */
  @Before
  public void setUp() {
    this.log = new StringBuilder();
    this.view = new MockGuiView(log);
    this.model = new GuiMultiCalendarManagerMockModel(log);
    this.controller = new GuiCalendarController(this.model, this.view);
    this.expectedDefault = "createCalendar: Default, America/New_York\n";

  }

  @Test
  public void testGo() throws IOException {
    controller.go();

    LocalDate today = LocalDate.now();
    int expectedYear = today.getYear();
    int expectedMonth = today.getMonthValue();
    String defaultTimeZone = ZoneId.systemDefault().getId();

    String expectedResult0 = "displayMonthView: " + expectedYear + ", " + expectedMonth + ", {}\n";
    String expectedResult1 = "displayCurrentCalendar: Default, " + defaultTimeZone + "\n";
    String expectedResult2 = "makeVisible\n";
    assertEquals(expectedDefault
        + "getCalendarEntity: Default\nuseThisCalendarEntity: \n"
        + expectedResult0 + expectedResult1 + expectedResult2, log.toString());
  }

  @Test
  public void testCreateCalendar() throws IOException {
    controller.go();
    log.setLength(0);

    controller.createCalendar("Meeting", "America/New_York");
    String expectedResult0 = "createCalendar: Meeting, America/New_York\n";
    assertEquals(expectedResult0, log.toString());
  }

  @Test
  public void testCreateEvent0() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    EventContext context =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    c.createEvent(context);
    // System.out.println(log.toString());
    String expectedResult =
        "displayError: No calendar selected. Please select or create a calendar first.\n";
    assertTrue(log.toString().contains(expectedResult));
  }

  @Test
  public void testCreateEvent1() throws IOException {
    controller.go();
    log.setLength(0);

    EventContext context =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context);
    assertTrue(log.toString().contains("displaySuccess: Event 'Meeting' created successfully!"));
    assertTrue(log.toString().contains("displayMonthView: 2025, 11, {2025-11-20"));
  }

  @Test
  public void testCreateEvent2() throws IOException {
    controller.go();
    log.setLength(0);

    EventContext context =
        new EventContext("", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description", "Boston",
            "PUBLIC");
    controller.createEvent(context);
    assertTrue(log.toString()
        .contains("displayError: Failed to create single event: Event Name cannot be empty"));
  }

  @Test
  public void testCreateEvent3() throws IOException {
    controller.go();
    log.setLength(0);

    EventContext context =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context);
    controller.createEvent(context);
    assertTrue(log.toString()
        .contains("displayError: Failed to create single event: Event already exists"));
  }

  @Test
  public void testCreateEventSeries0() throws IOException {
    MultiCalendarManagerInterface m2 = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c2 = new GuiCalendarController(m2, this.view);
    c2.go();
    log.setLength(0);

    EventContext context =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context, "RF", 4, null);
    // System.out.println(log.toString());
    //    assertTrue(
    //        log.toString().contains("No calendar selected.
    //        Please select or create a calendar first."));
  }

  @Test
  public void testCreateEventSeries1() throws IOException {
    controller.go();
    log.setLength(0);

    EventContext context =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context, "RF", 4, null);
    assertTrue(
        log.toString().contains("displaySuccess: Event series 'Meeting' created successfully!"));
  }

  @Test
  public void testCreateEventSeries2() throws IOException {
    controller.go();
    log.setLength(0);

    EventContext context =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context, "RF", 4, null);
    controller.createEventSeries(context, "RF", 4, null);
    assertTrue(log.toString()
        .contains("displayError: Failed to create event series: Event already exists"));
  }

  @Test
  public void testSwitchCalendar() throws IOException {
    controller.go();
    log.setLength(0);

    controller.switchCalendar("Default");
    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(log.toString().contains("useThisCalendarEntity"));
    assertTrue(log.toString().contains("displayMonthView: "));
    assertTrue(log.toString().contains("displayCurrentCalendar: "));
  }

  @Test
  public void testEditEvent0() throws IOException {
    controller.go();
    EventContext context =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context);
    log.setLength(0);

    EventContext newContext =
        new EventContext("Lecture", "2025-11-19T09:30", "2025-11-19T10:30", "Long Description",
            "New York", "PUBLIC");
    controller.editEvent("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", newContext, "Default");
    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(log.toString().contains("displaySuccess: Event 'Meeting' updated successfully!"));
    assertTrue(log.toString().contains("displayMonthView: 2025, 11, {2025-11-19"));
  }

  @Test
  public void testEditEvent1() throws IOException {
    controller.go();
    EventContext context0 =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context0);
    EventContext context1 =
        new EventContext("Lecture", "2025-11-19T09:30", "2025-11-19T10:30", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context1);
    log.setLength(0);

    EventContext newContext =
        new EventContext("Lecture", "2025-11-19T09:30", "2025-11-19T10:30", "Long Description",
            "New York", "PUBLIC");
    controller.editEvent("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", newContext, "Default");
    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(log.toString().contains("displayError: Failed to edit single event:"
        + " Event after edition conflicts with existing event"));
  }

  @Test
  public void testEditEvent2() throws IOException {
    controller.go();
    EventContext context0 =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context0);
    log.setLength(0);

    EventContext newContext =
        new EventContext("Presentation", "2025-11-19T09:30", "2025-11-19T10:30", "Long Description",
            "New York", "PUBLIC");
    controller.editEvent("Lecture", "2025-11-20T09:00", "2025-11-20T10:00", newContext, "Default");
    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(
        log.toString().contains("displayError: Failed to edit single event: Event does not exist"));
  }

  @Test
  public void testEditCalendarProperty() throws IOException {
    controller.go();
    log.setLength(0);

    controller.editCalendarProperty("Default", "name", "Meeting");
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayCurrentCalendar: Default, America/New_York"));
    assertTrue(log.toString().contains("getAllCalendars"));
    assertTrue(log.toString().contains("displayAvailableCalendars:"));
  }

  @Test
  public void testGetEventsAcrossCalendar() throws IOException {
    controller.go();
    EventContext context0 =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context0);
    controller.createCalendar("Lectures", "America/Chicago");
    controller.switchCalendar("Lectures");
    EventContext context1 =
        new EventContext("Meeting", "2025-11-19T09:30", "2025-11-19T10:30", "Long Description",
            "Chicago", "PUBLIC");
    controller.createEvent(context1);
    log.setLength(0);

    controller.getEventsAcrossCalendar("Meeting");
    assertTrue(log.toString().contains("getEventsAcrossCalendar: Meeting"));
  }

  @Test
  public void testNavigateToMonth() throws IOException {
    controller.go();
    log.setLength(0);

    controller.navigateToMonth(2025, 11);
    // System.out.println(log.toString());
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
  }

  @Test
  public void testViewEventsOnDate() throws IOException {
    controller.go();
    log.setLength(0);

    LocalDate date = LocalDate.of(2025, 11, 20);
    controller.viewEventsOnDate(date);
    // System.out.println(log.toString());
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayEventsOnDate: 2025-11-20"));
  }


}
