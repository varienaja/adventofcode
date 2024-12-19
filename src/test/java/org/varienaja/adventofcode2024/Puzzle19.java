package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {
  private Set<String> towels;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(317, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(883443544805484L, result);
  }

  @Test
  public void testA() {
    assertEquals(6, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(16, solveB(getStreamingTestInput()));
  }

  long countPossibilities(String pattern) {
    long[] possibilities = new long[pattern.length() + 1];
    possibilities[0] = 1; // There is 1 possibility to get to index 0

    for (int i = 0; i < possibilities.length; ++i) {
      for (String towel : towels) {
        if (pattern.startsWith(towel, i)) {
          // The amount of possibilities to get to the new index increases by the amount of possibilities we had so far
          possibilities[i + towel.length()] += possibilities[i];
        }
      }
    }

    return possibilities[pattern.length()];
  }

  private long solveA(Stream<String> input) {
    return input.peek(l -> towels = towels == null ? Arrays.stream(l.split(",\s+")).collect(Collectors.toSet()) : towels) //
        .skip(2) //
        .filter(pattern -> countPossibilities(pattern) > 0) //
        .count();
  }

  private long solveB(Stream<String> input) {
    return input.peek(l -> towels = towels == null ? Arrays.stream(l.split(",\s+")).collect(Collectors.toSet()) : towels) //
        .skip(2) //
        .mapToLong(this::countPossibilities) //
        .sum();
  }

}
