package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {
  private Map<String, Long> fromto2ways = new HashMap<>();

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(649L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(458948453421420L, result);
  }

  @Test
  public void testA() {
    assertEquals(5L, solveA(getTestInput('a')));
  }

  @Test
  public void testB() {
    assertEquals(2L, solveB(getTestInput('b')));
  }

  private long countWays(String from, String target, Map<String, String[]> fromtos) {
    if (from.equals(target)) {
      return 1L;
    }

    Long sum = fromto2ways.get(from + target);
    if (sum == null) {
      sum = Stream.of(fromtos.getOrDefault(from, new String[0])).mapToLong(t -> countWays(t, target, fromtos)).sum();
      fromto2ways.put(from + target, sum);
    }
    return sum;
  }

  private Map<String, String[]> parseInput(List<String> lines) {
    Map<String, String[]> fromtos = new HashMap<>();
    for (String line : lines) {
      String[] parts = line.split(": ");
      String[] tos = parts[1].split("\\s+");
      fromtos.put(parts[0], tos);
    }
    return fromtos;
  }

  private long solveA(List<String> lines) {
    return countWays("you", "out", parseInput(lines));
  }

  private long solveB(List<String> lines) {
    Map<String, String[]> fromtos = parseInput(lines);
    return countWays("svr", "dac", fromtos) * countWays("dac", "fft", fromtos) * countWays("fft", "out", fromtos) + //
        countWays("svr", "fft", fromtos) * countWays("fft", "dac", fromtos) * countWays("dac", "out", fromtos);
  }

}
