package calendar.calendarEntity;

import calendar.model.Calendar;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Represents a calendar entity with a unique name, timezone, and calendar object.
 */
public class CalendarEntity implements CalendarEntityInterface {
  private String calendarName;
  private ZoneId zoneId;
  private final Calendar calendar;

  /**
   * Private constructor used by the Builder.
   *
   * @param builder the builder instance
   */
  private CalendarEntity(CalendarEntityBuilder builder) {
    this.calendarName = builder.calendarName;
    this.zoneId = builder.zoneId;
    this.calendar = new Calendar();
  }

  /**
   * Static factory method to create a new CalendarEntityBuilder.
   *
   * @param calendarName the name of the calendar entity
   * @param zoneId       the timezone of the calendar entity
   * @return a new CalendarEntityBuilder instance
   */
  public static CalendarEntityBuilder builder(String calendarName, ZoneId zoneId) {
    return new CalendarEntityBuilder(calendarName, zoneId);
  }

  /**
   * Creates a new builder initialized with the current data, for modifying an existing calendar.
   *
   * @return a CalendarEntityBuilder with current values
   */
  public CalendarEntityBuilder toBuilder() {
    CalendarEntityBuilder builder = new CalendarEntityBuilder(
        this.calendarName,
        this.zoneId
    );
    return builder;
  }

  /**
   * Inner builder class for constructing a CalendarEntity object.
   */
  public static class CalendarEntityBuilder {
    private String calendarName;
    private ZoneId zoneId;

    /**
     * Constructs a new CalendarEntityBuilder with required parameters.
     *
     * @param calendarName the name of the calendar entity
     * @param zoneId       the timezone of the calendar entity
     * @throws IllegalArgumentException if calendar name is null or empty, or timezone is null
     */
    public CalendarEntityBuilder(String calendarName, ZoneId zoneId) {
      if (calendarName == null || calendarName.trim().isEmpty()) {
        throw new IllegalArgumentException("Calendar name cannot be null or empty");
      }
      if (zoneId == null) {
        throw new IllegalArgumentException("Timezone cannot be null");
      }
      this.calendarName = calendarName.trim();
      this.zoneId = zoneId;
    }

    /**
     * Sets the name of the calendar entity.
     *
     * @param calendarName the calendar name
     * @return the builder instance
     * @throws IllegalArgumentException if calendar name is null or empty
     */
    public CalendarEntityBuilder calendarName(String calendarName) {
      if (calendarName == null || calendarName.trim().isEmpty()) {
        throw new IllegalArgumentException("Calendar name cannot be null or empty");
      }
      this.calendarName = calendarName.trim();
      return this;
    }

    /**
     * Sets the timezone of the calendar entity.
     *
     * @param zoneId the timezone
     * @return the builder instance
     * @throws IllegalArgumentException if timezone is null
     */
    public CalendarEntityBuilder timezone(ZoneId zoneId) {
      if (zoneId == null) {
        throw new IllegalArgumentException("Timezone cannot be null");
      }
      this.zoneId = zoneId;
      return this;
    }

    /**
     * Creates and returns the CalendarEntity object.
     *
     * @return the CalendarEntity
     */
    public CalendarEntity build() {
      return new CalendarEntity(this);
    }
  }

  @Override
  public String getCalendarName() {
    return calendarName;
  }

  @Override
  public ZoneId getTimeZone() {
    return zoneId;
  }

  @Override
  public Calendar getCalendar() {
    return calendar;
  }

  /**
   * Updates the name of the calendar entity.
   *
   * @param calendarName the new name of the calendar entity
   * @throws IllegalArgumentException if the name is null or empty
   */
  void editCalendarName(String calendarName) {
    if (calendarName == null || calendarName.trim().isEmpty()) {
      throw new IllegalArgumentException("Calendar name cannot be null or empty");
    }
    this.calendarName = calendarName.trim();
  }

  /**
   * Updates the timezone of the calendar entity.
   *
   * @param zoneId the new timezone of the calendar entity
   * @throws IllegalArgumentException if timezone is null
   */
  void editTimeZone(ZoneId zoneId) {
    if (zoneId == null) {
      throw new IllegalArgumentException("Timezone cannot be null");
    }
    this.zoneId = zoneId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalendarEntity that = (CalendarEntity) o;
    return Objects.equals(calendarName, that.calendarName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(calendarName);
  }

  @Override
  public String toString() {
    return "CalendarEntity{" +
        "name='" + calendarName + '\'' +
        ", timezone=" + zoneId +
        '}';
  }
}