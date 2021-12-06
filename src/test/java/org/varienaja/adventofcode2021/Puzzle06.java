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

    for (int i = 0; i < days; i++) {
      long nw = histogram[0];
      System.arraycopy(histogram, 1, histogram, 0, histogram.length - 1);
      histogram[6] += nw;
      histogram[8] = nw;
    }

    return Arrays.stream(histogram).sum();
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
