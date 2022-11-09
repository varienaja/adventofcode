
package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {

  String compile(String[] subPatterns) {
    int size = (int)Math.sqrt(subPatterns.length);
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < size; y++) {
      StringBuilder s1 = new StringBuilder();
      StringBuilder s2 = new StringBuilder();
      StringBuilder s3 = new StringBuilder();
      StringBuilder s4 = new StringBuilder();
      for (int x = 0; x < size; x++) {
        String subPattern = subPatterns[x + y * size];

        String[] parts = subPattern.split("/");
        s1.append(parts[0]);
        s2.append(parts[1]);
        s3.append(parts[2]);
        if (parts.length == 4) {
          s4.append(parts[3]);
        }
      }

      sb.append(s1);
      sb.append("/");
      sb.append(s2);
      sb.append("/");
      sb.append(s3);
      if (s4.length() > 0) {
        sb.append("/");
        sb.append(s4);
      }
      sb.append("/");
    }

    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  private String enhance(String pattern, Map<String, String> src2dest) {
    String enhancement = src2dest.get(pattern);
    if (enhancement != null) {
      return enhancement;
    }

    for (String t : generateTransformations(pattern)) {
      enhancement = src2dest.get(t);
      if (enhancement != null) {
        return enhancement;
      }
    }

    throw new IllegalStateException("No enhancement found for " + pattern);
  }

  /**
   * Extracts the 2x2 or 3x3 sub patterns from a greater pattern.
   *
   * @param pattern the greater pattern
   * @return the sub patterns, ordered from left to right, top-bottom
   */
  private String[] extractSubPatterns(String pattern) {
    String[] lines = pattern.split("/");
    int pLength = lines.length % 2 == 0 ? 2 : 3;
    int size = lines.length / pLength;
    String[] result = new String[size * size];

    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        StringBuilder sb = new StringBuilder();

        for (int yy = 0; yy < pLength; yy++) {
          for (int xx = 0; xx < pLength; xx++) {
            sb.append(lines[pLength * y + yy].charAt(pLength * x + xx));
          }
          sb.append("/");
        }
        sb.setLength(sb.length() - 1);
        result[x + y * size] = sb.toString();
      }
    }

    return result;
  }

  private char[][] flipH(char[][] tile) {
    char[][] flipped = new char[tile.length][tile.length];
    for (int x = 0; x < tile.length; x++) {
      for (int y = 0; y < tile.length; y++) {
        flipped[x][y] = tile[x][tile.length - 1 - y];
      }
    }
    return flipped;
  }

  private char[][] flipV(char[][] tile) {
    char[][] flipped = new char[tile.length][tile.length];
    for (int x = 0; x < tile.length; x++) {
      for (int y = 0; y < tile.length; y++) {
        flipped[x][y] = tile[tile.length - 1 - x][y];
      }
    }
    return flipped;
  }

  List<String> generateTransformations(String pattern) {
    List<String> transformations = new LinkedList<>();

    String[] parts = pattern.split("/");
    char[][] tile = new char[parts.length][parts.length];
    for (int i = 0; i < parts.length; i++) {
      tile[i] = parts[i].toCharArray();
    }

    for (int rotate = 0; rotate < 4; rotate++) {
      tile = rotate(tile);
      for (int flipH = 0; flipH < 2; flipH++) {
        tile = flipH(tile);
        for (int flipV = 0; flipV < 2; flipV++) {
          tile = flipV(tile);
          StringBuilder sb = new StringBuilder();
          for (char[] line : tile) {
            sb.append(String.valueOf(line));
            sb.append("/");
          }

          sb.setLength(sb.length() - 1);
          transformations.add(sb.toString());
        }
      }
    }

    return transformations;
  }

  private char[][] rotate(char[][] tile) {
    char[][] rotated = new char[tile.length][tile.length];
    for (int x = 0; x < tile.length; x++) {
      for (int y = 0; y < tile.length; y++) {
        rotated[x][y] = tile[tile.length - 1 - y][x];
      }
    }
    return rotated;
  }

  private long solveA(List<String> input, int iterations) {
    String pattern = ".#./..#/###";
    Map<String, String> src2dest = new HashMap<>();
    for (String line : input) {
      String[] parts = line.split(" => ");
      src2dest.put(parts[0], parts[1]);
    }

    for (int i = 0; i < iterations; i++) {
      String[] subPatterns = extractSubPatterns(pattern);

      for (int ix = 0; ix < subPatterns.length; ix++) {
        subPatterns[ix] = enhance(subPatterns[ix], src2dest);
      }

      pattern = compile(subPatterns);
    }

    long dashCount = 0;
    for (char c : pattern.toCharArray()) {
      if (c == '#') {
        dashCount++;
      }
    }
    return dashCount;
  }

  @Test
  public void testDay19() {
    List<String> testInput = List.of( //
        "../.# => ##./#../...", //
        ".#./..#/### => #..#/..../..../#..#");
    assertEquals(12, solveA(testInput, 2));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines, 5);
    System.out.println(result);
    assertEquals(125, result);

    announceResultB();
    long stepCount = solveA(lines, 18);
    System.out.println(stepCount);
    assertEquals(1782917, stepCount);
  }

}
