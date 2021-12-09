package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {
  private int[] directions = new int[] {
      -1, 0, 1
  };
  private int maxX;
  private int maxY;

  private List<Point> findLows(List<String> lines) {
    List<Point> lows = new LinkedList<>();

    for (int x = 0; x < maxX; x++) {
      for (int y = 0; y < maxY; y++) {
        Point p = new Point(x, y);
        int h = getDepth(lines, p);

        boolean isLowPoint = true;
        for (int a : directions) {
          for (int b : directions) {
            int xx = x + a;
            int yy = y + b;
            if (xx >= 0 && xx < maxX && yy >= 0 && yy < maxY) {
              Point pp = new Point(xx, yy);
              if (p.nextTo(pp)) {
                if (getDepth(lines, pp) <= h) {
                  isLowPoint = false;
                }
              }
            }
          }
        }

        if (isLowPoint) {
          lows.add(p);
        }
      }
    }
    return lows;
  }

  private int getDepth(List<String> lines, Point p) {
    return Integer.parseInt("" + lines.get(p.y).charAt(p.x));
  }

  private int growBasin(Set<Point> basin, List<String> lines) {
    Set<Point> toAdd = new HashSet<>();
    for (Point p : basin) {
      for (int a : directions) {
        for (int b : directions) {
          int xx = p.x + a;
          int yy = p.y + b;
          if (xx >= 0 && xx < maxX && yy >= 0 && yy < maxY) {
            Point pp = new Point(xx, yy);
            if (p.nextTo(pp)) {
              if (getDepth(lines, pp) < 9) {
                toAdd.add(pp);
              }
            }
          }
        }
      }
    }

    if (basin.addAll(toAdd)) {
      growBasin(basin, lines);
    }
    return basin.size();
  }

  private long solveA(List<String> lines) {
    maxX = lines.get(0).length();
    maxY = lines.size();

    List<Point> lows = findLows(lines);
    return lows.size() + lows.stream().mapToInt(p -> Integer.parseInt("" + lines.get(p.y).charAt(p.x))).sum();
  }

  private long solveB(List<String> lines) {
    maxX = lines.get(0).length();
    maxY = lines.size();

    List<Integer> sizes = findLows(lines).stream().map(p -> {
      Set<Point> basin = new HashSet<>();
      basin.add(p);
      return growBasin(basin, lines);
    }).sorted().collect(Collectors.toList());

    long result = 1;
    for (int i = sizes.size() - 3; i < sizes.size(); i++) {
      result *= sizes.get(i);
    }

    return result;
  }

  @Test
  public void testDay09() {
    List<String> testInput = Arrays.asList( //
        "2199943210", "3987894921", "9856789892", "8767896789", "9899965678");
    assertEquals(15, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(532, result);

    assertEquals(1134, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(1110780, result);
  }

}
