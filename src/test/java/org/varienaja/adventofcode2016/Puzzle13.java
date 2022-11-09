package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {
  private int input = 1352;

  int calcNextPoints(int steps, Set<Point> seenPoints, Set<Point> newPoints, Point target, int maxSteps) {
    if (target == null) { // Part B
      if (steps == maxSteps) {
        seenPoints.addAll(newPoints);
        return seenPoints.size();
      }
    } else if (newPoints.contains(target)) {
      return steps;
    }

    Set<Point> newPoints2 = new HashSet<>();
    for (Point p : newPoints) {
      seenPoints.add(p);

      for (Point nb : p.getNSWENeighbours()) {
        if (isOpenSpace(nb.x, nb.y)) {
          if (!seenPoints.contains(nb)) {
            newPoints2.add(nb);
          }
        }
      }
    }

    return calcNextPoints(steps + 1, seenPoints, newPoints2, target, maxSteps);
  }

  boolean isOpenSpace(int x, int y) {
    if (x < 0 || y < 0) {
      return false;
    }
    long xx = x * x + 3 * x + 2 * x * y + y + y * y;
    xx += input;
    return Long.bitCount(xx) % 2 == 0;
  }

  private long solveA(int fx, int fy) {
    return calcNextPoints(0, new HashSet<>(), Collections.singleton(new Point(1, 1)), new Point(fx, fy), 0);
  }

  private long solveB(int steps) {
    return calcNextPoints(0, new HashSet<>(), Collections.singleton(new Point(1, 1)), null, 50);
  }

  @Test
  public void testDay13() {
    input = 10;
    assertEquals(11, solveA(7, 4));

    input = 1352;
    announceResultA();
    long sum = solveA(31, 39);
    System.out.println(sum);
    assertEquals(90, sum);

    announceResultB();
    sum = solveB(50);
    System.out.println(sum);
    assertEquals(135, sum);
  }

}
