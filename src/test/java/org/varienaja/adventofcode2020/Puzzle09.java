package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  private long solveA(List<String> lines) {
    int result = 0;
    return result;
  }

  private long solveB(List<String> lines) {
    int result = 0;
    return result;
  }

  @Test
  public void testDay09() {
    List<String> input = Arrays.asList( //
    );
    assertEquals(0, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    // assertEquals(0L, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines);
    // assertEquals(0L, result);
    System.out.println(result);
  }

}
