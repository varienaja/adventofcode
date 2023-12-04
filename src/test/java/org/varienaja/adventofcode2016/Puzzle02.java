package org.varienaja.adventofcode2016;

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
public class Puzzle02 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    String result = solveA(getInput());
    System.out.println(result);
    assertEquals("56983", result);
  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getInput());
    System.out.println(result);
    assertEquals("8B8B1", result);
  }

  @Test
  public void testA() {
    assertEquals("1985", solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals("5DB3", solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "ULL", //
        "RRDDD", //
        "LURDL", //
        "UUUUD");
  }

  private String solve(List<String> lines, boolean partB) {
    String[] pad = partB ? new String[] {
        "       ", //
        "   1   ", //
        "  234  ", //
        " 56789 ", //
        "  ABC  ", //
        "   D   ", //
        "       "
    } : new String[] {
        "     ", //
        " 123 ", //
        " 456 ", //
        " 789 ", //
        "     "
    };

    // We point to the 5 initially
    int x = 0;
    int y = 0;
    for (int cY = 0; cY < pad.length; cY++) {
      int cX = pad[cY].indexOf('5');
      if (cX >= 0) {
        x = cX;
        y = cY;
        break;
      }
    }

    StringBuilder result = new StringBuilder();
    for (String step : lines) {
      for (char c : step.toCharArray()) {
        int newX = x;
        int newY = y;
        switch (c) {
          case 'U':
            newY -= 1;
            break;
          case 'D':
            newY += 1;
            break;
          case 'R':
            newX += 1;
            break;
          case 'L':
            newX -= 1;
            break;
        }
        if (' ' != pad[newY].charAt(newX)) {
          x = newX;
          y = newY;
        }
      }
      result.append(pad[y].charAt(x));
    }

    return result.toString();
  }

  private String solveA(List<String> lines) {
    return solve(lines, false);
  }

  private String solveB(List<String> lines) {
    return solve(lines, true);
  }

}
