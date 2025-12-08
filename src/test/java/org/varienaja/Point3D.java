package org.varienaja;

import java.util.Objects;
import java.util.Set;

public class Point3D extends Point {
  public int z;

  public Point3D(int x, int y, int z) {
    super(x, y);
    this.z = z;
  }

  public Point3D(String x, String y, String z) {
    this(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Point3D o) {
      return x == o.x && y == o.y && z == o.z;
    }
    return false;
  }

  public double euclidianDistance(Point3D other) {
    return Math.sqrt(Math.pow(Math.abs(x - other.x), 2) + Math.pow(Math.abs(y - other.y), 2) + Math.pow(Math.abs(z - other.z), 2));
  }

  public Set<Point3D> getNSWEUDNeighbours() {
    return Set.of( //
        new Point3D(x + 1, y, z), //
        new Point3D(x - 1, y, z), //
        new Point3D(x, y + 1, z), //
        new Point3D(x, y - 1, z), //
        new Point3D(x, y, z + 1), //
        new Point3D(x, y, z - 1));
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + "," + z + ")";
  }

}
