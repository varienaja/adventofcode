package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {
  private int maxX;
  private int maxY;
  private int[] directions = {
      -1, 0, 1
  };

  private int countAdjacentOccupied(List<String> lines, int x, int y, boolean seeFar) {
    int adjacentOccupied = 0;

    for (int dY : directions) {
      for (int dX : directions) {
        if (dX == 0 && dY == 0) {
          continue;
        }

        int xX = x;
        int yY = y;
        boolean inBounds;

        do {
          xX += dX;
          yY += dY;
          inBounds = 0 <= xX && xX < maxX && 0 <= yY && yY < maxY;
        } while (seeFar && inBounds && lines.get(yY).charAt(xX) == '.');

        if (inBounds && lines.get(yY).charAt(xX) == '#') {
          adjacentOccupied++;
        }
      }
    }
    return adjacentOccupied;
  }

  private long solve(List<String> lines, int maxOccupied, boolean seeFar) {
    maxX = lines.get(0).length();
    maxY = lines.size();

    int occupied = 0;
    boolean changed;
    List<String> newArrangement;
    do {
      changed = false;
      newArrangement = new LinkedList<>(lines);
      for (int y = 0; y < maxY; y++) {
        char[] line = newArrangement.get(y).toCharArray();
        for (int x = 0; x < maxX; x++) {

          int adjacentOccupied = countAdjacentOccupied(lines, x, y, seeFar);
          if (line[x] == 'L' && adjacentOccupied == 0) {
            line[x] = '#';
            occupied++;
            changed = true;
          } else if (line[x] == '#' && adjacentOccupied >= maxOccupied) {
            line[x] = 'L';
            occupied--;
            changed = true;
          }
          newArrangement.set(y, new String(line));
        }
      }

      lines = new LinkedList<>(newArrangement);
    } while (changed);

    return occupied;
  }

  private long solveA(List<String> lines) {
    return solve(lines, 4, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, 5, true);
  }

  @Test
  public void testDay11() {
    List<String> input = Arrays.asList( //
        "L.LL.LL.LL", //
        "LLLLLLL.LL", //
        "L.L.L..L..", //
        "LLLL.LL.LL", //
        "L.LL.LL.LL", //
        "L.LLLLL.LL", //
        "..L.L.....", //
        "LLLLLLLLLL", //
        "L.LLLLLL.L", //
        "L.LLLLL.LL");
    assertEquals(37, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(2418L, result);
    System.out.println(result);

    assertEquals(26L, solveB(input));
    announceResultB();
    result = solveB(lines);
    assertEquals(2144L, result);
    System.out.println(result);
  }

}
