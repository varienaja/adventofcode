package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {
  private Map<String, Integer> registers;
  private int maxEver;

  private long solveA(List<String> input) {
    maxEver = Integer.MIN_VALUE;
    registers = new HashMap<>();

    for (String line : input) {
      String[] parts = line.split("\\s");
      int val = registers.getOrDefault(parts[4], 0);
      int compare = Integer.parseInt(parts[6]);
      boolean doIt = false;
      if (">".equals(parts[5])) {
        doIt = val > compare;
      } else if ("<".equals(parts[5])) {
        doIt = val < compare;
      } else if (">=".equals(parts[5])) {
        doIt = val >= compare;
      } else if ("==".equals(parts[5])) {
        doIt = val == compare;
      } else if ("<=".equals(parts[5])) {
        doIt = val <= compare;
      } else if ("!=".equals(parts[5])) {
        doIt = val != compare;
      }

      if (doIt) {
        int incdec = Integer.parseInt(parts[2]);
        int by = "dec".equals(parts[1]) ? incdec : -incdec;
        registers.compute(parts[0], (k, v) -> v == null ? 0 - by : v - by);
      }

      maxEver = Math.max(registers.values().stream().mapToInt(i -> i).max().orElse(-1), maxEver);
    }

    return registers.values().stream().mapToInt(i -> i).max().orElse(-1);
  }

  private long solveB(List<String> input) {
    solveA(input);
    return maxEver;
  }

  @Test
  public void testDay08() {
    List<String> testInput = Arrays.asList( //
        "b inc 5 if a > 1", //
        "a inc 1 if b < 5", //
        "c dec -10 if a >= 1", //
        "c inc -20 if c == 10");
    assertEquals(1, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(5752L, result);

    assertEquals(10, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(6366L, result);
  }

}
