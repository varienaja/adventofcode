package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    System.out.println(result);
    assertEquals(353L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    System.out.println(result);
    assertEquals(152L, result);
  }

  @Test
  public void testA() {
    assertEquals(12L, solveA("R5, L5, R5, R3"));
  }

  @Test
  public void testB() {
    assertEquals(4L, solveB("R8, R4, R4, R8"));
  }

  private long solve(String input, boolean partB) {
    int x = 0;
    int y = 0;
    int direction = 0; // 0,1,2,3: north, west, south, east
    Set<Point> visited = new LinkedHashSet<>();
    visited.add(new Point(0, 0));

    String[] steps = input.split(",\\s");
    for (String step : steps) {
      int a = Integer.parseInt(step.substring(1));
      if (step.startsWith("R")) {
        direction--;
      } else if (step.startsWith("L")) {
        direction++;
      }

      if (direction < 0) {
        direction = 3;
      }
      if (direction > 3) {
        direction = 0;
      }

      while (a > 0) {
        switch (direction) {
          case 0:
            x -= 1;
            break;
          case 1:
            y -= 1;
            break;
          case 2:
            x += 1;
            break;
          case 3:
            y += 1;
            break;
        }
        a--;
        Point p = new Point(x, y);
        if (partB && !visited.add(p)) {
          return Math.abs(x) + Math.abs(y);
        }
      }
    }
    return Math.abs(x) + Math.abs(y);
  }

  private long solveA(String input) {
    return solve(input, false);
  }

  private long solveB(String input) {
    return solve(input, true);
  }

}
