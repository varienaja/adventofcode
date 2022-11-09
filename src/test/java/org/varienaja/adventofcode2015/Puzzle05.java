package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  private long solveA(List<String> lines) {
    return lines.stream().filter(this::solveA).count();
  }

  private boolean solveA(String input) {
    // may not contain ab, cd, pq, xy
    if (input.contains("ab")) {
      return false;
    }
    if (input.contains("cd")) {
      return false;
    }
    if (input.contains("pq")) {
      return false;
    }
    if (input.contains("xy")) {
      return false;
    }

    // must contains at least one letter twice in a row
    boolean twiceInARow = false;
    for (char c : input.toCharArray()) {
      if (input.contains("" + c + c)) {
        twiceInARow = true;
        break;
      }
    }

    if (twiceInARow) {
      // must contain 3 vowels
      int vowelCount = 0;
      for (char c : input.toCharArray()) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
          vowelCount++;
        }
      }
      return vowelCount >= 3;
    }

    return false;
  }

  private long solveB(List<String> lines) {
    return lines.stream().filter(this::solveB).count();
  }

  private boolean solveB(String input) {
    char[] c = input.toCharArray();

    boolean pairExistsTwice = false;
    for (int i = 1; i < input.length(); i++) {
      String pair = "" + c[i - 1] + c[i];
      if (input.substring(i + 1).contains(pair)) {
        pairExistsTwice = true;
        break;
      }
    }

    if (pairExistsTwice) {
      // must contain xYx
      for (int i = 2; i < input.length(); i++) {
        if (c[i - 2] == c[i]) {
          return true;
        }
      }

    }

    return false;
  }

  @Test
  public void testDay05() {
    assertTrue(solveA("ugknbfddgicrmopn"));
    assertTrue(solveA("aaa"));
    assertFalse(solveA("jchzalrnumimnmhp"));
    assertFalse(solveA("haegwjzuvuyypxyu"));
    assertFalse(solveA("dvszwmarrgswjxmb"));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(236, result);
    System.out.println(result);

    assertTrue(solveB("qjhvhtzxzqqjkmpb"));
    assertTrue(solveB("xxyxx"));
    assertFalse(solveB("uurcxstgmygtbstg"));
    assertFalse(solveB("ieodomkazucvgmuy"));

    announceResultB();
    result = solveB(lines);
    assertEquals(51, result);
    System.out.println(result);
  }

}
