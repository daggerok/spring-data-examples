package daggerok.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.control.Try;

public final class Json {

  public static final ObjectMapper objectMapper = new ObjectMapper();

  private Json() {
    objectMapper.registerModule(new JavaTimeModule());
  }

  public static String stringify(final Object object) {

    return Try.of(() -> objectMapper.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(object))
              .get();
  }
}
