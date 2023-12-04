package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  public class Game {
    int id;
    Map<String, Integer> maxOutcome;

    Game(String line) {
      String[] parts = line.split(":\\s*");
      id = Integer.parseInt(parts[0].split("\\s+")[1]);

      maxOutcome = Stream.of(parts[1].split(";\\s*")) // e.g. "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
          .flatMap(outcome -> Stream.of(outcome.split(",\\s*"))) //
          .map(colorAmount -> colorAmount.split("\\s+")) // e.g. "6 red"
          .collect(Collectors.toMap(ac -> ac[1], ac -> Integer.parseInt(ac[0]), Math::max));
    }

    int calcPower() {
      return maxOutcome.values().stream() //
          .mapToInt(Integer::intValue) //
          .reduce((a, b) -> a * b).orElseThrow();
    }

    boolean isPossible(Map<String, Integer> requirements) {
      return requirements.entrySet().stream() //
          .allMatch(r -> maxOutcome.getOrDefault(r.getKey(), 0) <= r.getValue());
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(2156L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(66909L, result);
  }

  @Test
  public void testA() {
    assertEquals(8, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(2286, solveB(getStreamingTestInput()));
  }

  private long solveA(Stream<String> lines) {
    Map<String, Integer> requirements = Map.of("red", 12, "green", 13, "blue", 14);

    return lines.map(Game::new) //
        .filter(g -> g.isPossible(requirements)) //
        .mapToInt(g -> g.id) //
        .sum();
  }

  private long solveB(Stream<String> lines) {
    return lines.map(Game::new) //
        .mapToInt(Game::calcPower) //
        .sum();
  }

}
