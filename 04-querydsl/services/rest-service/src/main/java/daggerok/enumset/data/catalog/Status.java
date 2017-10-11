package daggerok.enumset.data.catalog;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {

  OK("good"),
  NOK("bad"),
  NA("bad");

  public final String name;
}
