package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  private int putCell(Map<Point, Integer> inGrid, Point inPoint) {
    int sum = 0;
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        Point neighbour = new Point(inPoint.x + x, inPoint.y + y);
        Integer value = inGrid.get(neighbour);
        if (value != null) {
          sum += value;
        }
      }
    }

    if (inPoint.x == 0 && inPoint.y == 0) {
      sum = 1;
    }
    inGrid.put(inPoint, sum);
    return sum;
  }

  private int solveDay3a(int inInput) {
    Map<Integer, Point> grid = new HashMap<>();

    int number = 1;
    int x = 0;
    int y = 0;
    int ribLength = 1;
    while (number <= inInput) {
      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        x++;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        y--;
      }
      ribLength++;

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        x--;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        y++;
      }
      ribLength++;
    }

    Point p = grid.get(inInput);
    return Math.abs(p.x) + Math.abs(p.y);
  }

  private int solveDay3b(int inInput) {
    Map<Point, Integer> grid = new HashMap<>();

    int x = 0;
    int y = 0;
    int ribLength = 1;
    while (true) {
      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        x++;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        y--;
      }
      ribLength++;

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        x--;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        y++;
      }
      ribLength++;
    }
  }

  @Test
  public void testDay3() {
    assertEquals(0, solveDay3a(1));
    assertEquals(3, solveDay3a(12));
    assertEquals(2, solveDay3a(23));
    assertEquals(31, solveDay3a(1024));

    System.out.print("Solution 3a: ");
    System.out.println(solveDay3a(347991));

    System.out.print("Solution 3b: ");
    System.out.println(solveDay3b(347991));
  }

}
