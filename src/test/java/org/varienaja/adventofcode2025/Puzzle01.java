package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1018L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(5815L, result);
  }

  @Test
  public void testA() {
    assertEquals(3, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(6, solveB(getTestInput()));
  }

  private long solve(List<String> lines, boolean partB) {
    int at = 50;
    long zeroes = 0;
    for (String line : lines) {
      int direction = line.startsWith("L") ? -1 : 1;
      int amount = Integer.parseInt(line.substring(1));

      if (partB) {
        zeroes += amount / 100;
      }
      amount = amount % 100; // 0 <= amount < 100

      int startAt = at;
      at += amount * direction;
      if (at < 0) {
        at += 100;
        if (partB && startAt != 0) { // Don't recount already covered-for zeroes
          zeroes++;
        }
      } else if (at >= 100) {
        at -= 100;
        if (partB && at != 0) { // Don't count a zero here, it is covered for below
          zeroes++;
        }
      }
      if (at == 0) {
        zeroes++;
      }
    }

    return zeroes;
  }

  private long solveA(List<String> lines) {
    return solve(lines, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, true);
  }

}
