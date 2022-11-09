package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(5975, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput(), 10000);
    System.out.println(sum);
    assertEquals(38670, sum);
  }

  private long solveA(List<String> input) {
    int GRIDSIZE = 400;
    Map<Character, Point> points = new HashMap<>();
    char ix = 'A';
    for (String line : input) {
      String[] parts = line.split(", ");

      int x = Integer.parseInt(parts[0]);
      int y = Integer.parseInt(parts[1]);
      points.put(ix++, new Point(x, y));
    }

    Map<Character, Integer> char2Counts = new TreeMap<>();
    char[][] grid = new char[GRIDSIZE][GRIDSIZE];
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        int minDist = Integer.MAX_VALUE;
        char c = ' ';
        for (Entry<Character, Point> e : points.entrySet()) {
          int dist = Math.abs(e.getValue().x - x) + Math.abs(e.getValue().y - y);
          if (dist < minDist) {
            minDist = dist;
            c = e.getKey();
          } else if (dist == minDist) {
            c = '.';
          }
        }
        grid[x][y] = c;
        char2Counts.compute(c, (k, v) -> v == null ? 1 : v + 1);
      }
    }

    // Remove extremes
    Set<Character> toRemove = new HashSet<>();
    for (int i = 0; i < GRIDSIZE; i++) {
      toRemove.add(grid[i][0]);
      toRemove.add(grid[i][GRIDSIZE - 1]);
      toRemove.add(grid[0][i]);
      toRemove.add(grid[GRIDSIZE - 1][i]);
    }
    for (Character r : toRemove) {
      points.remove(r);
      char2Counts.remove(r);
    }

    // Find max
    long max = Long.MIN_VALUE;
    for (Entry<Character, Integer> e : char2Counts.entrySet()) {
      if (e.getValue() > max) {
        max = e.getValue();
      }
    }

    return max;
  }

  private long solveB(List<String> input, int maxDistance) {
    int GRIDSIZE = 400;
    Map<Character, Point> points = new HashMap<>();
    char ix = 'A';
    for (String line : input) {
      String[] parts = line.split(", ");

      int x = Integer.parseInt(parts[0]);
      int y = Integer.parseInt(parts[1]);
      points.put(ix++, new Point(x, y));
    }

    long regionSize = 0;
    for (int x = 0; x < GRIDSIZE; x++) {
      for (int y = 0; y < GRIDSIZE; y++) {
        int totalDist = 0;
        for (Entry<Character, Point> e : points.entrySet()) {
          int dist = Math.abs(e.getValue().x - x) + Math.abs(e.getValue().y - y);
          totalDist += dist;
        }
        if (totalDist < maxDistance) {
          regionSize++;
        }
      }
    }

    return regionSize;
  }

  @Test
  public void testA() {
    assertEquals(17, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(16, solveB(testInput(), 32));
  }

  private List<String> testInput() {
    return List.of( //
        "1, 1", //
        "1, 6", //
        "8, 3", //
        "3, 4", //
        "5, 5", //
        "8, 9");
  }

}
