package org.varienaja;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Point {
  public enum Direction {
    north(0, -1), south(0, 1), west(-1, 0), east(1, 0);

    public static Direction fromChar(char c) {
      if (c == 'U' || c == 'N') { // Up, North
        return north;
      } else if (c == 'D' || c == 'S') {// Down, South
        return south;
      } else if (c == 'R' || c == 'E') { // Right, East
        return east;
      }
      return west;
    }

    protected int dx;
    protected int dy;

    Direction(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }
  }

  public final int x;
  public final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point add(Direction d) {
    return new Point(x + d.dx, y + d.dy);
  }

  public double angle() {
    double result = 0;
    if (x < 0) {
      result = Math.PI;
    }
    return result + Math.atan((double)y / (double)x);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Point) {
      Point o = (Point)other;
      return x == o.x && y == o.y;
    }
    return false;
  }

  public Set<Point> getAllNeighbours() {
    return new HashSet<>(Arrays.asList(new Point(x - 1, y), new Point(x + 1, y), new Point(x, y - 1), new Point(x, y + 1), new Point(x - 1, y - 1),
        new Point(x - 1, y + 1), new Point(x + 1, y - 1), new Point(x + 1, y + 1)));
  }

  public Point getEast() {
    return add(Direction.east);
  }

  public Point getNorth() {
    return add(Direction.north);
  }

  public Set<Point> getNSWENeighbours() {
    return new HashSet<>(Arrays.asList(getWest(), getEast(), getNorth(), getSouth()));
  }

  public Point getSouth() {
    return add(Direction.south);
  }

  public Point getWest() {
    return add(Direction.west);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  public int manhattanDistance(Point target) {
    return Math.abs(target.x - x) + Math.abs(target.y - y);
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
