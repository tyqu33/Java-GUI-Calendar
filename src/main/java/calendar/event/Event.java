package calendar.event;

import calendar.enums.EventStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Event implements EventInterface {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private String description ;
  private String location;
  private EventStatus status;
  private String seriesId;
  private static final LocalTime START_TIME = LocalTime.of(8, 0);
  private static final LocalTime END_TIME = LocalTime.of(17, 0);

  private Event (EventBuilder builder) {
    this.subject = builder.subject;
    if (builder.isAllDayEvent && builder.end == null) {
      this.start = builder.start.with(START_TIME);
      this.end = builder.start.with(END_TIME);
    } else {
      this.start = builder.start;
      this.end = builder.end;
    }
    this.description = builder.description;
    this.location = builder.location;
    this.status = builder.status;
    this.seriesId = builder.seriesId;
    if (this.end != null && this.end.isBefore(this.start)) {
      throw new IllegalArgumentException("End date/time must be after start date/time.");
    }
  }

  public static EventBuilder builder(String subject, LocalDateTime start) {
    return new EventBuilder(subject, start);
  }

  public static class EventBuilder {
    private String subject;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description ;
    private String location;
    private EventStatus status = EventStatus.PUBLIC;
    public boolean isAllDayEvent = false;
    private String seriesId;

    public EventBuilder(String subject, LocalDateTime start) {
      if (subject == null || subject.trim().isEmpty()) {
        throw new IllegalArgumentException("Subject cannot be null or empty");
      }
      if (start == null) {
        throw new IllegalArgumentException("Start date cannot be null");
      }
      this.subject = subject.trim();
      this.start = start;
    }

    public EventBuilder end(LocalDateTime end) {
      this.end = end;
      this.isAllDayEvent = false;
      return this;
    }

    public EventBuilder description(String description) {
      this.description = description;
      return this;
    }

    public EventBuilder location(String location) {
      this.location = location;
      return this;
    }

    public EventBuilder status(String status) {
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

    public EventBuilder setAllDayEvent() {
      this.isAllDayEvent = true;
      return this;
    }

    public EventBuilder seriesId(String seriesId) {
      this.seriesId = seriesId;
      return this;
    }

    public Event build() {
      return new Event(this);
    }
  }

  public boolean isAllDayEvent() {
    if (end != null && start.toLocalTime().equals(START_TIME) &&
        end.toLocalTime().equals(END_TIME)) {
      return true;
    }
    return false;
  }

  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public LocalDateTime getStartDateTime() {
    return start;
  }

  @Override
  public LocalDateTime getEndDateTime() {
    return end;
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
  public String getSeriesId() {
    return seriesId;
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
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(subject);
    sb.append(" from ");
    sb.append(start);
    if (end != null) {
      sb.append(" to ");
      sb.append(end);
    }
    if (location != null) {
      sb.append(" at ");
      sb.append(location);
    }
    return sb.toString();
  }
}
