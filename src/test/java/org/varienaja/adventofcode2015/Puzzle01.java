package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  private int solveA(String line) {
    int floor = 0;
    for (char c : line.toCharArray()) {
      switch (c) {
        case '(':
          floor++;
          break;
        case ')':
          floor--;
          break;
        default:
          break;
      }
    }
    return floor;
  }

  private int solveB(String line) {
    int pos = 0;
    int floor = 0;
    for (char c : line.toCharArray()) {
      pos++;
      switch (c) {
        case '(':
          floor++;
          break;
        case ')':
          floor--;
          break;
        default:
          break;
      }
      if (floor == -1) {
        break;
      }
    }
    return pos;
  }

  @Test
  public void testDay01() {
    assertEquals(0, solveA("(())"));
    assertEquals(0, solveA("()()"));

    assertEquals(3, solveA("((("));
    assertEquals(3, solveA("(()(()("));
    assertEquals(3, solveA("))((((("));

    assertEquals(-1, solveA("())"));
    assertEquals(-1, solveA("))("));

    assertEquals(-3, solveA(")))"));
    assertEquals(-3, solveA(")())())"));

    announceResultA();
    List<String> lines = getInput();
    int result = solveA(lines.get(0));
    assertEquals(138, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines.get(0));
    assertEquals(1771, result);
    System.out.println(result);
  }

}
