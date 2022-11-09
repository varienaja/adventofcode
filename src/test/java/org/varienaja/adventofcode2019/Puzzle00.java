package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle00 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(-1, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(-1, sum);
  }

  private long solveA(List<String> input) {
    return -1;
  }

  private long solveB(List<String> input) {
    return -1;
  }

  @Test
  public void testA() {
    assertEquals(-1, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(-1, solveB(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
    );
  }

}
