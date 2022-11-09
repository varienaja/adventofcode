package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {

  private long solveA(List<String> lines) {
    int maxY = lines.size();
    int maxX = lines.get(0).length();
    List<char[]> grid = lines.stream().map(String::toCharArray).collect(Collectors.toList());
    List<char[]> nextStep = lines.stream().map(String::toCharArray).collect(Collectors.toList());
    int steps = 0;
    boolean moved = true;

    while (moved) {
      moved = false;

      // >
      for (int y = 0; y < maxY; y++) {
        char[] line = grid.get(y);
        for (int x = 0; x < maxX; x++) {
          char c = line[x];
          if (c == '>') {
            int targetPos = (x + 1) % maxX;
            if (line[targetPos] == '.') {
              nextStep.get(y)[targetPos] = c;
              nextStep.get(y)[x] = '.';
              moved = true;
            }
          }
        }
      }

      grid = nextStep;
      nextStep = grid.stream().map(String::valueOf).map(String::toCharArray).collect(Collectors.toList());

      // v
      for (int x = 0; x < maxX; x++) {
        for (int y = 0; y < maxY; y++) {
          char[] line = grid.get(y);
          char c = line[x];
          if (c == 'v') {
            int targetPos = (y + 1) % grid.size();
            if (grid.get(targetPos)[x] == '.') {
              nextStep.get(targetPos)[x] = c;
              nextStep.get(y)[x] = '.';
              moved = true;
            }
          }
        }
      }

      grid = nextStep;
      nextStep = grid.stream().map(String::valueOf).map(String::toCharArray).collect(Collectors.toList());

      steps++;
    }

    return steps;
  }

  @Test
  public void testDay25() {
    List<String> testInput = Arrays.asList( //
        "v...>>.vv>", //
        ".vv>>.vv..", //
        ">>.>v>...v", //
        ">>v>>.>.v.", //
        "v>v.vv.v..", //
        ">.>>..v...", //
        ".vv..>.>v.", //
        "v.v..>>v.v", //
        "....v..v.>");
    assertEquals(58, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(563, result);
  }

}
