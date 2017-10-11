package daggerok.history.eventsourcing.applicationevent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CustomEvent extends ApplicationEvent {

  private static final long serialVersionUID = 8539877764403754141L;

  /**
   * Create a new ApplicationEvent.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   */
  public CustomEvent(final Object source) {
    super(source);
  }
}
