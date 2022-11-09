package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {
  long start = 20151125;
  long multiplicator = 252533;
  long divider = 33554393;

  private long solveA(int row, int col) {
    long ix = 0;
    long toAdd = 1;
    for (int i = 1; i <= col; i++) {
      ix += toAdd++;
    }

    toAdd--;
    for (int i = 1; i < row; i++) {
      ix += toAdd++;
    }

    for (long l = 1; l < ix; l++) {
      start = start * multiplicator;
      start = start % divider;
    }

    return start;
  }

  @Test
  public void testDay00() {
    assertEquals(27995004, solveA(6, 6));

    announceResultA();
    long sum = solveA(2981, 3075);
    System.out.println(sum);
    assertEquals(24376079, sum);
  }

}
