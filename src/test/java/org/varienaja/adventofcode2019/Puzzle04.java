package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA();
    System.out.println(sum);
    assertEquals(1955, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB();
    System.out.println(sum);
    assertEquals(1319, sum);
  }

  private boolean isValid(int pwd, boolean partB) {
    char[] cs = Integer.toString(pwd).toCharArray();

    // digits never decrease
    boolean increase = true;
    for (int i = 1; i < cs.length; i++) {
      if (cs[i] < cs[i - 1]) {
        increase = false;
      }
    }

    // two adjacent digits are the same
    boolean foundTwo = false;
    Map<Character, Integer> twos = new HashMap<>();
    for (int i = 1; i < cs.length; i++) {
      if (cs[i - 1] == cs[i]) {
        int l = 1;
        while (i + l - 1 < cs.length && cs[i - 1] == cs[i + l - 1]) {
          l++;
        }

        twos.put(cs[i], l);
        i += l - 1;
        foundTwo = true;
      }
    }

    if (partB) {
      // the two adjacent matching digits are not part of a larger group of matching digits
      foundTwo = twos.containsValue(2);
    }

    return increase && foundTwo;
  }

  private long solve(int min, int max, boolean partB) {
    long matchCount = 0;

    for (int pwd = min; pwd <= max; pwd++) {
      if (isValid(pwd, partB)) {
        matchCount++;
      }
    }

    return matchCount;
  }

  private long solveA() {
    return solve(134792, 675810, false);
  }

  private long solveB() {
    return solve(134792, 675810, true);
  }

  @Test
  public void testB() {
    assertTrue(isValid(112233, true));
    assertFalse(isValid(123444, true));
    assertTrue(isValid(111122, true));
  }

}
