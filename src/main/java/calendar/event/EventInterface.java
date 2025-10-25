package calendar.event;

import calendar.enums.EventStatus;
import calendar.enums.UserStatus;
import java.time.LocalDateTime;

/**
 * This interface contains necessary operations that an event should support.
 */
public interface EventInterface {
  String getSubject();

  LocalDateTime getStartDateTime();

  LocalDateTime getEndDateTime();
  String getDescription();
  String getLocation();
  EventStatus getEventStatus();
  String getSeriesId();

  void editDescription(String newDescription);
  void editLocation(String newLocation);
  void editEventStatus(String newEventStatus);
}
