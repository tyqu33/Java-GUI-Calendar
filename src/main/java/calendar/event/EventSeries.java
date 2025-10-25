package calendar.event;

import calendar.enums.EventStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EventSeries implements EventInterface{
  private final String subject;
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final String weekdays;
  private final Integer occurrences;
  private final LocalDate endDate;
  private final LocalDate firstOccurrence;
  private final String description;
  private final String location;
  private final EventStatus status;
  private List<Event> events;

  private EventSeries(EventSeriesBuilder builder) {
    this.subject = builder.subject;
    this.startTime = builder.startTime;
    this.endTime = builder.endTime;
    this.weekdays = builder.weekdays;
    this.occurrences = builder.occurrences;
    this.endDate = builder.endDate;
    this.firstOccurrence = builder.firstOccurrence;
    this.description = builder.description;
    this.location = builder.location;
    this.status = builder.status;
    this.events = new ArrayList<>();
  }

  public static EventSeriesBuilder builder(String subject, LocalDateTime start, String weekdays) {
    return new EventSeriesBuilder(subject, start, weekdays);
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
      this.subject = subject;
      this.startTime = start.toLocalTime();
      this.firstOccurrence = start.toLocalDate();
      this.weekdays = weekdays.toUpperCase();
    }

    public EventSeriesBuilder end(LocalDateTime end) {
      if (end != null) {
        if (!firstOccurrence.equals(end.toLocalDate())) {
          throw new IllegalArgumentException("Event series cannot span multiple dats");
        }
        this.endTime = end.toLocalTime();
      }
      return this;
    }

    public EventSeriesBuilder allDay() {
      this.isAllDay = true;
      this.endTime = null;
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

  public List<Event> createEvents() {
    events.clear();
    Set<DayOfWeek> targetDays = parseWeekdays(weekdays);
    LocalDate currentDate = this.firstOccurrence;
    int count = 0;
    int maxDate = 3650;
    int iterations = 0;
    while (iterations < maxDate) {
      iterations++;
      if (occurrences != null && count >= occurrences) {
        break;
      }
      if (endDate != null && currentDate.isAfter(endDate)) {
        break;
      }
      if (targetDays.contains(currentDate.getDayOfWeek())) {
        Event event = createSingleEvent(currentDate);
        this.events.add(event);
        count++;
        if (occurrences != null && count >= occurrences) {
          break;
        }
      }
      currentDate = currentDate.plusDays(1);
    }
    return new ArrayList<>(events);
  }

  private Event createSingleEvent(LocalDate date) {
    LocalDateTime start = LocalDateTime.of(date, startTime);
    LocalDateTime end = null;
    if (endTime != null) {
      end = LocalDateTime.of(date, endTime);
    }
    Event.EventBuilder builder = Event.builder(subject, start);
    if (end != null) {
      builder.end(end);
    } else {
      builder.setAllDayEvent();
    }
    if (description != null) {
      builder.description(description);
    }
    if (location != null) {
      builder.location(location);
    }
    builder.status(status == EventStatus.PRIVATE?"private":"public");
    return builder.build();
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

  public boolean containsEvent(Event event) {
    return events.contains(event);
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

  }

  @Override
  public void editLocation(String newLocation) {

  }

  @Override
  public void editEventStatus(String newEventStatus) {

  }

  @Override
  public int hashCode() {
    if (occurrences != null) {
      return Objects.hash(subject, startTime, weekdays, occurrences);
    } else  {
      return Objects.hash(subject, startTime, weekdays, endDate);
    }
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
    if (occurrences != null) {
      return Objects.equals(subject, that.subject) &&
          Objects.equals(startTime, that.startTime) &&
          Objects.equals(weekdays, that.weekdays) &&
          Objects.equals(occurrences, that.occurrences);
    } else {
      return Objects.equals(subject, that.subject) &&
          Objects.equals(startTime, that.startTime) &&
          Objects.equals(weekdays, that.weekdays) &&
          Objects.equals(endDate, that.endDate);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Event Series: ");
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
