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
public class Puzzle02 extends PuzzleAbs {

  private long solveA(List<String> lines) {
    long sum = 0;
    for (String dimension : lines) {
      String[] parts = dimension.split("x");
      assert parts.length == 3;

      int[] nums = new int[parts.length]; // l,w,h
      for (int i = 0; i < parts.length; i++) {
        nums[i] = Integer.parseInt(parts[i]);
      }

      int[] sides = new int[parts.length];
      sides[0] = nums[0] * nums[1];
      sides[1] = nums[1] * nums[2];
      sides[2] = nums[2] * nums[0];

      int min = Math.min(Math.min(sides[0], sides[1]), sides[2]);

      sum += 2 * sides[0] + 2 * sides[1] + 2 * sides[2] + min;
    }
    return sum;
  }

  private long solveB(List<String> lines) {
    long sum = 0;
    for (String dimension : lines) {

      String[] parts = dimension.split("x");
      assert parts.length == 3;

      int[] nums = new int[parts.length]; // l,w,h
      for (int i = 0; i < parts.length; i++) {
        nums[i] = Integer.parseInt(parts[i]);
      }

      int max = Math.max(Math.max(nums[0], nums[1]), nums[2]);

      sum += 2 * nums[0] + 2 * nums[1] + 2 * nums[2] - 2 * max + nums[0] * nums[1] * nums[2];
    }
    return sum;
  }

  @Test
  public void testDay02() {
    List<String> testInput = List.of("2x3x4");
    assertEquals(58, solveA(testInput));
    assertEquals(43, solveA(List.of("1x1x10")));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(1606483, result);
    System.out.println(result);

    assertEquals(34, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    assertEquals(3842356, result);
    System.out.println(result);
  }

}
