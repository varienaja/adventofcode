package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(109785, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(504, sum);
  }

  private long solve(List<String> input, boolean partA) {
    Map<Point, Integer> grid = new HashMap<>();

    Pattern p = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int px = Integer.parseInt(m.group(2));
        int py = Integer.parseInt(m.group(3));
        int width = Integer.parseInt(m.group(4));
        int height = Integer.parseInt(m.group(5));

        for (int x = px; x < px + width; x++) {
          for (int y = py; y < py + height; y++) {
            grid.compute(new Point(x, y), (k, v) -> v == null ? 1 : v + 1);
          }
        }
      }
    }

    if (partA) {
      return grid.values().stream().filter(v -> v > 1).count();
    }

    // Check which ID does never overlap
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int id = Integer.parseInt(m.group(1));
        int px = Integer.parseInt(m.group(2));
        int py = Integer.parseInt(m.group(3));
        int width = Integer.parseInt(m.group(4));
        int height = Integer.parseInt(m.group(5));

        boolean overlaps = false;
        for (int x = px; x < px + width; x++) {
          for (int y = py; y < py + height; y++) {
            if (grid.get(new Point(x, y)) > 1) {
              overlaps = true;
            }
          }
        }

        if (!overlaps) {
          return id;
        }
      }
    }

    return -1;
  }

  private long solveA(List<String> input) {
    return solve(input, true);
  }

  private long solveB(List<String> input) {
    return solve(input, false);
  }

  @Test
  public void testA() {
    List<String> testInput = List.of(//
        "#1 @ 1,3: 4x4", //
        "#2 @ 3,1: 4x4", //
        "#3 @ 5,5: 2x2");
    assertEquals(4, solveA(testInput));
  }

  @Test
  public void testB() {
    List<String> testInput = List.of(//
        "#1 @ 1,3: 4x4", //
        "#2 @ 3,1: 4x4", //
        "#3 @ 5,5: 2x2");
    assertEquals(3, solveB(testInput));
  }

}
