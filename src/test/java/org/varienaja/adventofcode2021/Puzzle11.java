package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.Point;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {
  private List<char[]> grid;
  private int maxX;
  private int maxY;

  private int flash(int x, int y) {
    grid.get(y)[x] = '0';
    int flashCount = 1;

    for (Point n : new Point(x, y).getAllNeighbours()) {
      if (0 <= n.y && n.y < maxY && 0 <= n.x && n.x < maxX && grid.get(n.y)[n.x] != '0') {
        if (++grid.get(n.y)[n.x] > '9') {
          flashCount += flash(n.x, n.y);
        }
      }
    }

    return flashCount;
  }

  private void initGrid(List<String> lines) {
    grid = lines.stream().map(String::toCharArray).collect(Collectors.toList());
    maxX = lines.get(0).length();
    maxY = lines.size();
  }

  private int simulateStep() {
    for (int y = 0; y < maxY; y++) {
      for (int x = 0; x < maxX; x++) {
        grid.get(y)[x]++;
      }
    }

    int flashCount = 0;
    for (int y = 0; y < maxY; y++) {
      for (int x = 0; x < maxX; x++) {
        if (grid.get(y)[x] > '9') {
          flashCount += flash(x, y);
        }
      }
    }

    return flashCount;
  }

  private long solveA(List<String> lines, int steps) {
    initGrid(lines);

    return IntStream.range(0, steps).map(i -> simulateStep()).sum();
  }

  private long solveB(List<String> lines) {
    initGrid(lines);
    int octopusCount = maxX * maxY;

    int step = 0;
    int flashCount;
    do {
      flashCount = simulateStep();
      step++;
    } while (flashCount != octopusCount);

    return step;
  }

  @Test
  public void testDay11() {
    List<String> testInput = Arrays.asList( //
        "5483143223", //
        "2745854711", //
        "5264556173", //
        "6141336146", //
        "6357385478", //
        "4167524645", //
        "2176841721", //
        "6882881134", //
        "4846848554", //
        "5283751526");
    assertEquals(204, solveA(testInput, 10));
    assertEquals(1656, solveA(testInput, 100));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines, 100);
    System.out.println(result);
    assertEquals(1627, result);

    assertEquals(195, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(329, result);
  }

}
