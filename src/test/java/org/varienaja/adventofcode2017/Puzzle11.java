package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {
  static class Point3D {
    int x, y, z;

    public Point3D(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public Point3D add(Point3D p) {
      return new Point3D(x + p.x, y + p.y, z + p.z);
    }

    @Override
    public boolean equals(Object other) {
      if (other instanceof Point3D) {
        Point3D o = (Point3D)other;
        return x == o.x && y == o.y && z == o.z;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z);
    }
  }

  private static final Point3D n = new Point3D(0, 1, -1);
  private static final Point3D ne = new Point3D(1, 0, -1);
  private static final Point3D se = new Point3D(1, -1, 0);
  private static final Point3D s = new Point3D(0, -1, 1);
  private static final Point3D sw = new Point3D(-1, 0, 1);
  private static final Point3D nw = new Point3D(-1, 1, 0);
  private static Map<String, Point3D> direction2Point = new LinkedHashMap<>();
  static {
    direction2Point.put("ne", ne);
    direction2Point.put("nw", nw);
    direction2Point.put("se", se);
    direction2Point.put("sw", sw);
    direction2Point.put("n", n);
    direction2Point.put("s", s);
  }
  private long maxDist;

  private long dist(Point3D start, Point3D end) {
    return (Math.abs(start.x - end.x) + Math.abs(start.y - end.y) + Math.abs(start.z - end.z)) / 2;
  }

  private long solveA(String input) {
    maxDist = 0;
    String[] parts = input.split(",");

    Point3D start = new Point3D(0, 0, 0);
    Point3D pos = start;
    for (String part : parts) {
      pos = pos.add(direction2Point.get(part));
      maxDist = Math.max(maxDist, dist(start, pos));
    }

    return dist(start, pos);
  }

  private long solveB(String input) {
    solveA(input);
    return maxDist;
  }

  @Test
  public void testDay10() {
    assertEquals(3, solveA("ne,ne,ne"));
    assertEquals(0, solveA("ne,ne,sw,sw"));
    assertEquals(2, solveA("ne,ne,s,s"));
    assertEquals(3, solveA("se,sw,se,sw,sw"));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines.get(0));
    System.out.println(result);
    assertEquals(675, result);

    announceResultB();
    result = solveB(lines.get(0));
    System.out.println(result);
    assertEquals(1424, result);
  }

}
