package org.varienaja;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A square shape.
 */
public class SquareShape {
  private final Point upperLeft;
  private final Point lowerRight;

  /**
   * Constructor.
   *
   * @param x1 left x coordinate
   * @param y1 upper y coordinate
   * @param x2 right x coordinate
   * @param y2 lower y coordinate
   */
  public SquareShape(int x1, int y1, int x2, int y2) {
    this(new Point(x1, y1), new Point(x2, y2));
  }

  /**
   * Constructor.
   *
   * @param upperLeft upper left point of this square
   * @param lowerRight lower right point of this square
   */
  public SquareShape(Point upperLeft, Point lowerRight) {
    this.upperLeft = Objects.requireNonNull(upperLeft);
    this.lowerRight = Objects.requireNonNull(lowerRight);
    assert upperLeft.x <= lowerRight.x && upperLeft.y <= lowerRight.y;
  }

  public Set<Point> getNeighbours() {
    Set<Point> nbs = new HashSet<>();

    for (int x = upperLeft.x - 1; x <= lowerRight.x + 1; ++x) {
      nbs.add(new Point(x, upperLeft.y - 1));
      nbs.add(new Point(x, lowerRight.y + 1));
    }

    for (int y = upperLeft.y - 1; y <= lowerRight.y + 1; ++y) {
      nbs.add(new Point(upperLeft.x - 1, y));
      nbs.add(new Point(lowerRight.x + 1, y));
    }

    return nbs;
  }

}
