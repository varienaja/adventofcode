package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.awt.Polygon;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInput(), true);
    System.out.println(result);
    assertEquals(4733727792L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInput(), false);
    System.out.println(result);
    assertEquals(1566346198L, result);
  }

  @Test
  public void testA() {
    assertEquals(50L, solve(getTestInput(), true));
  }

  @Test
  public void testB() {
    assertEquals(24L, solve(getTestInput(), false));
  }

  private long solve(List<String> lines, boolean partA) {
    Polygon pg = new Polygon();

    List<Point> points = new LinkedList<>();
    for (String line : lines) {
      String[] parts = line.split(",");
      Point p = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
      points.add(p);

      pg.addPoint(p.x, p.y);
    }

    long max = 0L;
    Iterator<Point> it = points.iterator();
    while (it.hasNext()) {
      Point p1 = it.next();
      it.remove();
      for (Point p2 : points) {
        long area = (1L + Math.abs((long)(p1.x - p2.x))) * (1L + Math.abs((long)(p1.y - p2.y)));
        if (area > max) {
          if (partA || pg.contains(1 + Math.min(p1.x, p2.x), 1 + Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x) - 1, Math.abs(p1.y - p2.y) - 1)) {
            max = area;
          }
        }
      }
    }

    return max;
  }

}
