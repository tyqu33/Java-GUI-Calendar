package calendar.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventKey {
  private final String subject;
  private final LocalDateTime startDateTime;
  private final LocalDateTime endDateTime;

  public EventKey(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    this.subject = subject;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
  }

  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventKey eventKey = (EventKey) o;
    return Objects.equals(subject, eventKey.subject) &&
        Objects.equals(startDateTime, eventKey.startDateTime) &&
        Objects.equals(endDateTime, eventKey.endDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, startDateTime, endDateTime);
  }
}
