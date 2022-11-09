package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  private int solveDay6a(String inInput) {
    Pattern pattern = Pattern.compile("\\s+");
    int[] register = pattern.splitAsStream(inInput).map(Integer::parseInt).mapToInt(v -> v).toArray();

    Set<String> seen = new HashSet<>();
    while (seen.add(Arrays.toString(register))) {
      // Find highest block
      int ixMax = 0;
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < register.length; i++) {
        if (register[i] > max) {
          ixMax = i;
          max = register[i];
        }
      }

      // distribute this value over all blocks
      register[ixMax] = 0;
      for (int i = 0; i < max; i++) {
        ixMax = (ixMax + 1) % register.length;
        register[ixMax]++;
      }
    }
    return seen.size();
  }

  private int solveDay6b(String inInput) {
    Pattern pattern = Pattern.compile("\\s+");
    int[] register = pattern.splitAsStream(inInput).map(Integer::parseInt).mapToInt(v -> v).toArray();

    Integer oldPosition = null;
    Map<String, Integer> seen = new HashMap<>();
    while ((oldPosition = seen.put(Arrays.toString(register), seen.size())) == null) {
      // Find highest block
      int ixMax = 0;
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < register.length; i++) {
        if (register[i] > max) {
          ixMax = i;
          max = register[i];
        }
      }

      // distribute this value over all blocks
      register[ixMax] = 0;
      for (int i = 0; i < max; i++) {
        ixMax = (ixMax + 1) % register.length;
        register[ixMax]++;
      }
    }
    return seen.size() - oldPosition;
  }

  @Test
  public void testDay6() {
    String input = "0 2 7 0";
    assertEquals(5, solveDay6a(input));

    System.out.print("Solution 6a: ");
    input = "4  10  4   1   8   4   9   14  5   1   14  15  0   15  3   5";
    System.out.println(solveDay6a(input));

    input = "0 2 7 0";
    assertEquals(4, solveDay6b(input));

    System.out.print("Solution 6b: ");
    input = "4  10  4   1   8   4   9   14  5   1   14  15  0   15  3   5";
    System.out.println(solveDay6b(input));
  }

}
