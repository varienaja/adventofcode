package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(465126289353L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(70597497486371L, result);
  }

  @Test
  public void testA() {
    assertEquals(3749, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(11387, solveB(getStreamingTestInput()));
  }

  private boolean checkEquation(long testValue, String[] operands, int ix, boolean partB) {
    long last = Long.parseLong(operands[ix]);
    if (ix == 0) {
      return testValue == last;
    }

    boolean divOK = testValue % last == 0 && checkEquation(testValue / last, operands, ix - 1, partB);
    boolean minusOK = checkEquation(testValue - last, operands, ix - 1, partB);
    if (partB) {
      String cv = Long.toString(testValue);
      String lastS = operands[ix];
      partB = cv.equals(lastS) || (cv.endsWith(lastS) && //
          checkEquation(testValue / ((long)Math.pow(10, lastS.length())), operands, ix - 1, partB));
    }
    return divOK || minusOK || partB;
  }

  private long checkEquations(Stream<String> lines, boolean partB) {
    return lines //
        .map(line -> line.split(":\s+")) //
        .mapToLong(parts -> {
          long testValue = Long.parseLong(parts[0]);
          String[] operands = parts[1].split("\s+");

          return checkEquation(testValue, operands, operands.length - 1, partB) ? testValue : 0;
        }).sum();
  }

  private long solveA(Stream<String> lines) {
    return checkEquations(lines, false);
  }

  private long solveB(Stream<String> lines) {
    return checkEquations(lines, true);
  }

}
