package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  private long solve(String input, int days) {
    long[] histogram = new long[9];
    Arrays.stream(input.split(",")).map(s -> Integer.parseInt(s)).forEach(i -> histogram[i]++);

    return solveInternal(histogram, days);
  }

  private long solveInternal(long[] histogram, int days) {
    if (days == 0) {
      return Arrays.stream(histogram).sum();
    }

    long nw = histogram[0];
    for (int i = 1; i < histogram.length; i++) {
      histogram[i - 1] = histogram[i];
    }
    histogram[6] += nw;
    histogram[8] = nw;
    return solveInternal(histogram, days - 1);
  }

  @Test
  public void testDay01() {
    String testInput = "3,4,3,1,2";
    assertEquals(26, solve(testInput, 18));
    assertEquals(5934, solve(testInput, 80));

    announceResultA();
    String lines = getInput().get(0);
    long result = solve(lines, 80);
    System.out.println(result);
    assertEquals(374927, result);

    assertEquals(26984457539L, solve(testInput, 256));
    announceResultB();
    result = solve(lines, 256);
    System.out.println(result);
    assertEquals(1687617803407L, result);
  }

}
