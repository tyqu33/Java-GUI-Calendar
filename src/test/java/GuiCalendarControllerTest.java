import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import calendar.calendarentity.CalendarEntityInterface;
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
    c2.createEventSeries(context, "RF", 4, null);
    // System.out.println(log.toString());
    assertTrue(log.toString()
        .contains("No calendar selected. Please select or create a calendar first."));
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
  public void testCreateEventSeries3() throws IOException {
    controller.go();
    log.setLength(0);

    EventContext context =
        new EventContext("", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context, "RF", 4, null);
    assertTrue(log.toString().contains("displayError: "
        + "Failed to create event series: Event Series Name cannot be empty"));
  }

  @Test
  public void testGetAllCalendarNames() throws IOException {
    controller.go();
    log.setLength(0);

    controller.getAllCalendarNames();
    assertTrue(log.toString().contains("getAllCalendars"));
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
  public void testEditEvent3() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    EventContext newContext =
        new EventContext("Presentation", "2025-11-19T09:30", "2025-11-19T10:30", "Long Description",
            "New York", "PUBLIC");
    c.editEvent("Lecture", "2025-11-20T09:00", "2025-11-20T10:00", newContext, null);
    assertTrue(
        log.toString().contains("displayError: No calendar selected."
            + " Please select or create a calendar first."));
  }

  @Test
  public void testEditCalendarProperty0() throws IOException {
    controller.go();
    log.setLength(0);

    controller.editCalendarProperty("Default", "name", "Meeting");
    // System.out.println(log.toString());
    assertTrue(log.toString().contains("editCalendar: Default, name, Meeting"));
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayCurrentCalendar: Default, America/New_York"));
    assertTrue(log.toString().contains("getAllCalendars"));
    assertTrue(log.toString().contains("displayAvailableCalendars:"));
    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
  }

  @Test
  public void testEditCalendarProperty1() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log) {
      @Override
      public CalendarEntityInterface editCalendar(String calendarName, String property,
                                                  String propertyValue) {
        throw new IllegalArgumentException("Forced IAE");
      }
    };
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    c.editCalendarProperty("Default", "name", "Meeting");
    System.out.println(log.toString());
    assertTrue(log.toString().contains("displayError: Failed to edit calendar: Forced IAE"));
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
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
  }

  @Test
  public void testRefreshCurrentMonth0() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    c.navigateToMonth(2025, 11);
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
    assertFalse(log.toString().contains("displayError"));
  }

  @Test
  public void testRefreshCurrentMonth1() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log) {
      @Override
      public CalendarEntityInterface getCurrentCalendarEntity() {
        throw new IllegalArgumentException("Forced IAE");
      }
    };
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    c.navigateToMonth(2025, 11);
    // System.out.println(log.toString());
    assertTrue(log.toString().contains("displayError: Failed to refresh calendar: Forced IAE"));
    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
  }

  @Test
  public void testViewEventsOnDate0() throws IOException {
    controller.go();
    log.setLength(0);

    LocalDate date = LocalDate.of(2025, 11, 20);
    controller.viewEventsOnDate(date);
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayEventsOnDate: 2025-11-20"));
  }

  @Test
  public void testViewEventsOnDate1() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    LocalDate date = LocalDate.of(2025, 11, 20);
    c.viewEventsOnDate(date);
    // System.out.println(log.toString());
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayError: No calendar selected!"));
  }

  @Test
  public void testExportCalendar0() throws IOException {
    controller.go();
    log.setLength(0);

    controller.exportCalendar("Default.csv");
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("exportCalendar: Default.csv, Subject,Start Date,"
        + "Start Time,End Date,End Time,All Day Event,Description,Location,Private"));
  }

  @Test
  public void testExportCalendar1() throws IOException {
    controller.go();
    log.setLength(0);

    controller.exportCalendar("Default");
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayError: Invalid file format. Use .csv or .ical"));
  }

  @Test
  public void testExportCalendar2() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    c.exportCalendar("Default");
    // System.out.println(log.toString());
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayError: No calendar selected!"));
  }

  @Test
  public void testGetCurrentCalendarName0() throws IOException {
    controller.go();
    log.setLength(0);

    String name = controller.getCurrentCalendarName();
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertEquals("Default", name);
  }

  @Test
  public void testGetCurrentCalendarName1() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    String name = c.getCurrentCalendarName();
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertEquals("Default", name);
  }

  @Test
  public void testGetCurrentCalendarTimezone0() throws IOException {
    controller.go();
    log.setLength(0);

    String timezone = controller.getCurrentCalendarTimezone();
    String expectedTimeZone = ZoneId.systemDefault().getId();
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertEquals(expectedTimeZone, timezone);
  }

  @Test
  public void testGetCurrentCalendarTimezone01() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    String timezone = c.getCurrentCalendarTimezone();
    String expectedTimeZone = ZoneId.systemDefault().getId();
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertEquals(expectedTimeZone, timezone);
  }

  @Test
  public void testEditEventSeries0() throws IOException {
    controller.go();
    log.setLength(0);

    EventContext newContext =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.editEventSeries("Meeting", "2025-11-20T08:00",
        "2025-11-20T12:00", newContext, "Default");
    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(log.toString().contains("displayError: Failed to edit event series: Event does "
        + "not exist, subject: Meeting, start: 2025-11-20T08:00, end: 2025-11-20T12:00"));
  }

  @Test
  public void testEditEventSeries1() throws IOException {
    controller.go();
    EventContext context =
        new EventContext("Meeting", "2025-11-20T08:00", "2025-11-20T12:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context, "RF", 4, null);
    log.setLength(0);

    EventContext newContext =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.editEventSeries("Meeting", "2025-11-20T08:00",
        "2025-11-20T12:00", newContext, "Default");
    // System.out.println(log.toString());
    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(log.toString().contains("displaySuccess: Event series 'Meeting'"
        + " updated successfully!"));
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));

    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
    assertTrue(log.toString().contains("2025-11-20"));
    assertTrue(log.toString().contains("2025-11-21"));
    assertTrue(log.toString().contains("2025-11-27"));
    assertTrue(log.toString().contains("2025-11-28"));
  }

  @Test
  public void testEditEventSeries2() throws IOException {
    controller.go();
    EventContext context =
        new EventContext("Meeting", "2025-11-20T08:00", "2025-11-20T12:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context, "RF", 4, null);
    log.setLength(0);

    EventContext newContext =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.editEventSeries("Meeting", "2025-11-20T08:00",
        "2025-11-20T12:00", newContext, null);
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displaySuccess: Event series 'Meeting'"
        + " updated successfully!"));
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));

    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
    assertTrue(log.toString().contains("2025-11-20"));
    assertTrue(log.toString().contains("2025-11-21"));
    assertTrue(log.toString().contains("2025-11-27"));
    assertTrue(log.toString().contains("2025-11-28"));
  }

  @Test
  public void testEditEventSeries3() throws IOException {
    controller.go();
    EventContext context =
        new EventContext("Meeting", "2025-11-20T08:00", "2025-11-20T12:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context, "RF", 4, null);
    log.setLength(0);

    EventContext newContext =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.editEventSeries("Meeting", "2025-11-20T08:00",
        "2025-11-20T12:00", newContext, "");
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displaySuccess: Event series 'Meeting'"
        + " updated successfully!"));
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));

    assertTrue(log.toString().contains("displayMonthView: 2025, 11"));
    assertTrue(log.toString().contains("2025-11-20"));
    assertTrue(log.toString().contains("2025-11-21"));
    assertTrue(log.toString().contains("2025-11-27"));
    assertTrue(log.toString().contains("2025-11-28"));
  }

  @Test
  public void testEditEventSeries4() throws IOException {
    MultiCalendarManagerInterface m = new GuiMultiCalendarManagerMockModel(this.log, 1);
    GuiCalendarController c = new GuiCalendarController(m, this.view);
    c.go();
    log.setLength(0);

    EventContext newContext =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    c.editEventSeries("Meeting", "2025-11-20T08:00",
        "2025-11-20T12:00", newContext, null);
    assertTrue(log.toString().contains("getCurrentCalendarEntity"));
    assertTrue(log.toString().contains("displayError: No calendar selected."
        + " Please select or create a calendar first."));
  }

  @Test
  public void testEditEventSeries5() throws IOException {
    controller.go();
    EventContext context =
        new EventContext("Meeting", "2025-11-20T08:00", "2025-11-20T12:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEvent(context);
    log.setLength(0);

    EventContext newContext =
        new EventContext("Meeting", "2025-11-20T09:00", "2025-11-20T10:00", "Long Description",
            "Boston", "PUBLIC");
    controller.editEventSeries("Meeting", "2025-11-20T08:00",
        "2025-11-20T12:00", newContext, "Default");

    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(log.toString().contains("displayError: Failed to update event series."));
  }

  @Test
  public void testEditEventSeries6() throws IOException {
    controller.go();
    EventContext context0 =
        new EventContext("Meeting", "2025-11-20T08:00", "2025-11-20T12:00", "Long Description",
            "Boston", "PUBLIC");
    controller.createEventSeries(context0, "RF", 4, null);
    EventContext context1 =
        new EventContext("Lecture", "2025-11-20T09:00", "2025-11-20T10:00", "",
            "", "PUBLIC");
    controller.createEvent(context1);
    log.setLength(0);

    controller.editEventSeries("Meeting", "2025-11-20T08:00",
        "2025-11-20T12:00", context1, "Default");

    // System.out.println(log.toString());
    assertTrue(log.toString().contains("getCalendarEntity: Default"));
    assertTrue(log.toString().contains("displayError: Failed to edit event series: "
        + "Event already exists"));
  }

}
