package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput(), 1);
    System.out.println(result);
    assertEquals(10490062L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveA(getInput(), 999999);
    System.out.println(result);
    assertEquals(382979724122L, result);
  }

  @Test
  public void testA() {
    assertEquals(374, solveA(getTestInput(), 1));
  }

  @Test
  public void testB() {
    assertEquals(1030, solveA(getTestInput(), 9));
    assertEquals(8410, solveA(getTestInput(), 99));
  }

  private long solveA(List<String> lines, long increase) {
    Set<Integer> rowsNoGalaxy = new HashSet<>();
    for (int y = 0; y < lines.size(); ++y) {
      if (!lines.get(y).contains("#")) {
        rowsNoGalaxy.add(y);
      }
    }

    Set<Integer> colsNoGalaxy = new HashSet<>();
    for (int x = 0; x < lines.get(0).length(); ++x) {
      StringBuilder sb = new StringBuilder();
      for (String line : lines) {
        sb.append(line.charAt(x));
      }
      if (!sb.toString().contains("#")) {
        colsNoGalaxy.add(x);
      }
    }

    List<Point> galaxies = new ArrayList<>();
    int dy = 0;
    for (int y = 0; y < lines.size(); ++y) {
      int dx = 0;
      dy += rowsNoGalaxy.contains(y) ? increase : 0;

      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        dx += colsNoGalaxy.contains(x) ? increase : 0;

        if (line.charAt(x) != '.') {
          galaxies.add(new Point(x + dx, y + dy));
        }
      }
    }

    long sum = 0L;
    for (int p1 = 0; p1 < galaxies.size(); ++p1) {
      for (int p2 = p1; p2 < galaxies.size(); ++p2) {
        sum += galaxies.get(p1).manhattanDistance(galaxies.get(p2));
      }
    }
    return sum;
  }

}
