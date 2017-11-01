package daggerok.utils;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ClassName {

  public static String nameOf(final Class<?> theClass) {
    return theClass.getSimpleName();
  }
}
