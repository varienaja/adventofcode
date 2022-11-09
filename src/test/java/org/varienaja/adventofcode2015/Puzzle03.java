package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  private long solveA(String lines) {
    Map<Point, Integer> v = new HashMap<>();
    v.put(new Point(0, 0), 1);
    int cX = 0;
    int cY = 0;
    for (char c : lines.toCharArray()) {
      switch (c) {
        case '^':
          cY--;
          break;
        case 'v':
          cY++;
          break;
        case '<':
          cX--;
          break;
        case '>':
          cX++;
          break;
        default:
          break;
      }
      Point p = new Point(cX, cY);
      Integer visits = v.get(p);
      v.put(p, visits == null ? 1 : visits + 1);
    }

    return v.size();
  }

  private long solveB(String line) {
    Map<Point, Integer> v = new HashMap<>();
    v.put(new Point(0, 0), 1);
    int cXSanta = 0;
    int cYSanta = 0;
    int cXBot = 0;
    int cYBot = 0;

    boolean isSantasTurn = true;
    for (char c : line.toCharArray()) {
      switch (c) {
        case '^':
          if (isSantasTurn) {
            cYSanta--;
          } else {
            cYBot--;
          }
          break;
        case 'v':
          if (isSantasTurn) {
            cYSanta++;
          } else {
            cYBot++;
          }
          break;
        case '<':
          if (isSantasTurn) {
            cXSanta--;
          } else {
            cXBot--;
          }
          break;
        case '>':
          if (isSantasTurn) {
            cXSanta++;
          } else {
            cXBot++;
          }
          break;
        default:
          break;
      }
      Point p = isSantasTurn ? new Point(cXSanta, cYSanta) : new Point(cXBot, cYBot);
      Integer visits = v.get(p);
      v.put(p, visits == null ? 1 : visits + 1);
      isSantasTurn = !isSantasTurn;
    }

    return v.size();
  }

  @Test
  public void testDay03() {
    assertEquals(2, solveA(">"));
    assertEquals(4, solveA("^>v<"));
    assertEquals(2, solveA("^v^v^v^v^v"));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines.get(0));
    assertEquals(2572, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines.get(0));
    assertEquals(2631, result);
    System.out.println(result);
  }

}
