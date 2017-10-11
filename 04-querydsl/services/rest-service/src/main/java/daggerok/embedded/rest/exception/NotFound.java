package daggerok.embedded.rest.exception;

import static java.lang.String.format;

public class NotFound extends RuntimeException {

  private static final long serialVersionUID = 502579245472048750L;

  private NotFound(final String message) {
    super(message);
  }

  public static NotFound byId(final Long id) {
    return new NotFound(format("entity by id %d wasn't found.", id));
  }
}
