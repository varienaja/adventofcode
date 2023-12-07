package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(4403592L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(38017587L, result);
  }

  @Test
  public void testA() {
    assertEquals(288, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(71503, solveB(getTestInput()));
  }

  private long solve(List<Long> times, List<Long> distances) {
    return IntStream.range(0, times.size()) //
        .mapToLong(ix -> {
          double b = times.get(ix);
          double c = distances.get(ix);
          double x = (-b + Math.sqrt(b * b - 4 * c)) / -2;
          long v1 = (long)Math.floor(x);
          long v2 = (long)Math.ceil(b - x);
          return v2 - v1 - 1; // Number of possibilities for 'time' such that we get farther than 'dist'
        }).reduce((a, b) -> a * b).orElseThrow();
  }

  private long solveA(List<String> lines) {
    List<Long> times = parseNumbers(lines.get(0).split(":\\s+")[1], "\\s+");
    List<Long> distances = parseNumbers(lines.get(1).split(":\\s+")[1], "\\s+");
    return solve(times, distances);
  }

  private long solveB(List<String> lines) {
    List<Long> times = List.of(Long.parseLong(lines.get(0).split(":\\s+")[1].replaceAll("\\s+", "")));
    List<Long> distances = List.of(Long.parseLong(lines.get(1).split(":\\s+")[1].replaceAll("\\s+", "")));

    return solve(times, distances);
  }

}
