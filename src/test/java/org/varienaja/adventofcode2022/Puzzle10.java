package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(13440L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    solveB(getInput()); // PBZGRAZA
  }

  @Test
  @Ignore
  public void testA() {
    assertEquals(0, solveA(getTestInput()));
  }

  @Test
  @Ignore
  public void testB() {
    assertEquals(-1, solveB(getTestInput()));
  }

  private List<String> getTestInput() {
    return List.of();
  }

  private long solveA(List<String> lines) {
    Map<Long, Long> cycle2x = new HashMap<>();
    long cycle = 1;
    long x = 1;

    for (String line : lines) {
      if ("noop".equals(line)) {
        cycle2x.put(cycle, x);
        ++cycle;
        cycle2x.put(cycle, x);
      } else if (line.startsWith("addx")) {
        long val = Long.parseLong(line.substring(line.indexOf(' ') + 1));
        ++cycle;
        cycle2x.put(cycle, x);

        x += val;
        ++cycle;
        cycle2x.put(cycle, x);
      }
    }

    long[] ixs = new long[] {
        20, 60, 100, 140, 180, 220
    };
    long result = 0;
    for (long ix : ixs) {
      result += cycle2x.get(ix) * ix;
    }

    return result;
  }

  private long solveB(List<String> lines) {
    Map<Long, Long> cycle2x = new HashMap<>();
    long cycle = 1;
    long x = 1;

    for (String line : lines) {
      if ("noop".equals(line)) {
        cycle2x.put(cycle, x);
        ++cycle;
        cycle2x.put(cycle, x);
      } else if (line.startsWith("addx")) {
        long val = Long.parseLong(line.substring(line.indexOf(' ') + 1));
        ++cycle;
        cycle2x.put(cycle, x);

        x += val;
        ++cycle;
        cycle2x.put(cycle, x);
      }
    }

    System.out.println();

    for (int r = 0; r < 6; r++) {
      for (int c = 1; c <= 40; c++) {
        long spritePos = cycle2x.get(r * 40L + c);
        boolean visible = c >= spritePos && c <= spritePos + 2;
        if (visible) {
          System.out.print("#");
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }

    return -1;
  }

}
