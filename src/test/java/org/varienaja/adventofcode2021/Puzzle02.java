package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  private long solve(List<String> lines, boolean part1) {
    long horizontal = 0;
    long depth = 0;
    long aim = 0;
    for (String line : lines) {
      String[] parts = line.split("\\s");
      long dist = Integer.parseInt(parts[1]);
      switch (parts[0]) {
        case "forward":
          horizontal += dist;
          if (!part1) {
            depth += aim * dist;
          }
          break;
        case "down":
          if (part1) {
            depth += dist;
          } else {
            aim += dist;
          }
          break;
        case "up":
          if (part1) {
            depth -= dist;
          } else {
            aim -= dist;
          }
          break;
      }
    }
    return horizontal * depth;
  }

  private long solveA(List<String> lines) {
    return solve(lines, true);
  }

  private long solveB(List<String> lines) {
    return solve(lines, false);
  }

  @Test
  public void testDay02() {
    List<String> testInput = Arrays.asList("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2");
    assertEquals(150, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(2091984, result);
    System.out.println(result);

    assertEquals(900, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    assertEquals(2086261056, result);
    System.out.println(result);
  }

}
