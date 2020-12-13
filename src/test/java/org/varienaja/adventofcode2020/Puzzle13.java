package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {

  private long solveA(String lines, int start) {
    String[] busLines = lines.split(",");

    int diff = 0;
    while (true) {
      for (int c = 0; c < busLines.length; c++) {
        if (!"x".contentEquals(busLines[c])) {
          int bl = Integer.parseInt(busLines[c]);
          if ((start + diff) % bl == 0) {
            return diff * bl;
          }
        }
      }

      diff++;
    }
  }

  private long solveB(String input, long start) {
    String[] busLines = input.split(",");

    long result = start;
    long step = 1;

    for (int timeDiff = 0; timeDiff < busLines.length; timeDiff++) {
      if (!"x".equals(busLines[timeDiff])) {
        int bl = Integer.parseInt(busLines[timeDiff]);

        while ((result + timeDiff) % bl != 0) {
          result += step;
        }
        step *= bl;
      }
    }

    return result;
  }

  @Test
  public void testDay12() {
    String input = "7,13,x,x,59,x,31,19";
    assertEquals(295, solveA(input, 939));

    announceResultA();
    String lines = "29,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,631,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,13,19,x,x,x,23,x,x,x,x,x,x,x,383,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,17";
    long result = solveA(lines, 1000507);
    System.out.println(result);
    assertEquals(370L, result);

    assertEquals(1068781L, solveB(input, 1L));
    assertEquals(754018L, solveB("67,7,59,61", 1L));
    assertEquals(779210L, solveB("67,x,7,59,61", 1L));
    assertEquals(1261476L, solveB("67,7,x,59,61", 1L));
    assertEquals(1202161486L, solveB("1789,37,47,1889", 1L));
    announceResultB();
    result = solveB(lines, 100_000_000_000_000L);
    System.out.println(result);
    assertEquals(894954360381385L, result);
  }

}
