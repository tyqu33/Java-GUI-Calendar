package calendar.view;

import calendar.enums.UserStatus;
import calendar.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Swing-based graphical user interface for the calendar application.
 * Provides a month view and interactive controls for managing calendars and events.
 */
public class JframeCalendarView extends JFrame implements CalendarViewInterface {

  private int currentYear;
  private int currentMonth;

  private JLabel monthYearLabel;
  private JLabel currentCalendarLabel;
  private JPanel calendarGridPanel;
  private JButton[][] dayButtons;
  private JComboBox<String> calendarSelector;
  private JButton prevMonthButton;
  private JButton nextMonthButton;
  private JButton createEventButton;
  private JButton createCalendarButton;
  private JButton exportButton;
  private JTextArea eventDisplayArea;
  //private static final Color LIGHT_BLUE_GRAY = new Color(230, 235, 240);

  /**
   * Constructor for SwingCalendarView.
   *
   * @param title the title of the window
   */
  public JframeCalendarView(String title) {
    super(title);

    LocalDate today = LocalDate.now();
    this.currentYear = today.getYear();
    this.currentMonth = today.getMonthValue();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setSize(1000, 700);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10));
    initializeComponents();
    layoutComponents();

    pack();
  }

  /**
   * Initialize all UI components.
   */
  private void initializeComponents() {
    monthYearLabel = new JLabel("", SwingConstants.CENTER);
    monthYearLabel.setFont(new Font("Arial", Font.BOLD, 24));
    prevMonthButton = new JButton("<");
    nextMonthButton = new JButton(">");
    currentCalendarLabel = new JLabel("Calendar: Default");
    currentCalendarLabel.setFont(new Font("Arial", Font.PLAIN, 14));

    calendarSelector = new JComboBox<>();
    calendarSelector.addItem("Default");
    calendarSelector.addItem("Work");
    calendarSelector.addItem("Personal");
    createCalendarButton = new JButton("New Calendar");

    createEventButton = new JButton("Create Event");
    exportButton = new JButton("Export");

    calendarGridPanel = new JPanel(new GridLayout(7, 7, 5, 5));
    calendarGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    dayButtons = new JButton[6][7];

    // Event display
    eventDisplayArea = new JTextArea(10, 30);
    eventDisplayArea.setEditable(false);
    eventDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    eventDisplayArea.setBorder(BorderFactory.createTitledBorder("Events on Selected Date"));
    eventDisplayArea.setText("No events");
  }

  /**
   * Layout all components in the frame.
   */
  private void layoutComponents() {
    // Top panel
    JPanel topPanel = new JPanel(new BorderLayout());
    //topPanel.setBackground(LIGHT_BLUE_GRAY);
    topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

    JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    navigationPanel.add(prevMonthButton);
    navigationPanel.add(monthYearLabel);
    navigationPanel.add(nextMonthButton);
    //navigationPanel.setBackground(LIGHT_BLUE_GRAY);
    topPanel.add(navigationPanel, BorderLayout.CENTER);

    JPanel leftControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
    leftControlPanel.add(new JLabel("Calendar: Default"));
    leftControlPanel.add(new JLabel("Switch to:"));
    leftControlPanel.add(calendarSelector);
    //leftControlPanel.setBackground(LIGHT_BLUE_GRAY);
    topPanel.add(leftControlPanel, BorderLayout.WEST);

    JPanel rightControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightControlPanel.add(createCalendarButton);
    topPanel.add(rightControlPanel, BorderLayout.EAST);

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    leftPanel.add(Box.createRigidArea(new Dimension(0, 35)));

    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    actionPanel.add(createEventButton);
    actionPanel.add(exportButton);

    actionPanel.setAlignmentX(0.0f);
    leftPanel.add(actionPanel);
    leftPanel.add(new JScrollPane(eventDisplayArea));

    buildCalendarGrid();
    add(topPanel, BorderLayout.NORTH);
    add(leftPanel, BorderLayout.WEST);
    add(calendarGridPanel, BorderLayout.CENTER);

    updateMonthYearLabel();
  }

  /**
   * Build the calendar grid with day buttons.
   */
  private void buildCalendarGrid() {
    calendarGridPanel.removeAll();

    String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    for (String dayName : dayNames) {
      JLabel label = new JLabel(dayName, SwingConstants.CENTER);
      label.setFont(new Font("Arial", Font.BOLD, 14));
      //label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
      calendarGridPanel.add(label);
    }
    YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
    LocalDate firstOfMonth = yearMonth.atDay(1);
    int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;

    int dayCounter = 1;
    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < 7; col++) {
        JButton dayButton = new JButton();
        dayButton.setPreferredSize(new Dimension(100, 80));
        //dayButton.setOpaque(true);

        //dayButton.setBorderPainted(false);
        //dayButton.setBackground(Color.WHITE);
        if (row == 0 && col < firstDayOfWeek) {
          dayButton.setEnabled(false);
          dayButton.setText("");
        } else if (dayCounter > yearMonth.lengthOfMonth()) {
          dayButton.setEnabled(false);
          dayButton.setText("");
        } else {
          final int day = dayCounter;
          dayButton.setText(String.valueOf(day));
          dayButton.setFont(new Font("Arial", Font.PLAIN, 16));

          LocalDate date = LocalDate.of(currentYear, currentMonth, day);

          if (date.equals(LocalDate.now())) {
            dayButton.setBackground(Color.YELLOW);
          }


          dayCounter++;
        }

        dayButtons[row][col] = dayButton;
        calendarGridPanel.add(dayButton);
      }
    }

    calendarGridPanel.revalidate();
    calendarGridPanel.repaint();
  }

  /**
   * Update the month/year label.
   */
  private void updateMonthYearLabel() {
    String monthName = YearMonth.of(currentYear, currentMonth)
        .getMonth()
        .getDisplayName(TextStyle.FULL, Locale.getDefault());
    monthYearLabel.setText(monthName + " " + currentYear);
  }

  @Override
  public void displayMonthView(int year, int month, Map<LocalDate, List<Event>> events) {

  }

  @Override
  public void displayCurrentCalendar(String calendarName, String timezone) {

  }

  @Override
  public void displayAvailableCalendars(List<String> calendarNames) {

  }

  @Override
  public void displayEventsOnDate(List<Event> events, LocalDate date) {

  }

  @Override
  public void displayEventsBetween(List<Event> events, LocalDateTime start, LocalDateTime end) {

  }

  @Override
  public void displayUserStatus(UserStatus status) {

  }

  @Override
  public void exportCalendar(String content, String fileName) {

  }

  @Override
  public void displaySuccess(String message) {

  }

  @Override
  public void displayError(String error) {

  }

  @Override
  public void displayWarning(String warning) {

  }

  @Override
  public void displayWelcome() {

  }

  @Override
  public void makeVisible() {

  }

  @Override
  public void refresh() {

  }
}