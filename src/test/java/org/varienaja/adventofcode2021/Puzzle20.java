package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {
  private int[] dd = new int[] {
      -1, 0, 1
  };

  private Set<Point> enhance(Set<Point> lightPixels, char[] algo, char outSide) {
    IntSummaryStatistics xStats = lightPixels.stream().mapToInt(p -> p.x).summaryStatistics();
    IntSummaryStatistics yStats = lightPixels.stream().mapToInt(p -> p.y).summaryStatistics();
    int minX = xStats.getMin() - 1;
    int maxX = xStats.getMax() + 1;
    int minY = yStats.getMin() - 1;
    int maxY = yStats.getMax() + 1;

    Set<Point> result = new HashSet<>();
    for (int y = minY; y <= maxY; y++) {
      for (int x = minX; x <= maxX; x++) {

        StringBuilder sb = new StringBuilder();
        for (int dy : dd) {
          for (int dx : dd) {
            Point pixel = new Point(x + dx, y + dy);
            if (xStats.getMin() <= pixel.x && pixel.x <= xStats.getMax() && //
                yStats.getMin() <= pixel.y && pixel.y <= yStats.getMax()) {
              sb.append(lightPixels.contains(pixel) ? '1' : '0');
            } else { // Outside 'my' pixels
              sb.append(outSide);
            }
          }
        }
        if (algo[Integer.parseInt(sb.toString(), 2)] == '#') {
          result.add(new Point(x, y));
        }
      }
    }

    return result;
  }

  private Set<Point> parse(List<String> lines) {
    Set<Point> lightPixels = new HashSet<>();
    for (int y = 0; y < lines.size() - 2; y++) {
      String line = lines.get(y + 2);
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          lightPixels.add(new Point(x, y));
        }
      }
    }
    return lightPixels;
  }

  private long solve(Set<Point> lightPixels, char[] algo, int rounds) {
    char[] outside = new char[] {
        '0', '0'
    };
    if (algo[0] == '#' && algo[511] == '.') {
      outside[1] = '1';
    }

    for (int i = 0; i < rounds; i++) {
      lightPixels = enhance(lightPixels, algo, outside[i % 2]);
    }
    return lightPixels.size();
  }

  private long solveA(List<String> lines) {
    char[] algo = lines.get(0).toCharArray();
    return solve(parse(lines), algo, 2);
  }

  private long solveB(List<String> lines) {
    char[] algo = lines.get(0).toCharArray();
    return solve(parse(lines), algo, 50);
  }

  @Test
  public void testDay20() {
    List<String> testInput = Arrays.asList( //
        "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#", //
        "", //
        "#..#.", //
        "#....", //
        "##..#", //
        "..#..", //
        "..###");
    assertEquals(35, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(5395, result);

    assertEquals(3351, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(17584, result);
  }

}
