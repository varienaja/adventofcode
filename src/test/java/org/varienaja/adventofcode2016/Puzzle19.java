package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {
  private int input = 3005290;

  private long solveA(int elfCount) {
    int[] elves = new int[elfCount];
    Arrays.fill(elves, 1);

    int elvesWithPresentsCount = elfCount;
    int ix = -1;
    while (elvesWithPresentsCount != 1) {

      do {
        ix++;
        if (ix >= elfCount) {
          ix = 0;
        }
      } while (elves[ix] == 0);

      int ixElfWithPresent = ix;
      do {
        ixElfWithPresent++;
        if (ixElfWithPresent >= elfCount) {
          ixElfWithPresent = 0;
        }
      } while (elves[ixElfWithPresent] == 0);

      elves[ix] += elves[ixElfWithPresent];
      elves[ixElfWithPresent] = 0;
      elvesWithPresentsCount--;
      if (elvesWithPresentsCount == 1) {
        return ix + 1;
      }

      ix = ixElfWithPresent;
    }

    return -1;
  }

  private long solveB(int initialElfCount) {
    int[] elves = new int[initialElfCount];
    Arrays.fill(elves, 1);

    int elvesWithPresentsCount = initialElfCount;
    int ix = -1;
    while (elvesWithPresentsCount != 1) {

      do {
        ix++;
        if (ix >= initialElfCount) {
          ix = 0;
        }
      } while (elves[ix] == 0);

      int ixElfToStealFrom = ix;
      int toSkip = elvesWithPresentsCount / 2;
      do {
        ixElfToStealFrom++;
        if (ixElfToStealFrom >= initialElfCount) {
          ixElfToStealFrom = 0;
        }
        if (elves[ixElfToStealFrom] > 0) {
          toSkip--;
        }
      } while (toSkip > 0);

      elves[ix] += elves[ixElfToStealFrom];
      elves[ixElfToStealFrom] = 0;
      elvesWithPresentsCount--;
      if (elvesWithPresentsCount % 1000 == 0) {
        System.out.println(elvesWithPresentsCount);
      }
      if (elvesWithPresentsCount == 1) {
        return ix + 1;
      }
    }

    return -1;
  }

  int solveBAlt(int initialElfCount) {
    int result = 1;

    for (int i = 1; i < initialElfCount; i++) {
      result = result % i + 1;
      if (result > (i + 1) / 2) {
        result++;
      }
    }
    return result;

  }

  @Test
  public void testDay19() {
    assertEquals(3, solveA(5));

    announceResultA();
    long elf = solveA(input);
    System.out.println(elf);
    assertEquals(1816277, elf);

    // Whoaa.. there's a pattern!
    for (int i = 2; i < 100; i++) {
      assertEquals(solveB(i), solveBAlt(i));
    }

    announceResultB();
    assertEquals(2, solveB(5));
    elf = solveBAlt(input);
    System.out.println(elf);
    assertEquals(1410967, elf);
  }

}
