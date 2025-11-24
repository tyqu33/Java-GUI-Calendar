package calendar.view;

import calendar.controller.Features;
import calendar.enums.EventStatus;
import calendar.enums.UserStatus;
import calendar.event.Event;
import calendar.event.EventContext;
import calendar.event.EventDecorator;
import calendar.event.EventInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.DateFormatter;

/**
 * Swing-based graphical user interface for the calendar application.
 * Provides a month view and interactive controls for managing calendars and events.
 */
public class JframeCalendarView extends JFrame implements CalendarViewInterface {
  private Features features;

  private int currentYear;
  private int currentMonth;

  private JLabel monthYearLabel;
  private JLabel currentCalendarLabel;
  private JLabel currentTimezoneLabel;
  private JPanel calendarGridPanel;
  private JButton[][] dayButtons;
  private JComboBox<String> calendarSelector;
  private JButton prevMonthButton;
  private JButton nextMonthButton;
  private JButton createEventButton;
  private JButton createEventSeriesButton;
  private JButton createCalendarButton;
  private JButton editCalendarButton;
  private JButton exportButton;
  private JButton searchButton;
  // private JTextArea eventDisplayArea;
  private JPanel eventDisplayArea;
  private JScrollPane eventScrollPane;
  private boolean readingCalendarList = false;

  public static final String[] months = {"January", "February", "March", "April", "May", "June",
      "July", "August", "September", "October", "November", "December"};
  public static final int START_YEAR = 1950;
  public static final int END_YEAR = 2150;

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

    currentTimezoneLabel = new JLabel("Timezone: " + ZoneId.systemDefault().getId());
    currentTimezoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));

    calendarSelector = new JComboBox<>();
    calendarSelector.addItem("Default");
    searchButton = new JButton("Search");
    createCalendarButton = new JButton("New Calendar");

    editCalendarButton = new JButton("Edit");
    editCalendarButton.setPreferredSize(new Dimension(60, 25));
    editCalendarButton.setFont(new Font("Arial", Font.PLAIN, 11));

    createEventButton = new JButton("Create Event");
    createEventSeriesButton = new JButton("Create EventSeries");
    exportButton = new JButton("Export");

    calendarGridPanel = new JPanel(new GridLayout(7, 7, 5, 5));
    calendarGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    dayButtons = new JButton[6][7];

    // Event display
    //    eventDisplayArea = new JTextArea(10, 30);
    //    eventDisplayArea.setEditable(false);
    //    eventDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    //    eventDisplayArea.setBorder(BorderFactory.createTitledBorder("Events on Selected Date"));
    //    eventDisplayArea.setText("No events");

    eventDisplayArea = new JPanel();
    eventDisplayArea.setLayout(new BoxLayout(eventDisplayArea, BoxLayout.Y_AXIS));
    eventDisplayArea.setBackground(Color.WHITE);
    eventDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

    eventScrollPane = new JScrollPane(eventDisplayArea);
    eventScrollPane.setBorder(BorderFactory.createTitledBorder("Events on Selected Date"));
    eventScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    eventScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    eventScrollPane.getViewport().setBackground(Color.WHITE);
    eventScrollPane.setPreferredSize(new Dimension(300, 400));
  }

  /**
   * Layout all components in the frame.
   */
  private void layoutComponents() {
    // Top panel
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

    JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    navigationPanel.add(prevMonthButton);
    navigationPanel.add(monthYearLabel);
    navigationPanel.add(nextMonthButton);
    topPanel.add(navigationPanel, BorderLayout.CENTER);

    JPanel leftControlPanel = new JPanel();
    leftControlPanel.setLayout(new BoxLayout(leftControlPanel, BoxLayout.Y_AXIS));
    leftControlPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

    JPanel calendarInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    calendarInfoPanel.add(currentCalendarLabel);
    editCalendarButton.setPreferredSize(new Dimension(60, 25));
    editCalendarButton.setFont(new Font("Arial", Font.PLAIN, 11));
    calendarInfoPanel.add(editCalendarButton);
    calendarInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JPanel timezonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    currentTimezoneLabel = new JLabel("Timezone: " + ZoneId.systemDefault().getId());
    currentTimezoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    timezonePanel.add(currentTimezoneLabel);
    timezonePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    switchPanel.add(new JLabel("Switch to:"));
    switchPanel.add(calendarSelector);
    switchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    leftControlPanel.add(calendarInfoPanel);
    leftControlPanel.add(timezonePanel);
    leftControlPanel.add(switchPanel);

    topPanel.add(leftControlPanel, BorderLayout.WEST);

    JPanel rightControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightControlPanel.add(searchButton);
    rightControlPanel.add(createCalendarButton);
    topPanel.add(rightControlPanel, BorderLayout.EAST);

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    //leftPanel.setBackground(LIGHT_BLUE_GRAY);

    leftPanel.add(Box.createRigidArea(new Dimension(0, 35)));

    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
    actionPanel.setAlignmentX(0.0f);
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
    // leftPanel.add(new JScrollPane(eventDisplayArea));
    leftPanel.add(eventScrollPane);

    buildCalendarGrid(new HashMap<>());
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
  private void buildCalendarGrid(Map<LocalDate, List<Event>> eventsMap) {
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
        dayButton.setOpaque(true);
        dayButton.setBorderPainted(true);

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
          dayButton.setBackground(Color.WHITE);

          if (date.equals(LocalDate.now())) {
            dayButton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 224), 3));
          }

          StringBuilder buttonText = new StringBuilder();
          buttonText.append("<html><div style='text-align: center; padding-top: 5px;'>");
          buttonText.append("<b>").append(day).append("</b>");

          boolean hasEvents = eventsMap != null && eventsMap.containsKey(date)
              && !eventsMap.get(date).isEmpty();

          if (hasEvents) {
            List<Event> eventsOnDate = eventsMap.get(date);
            if (!eventsOnDate.isEmpty()) {
              buttonText.append("<br><span style='font-size: 9px;'>");
              int count = 0;
              for (Event event : eventsOnDate) {
                if (count >= 2) {
                  buttonText.append("...");
                  break;
                }
                String eventName = event.getSubject();
                if (eventName.length() > 10) {
                  eventName = eventName.substring(0, 10) + "...";
                }
                buttonText.append(eventName);
                if (count < eventsOnDate.size() - 1 && count < 1) {
                  buttonText.append("<br>");
                }
                count++;
              }
              buttonText.append("</span>");
            }
            buttonText.append("</div></html>");
            dayButton.setText(buttonText.toString());
            dayButton.setFont(new Font("Arial", Font.PLAIN, 12));
            dayButton.setVerticalAlignment(SwingConstants.TOP);

            if (hasEvents) {
              dayButton.setBackground(new Color(230, 240, 255));
            }
          }

          dayButton.addActionListener(e -> {
            if (this.features != null) {
              features.viewEventsOnDate(date);
            }
          });

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
    buildCalendarGrid(events);
  }

  @Override
  public void displayCurrentCalendar(String calendarName, String timezone) {
    currentCalendarLabel.setText("Calendar: " + calendarName);
    currentTimezoneLabel.setText("Timezone: " + timezone);
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
    eventDisplayArea.removeAll();
    if (events == null || events.isEmpty()) {
      // eventDisplayArea.setText("No events on " + date);
      JLabel emptyLabel = new JLabel("No events on " + date);
      emptyLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      eventDisplayArea.add(emptyLabel);
      // return;
    } else {


      //    StringBuilder sb = new StringBuilder();
      //    sb.append("Events on ").append(date).append(":\n\n");
      //    for (Event event : events) {
      //      sb.append("• ").append(event.getSubject()).append("\n");
      //      sb.append("  Time: ");
      //
      //      if (event.isAllDayEvent()) {
      //        sb.append("All Day");
      //      } else {
      //        sb.append(event.getStartDateTime().toLocalTime());
      //        if (event.getEndDateTime() != null) {
      //          sb.append(" - ").append(event.getEndDateTime().toLocalTime());
      //        }
      //      }
      //      sb.append("\n");
      //
      //      if (event.getLocation() != null && !event.getLocation().isEmpty()) {
      //        sb.append("  Location: ").append(event.getLocation()).append("\n");
      //      }
      //      sb.append("\n");
      //    }
      JLabel eventsLabel = new JLabel("Events on " + date + ":\n\n");
      eventsLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      eventsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      eventDisplayArea.add(eventsLabel);

      for (Event event : events) {
        JPanel singleEventPanel = createSingleEventPanel(event);
        singleEventPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        eventDisplayArea.add(singleEventPanel);
        eventDisplayArea.add(Box.createVerticalStrut(5));
      }
    }
    eventDisplayArea.add(Box.createVerticalGlue());
    eventDisplayArea.revalidate();
    eventDisplayArea.repaint();

    // eventDisplayArea.setText(sb.toString());
  }

  private JPanel createSingleEventPanel(Event event) {
    return createSingleEventPanel(event, null);
  }

  private JPanel createSingleEventPanel(Event event, String calendarName) {
    JPanel row = new JPanel(new BorderLayout(5, 5));
    row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

    String eventInfo = printSingleEventInfo(event).toString();
    JLabel eventInfoLabel = new JLabel("<html>" + eventInfo.replace("\n", "<br>") + "</html>");
    eventInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
    row.add(eventInfoLabel, BorderLayout.CENTER);

    JButton optionsButton = new JButton("...");
    optionsButton.setPreferredSize(new Dimension(30, 20));
    optionsButton.setFocusable(false);

    boolean ifSeries = event.getSeriesId() != null;

    optionsButton.addActionListener(e -> {
      JPopupMenu popup = new JPopupMenu();

      boolean isPartOfSeries = event.getSeriesId() != null;

      JMenuItem editItem = new JMenuItem("Edit");
      editItem.addActionListener(evt -> {
        //        String oldEventName = event.getSubject();
        //        LocalDateTime oldStart = event.getStartDateTime();
        //        LocalDateTime oldEnd = event.getEndDateTime();
        EditSingleEventDialog dialog = new EditSingleEventDialog(this, features,
            event, calendarName, false);
        dialog.setVisible(true);
      });

      if (ifSeries) {
        //        JMenuItem editSeriesItem = new JMenuItem("Edit Series");
        //        editSeriesItem.addActionListener(evt -> {
        //          EditEventSeriesDialog dialog = new EditEventSeriesDialog(this, features,
        //              event, calendarName);
        //          dialog.setVisible(true);
        //        });
      }


      /*JMenuItem copyItem = new JMenuItem("Copy To");
      copyItem.addActionListener(evt -> {
        CopySingleEventDialog dialog = new CopySingleEventDialog(this, features);
        dialog.setVisible(true);
      });*/
      popup.add(editItem);
      //popup.addSeparator();
      //popup.add(copyItem);

      if (isPartOfSeries) {
        JMenuItem editSeriesItem = new JMenuItem("Edit Series");
        editSeriesItem.addActionListener(evt -> {
          EditSingleEventDialog dialog = new EditSingleEventDialog(
              this, features, event, calendarName, true);
          dialog.setVisible(true);
        });
        popup.add(editSeriesItem);
      }

      popup.show(optionsButton, 0, eventInfoLabel.getHeight());
    });
    JPanel btnContainer = new JPanel(new BorderLayout());
    btnContainer.setOpaque(false);
    btnContainer.add(optionsButton, BorderLayout.CENTER);

    row.add(btnContainer, BorderLayout.EAST);
    return row;
  }

  private StringBuilder printSingleEventInfo(Event event) {
    StringBuilder sb = new StringBuilder();
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
    return sb;
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
    buildCalendarGrid(new HashMap<>());
    revalidate();
    repaint();
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;

    createCalendarButton.addActionListener(evt -> {
      CreateCalendarDialog dialog = new CreateCalendarDialog(this, features);
      dialog.go();
      dialog.setVisible(true);
    });

    // scroll down to show all calendar names
    calendarSelector.addPopupMenuListener(new PopupMenuListener() {
      @Override
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        readingCalendarList = true;
        try {
          Object currentSelection = calendarSelector.getSelectedItem();

          calendarSelector.removeAllItems();

          Collection<String> allCalendarNames = features.getAllCalendarNames();
          for (String calendarName : allCalendarNames) {
            calendarSelector.addItem(calendarName);
          }
          if (currentSelection != null) {
            boolean stillExists = false;
            for (String name : allCalendarNames) {
              if (name.equals(currentSelection)) {
                stillExists = true;
                break;
              }
            }
            if (stillExists) {
              calendarSelector.setSelectedItem(currentSelection);
            } else {
              String currentCalendarName = features.getCurrentCalendarName();
              calendarSelector.setSelectedItem(currentCalendarName);
            }
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

    // Create Event button
    createEventButton.addActionListener(evt -> {
      CreateSingleEventDialog dialog = new CreateSingleEventDialog(this, features);
      dialog.setVisible(true);
    });

    // Create Event Series button
    createEventSeriesButton.addActionListener(evt -> {
      CreateEventSeriesDialog dialog = new CreateEventSeriesDialog(this, features);
      dialog.setVisible(true);
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

    // previous month
    prevMonthButton.addActionListener(evt -> {
      if (currentMonth == 1) {
        currentMonth = 12;
        currentYear--;
      } else {
        currentMonth--;
      }
      features.navigateToMonth(currentYear, currentMonth);
    });

    // next month
    nextMonthButton.addActionListener(evt -> {
      if (currentMonth == 12) {
        currentMonth = 1;
        currentYear++;
      } else {
        currentMonth++;
      }
      features.navigateToMonth(currentYear, currentMonth);
    });

    // export
    exportButton.addActionListener(evt -> {
      String[] options = {"CSV", "iCal"};
      int choice = JOptionPane.showOptionDialog(this,
          "Select export format:",
          "Export Calendar",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]);

      if (choice == -1) {
        return;
      }

      String extension = (choice == 0) ? ".csv" : ".ical";
      String fileName = JOptionPane.showInputDialog(this,
          "Enter file name:",
          "calendar" + extension);
      if (fileName != null && !fileName.trim().isEmpty()) {
        features.exportCalendar(fileName.trim());
      }
    });

    searchButton.addActionListener(evt -> {
      SearchEventAcrossCalendarDialog dialog = new SearchEventAcrossCalendarDialog(this, features);
      dialog.go();
      dialog.setVisible(true);
    });

    editCalendarButton.addActionListener(evt -> {
      EditCalendarDialog dialog = new EditCalendarDialog(JframeCalendarView.this, features);
      dialog.setVisible(true);
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
    private SingleEventPanel panel;

    public CreateSingleEventDialog(JFrame parent, Features features) {
      super(parent, "Create Single Event", true);
      setLayout(new BorderLayout(10, 10));
      panel = new SingleEventPanel();
      add(panel, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      JButton createButton = new JButton("Create");
      JButton cancelButton = new JButton("Cancel");

      createButton.addActionListener(e -> {
        EventContext context = new EventContext(this.panel.getEventName(),
            this.panel.getStartTime().toString(), this.panel.getEndTime().toString(),
            this.panel.getDescription(), this.panel.getEventLocation(), this.panel.getEventStatus()
            );
        features.createEvent(context);
        dispose();
      });
      cancelButton.addActionListener(e -> dispose());

      buttonPanel.add(createButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);
      setLocationRelativeTo(getParent());
      pack();
    }

    private void go() {
    }
  }

  private class EditSingleEventDialog extends JDialog {
    private SingleEventPanel panel;
    private boolean isEditSeries;

    public EditSingleEventDialog(JFrame parent, Features features,
                                 Event oldEvent, String calendarName, boolean isEditSeries) {
      super(parent, isEditSeries ? "Edit Event Series" : "Edit Single Event", true);
      this.isEditSeries = isEditSeries;
      setLayout(new BorderLayout(10, 10));
      panel = new SingleEventPanel();
      add(panel, BorderLayout.CENTER);
      panel.setOldPropertyValues(oldEvent);

      JPanel buttonPanel = new JPanel();
      JButton confirmButton = new JButton("Confirm");
      JButton cancelButton = new JButton("Cancel");

      confirmButton.addActionListener(e -> {
        EventContext newContext = new EventContext(
            this.panel.getEventName(),
            this.panel.getStartTime().toString(),
            this.panel.getEndTime().toString(),
            this.panel.getDescription(),
            this.panel.getEventLocation(),
            this.panel.getEventStatus()
        );

        if (isEditSeries) {
          features.editEventSeries(
              oldEvent.getSubject(),
              oldEvent.getStartDateTime().toString(),
              oldEvent.getEndDateTime().toString(),
              newContext,
              calendarName
          );
        } else {
          features.editEvent(
            oldEvent.getSubject(),
            oldEvent.getStartDateTime().toString(),
            oldEvent.getEndDateTime().toString(),
            newContext,
            calendarName
        );
        }
        dispose();
      });
      cancelButton.addActionListener(e -> dispose());

      buttonPanel.add(confirmButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);
      setLocationRelativeTo(getParent());
      pack();
    }

    private void go() {
    }
  }


  private class SingleEventPanel extends JPanel {
    private JTextField inputEventName;
    private JComboBox<Integer> inputYear;
    private JComboBox<String> inputMonth;
    private JComboBox<Integer> inputDay;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JTextField inputDescription;
    private JTextField inputLocation;
    private JComboBox<String> inputEventStatus;

    public SingleEventPanel() {
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
      this.initializeEventComponents();
    }

    private void initializeEventComponents() {
      JPanel namePanel = new JPanel(new BorderLayout());
      namePanel.setBorder(BorderFactory.createTitledBorder("Event Name:"));
      this.inputEventName = new JTextField();
      namePanel.add(this.inputEventName);
      this.add(namePanel);


      JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      datePanel.setBorder(BorderFactory.createTitledBorder("Date:"));
      int currentYear = LocalDate.now().getYear();
      Integer[] years = new Integer[END_YEAR - START_YEAR];
      for (int i = 0; i < END_YEAR - START_YEAR; i++) {
        years[i] = i + START_YEAR;
      }
      this.inputYear = new JComboBox<>(years);
      this.inputYear.setSelectedItem(currentYear);

      this.inputMonth = new JComboBox<>(months);
      this.inputMonth.setSelectedIndex(LocalDateTime.now().getMonthValue() - 1);

      this.inputDay = new JComboBox<>();
      getDayOptions(this.inputYear, this.inputMonth, this.inputDay);
      inputYear.addActionListener(
          e -> getDayOptions(this.inputYear, this.inputMonth, this.inputDay));
      inputMonth.addActionListener(
          e -> getDayOptions(this.inputYear, this.inputMonth, this.inputDay));

      datePanel.add(this.inputYear);
      datePanel.add(this.inputMonth);
      datePanel.add(this.inputDay);
      this.add(datePanel);

      JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      startPanel.setBorder(BorderFactory.createTitledBorder("Start Time:"));
      startTimeSpinner = createTimeSpinner();
      startPanel.add(startTimeSpinner);
      this.add(startPanel);

      JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      endPanel.setBorder(BorderFactory.createTitledBorder("End Time:"));
      endTimeSpinner = createTimeSpinner();
      endPanel.add(endTimeSpinner);
      this.add(endPanel);

      JPanel descriptionPanel = new JPanel(new BorderLayout());
      descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description:"));
      this.inputDescription = new JTextField();
      descriptionPanel.add(this.inputDescription);
      this.add(descriptionPanel);

      JPanel locationPanel = new JPanel(new BorderLayout());
      locationPanel.setBorder(BorderFactory.createTitledBorder("Location:"));
      this.inputLocation = new JTextField();
      locationPanel.add(this.inputLocation);
      this.add(locationPanel);

      JPanel eventStatusPanel = new JPanel(new BorderLayout());
      eventStatusPanel.setBorder(BorderFactory.createTitledBorder("Event Status:"));
      String[] eventStatusList = new String[EventStatus.values().length];
      for (int index = 0; index < EventStatus.values().length; index++) {
        eventStatusList[index] = EventStatus.values()[index].toString();
      }
      this.inputEventStatus = new JComboBox<>(eventStatusList);
      this.inputEventStatus.setSelectedItem("PUBLIC");
      eventStatusPanel.add(this.inputEventStatus);
      this.add(eventStatusPanel);
    }

    String getEventName() {
      return this.inputEventName.getText();
    }

    LocalDateTime getStartTime() {
      return getInputDateTime(this.startTimeSpinner, this.inputYear,
          this.inputMonth, this.inputDay);
    }

    LocalDateTime getEndTime() {
      return getInputDateTime(this.endTimeSpinner, this.inputYear, this.inputMonth, this.inputDay);
    }

    String getDescription() {
      return this.inputDescription.getText();
    }

    String getEventLocation() {
      return this.inputLocation.getText();
    }

    String getEventStatus() {
      return this.inputEventStatus.getSelectedItem().toString();
    }

    void setOldPropertyValues(Event event) {
      inputEventName.setText(event.getSubject());
      LocalDateTime start = event.getStartDateTime();
      LocalDateTime end = event.getEndDateTime();

      inputYear.setSelectedItem(start.getYear());
      inputMonth.setSelectedIndex(start.getMonthValue() - 1);
      // remember to test the day of month when changing year and month!
      getDayOptions(inputYear, inputMonth, inputDay);
      inputDay.setSelectedItem(start.getDayOfMonth());
      setLocalDateTimeToSpinner(event.getStartDateTime(), startTimeSpinner);
      setLocalDateTimeToSpinner(event.getEndDateTime(), endTimeSpinner);
      inputDescription.setText(event.getDescription() != null ? event.getDescription() : "");
      inputLocation.setText(event.getLocation() != null ? event.getLocation() : "");
      inputEventStatus.setSelectedItem(event.getEventStatus().toString());
    }

  }

  private void getDayOptions(JComboBox<Integer> inputYear,
                             JComboBox<String> inputMonth,
                             JComboBox<Integer> inputDay) {
    Integer year = (Integer) inputYear.getSelectedItem();
    int month = inputMonth.getSelectedIndex() + 1;

    Object defaultDay = inputDay.getSelectedItem();
    int daysInThisMonth = YearMonth.of(year, month).lengthOfMonth();
    inputDay.removeAllItems();
    for (int i = 1; i <= daysInThisMonth; i++) {
      inputDay.addItem(i);
    }
    if (defaultDay != null && (Integer) defaultDay <= daysInThisMonth) {
      inputDay.setSelectedItem(defaultDay);
    }
  }

  private JSpinner createTimeSpinner() {
    SpinnerDateModel model = new SpinnerDateModel();
    model.setCalendarField(Calendar.MINUTE);

    JSpinner spinner = new JSpinner(model);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
    spinner.setEditor(editor);

    JFormattedTextField field = editor.getTextField();
    DateFormatter dateFormat = (DateFormatter) field.getFormatter();
    dateFormat.setAllowsInvalid(false);
    dateFormat.setOverwriteMode(true);

    spinner.setPreferredSize(new Dimension(80, 30));
    return spinner;
  }

  private LocalDateTime getInputDateTime(JSpinner spinner,
                                         JComboBox<Integer> inputYear,
                                         JComboBox<String> inputMonth,
                                         JComboBox<Integer> inputDay) {
    int year = (Integer) inputYear.getSelectedItem();
    int month = (Integer) inputMonth.getSelectedIndex() + 1;
    int day = (Integer) inputDay.getSelectedItem();

    Date date = (Date) spinner.getValue();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    return LocalDateTime.of(year, month, day, hour, minute);
  }

  private void setLocalDateTimeToSpinner(LocalDateTime localDateTime, JSpinner spinner) {
    Calendar calendar = Calendar.getInstance();
    // calendar.setTime(localDateTime.toLocalDate());
    calendar.set(Calendar.HOUR_OF_DAY, localDateTime.getHour());
    calendar.set(Calendar.MINUTE, localDateTime.getMinute());
    spinner.setValue(calendar.getTime());
  }

  /**
   * Dialog for creating a recurring event series.
   */
  private class CreateEventSeriesDialog extends JDialog {
    private JTextField inputSubject;
    private JComboBox<Integer> inputYear;
    private JComboBox<String> inputMonth;
    private JComboBox<Integer> inputDay;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JCheckBox allDayCheckbox;
    private JTextField inputDescription;
    private JTextField inputLocation;
    private JComboBox<String> inputStatus;
    private JCheckBox[] weekdayCheckboxes;
    private JRadioButton occurrencesRadio;
    private JRadioButton endDateRadio;
    private JSpinner occurrencesSpinner;
    private JComboBox<Integer> endYearComboBox;
    private JComboBox<String> endMonthComboBox;
    private JComboBox<Integer> endDayComboBox;

    public CreateEventSeriesDialog(JFrame parent, Features features) {
      super(parent, "Create Event Series", true);
      setLayout(new BorderLayout(10, 10));

      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

      JPanel basicPanel = new JPanel();
      basicPanel.setLayout(new BoxLayout(basicPanel, BoxLayout.Y_AXIS));
      basicPanel.setBorder(BorderFactory.createTitledBorder("Event Details"));

      JPanel subjectPanel = new JPanel(new BorderLayout());
      subjectPanel.setBorder(BorderFactory.createTitledBorder("Event Name:"));
      inputSubject = new JTextField();
      subjectPanel.add(inputSubject);
      basicPanel.add(subjectPanel);

      JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      datePanel.setBorder(BorderFactory.createTitledBorder("Date:"));

      int currentYear = LocalDate.now().getYear();
      Integer[] years = new Integer[END_YEAR - START_YEAR];
      for (int i = 0; i < END_YEAR - START_YEAR; i++) {
        years[i] = i + START_YEAR;
      }
      this.inputYear = new JComboBox<>(years);
      this.inputYear.setSelectedItem(currentYear);

      this.inputMonth = new JComboBox<>(months);
      this.inputMonth.setSelectedIndex(LocalDateTime.now().getMonthValue() - 1);

      this.inputDay = new JComboBox<>();
      getDayOptions(this.inputYear, this.inputMonth, this.inputDay);

      inputYear.addActionListener(
          e -> getDayOptions(this.inputYear, this.inputMonth, this.inputDay));
      inputMonth.addActionListener(
          e -> getDayOptions(this.inputYear, this.inputMonth, this.inputDay));

      datePanel.add(this.inputYear);
      datePanel.add(this.inputMonth);
      datePanel.add(this.inputDay);
      basicPanel.add(datePanel);

      JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      startPanel.setBorder(BorderFactory.createTitledBorder("Start Time:"));
      startTimeSpinner = createTimeSpinner();
      startPanel.add(startTimeSpinner);
      basicPanel.add(startPanel);

      JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      endPanel.setBorder(BorderFactory.createTitledBorder("End Time:"));
      endTimeSpinner = createTimeSpinner();
      endPanel.add(endTimeSpinner);
      basicPanel.add(endPanel);

      // All Day Event checkbox
      JPanel allDayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      allDayPanel.setBorder(BorderFactory.createTitledBorder("All Day Event:"));
      allDayCheckbox = new JCheckBox("This is an all-day event");
      allDayCheckbox.addActionListener(e -> {
        boolean allDay = allDayCheckbox.isSelected();
        startTimeSpinner.setEnabled(!allDay);
        endTimeSpinner.setEnabled(!allDay);
      });
      allDayPanel.add(allDayCheckbox);
      basicPanel.add(allDayPanel);

      JPanel descriptionPanel = new JPanel(new BorderLayout());
      descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description:"));
      inputDescription = new JTextField();
      descriptionPanel.add(inputDescription);
      basicPanel.add(descriptionPanel);

      JPanel locationPanel = new JPanel(new BorderLayout());
      locationPanel.setBorder(BorderFactory.createTitledBorder("Location:"));
      inputLocation = new JTextField();
      locationPanel.add(inputLocation);
      basicPanel.add(locationPanel);

      JPanel statusPanel = new JPanel(new BorderLayout());
      statusPanel.setBorder(BorderFactory.createTitledBorder("Event Status:"));
      String[] eventStatusList = new String[EventStatus.values().length];
      for (int index = 0; index < EventStatus.values().length; index++) {
        eventStatusList[index] = EventStatus.values()[index].toString();
      }
      inputStatus = new JComboBox<>(eventStatusList);
      inputStatus.setSelectedItem("PUBLIC");
      statusPanel.add(inputStatus);
      basicPanel.add(statusPanel);

      mainPanel.add(basicPanel);
      mainPanel.add(Box.createVerticalStrut(10));

      JPanel recurrencePanel = new JPanel();
      recurrencePanel.setLayout(new BoxLayout(recurrencePanel, BoxLayout.Y_AXIS));
      recurrencePanel.setBorder(BorderFactory.createTitledBorder("Recurrence"));

      JPanel weekdaysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      weekdaysPanel.add(new JLabel("Repeat on:"));

      String[] dayLabels = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
      weekdayCheckboxes = new JCheckBox[7];
      for (int i = 0; i < 7; i++) {
        weekdayCheckboxes[i] = new JCheckBox(dayLabels[i]);
        weekdaysPanel.add(weekdayCheckboxes[i]);
      }
      recurrencePanel.add(weekdaysPanel);

      JPanel endConditionPanel = new JPanel();
      endConditionPanel.setLayout(new BoxLayout(endConditionPanel, BoxLayout.Y_AXIS));

      ButtonGroup endGroup = new ButtonGroup();

      // End after N occurrences
      JPanel occurrencesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      occurrencesRadio = new JRadioButton("End after");
      occurrencesSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
      occurrencesPanel.add(occurrencesRadio);
      occurrencesPanel.add(occurrencesSpinner);
      occurrencesPanel.add(new JLabel("occurrences"));
      endGroup.add(occurrencesRadio);

      // End by date
      JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      endDateRadio = new JRadioButton("End by date:");

      LocalDate futureDate = LocalDate.now().plusMonths(1);

      Integer[] endYears = new Integer[END_YEAR - START_YEAR];
      for (int i = 0; i < END_YEAR - START_YEAR; i++) {
        endYears[i] = i + START_YEAR;
      }
      endYearComboBox = new JComboBox<>(endYears);
      endYearComboBox.setSelectedItem(futureDate.getYear());

      endMonthComboBox = new JComboBox<>(months);
      endMonthComboBox.setSelectedIndex(futureDate.getMonthValue() - 1);

      endDayComboBox = new JComboBox<>();
      getDayOptions(endYearComboBox, endMonthComboBox, endDayComboBox);

      endYearComboBox.addActionListener(
          e -> getDayOptions(endYearComboBox, endMonthComboBox, endDayComboBox));
      endMonthComboBox.addActionListener(
          e -> getDayOptions(endYearComboBox, endMonthComboBox, endDayComboBox));

      endDatePanel.add(endDateRadio);
      endDatePanel.add(endYearComboBox);
      endDatePanel.add(endMonthComboBox);
      endDatePanel.add(endDayComboBox);
      endGroup.add(endDateRadio);

      occurrencesRadio.setSelected(true);
      occurrencesRadio.addActionListener(e -> {
        occurrencesSpinner.setEnabled(true);
        endYearComboBox.setEnabled(false);
        endMonthComboBox.setEnabled(false);
        endDayComboBox.setEnabled(false);
      });

      endDateRadio.addActionListener(e -> {
        occurrencesSpinner.setEnabled(false);
        endYearComboBox.setEnabled(true);
        endMonthComboBox.setEnabled(true);
        endDayComboBox.setEnabled(true);
      });

      endYearComboBox.setEnabled(false);
      endMonthComboBox.setEnabled(false);
      endDayComboBox.setEnabled(false);

      endConditionPanel.add(occurrencesPanel);
      endConditionPanel.add(endDatePanel);
      recurrencePanel.add(endConditionPanel);
      mainPanel.add(recurrencePanel);

      JScrollPane scrollPane = new JScrollPane(mainPanel);
      add(scrollPane, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      JButton confirmButton = new JButton("Create");
      JButton cancelButton = new JButton("Cancel");

      confirmButton.addActionListener(ev -> {
        try {
          String subject = inputSubject.getText().trim();
          if (subject.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Subject cannot be empty!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
          }
          StringBuilder weekdays = new StringBuilder();
          String[] weekdayCodes = {"M", "T", "W", "R", "F", "S", "U"};
          for (int i = 0; i < 7; i++) {
            if (weekdayCheckboxes[i].isSelected()) {
              weekdays.append(weekdayCodes[i]);
            }
          }
          if (weekdays.length() == 0) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one day!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
          }
          int year = (Integer) inputYear.getSelectedItem();
          int month = inputMonth.getSelectedIndex() + 1;
          int day = (Integer) inputDay.getSelectedItem();
          LocalDate startDate = LocalDate.of(year, month, day);

          String startDateTime;
          String endDateTime;

          if (allDayCheckbox.isSelected()) {
            startDateTime = startDate.toString();
            endDateTime = null;
          } else {
            LocalDateTime startDT = getInputDateTime(startTimeSpinner, inputYear,
                inputMonth, inputDay);
            LocalDateTime endDT = getInputDateTime(endTimeSpinner, inputYear,
                inputMonth, inputDay);

            startDateTime = startDT.toString();
            endDateTime = endDT.toString();
          }

          String description = inputDescription.getText().trim();
          String location = inputLocation.getText().trim();
          String status = inputStatus.getSelectedItem().toString();

          int occurrences = 0;
          String seriesEndDate = null;
          if (occurrencesRadio.isSelected()) {
            occurrences = (Integer) occurrencesSpinner.getValue();
          } else {
            int endYear = (Integer) endYearComboBox.getSelectedItem();
            int endMonth = endMonthComboBox.getSelectedIndex() + 1;
            int endDay = (Integer) endDayComboBox.getSelectedItem();
            LocalDate endDateObj = LocalDate.of(endYear, endMonth, endDay);
            seriesEndDate = endDateObj.toString();
          }

          EventContext context = new EventContext(subject, startDateTime, endDateTime,
              description.isEmpty() ? null : description, location.isEmpty() ? null : location,
              status);
          features.createEventSeries(
              context,
              weekdays.toString(),
              occurrences,
              seriesEndDate
          );

          dispose();

        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this,
              "Invalid input: " + ex.getMessage(),
              "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      });

      cancelButton.addActionListener(ev -> dispose());
      buttonPanel.add(confirmButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);

      setLocationRelativeTo(getParent());
      setSize(550, 700);
    }
  }



  private class SearchEventAcrossCalendarDialog extends JDialog {
    private JTextField inputKeyword;
    private JButton searchButton;

    public SearchEventAcrossCalendarDialog(JFrame parent, Features features) {
      super(parent, "Search Events", true);
      setLayout(new BorderLayout(10, 10));
      setPreferredSize(new Dimension(600, 500));
      //setSize(550, 700);
      JPanel panel = new JPanel(new BorderLayout(10, 10));
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      panel.add(new JLabel("Keyword:"), BorderLayout.WEST);
      this.inputKeyword = new JTextField(15);
      panel.add(this.inputKeyword, BorderLayout.CENTER);

      searchButton = new JButton("Search");
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
      buttonPanel.add(searchButton);
      panel.add(buttonPanel, BorderLayout.EAST);
      add(panel, BorderLayout.NORTH);

      JPanel resultPanel = new JPanel();
      resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
      resultPanel.setBackground(Color.WHITE);
      JScrollPane scrollPane = new JScrollPane(resultPanel);
      scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
      scrollPane.getViewport().setBackground(Color.WHITE);
      add(scrollPane, BorderLayout.CENTER);

      // buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 20));
      // JButton searchButton = new JButton("Search");
      searchButton.addActionListener(ev -> {
        String keyword = inputKeyword.getText();
        if (keyword != null && !keyword.isEmpty()) {
          resultPanel.removeAll();
          List<EventDecorator> events =
              (List<EventDecorator>) features.getEventsAcrossCalendar(keyword);
          if (events == null || events.isEmpty()) {
            resultPanel.add(new JLabel("No events found."));
          } else {
            for (EventDecorator eventDecorator : events) {
              String calendarName = eventDecorator.getCalendarName();
              JPanel row = createSingleEventPanel((Event) eventDecorator.getEvent(), calendarName);
              row.setBorder(BorderFactory.createTitledBorder("Calendar: " + calendarName));
              row.setAlignmentX(Component.LEFT_ALIGNMENT);
              row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

              resultPanel.add(row);
              resultPanel.add(Box.createVerticalStrut(5));
            }
          }
        }
        // dispose();
        resultPanel.revalidate();
        resultPanel.repaint();
      });
      setLocationRelativeTo(getParent());
      pack();
    }

    private void go() {
    }
  }

  private class CopySingleEventDialog extends JDialog {
    private JTextField inputEventName;
    private JComboBox<Integer> inputYear;
    private JComboBox<String> inputMonth;
    private JComboBox<Integer> inputDay;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JTextField inputDescription;
    private JTextField inputLocation;
    private JComboBox<String> inputEventStatus;

    public CopySingleEventDialog(JFrame parent, Features features) {
      super(parent, "Copy Single Event", true);
      setLayout(new BorderLayout(10, 10));
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
    }
  }

  private class EditCalendarDialog extends JDialog {
    private JTextField inputCalendarName;
    private JComboBox<String> inputTimezone;
    private String currentCalendarName;

    public EditCalendarDialog(JFrame parent, Features features) {
      super(parent, "Edit Calendar", true);
      setLayout(new BorderLayout(10, 10));

      currentCalendarName = features.getCurrentCalendarName();
      String currentTimezone = features.getCurrentCalendarTimezone();

      JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
      panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

      panel.add(new JLabel("Calendar Name:"));
      this.inputCalendarName = new JTextField(currentCalendarName);
      panel.add(this.inputCalendarName);

      panel.add(new JLabel("Time Zone:"));
      Set<String> zoneIdSet = ZoneId.getAvailableZoneIds();
      String[] zoneIds = zoneIdSet.stream().sorted().toArray(String[]::new);
      this.inputTimezone = new JComboBox<>(zoneIds);
      this.inputTimezone.setSelectedItem(currentTimezone);
      panel.add(this.inputTimezone);

      add(panel, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      JButton saveButton = new JButton("Save");
      JButton cancelButton = new JButton("Cancel");

      saveButton.addActionListener(ev -> {
        String newCalendarName = inputCalendarName.getText().trim();
        String newTimeZone = inputTimezone.getSelectedItem().toString();

        if (newCalendarName.isEmpty()) {
          JOptionPane.showMessageDialog(this,
              "Calendar name cannot be empty!",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (!newCalendarName.equals(currentCalendarName)) {
          features.editCalendarProperty(currentCalendarName, "name", newCalendarName);
        }

        if (!newTimeZone.equals(currentTimezone)) {
          features.editCalendarProperty(
              newCalendarName.equals(currentCalendarName) ? currentCalendarName : newCalendarName,
              "timezone",
              newTimeZone
          );
        }
        dispose();
      });
      cancelButton.addActionListener(ev -> dispose());

      buttonPanel.add(saveButton);
      buttonPanel.add(cancelButton);
      add(buttonPanel, BorderLayout.SOUTH);

      setLocationRelativeTo(getParent());
      pack();
    }
  }
}