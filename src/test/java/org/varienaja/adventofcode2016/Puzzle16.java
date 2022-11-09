package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {
  private String input = "10010000000110000";

  private String checksum(String a) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < a.length(); i += 2) {
      sb.append(a.charAt(i) == a.charAt(i + 1) ? '1' : '0');
    }

    String result = sb.toString();
    if (result.length() % 2 == 0) {
      result = checksum(result);
    }
    return result;
  }

  private String doStep(String a) {
    StringBuilder sb = new StringBuilder();
    for (int i = a.length() - 1; i >= 0; i--) {
      sb.append(a.charAt(i) == '1' ? '0' : '1');
    }
    String b = sb.toString();
    return a + "0" + b;
  }

  private String solve(String input, int length) {
    String a = input;
    while (a.length() < length) {
      a = doStep(a);
    }
    a = a.substring(0, length);

    String checksum = checksum(a);
    return checksum;
  }

  private String solveA(String input) {
    return solve(input, 272);
  }

  private String solveB(String input) {
    return solve(input, 35651584);
  }

  @Test
  public void testDay16() {
    assertEquals("100", doStep("1"));
    assertEquals("001", doStep("0"));
    assertEquals("11111000000", doStep("11111"));
    assertEquals("1111000010100101011110000", doStep("111100001010"));
    assertEquals("100", checksum("110010110100"));
    assertEquals("01100", solve("10000", 20));

    announceResultA();
    String answer = solveA(input);
    System.out.println(answer);
    assertEquals("10010110010011110", answer);

    announceResultB();
    answer = solveB(input);
    System.out.println(answer);
    assertEquals("01101011101100011", answer);
  }

}
