package pico.erp.fax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface FaxEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class SentEvent implements Event {

    public final static String CHANNEL = "event.fax.sent";

    private FaxId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class FailedEvent implements Event {

    public final static String CHANNEL = "event.fax.failed";

    private FaxId id;

    public String channel() {
      return CHANNEL;
    }

  }

}
