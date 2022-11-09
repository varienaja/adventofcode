package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {

  private long solveA(long pk1, long pk2) {
    long loopsize2 = 0;
    long start = 1;
    while (start != pk2) {
      start *= 7;
      start = start % 20201227L;
      loopsize2++;
    }

    start = 1;
    for (long l = 0; l < loopsize2; l++) {
      start *= pk1;
      start = start % 20201227L;
    }
    return start;
  }

  @Test
  public void testDay25() {
    assertEquals(14897079L, solveA(5764801L, 17807724L));

    announceResultA();
    long result = solveA(13316116, 13651422);
    assertEquals(12929L, result);
    System.out.println(result);
  }

}
