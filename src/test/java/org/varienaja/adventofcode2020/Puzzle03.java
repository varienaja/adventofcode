package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  private long solveA(List<String> lines, int dX, int dY) {
    int width = lines.get(0).length();
    int x = dX;
    int y = dY;

    long treeCount = 0;
    do {
      if (lines.get(y).charAt(x) == '#') {
        treeCount++;
      }

      x += dX;
      if (x >= width) {
        x -= width;
      }
      y += dY;
    } while (y < lines.size());
    return treeCount;
  }

  private long solveB(List<String> lines) {
    return solveA(lines, 1, 1) * //
        solveA(lines, 3, 1) * //
        solveA(lines, 5, 1) * //
        solveA(lines, 7, 1) * //
        solveA(lines, 1, 2);
  }

  @Test
  public void testDay03() throws IOException, URISyntaxException {
    List<String> input = Arrays.asList( //
        "..##.......", //
        "#...#...#..", //
        ".#....#..#.", //
        "..#.#...#.#", //
        ".#...##..#.", //
        "..#.##.....", //
        ".#.#.#....#", //
        ".#........#", //
        "#.##...#...", //
        "#...##....#", //
        ".#..#...#.#");
    assertEquals(7, solveA(input, 3, 1));

    announceResultA();
    List<String> lines = getInput();
    System.out.println(solveA(lines, 3, 1));

    assertEquals(336, solveB(input));
    announceResultB();
    System.out.println(solveB(lines));
  }

}
