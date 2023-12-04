package org.varienaja;

import java.util.Objects;

/**
 * A {@link Point} that holds contextual information.
 *
 * @param <T> type of the context
 */
public class InfoPoint<T extends Object> extends Point {
  private final T info;

  /**
   * Constructor.
   *
   * @param x x-value
   * @param y y-value
   * @param info contextual information
   */
  public InfoPoint(int x, int y, T info) {
    super(x, y);
    this.info = Objects.requireNonNull(info);
  }

  /**
   * Retrieves the contextual information.
   *
   * @return the information
   */
  public T getInfo() {
    return info;
  }

}
