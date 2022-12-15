package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1016L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(25402L, result);
  }

  @Test
  public void testA() {
    assertEquals(24L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(93L, solveB(getTestInput()));
  }

  private List<String> getTestInput() {
    return List.of( //
        "498,4 -> 498,6 -> 496,6", //
        "503,4 -> 502,4 -> 502,9 -> 494,9");
  }

  private long solve(List<String> lines, boolean partA) {
    int maxY = Integer.MIN_VALUE;

    Set<Point> world = new HashSet<>();
    for (String line : lines) {
      String[] parts = line.split(" -> ");
      for (int i = 1; i < parts.length; i++) {
        int x1 = Integer.parseInt(parts[i - 1].split(",")[0]);
        int x2 = Integer.parseInt(parts[i].split(",")[0]);

        int y1 = Integer.parseInt(parts[i - 1].split(",")[1]);
        int y2 = Integer.parseInt(parts[i].split(",")[1]);
        maxY = Math.max(maxY, Math.max(y1, y2));

        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
          for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            world.add(new Point(x, y));
          }
        }
      }
    }

    List<Point> movement = List.of(new Point(0, 1), new Point(-1, 1), new Point(1, 1));
    Point starter = new Point(500, 0);
    long result = 0;

    while (!world.contains(starter)) {
      boolean moving = true;
      Point src = starter;
      result++;
      while (moving) {
        boolean notFits = true;
        Point nextPos = null;
        Iterator<Point> it = movement.iterator();
        while (it.hasNext() && notFits) {
          Point m = it.next();
          nextPos = new Point(src.x + m.x, src.y + m.y);
          notFits = world.contains(nextPos);
        }

        if (notFits) {
          moving = false;
          world.add(src);
        } else {
          src = nextPos;
        }

        if (nextPos.y > maxY) {
          if (partA) {
            return result - 1; // Don't count grain of sand that fell into the void
          }
          world.add(nextPos);
          moving = false;
        }
      }
    }

    return result;
  }

  private long solveA(List<String> lines) {
    return solve(lines, true);
  }

  private long solveB(List<String> lines) {
    return solve(lines, false);
  }

}
