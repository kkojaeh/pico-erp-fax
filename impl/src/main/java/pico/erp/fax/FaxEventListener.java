package pico.erp.fax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class FaxEventListener {

  private static final String LISTENER_NAME = "listener.fax-event-listener";

  @Lazy
  @Autowired
  private FaxService faxService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + FaxEvents.FailedEvent.CHANNEL)
  public void onFaxFailed(FaxEvents.FailedEvent event) {

  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + FaxEvents.SentEvent.CHANNEL)
  public void onFaxSent(FaxEvents.SentEvent event) {

  }


}
