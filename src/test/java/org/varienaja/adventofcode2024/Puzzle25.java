package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(3619, result);
  }

  @Test
  public void testA() {
    assertEquals(3, solveA(getTestInput()));
  }

  private String convertKeyToPins(List<String> lines) {
    Collections.reverse(lines);
    return convertLockToPins(lines);
  }

  private String convertLockToPins(List<String> lines) {
    StringBuilder sb = new StringBuilder();
    for (int x = 0; x < lines.get(0).length(); ++x) {
      int y = 1;
      while (lines.get(y).charAt(x) == '#') {
        ++y;
      }
      sb.append(Integer.toString(y - 1));
    }
    return sb.toString();
  }

  private boolean fits(String key, String lock) {
    for (int ix = 0; ix < key.length(); ++ix) {
      int kHeight = Integer.parseInt("" + key.charAt(ix));
      int lHeight = Integer.parseInt("" + lock.charAt(ix));
      if (kHeight + lHeight > 5) {
        return false;
      }

    }
    return true;
  }

  private long solveA(List<String> input) {
    List<String> locks = new LinkedList<>();
    List<String> keys = new LinkedList<>();

    List<String> parsed = new LinkedList<>();
    for (String line : input) {
      if (line.isEmpty()) {
        if (parsed.get(0).equals("#####")) { // Lock
          locks.add(convertLockToPins(parsed));
        } else {
          keys.add(convertKeyToPins(parsed));
        }
        parsed.clear();
      } else {
        parsed.add(line);
      }
    }

    long result = 0;
    for (String lock : locks) {
      for (String key : keys) {
        if (fits(lock, key)) {
          ++result;
        }
      }
    }

    return result;
  }

}
