package calendar.event;

import calendar.model.EventKey;
import java.util.HashSet;
import java.util.Set;

public class EventSeriesOptional {
  private String seriesId;
  private Set<EventKey> eventKeys;

  public EventSeriesOptional(String seriesId) {
    this.seriesId = seriesId;
    this.eventKeys = new HashSet<>();
  }

  public String getSeriesId() {
    return seriesId;
  }
  public Set<EventKey> getEventKeys() {
    return eventKeys;
  }
  public void addEventKeys(EventKey eventKey) {
    this.eventKeys.add(eventKey);
  }

  public void removeEventKey(EventKey eventKey) {
    this.eventKeys.remove(eventKey);
  }

}
