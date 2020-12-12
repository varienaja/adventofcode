package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  private int[] rotateL(int x, int y, int degrees) {
    for (int deg = 0; deg < degrees; deg += 90) {
      int t = -x;
      x = y;
      y = t;
    }
    return new int[] {
        x, y
    };
  }

  private int[] rotateR(int x, int y, int degrees) {
    return rotateL(x, y, 360 - degrees);
  }

  private long solveA(List<String> lines) {
    // boat direction
    int dx = 1;
    int dy = 0;
    // boat position
    int x = 0;
    int y = 0;

    for (String line : lines) {
      char D = line.charAt(0);
      int dist = Integer.parseInt(line.substring(1));

      switch (D) {
        case 'N':
          y -= dist;
          break;
        case 'S':
          y += dist;
          break;
        case 'E':
          x += dist;
          break;
        case 'W':
          x -= dist;
          break;
        case 'L':
          int[] rotatedL = rotateL(dx, dy, dist);
          dx = rotatedL[0];
          dy = rotatedL[1];
          break;
        case 'R':
          int[] rotatedR = rotateR(dx, dy, dist);
          dx = rotatedR[0];
          dy = rotatedR[1];
          break;
        case 'F':
          x += dx * dist;
          y += dy * dist;
          break;
      }
    }

    return Math.abs(x) + Math.abs(y);
  }

  private long solveB(List<String> lines) {
    // waypoint position (relative to boat)
    int wx = 10;
    int wy = -1;
    // boat position
    int x = 0;
    int y = 0;

    for (String line : lines) {
      char D = line.charAt(0);
      int dist = Integer.parseInt(line.substring(1));

      switch (D) {
        case 'N':
          wy -= dist;
          break;
        case 'S':
          wy += dist;
          break;
        case 'E':
          wx += dist;
          break;
        case 'W':
          wx -= dist;
          break;
        case 'L':
          int[] rotatedL = rotateL(wx, wy, dist);
          wx = rotatedL[0];
          wy = rotatedL[1];
          break;
        case 'R':
          int[] rotatedR = rotateR(wx, wy, dist);
          wx = rotatedR[0];
          wy = rotatedR[1];
          break;
        case 'F':
          x += wx * dist;
          y += wy * dist;
          break;
      }
    }

    return Math.abs(x) + Math.abs(y);
  }

  @Test
  public void testDay12() {
    List<String> input = Arrays.asList( //
        "F10", //
        "N3", //
        "F7", //
        "R90", //
        "F11");
    assertEquals(25, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(1294L, result);
    System.out.println(result);

    assertEquals(286L, solveB(input));
    announceResultB();
    result = solveB(lines);
    assertEquals(20592L, result);
    System.out.println(result);
  }

}
