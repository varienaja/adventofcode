package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    assertEquals(494L, result);
    System.out.println(result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    assertEquals(833L, result);
    System.out.println(result);
  }

  private List<String> getTestInput() {
    return List.of( //
        "2-4,6-8", //
        "2-3,4-5", //
        "5-7,7-9", //
        "2-8,3-7", //
        "6-6,4-6", //
        "2-6,4-8");
  }

  private long solveA(List<String> lines) {
    long result = 0L;

    for (String line : lines) {
      String[] parts = line.split("[,-]");
      int start1 = Integer.parseInt(parts[0]);
      int end1 = Integer.parseInt(parts[1]);
      int start2 = Integer.parseInt(parts[2]);
      int end2 = Integer.parseInt(parts[3]);

      if (start1 >= start2 && end1 <= end2 || //
          (start2 >= start1) && end2 <= end1) {
        result++;
      }
    }

    return result;
  }

  private long solveB(List<String> lines) {
    long result = 0L;

    for (String line : lines) {
      String[] parts = line.split("[,-]");
      int start1 = Integer.parseInt(parts[0]);
      int end1 = Integer.parseInt(parts[1]);
      int start2 = Integer.parseInt(parts[2]);
      int end2 = Integer.parseInt(parts[3]);

      if (start1 <= end2 && start2 <= end1) {
        result++;
      }
    }

    return result;
  }

  @Test
  public void testA() {
    assertEquals(2L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(4, solveB(getTestInput()));
  }

}
