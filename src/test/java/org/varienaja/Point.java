package org.varienaja;

import java.util.Objects;

public class Point {
  public int x;
  public int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Point) {
      Point o = (Point)other;
      return x == o.x && y == o.y;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  public boolean nextTo(Point other) {
    return (x == other.x && (y == other.y + 1 || y == other.y - 1)) //
        || (y == other.y && (x == other.x + 1 || x == other.x - 1));
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
