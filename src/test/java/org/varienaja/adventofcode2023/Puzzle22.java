package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.Point3D;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {

  record Brick(Point3D p1, Point3D p2) {
    int minX() {
      return Math.min(p1.x, p2.x);
    }

    int maxX() {
      return Math.max(p1.x, p2.x);
    }

    int minY() {
      return Math.min(p1.y, p2.y);
    }

    int maxY() {
      return Math.max(p1.y, p2.y);
    }

    int minZ() {
      return Math.min(p1.z, p2.z);
    }

    int maxZ() {
      return Math.max(p1.z, p2.z);
    }

    boolean isOver(Point p) {
      return p.x >= minX() && p.x <= maxX() && p.y >= minY() && p.y <= maxY();
    }

    void lowerBy(int distance) {
      p1.z -= distance;
      p2.z -= distance;
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(416L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(60963L, result);
  }

  @Test
  public void testA() {
    assertEquals(5, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(7, solveB(getTestInput()));
  }

  /**
   * Lowers the given Brick b until it touches the given bottom.
   *
   * @param bottom2Height the bottom heights
   * @param b the Brick
   * @return the amount of distance that the Brick was lowered
   */
  int lower(Map<Point, Integer> bottom2Height, Brick b) {
    int minDist = bottom2Height.entrySet().stream() //
        .filter(e -> b.isOver(e.getKey())) //
        .mapToInt(e -> b.minZ() - 1 - e.getValue()) //
        .min().orElseThrow();

    b.lowerBy(minDist);

    bottom2Height.entrySet().stream() //
        .filter(e -> b.isOver(e.getKey())) //
        .forEach(e -> e.setValue(b.maxZ()));

    return minDist;
  }

  private long solve(List<String> lines, boolean partB) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;

    // Parse
    List<Brick> bricks = new LinkedList<>();
    for (String line : lines) {
      String[] parts = line.split("~");
      String[] end1 = parts[0].split(",");
      String[] end2 = parts[1].split(",");

      Point3D e1 = new Point3D(end1[0], end1[1], end1[2]);
      Point3D e2 = new Point3D(end2[0], end2[1], end2[2]);
      Brick b = new Brick(e1, e2);
      bricks.add(b);

      minX = Math.min(minX, Math.min(b.p1.x, b.p2.x));
      minY = Math.min(minY, Math.min(b.p1.y, b.p2.y));
      maxX = Math.max(maxX, Math.max(b.p1.x, b.p2.x));
      maxY = Math.max(maxY, Math.max(b.p1.y, b.p2.y));
    }

    // Init bottom
    Map<Point, Integer> bottom2Height = new HashMap<>();
    for (int x = minX; x <= maxX; x++) {
      for (int y = minX; y <= maxY; y++) {
        bottom2Height.put(new Point(x, y), 0);
      }
    }

    // Settle
    Collections.sort(bricks, (b1, b2) -> Integer.compare(b1.minZ(), b2.minZ()));
    bricks.forEach(b -> lower(bottom2Height, b));

    // Test which Bricks can be disintegrated
    int disintegrateOKCount = 0;
    int fallenBricksSum = 0;
    for (int i = 0; i < bricks.size(); ++i) {
      bottom2Height.entrySet().forEach(e -> e.setValue(0)); // Reset bottom heights

      List<Brick> tester = new LinkedList<>();
      for (Brick b : bricks) {
        tester.add(new Brick(new Point3D(b.p1.x, b.p1.y, b.p1.z), new Point3D(b.p2.x, b.p2.y, b.p2.z)));
      }
      tester.remove(i); // Copy settled Bricks, remove one

      long moveCount = tester.stream() //
          .mapToInt(b -> lower(bottom2Height, b)) //
          .filter(d -> d > 0).count();

      disintegrateOKCount += moveCount == 0 ? 1 : 0; // If not a single Brick moved: OK to disintegrate
      fallenBricksSum += moveCount;
    }

    return partB ? fallenBricksSum : disintegrateOKCount;
  }

  private long solveA(List<String> lines) {
    return solve(lines, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, true);
  }

}
