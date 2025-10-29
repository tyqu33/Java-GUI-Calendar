package calendar.event;

import calendar.enums.EventStatus;
import calendar.model.EventKey;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class EventSeries implements EventInterface{
  private final String seriesId;
  private final String subject;
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final String weekdays;
  private final Integer occurrences;
  private final LocalDate endDate;
  private final LocalDate firstOccurrence;
  private String description;
  private String location;
  private EventStatus status;
  private final boolean isAllDay;
  private static final LocalTime START_TIME = LocalTime.of(8, 0);
  private static final LocalTime END_TIME = LocalTime.of(17, 0);

  private final Set<EventKey> eventKeys;
  private final Set<LocalDate> removeDates;

  private EventSeries(EventSeriesBuilder builder) {
    if (builder.existingSeriesId != null) {
      this.seriesId = builder.existingSeriesId;
    } else {
      this.seriesId = UUID.randomUUID().toString();
    }
    this.subject = builder.subject;
    this.weekdays = builder.weekdays;
    this.occurrences = builder.occurrences;
    this.endDate = builder.endDate;
    this.firstOccurrence = builder.firstOccurrence;
    this.description = builder.description;
    this.location = builder.location;
    this.status = builder.status;
    this.isAllDay = builder.isAllDay;
    this.eventKeys = new HashSet<>();
    this.removeDates = new HashSet<>(builder.existingRemovedDates);
    if (this.isAllDay) {
      this.startTime = START_TIME;
      this.endTime = END_TIME;
    } else {
      this.startTime = builder.startTime;
      this.endTime = builder.endTime;
    }
    generateKeys();
  }

  public static EventSeriesBuilder builder(String subject, LocalDateTime start, String weekdays) {
    return new EventSeriesBuilder(subject, start, weekdays);
  }

  public EventSeriesBuilder toBuilder() {
    EventSeriesBuilder builder = new EventSeriesBuilder(
        this.subject,
        LocalDateTime.of(this.firstOccurrence, this.startTime),
        this.weekdays);
    if (this.endTime != null) {
      builder.end(LocalDateTime.of(this.firstOccurrence, this.endTime));
    } else if (this.isAllDay) {
      builder.setAllDay();
    }

    builder.description(this.description)
        .location(this.location)
        .status(this.status == EventStatus.PRIVATE ? "private" : "public")
        .withExistingId(this.seriesId)
        .withExistingRemovedDates(this.removeDates);

    if (this.occurrences != null) {
      builder.occurrences(this.occurrences);
    } else if (this.endDate != null) {
      builder.setEndDate(this.endDate);
    }
    return builder;
  }

  public static class EventSeriesBuilder {
    private String subject;
    private LocalTime startTime;
    private LocalDate firstOccurrence;
    private String weekdays;
    private LocalTime endTime;
    private Integer occurrences;
    private LocalDate endDate;
    private String description;
    private String location;
    private EventStatus status = EventStatus.PUBLIC;
    private boolean isAllDay = false;
    private String existingSeriesId = null;
    private Set<LocalDate> existingRemovedDates = new HashSet<>();

    public EventSeriesBuilder(String subject, LocalDateTime start, String weekdays) {
      if (subject == null || subject.trim().isEmpty()) {
        throw new IllegalArgumentException("Subject cannot be null or empty");
      }
      if (start == null) {
        throw new IllegalArgumentException("Start time cannot be null");
      }
      if (weekdays == null || weekdays.trim().isEmpty()) {
        throw new IllegalArgumentException("Weekdays cannot be null or empty");
      }
      if (!isValidWeekdays(weekdays)) {
        throw new IllegalArgumentException("Invalid weekdays provided");
      }
      this.subject = subject.trim();
      this.startTime = start.toLocalTime();
      this.firstOccurrence = start.toLocalDate();
      this.weekdays = weekdays.toUpperCase();
    }

    public EventSeriesBuilder end(LocalDateTime end) {
      if (end != null) {
        if (end.toLocalTime().isBefore(this.startTime)) {
          throw new IllegalArgumentException("End time cannot be before start time");
        }
        this.endTime = end.toLocalTime();
      }
      return this;
    }

    public EventSeriesBuilder setAllDay() {
      this.isAllDay = true;
      this.endTime = null;
      return this;
    }

    public EventSeriesBuilder withExistingId(String seriesId) {
      this.existingSeriesId = seriesId;
      return this;
    }

    public EventSeriesBuilder withExistingRemovedDates(Set<LocalDate> removedDates) {
      this.existingRemovedDates = new HashSet<>(removedDates);
      return this;
    }

    public EventSeriesBuilder occurrences(Integer occurrences) {
      if (occurrences <= 0) {
        throw new IllegalArgumentException("Occurrences must be greater than zero");
      }
      this.occurrences = occurrences;
      this.endDate = null;
      return this;
    }

    public EventSeriesBuilder setEndDate(LocalDate endDate) {
      if (endDate == null) {
        throw new IllegalArgumentException("End date cannot be null");
      }
      if (endDate.isBefore(firstOccurrence)) {
        throw new IllegalArgumentException("End date cannot be before first occurrence");
      }
      this.endDate = endDate;
      this.occurrences = null;
      return this;
    }

    public EventSeriesBuilder description(String description) {
      this.description = description;
      return this;
    }

    public EventSeriesBuilder location(String location) {
      this.location = location;
      return this;
    }

    public EventSeriesBuilder status(String status) {
      if (status == null || status.trim().isEmpty()) {
        return this;
      }
      if (status.equals("private")) {
        this.status = EventStatus.PRIVATE;
      } else {
        this.status = EventStatus.PUBLIC;
      }
      return this;
    }

    public EventSeries build() {
      if (occurrences == null && endDate == null) {
        throw new IllegalArgumentException("Must input either occurrences or end date");
      }
      if (!isAllDay && endTime == null) {
        throw new IllegalArgumentException("Must be all-day event or input end date");
      }
      return new EventSeries(this);
    }

    private boolean isValidWeekdays(String weekdays) {
      if (weekdays == null || weekdays.trim().isEmpty()) {
        throw new IllegalArgumentException("Weekdays cannot be null or empty");
      }
      String validWeekdays = "MTWRFSU";
      for (char c : weekdays.toUpperCase().toCharArray()) {
        if (validWeekdays.indexOf(c) == -1) {
          return false;
        }
      }
      return true;
    }
  }

  public Set<EventKey> generateKeys() {
    this.eventKeys.clear();
    Set<DayOfWeek> targetDays = parseWeekdays(weekdays);
    LocalDate currentDate = this.firstOccurrence;
    int count = 0;
    int maxDays = 3650;
    int daysSearched = 0;
    LocalTime currentTime = this.startTime;
    LocalTime endTime = this.endTime;

    while (daysSearched < maxDays) {
      if (occurrences != null && count >= occurrences) {
        break;
      }
      if (endDate != null && currentDate.isAfter(endDate)) {
        break;
      }
      if (targetDays.contains(currentDate.getDayOfWeek())) {
        if (!removeDates.contains(currentDate)) {
          LocalDateTime startDate = LocalDateTime.of(currentDate, currentTime);
          LocalDateTime endDate = LocalDateTime.of(currentDate, endTime);
          EventKey eventKey = new EventKey(subject, startDate, endDate);
          this.eventKeys.add(eventKey);
        }
        count++;
      }
      currentDate = currentDate.plusDays(1);
      daysSearched++;
    }
    if (this.eventKeys.isEmpty()) {
      throw new IllegalArgumentException("No events found");
    }
    return Collections.unmodifiableSet(this.eventKeys);
  }

  public void markDateRemoved(LocalDate date) {
    this.removeDates.add(date);
  }

  public void updateSeriesKeys(EventKey key, boolean add) {
    if (add) {
      this.eventKeys.add(key);
    } else {
      this.eventKeys.remove(key);
    }
  }

  private Set<DayOfWeek> parseWeekdays(String weekdays) {
    Set<DayOfWeek> targetDays = new HashSet<>();
    for (char c : weekdays.toUpperCase().toCharArray()) {
      switch (c) {
        case 'M':
          targetDays.add(DayOfWeek.MONDAY);
          break;
        case 'T':
          targetDays.add(DayOfWeek.TUESDAY);
          break;
        case 'W':
          targetDays.add(DayOfWeek.WEDNESDAY);
          break;
        case 'R':
          targetDays.add(DayOfWeek.THURSDAY);
          break;
        case 'F':
          targetDays.add(DayOfWeek.FRIDAY);
          break;
        case 'S':
          targetDays.add(DayOfWeek.SATURDAY);
          break;
        case 'U':
          targetDays.add(DayOfWeek.SUNDAY);
          break;
      }
    }
    return targetDays;
  }

  @Override
  public String getSeriesId() {
    return seriesId;
  }

  public Set<EventKey> getEventKeys() {
    return new HashSet<>(eventKeys);
  }

  public void addEventKey(EventKey eventKey) {
    this.eventKeys.add(eventKey);
  }

  public void removeEventKey(EventKey eventKey) {
    this.eventKeys.remove(eventKey);
  }

  public Set<EventKey> getSeriesKeys() {
    return Collections.unmodifiableSet(this.eventKeys);
  }

  public Set<LocalDate> getRemovedDates() {
    return Collections.unmodifiableSet(removeDates);
  }

  public LocalDate getFirstOccurrence() {
    return  firstOccurrence;
  }

  public String getWeekdays() {
    return weekdays;
  }

  public Integer getOccurrences() {
    return occurrences;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public boolean isAllDay() {
    return isAllDay;
  }

  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public LocalDateTime getStartDateTime() {
    return LocalDateTime.of(firstOccurrence, startTime);
  }

  @Override
  public LocalDateTime getEndDateTime() {
    return LocalDateTime.of(firstOccurrence, endTime);
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public EventStatus getEventStatus() {
    return status;
  }

  @Override
  public void editDescription(String newDescription) {
    this.description = newDescription;
  }

  @Override
  public void editLocation(String newLocation) {
    this.location = newLocation;
  }

  @Override
  public void editEventStatus(String newEventStatus) {
    if (newEventStatus.equals("private")) {
      this.status = EventStatus.PRIVATE;
    } else {
      this.status = EventStatus.PUBLIC;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(seriesId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventSeries that = (EventSeries) o;
    return Objects.equals(seriesId, that.seriesId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("EventSeries: ID: ");
    sb.append(seriesId.substring(0, 8));
    sb.append(" subject: ");
    sb.append(subject);
    sb.append(" at ");
    sb.append(startTime);
    sb.append(" on ");
    sb.append(weekdays);
    if (occurrences != null) {
      sb.append(" for ").append(occurrences).append(" times");
    } else {
      sb.append(" until ").append(endDate);
    }
    return sb.toString();
  }
}
