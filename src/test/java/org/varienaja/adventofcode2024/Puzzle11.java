package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {
  private Map<Map.Entry<Long, Integer>, Long> knowledge = new HashMap<>();

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInputString(), 25);
    System.out.println(result);
    assertEquals(228668, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInputString(), 75);
    System.out.println(result);
    assertEquals(270673834779359L, result);
  }

  @Test
  public void testA() {
    assertEquals(22, solve("125 17", 6));
  }

  private long solve(long nr, int rounds) {
    Long answer = knowledge.get(Map.entry(nr, rounds));
    if (answer != null) {
      return answer;
    }
    long result;
    if (rounds == 0) {
      result = 1;
    } else if (nr == 0) {
      result = solve(1, rounds - 1);
    } else {
      String s = Long.toString(nr);
      if (s.length() % 2 == 0) {
        String s1 = s.substring(s.length() / 2);
        String s2 = s.substring(0, s.length() / 2);
        result = solve(Long.parseLong(s1), rounds - 1) + solve(Long.parseLong(s2), rounds - 1);
      } else {
        result = solve(nr * 2024, rounds - 1);
      }
    }
    knowledge.put(Map.entry(nr, rounds), result);
    return result;
  }

  private long solve(String input, int rounds) {
    return Arrays.stream(input.split("\s+")) //
        .mapToLong(Long::parseLong) // for each stone
        .map(stone -> solve(stone, rounds)) // calculate how many it will be after this many rounds
        .sum();
  }

}
