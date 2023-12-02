package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.ToIntFunction;
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
    assertEquals(8, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(2286, solveB(getTestInput()));
  }

  private Stream<String> getTestInput() {
    return Stream.of( //
        "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green", //
        "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue", //
        "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red", //
        "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red", //
        "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green" //
    );
  }

  private Map<String, List<Map<String, Integer>>> parse(Stream<String> lines) {
    return lines.map(line -> line.split(":\\s*")) //
        .collect(Collectors.toMap(parts -> parts[0], parts -> parseSingleGameStates(parts[1])));
  }

  private Integer parseGameId(String gameName) {
    return Integer.parseInt(gameName.split("\\s+")[1]); // e.g. "Game 4"
  }

  private List<Map<String, Integer>> parseSingleGameStates(String singleGame) {
    return Stream.of(singleGame.split(";\\s*")) // e.g. "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
        .map(outcome -> {
          return Stream.of(outcome.split(",\\s*")) // e.g. "2 green, 6 blue"
              .map(colorAmount -> colorAmount.split("\\s+")) // e.g. "6 red"
              .collect(Collectors.toMap(ac -> ac[1], ac -> Integer.parseInt(ac[0])));
        }).toList();
  }

  private long solve(Stream<String> lines, ToIntFunction<Entry<String, List<Map<String, Integer>>>> f) {
    return parse(lines).entrySet().stream() //
        .mapToInt(f) // value each Game according to the given method
        .sum(); // sum it
  }

  private long solveA(Stream<String> lines) {
    Map<String, Integer> requirements = Map.of("red", 12, "green", 13, "blue", 14);

    return solve(lines, e -> e.getValue().stream() //
        .filter(outcome -> requirements.entrySet().stream() // search for counter-examples...
            .anyMatch(r -> outcome.getOrDefault(r.getKey(), 0) > r.getValue())) // ...that don't match a requirement
        .mapToInt(x -> 0) // ..value those Games 0
        .findAny().orElse(parseGameId(e.getKey()))); // otherwise GameId
  }

  private long solveB(Stream<String> lines) {
    return solve(lines, e -> e.getValue().stream() //
        .flatMap(outcome -> outcome.entrySet().stream()) //
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max)) // minimum red, greed, blue needed
        .values().stream().mapToInt(Integer::intValue).reduce((a, b) -> a * b) // calc 'power'
        .orElseThrow());
  }

}
