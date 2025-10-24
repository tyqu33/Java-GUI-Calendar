package calendar.event;

import java.time.LocalDateTime;

public class Event implements EventInterface {

  @Override
  public String getSubject() {
    return "";
  }

  @Override
  public LocalDateTime getStartDateTime() {
    return null;
  }

  @Override
  public LocalDateTime getEndDateTime() {
    return null;
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public String getLocation() {
    return "";
  }

  @Override
  public String getEventStatus() {
    return "";
  }
}
