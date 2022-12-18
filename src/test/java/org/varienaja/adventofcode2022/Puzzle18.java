package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  class Point3D {
    final int[] dims;

    Point3D(Point3D p) {
      dims = new int[3];
      System.arraycopy(p.dims, 0, dims, 0, 3);
    }

    Point3D(String x, String y, String z) {
      dims = new int[3];
      dims[0] = Integer.parseInt(x);
      dims[1] = Integer.parseInt(y);
      dims[2] = Integer.parseInt(z);
    }

    @Override
    public boolean equals(Object o) {
      Point3D p = (Point3D)o;
      return Arrays.equals(dims, p.dims);
    }

    @Override
    public int hashCode() {
      return Objects.hash(dims[0], dims[1], dims[2]);
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(3466L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(2012L, result);
  }

  @Test
  public void testA() {
    assertEquals(64, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(58, solveB(getTestInput()));
  }

  private List<String> getTestInput() {
    return List.of( //
        "2,2,2", //
        "1,2,2", //
        "3,2,2", //
        "2,1,2", //
        "2,3,2", //
        "2,2,1", //
        "2,2,3", //
        "2,2,4", //
        "2,2,6", //
        "1,2,5", //
        "3,2,5", //
        "2,1,5", //
        "2,3,5");
  }

  private Set<Point3D> parse(List<String> lines) {
    Set<Point3D> result = new HashSet<>();
    for (String line : lines) {
      String[] parts = line.split(",");

      Point3D point = new Point3D(parts[0], parts[1], parts[2]);
      result.add(point);
    }
    return result;
  }

  private long solveA(List<String> lines) {
    Set<Point3D> points = parse(lines);

    long result = 0;
    for (Point3D p : points) { // check 6 nbs of p
      for (int i = 0; i < 3; i++) {
        for (int dp : new int[] {
            -1, 1
        }) {
          Point3D nb = new Point3D(p);
          nb.dims[i] += dp;
          if (!points.contains(nb)) {
            ++result;
          }
        }
      }
    }

    return result;
  }

  private long solveB(List<String> lines) {
    Set<Point3D> points = parse(lines);
    IntSummaryStatistics stats = points.stream().flatMapToInt(p -> Arrays.stream(p.dims)).summaryStatistics();
    int min = stats.getMin() - 1;
    int max = stats.getMax() + 1;

    // Sweep cube from (min,min,min) to (max,max,max), collect everything that is 'outside' lava (not in points)
    Set<Point3D> outside = new HashSet<>();
    Point3D starter = new Point3D("" + min, "" + min, "" + min);
    if (points.contains(starter)) {
      throw new IllegalStateException();
    }
    Set<Point3D> toRemove = new HashSet<>();
    toRemove.add(starter);
    while (!toRemove.isEmpty()) {
      Iterator<Point3D> it = toRemove.iterator();
      Point3D c = it.next();
      it.remove();
      outside.add(c);

      for (int i = 0; i < 3; i++) {
        for (int dp : new int[] {
            -1, 1
        }) {
          Point3D nb = new Point3D(c);
          nb.dims[i] += dp;
          if (!points.contains(nb)) {
            if (nb.dims[0] >= min && nb.dims[0] <= max && //
                nb.dims[1] >= min && nb.dims[1] <= max && //
                nb.dims[2] >= min && nb.dims[2] <= max) {
              if (!outside.contains(nb)) {
                toRemove.add(nb);
              }
            }
          }
        }
      }
    }

    long result = 0;
    for (Point3D p : points) { // check 6 nbs of p
      for (int i = 0; i < 3; i++) {
        for (int dp : new int[] {
            -1, 1
        }) {
          Point3D nb = new Point3D(p);
          nb.dims[i] += dp;
          if (outside.contains(nb)) {
            ++result;
          }
        }
      }
    }

    return result;
  }
}
