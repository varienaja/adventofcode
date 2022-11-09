package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle00 extends PuzzleAbs {

  private long solveA(List<String> input) {
    return -1;
  }

  private long solveB(List<String> input) {
    return -1;
  }

  @Test
  public void testDay00() {
    List<String> testInput = List.of();
    assertEquals(-1, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(-1, sum);

    announceResultB();
    assertEquals(-1, solveB(testInput));
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(-1, sum);
  }

}
