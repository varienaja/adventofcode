package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  private long countSafe(String line) {
    long safeCount = 0;
    for (char c : line.toCharArray()) {
      if (c == '.') {
        safeCount++;
      }
    }
    return safeCount;
  }

  private String generateNextLine(String input) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < input.length(); i++) {
      char left = i == 0 ? '.' : input.charAt(i - 1);
      char middle = input.charAt(i);
      char right = i == input.length() - 1 ? '.' : input.charAt(i + 1);

      String tester = "" + left + middle + right;
      if ("^^.".equals(tester) || ".^^".equals(tester) || "..^".equals(tester) || "^..".equals(tester)) {
        sb.append('^');
      } else {
        sb.append('.');
      }
    }

    return sb.toString();
  }

  private long solveA(String input, int toGenerate) {
    long safeCount = countSafe(input);

    for (int i = 0; i < toGenerate; i++) {
      input = generateNextLine(input);
      safeCount += countSafe(input);
    }

    return safeCount;
  }

  @Test
  public void testDay18() {
    assertEquals(6, solveA("..^^.", 2));
    assertEquals(38, solveA(".^^.^.^^^^", 9));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines.get(0), 39);
    System.out.println(sum);
    assertEquals(1987, sum); // too high: 2047

    announceResultB();
    sum = solveA(lines.get(0), 400000 - 1);
    System.out.println(sum);
    assertEquals(19984714, sum);
  }

}
