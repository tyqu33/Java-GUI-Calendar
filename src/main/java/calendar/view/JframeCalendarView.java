package calendar.view;

import calendar.controller.Features;
import calendar.enums.UserStatus;
import calendar.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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
  private JButton createEventSeriesButton;
  private JButton createCalendarButton;
  private JButton exportButton;
  private JTextArea eventDisplayArea;
  //private static final Color LIGHT_BLUE_GRAY = new Color(230, 235, 240);
  private boolean readingCalendarList = false;

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
    // calendarSelector.addItem("Work");
    // calendarSelector.addItem("Personal");
    createCalendarButton = new JButton("New Calendar");

    createEventButton = new JButton("Create Event");
    createEventSeriesButton = new JButton("Create EventSeries");
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
    //navigationPanel.setBackground(LIGHT_BLUE_GRAY);
    navigationPanel.add(prevMonthButton);
    navigationPanel.add(monthYearLabel);
    navigationPanel.add(nextMonthButton);
    topPanel.add(navigationPanel, BorderLayout.CENTER);

    JPanel leftControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
    //leftControlPanel.setBackground(LIGHT_BLUE_GRAY);
    leftControlPanel.add(new JLabel("Calendar: Default"));
    leftControlPanel.add(new JLabel("Switch to:"));
    leftControlPanel.add(calendarSelector);
    topPanel.add(leftControlPanel, BorderLayout.WEST);

    JPanel rightControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightControlPanel.add(createCalendarButton);
    topPanel.add(rightControlPanel, BorderLayout.EAST);

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    //leftPanel.setBackground(LIGHT_BLUE_GRAY);

    leftPanel.add(Box.createRigidArea(new Dimension(0, 35)));

    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
    //actionPanel.setBackground(LIGHT_BLUE_GRAY);

    actionPanel.setAlignmentX(0.0f);

    createEventButton.setAlignmentX(0.0f);
    actionPanel.add(createEventButton);
    actionPanel.add(Box.createVerticalStrut(5));

    createEventSeriesButton.setAlignmentX(0.0f);
    actionPanel.add(createEventSeriesButton);
    actionPanel.add(Box.createVerticalStrut(5));

    exportButton.setAlignmentX(0.0f);
    actionPanel.add(exportButton);

    leftPanel.add(actionPanel);

    leftPanel.add(Box.createVerticalStrut(10));
    leftPanel.add(new JScrollPane(eventDisplayArea));

    buildCalendarGrid();
    //calendarGridPanel.setBackground(LIGHT_BLUE_GRAY);

    add(topPanel, BorderLayout.NORTH);
    add(leftPanel, BorderLayout.WEST);
    add(calendarGridPanel, BorderLayout.CENTER);

    //this.getContentPane().setBackground(LIGHT_BLUE_GRAY);

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
    this.currentYear = year;
    this.currentMonth = month;
    updateMonthYearLabel();
    buildCalendarGrid();
  }

  @Override
  public void displayCurrentCalendar(String calendarName, String timezone) {
    currentCalendarLabel.setText("Calendar: " + calendarName + " (" + timezone + ")");
  }

  @Override
  public void displayAvailableCalendars(List<String> calendarNames) {
    calendarSelector.removeAllItems();
    for (String name : calendarNames) {
      calendarSelector.addItem(name);
    }
  }

  @Override
  public void displayEventsOnDate(List<Event> events, LocalDate date) {
    if (events == null || events.isEmpty()) {
      eventDisplayArea.setText("No events on " + date);
      return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("Events on ").append(date).append(":\n\n");
    for (Event event : events) {
      sb.append("• ").append(event.getSubject()).append("\n");
      sb.append("  Time: ");

      if (event.isAllDayEvent()) {
        sb.append("All Day");
      } else {
        sb.append(event.getStartDateTime().toLocalTime());
        if (event.getEndDateTime() != null) {
          sb.append(" - ").append(event.getEndDateTime().toLocalTime());
        }
      }
      sb.append("\n");

      if (event.getLocation() != null && !event.getLocation().isEmpty()) {
        sb.append("  Location: ").append(event.getLocation()).append("\n");
      }
      sb.append("\n");
    }

    eventDisplayArea.setText(sb.toString());
  }

  @Override
  public void displayEventsBetween(List<Event> events, LocalDateTime start, LocalDateTime end) {
    //
  }

  @Override
  public void displayUserStatus(UserStatus status) {
    //
  }

  @Override
  public void exportCalendar(String content, String fileName) {
    try {
      Path filePath = Paths.get(fileName);
      Files.write(filePath, content.getBytes());
      displaySuccess("Exported to: " + filePath.toAbsolutePath());
    } catch (IOException e) {
      displayError("Export failed: " + e.getMessage());
    }
  }

  @Override
  public void displaySuccess(String message) {
    JOptionPane.showMessageDialog(this, message, "Success",
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void displayError(String error) {
    JOptionPane.showMessageDialog(this, error, "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayWarning(String warning) {
    JOptionPane.showMessageDialog(this, warning, "Warning",
        JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void displayWelcome() {
    /**JOptionPane.showMessageDialog(this,
     "Welcome to Virtual Calendar Application!\n\n"
     + "Select a date to view events\n"
     + "Click 'Create Event' to add new events",
     "Welcome",
     JOptionPane.INFORMATION_MESSAGE);**/
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void refresh() {
    buildCalendarGrid();
    revalidate();
    repaint();
  }

  @Override
  public void addFeatures(Features features) {
    createCalendarButton.addActionListener(evt -> {
      CreateCalendarDialog dialog = new CreateCalendarDialog(this, features);
      dialog.go();
      dialog.setVisible(true);
    });

    //    createEventButton.addActionListener(evt -> {
    //      createSingleEventDialog dialog0 = new CreateSingleEventDialog(this, features);
    //      dialog0.go();
    //      dialog0.setVisible(true);
    //    });

    // scroll down to show all calendar names
    calendarSelector.addPopupMenuListener(new PopupMenuListener() {
      @Override
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        readingCalendarList = true;
        try {
          calendarSelector.removeAllItems();
          calendarSelector.addItem("Default");
          for (String calendarName : features.getAllCalendarNames()) {
            if (!calendarName.equals("Default")) {
              calendarSelector.addItem(calendarName);
            }
          }
          Object currentSelection = calendarSelector.getSelectedItem();
          if (currentSelection != null) {
            calendarSelector.setSelectedItem(currentSelection);
          }
        } finally {
          readingCalendarList = false;
        }
      }

      @Override
      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

      }

      @Override
      public void popupMenuCanceled(PopupMenuEvent e) {

      }

    });
    // click on the scroll down list and select one calendar name, meaning using this calendar
    calendarSelector.addActionListener(evt -> {
      if (readingCalendarList) {
        return;
      }
      String name = (String) calendarSelector.getSelectedItem();
      if (name != null) {
        features.switchCalendar(name);
      }
    });

  }

  private class CreateCalendarDialog extends JDialog {
    private JTextField inputCalendarName;
    private JComboBox<String> inputTimezone;

    public CreateCalendarDialog(JFrame parent, Features features) {
      super(parent, "Create Calendar", true);
      setLayout(new BorderLayout(10, 10));
      JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
      panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

      panel.add(new JLabel("Calendar Name:"));
      this.inputCalendarName = new JTextField();
      panel.add(this.inputCalendarName);

      panel.add(new JLabel("Time Zone:"));
      Set<String> zoneIdSet = ZoneId.getAvailableZoneIds();
      String[] zoneIds = zoneIdSet.stream().sorted().toArray(String[]::new);
      this.inputTimezone = new JComboBox<>(zoneIds);
      this.inputTimezone.setSelectedItem(ZoneId.systemDefault().getId());
      panel.add(this.inputTimezone);
      add(panel, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      JButton confirmButton = new JButton("Confirm");
      JButton cancelButton = new JButton("Cancel");

      confirmButton.addActionListener(ev -> {
        String calendarName = inputCalendarName.getText();
        String timeZone = inputTimezone.getSelectedItem().toString();
        // remember to TEST if calendar name is null or empty!
        features.createCalendar(calendarName, timeZone);
        dispose();
      });

      cancelButton.addActionListener(ev -> {
        dispose();
      });

      buttonPanel.add(confirmButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);
      setLocationRelativeTo(getParent());
      pack();
    }

    private void go() {
    }
  }

  private class CreateSingleEventDialog extends JDialog {
    private JTextField inputEventName;
    private JTextField inputYear;
    private JComboBox<String> inputMonth;
    private JComboBox<String> inputDay;

    public CreateSingleEventDialog(JFrame parent, Features features) {
      super(parent, "Create Single Event", true);
      setLayout(new BorderLayout(10, 10));
      JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
      panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

      panel.add(new JLabel("Event Name:"));
      this.inputEventName = new JTextField();
      panel.add(this.inputEventName);


      JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      datePanel.setBorder(BorderFactory.createTitledBorder("Date"));
      this.inputYear = new JTextField();
      datePanel.add(this.inputYear);

      String[] months = {"January", "February", "March", "April", "May", "June",
          "July", "August", "September", "October", "November", "December"};
      inputMonth = new JComboBox<>(months);
      inputMonth.setSelectedIndex(LocalDateTime.now().getMonthValue() - 1);

      pack();
    }

    private void go() {
    }
  }
}