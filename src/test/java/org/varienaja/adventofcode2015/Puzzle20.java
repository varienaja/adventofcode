package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {

  private long solveA(int input) {
    int findSum = input / 10;

    int nr = 0;
    boolean found = false;
    while (!found) {
      nr++;
      int sum = 0;
      for (int d = 1; d <= Math.sqrt(nr); d++) {
        if (nr % d == 0) {
          if (nr / d == d) {
            sum += d;
          } else {
            sum += d + (nr / d);
          }
        }
      }
      found = sum >= findSum;
    }

    return nr;
  }

  private long solveB(int input) {
    Map<Integer, Integer> nr2Visits = new HashMap<>();
    int nr = 0;
    boolean found = false;
    while (!found) {
      nr++;
      int sum = 0;
      for (int d = 1; d <= Math.sqrt(nr); d++) {
        if (nr % d == 0) {
          nr2Visits.putIfAbsent(nr, 0);
          nr2Visits.putIfAbsent(nr / d, 0);
          if (nr / d == d) {
            if (nr2Visits.get(d) < 50) {
              sum += 11 * d;
              nr2Visits.compute(d, (k, v) -> v + 1);
            }
          } else {
            if (nr2Visits.get(d) < 50) {
              sum += 11 * d;
              nr2Visits.compute(d, (k, v) -> v + 1);
            }
            if (nr2Visits.get(nr / d) < 50) {
              sum += 11 * (nr / d);
              nr2Visits.compute(nr / d, (k, v) -> v + 1);
            }
          }
        }
      }
      found = sum >= input;
    }

    return nr;
  }

  @Test
  public void testDay00() {
    int testInput = 150;
    assertEquals(8, solveA(testInput));

    announceResultA();
    int input = 36000000;
    long sum = solveA(input);
    System.out.println(sum);
    assertEquals(831600, sum);

    announceResultB();
    sum = solveB(input);
    System.out.println(sum);
    assertEquals(884520, sum);
  }

}
