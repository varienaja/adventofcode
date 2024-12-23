package org.varienaja;

import java.util.Objects;
import java.util.Set;

public class Point {
  public static Point dNorthEast = new Point(1, -1);
  public static Point dNorthWest = new Point(-1, -1);
  public static Point dNorth = new Point(0, -1);
  public static Point dSouth = new Point(0, 1);
  public static Point dSouthEast = new Point(1, 1);
  public static Point dSouthWest = new Point(-1, 1);
  public static Point dWest = new Point(-1, 0);
  public static Point dEast = new Point(1, 0);

  public static final Set<Point> ALLDIRECTIONS = Set.of(Point.dEast, Point.dSouthEast, Point.dSouth, Point.dSouthWest, Point.dWest, Point.dNorthWest,
      Point.dNorth, Point.dNorthEast);

  public static Point directionFromChar(char c) {
    if (c == 'U' || c == 'N' || c == '^') { // Up, North
      return dNorth;
    } else if (c == 'D' || c == 'S' || c == 'v') { // Down, South
      return dSouth;
    } else if (c == 'R' || c == 'E' || c == '>') { // Right, East
      return dEast;
    }
    return dWest;
  }

  public final int x;
  public final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point add(Point d) {
    return new Point(x + d.x, y + d.y);
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
    return Set.of(getWest(), getEast(), getNorth(), getSouth(), getNorthEast(), getNorthWest(), getSouthEast(), getSouthWest());
  }

  public Point getEast() {
    return add(dEast);
  }

  public Set<Point> getEastNeighbours() {
    return Set.of(getEast(), getNorthEast(), getSouthEast());
  }

  public Point getNorth() {
    return add(dNorth);
  }

  public Point getNorthEast() {
    return add(dNorthEast);
  }

  public Set<Point> getNorthNeighbours() {
    return Set.of(getNorth(), getNorthWest(), getNorthEast());
  }

  public Point getNorthWest() {
    return add(dNorthWest);
  }

  public Set<Point> getNSWENeighbours() {
    return Set.of(getWest(), getEast(), getNorth(), getSouth());
  }

  public Point getSouth() {
    return add(dSouth);
  }

  public Point getSouthEast() {
    return add(dSouthEast);
  }

  public Set<Point> getSouthNeighbours() {
    return Set.of(getSouth(), getSouthEast(), getSouthWest());
  }

  public Point getSouthWest() {
    return add(dSouthWest);
  }

  public Point getWest() {
    return add(dWest);
  }

  public Set<Point> getWestNeighbours() {
    return Set.of(getWest(), getNorthWest(), getSouthWest());
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
