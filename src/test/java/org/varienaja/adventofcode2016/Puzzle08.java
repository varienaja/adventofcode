package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {
  private StringBuilder[] grid;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(110L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    solveB(getInput());
  }

  @Test
  public void testA() {
    StringBuilder[] grid = new StringBuilder[3];
    for (int i = 0; i < grid.length; i++) {
      grid[i] = new StringBuilder(".......");
    }

    transformGrid(grid, "rect 3x2");
    assertEquals("[###...., ###...., .......]", Arrays.toString(grid));
    transformGrid(grid, "rotate column x=1 by 1");
    assertEquals("[#.#...., ###...., .#.....]", Arrays.toString(grid));
    transformGrid(grid, "rotate row y=0 by 4");
    assertEquals("[....#.#, ###...., .#.....]", Arrays.toString(grid));
    transformGrid(grid, "rotate column x=1 by 1");
    assertEquals("[.#..#.#, #.#...., .#.....]", Arrays.toString(grid));
  }

  private long solveA(List<String> lines) {
    grid = new StringBuilder[6];
    for (int i = 0; i < grid.length; i++) {
      grid[i] = new StringBuilder("..................................................");
    }

    for (String operation : lines) {
      transformGrid(grid, operation);
    }
    long counter = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length(); j++) {
        if (grid[i].charAt(j) == '#') {
          counter++;
        }
      }
    }
    return counter;
  }

  private long solveB(List<String> lines) {
    solveA(lines);
    System.out.println();
    for (int i = 0; i < grid.length; i++) {
      System.out.println(grid[i]);
    } // zjhrkcplyj
    return -1;
  }

  private void transformGrid(StringBuilder[] inGrid, String inOperation) {
    if (inOperation.startsWith("rect")) {
      int xPos = inOperation.indexOf('x');
      int w = Integer.parseInt(inOperation.substring(5, xPos));
      int h = Integer.parseInt(inOperation.substring(xPos + 1));
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          inGrid[i].setCharAt(j, '#');
        }
      }
    } else if (inOperation.startsWith("rotate column")) {
      int aPos = inOperation.indexOf("x=");
      int bPos = inOperation.indexOf(" by ");
      int x = Integer.parseInt(inOperation.substring(aPos + 2, bPos));
      int by = Integer.parseInt(inOperation.substring(bPos + 4));

      for (int j = 0; j < by; j++) {
        for (int i = inGrid.length - 1; i > 0; i--) {
          char tmp = inGrid[i].charAt(x);
          inGrid[i].setCharAt(x, inGrid[i - 1].charAt(x));
          inGrid[i - 1].setCharAt(x, tmp);
        }
      }
    } else if (inOperation.startsWith("rotate row")) {
      int aPos = inOperation.indexOf("y=");
      int bPos = inOperation.indexOf(" by ");
      int y = Integer.parseInt(inOperation.substring(aPos + 2, bPos));
      int by = Integer.parseInt(inOperation.substring(bPos + 4));

      for (int j = 0; j < by; j++) {
        for (int i = inGrid[y].length() - 1; i > 0; i--) {
          char tmp = inGrid[y].charAt(i - 1);
          inGrid[y].setCharAt(i - 1, inGrid[y].charAt(i));
          inGrid[y].setCharAt(i, tmp);
        }
      }
    }
  }

}
