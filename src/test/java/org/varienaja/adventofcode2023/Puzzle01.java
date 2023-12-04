package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
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
public class Puzzle01 extends PuzzleAbs {
  private static final Map<String, Integer> CHAR2DIGIT = new HashMap<>();
  private static final Map<String, Integer> WORD2DIGIT = new HashMap<>();
  static {
    CHAR2DIGIT.putAll(Map.of("1", 1, "2", 2, "3", 3, "4", 4, "5", 5, "6", 6, "7", 7, "8", 8, "9", 9));
    WORD2DIGIT.putAll(Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8, "nine", 9));
    WORD2DIGIT.putAll(CHAR2DIGIT);
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(55108L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(56324L, result);
  }

  @Test
  public void testA() {
    assertEquals(142, solveA(getStreamingTestInput('a')));
  }

  @Test
  public void testB() {
    assertEquals(281, solveB(getStreamingTestInput('b')));
  }

  private long solve(Stream<String> lines, Map<String, Integer> word2digit) {
    return lines.mapToInt(line -> {
      Deque<Integer> digits = word2digit.entrySet().stream() //
          .filter(e -> line.contains(e.getKey())) //
          .flatMap(e -> Stream.of( //
              Map.entry(line.indexOf(e.getKey()), e.getValue()), //
              Map.entry(line.lastIndexOf(e.getKey()), e.getValue()))) //
          .distinct() //
          .sorted(Comparator.comparing(Entry::getKey)) //
          .map(Entry::getValue) //
          .collect(Collectors.toCollection(LinkedList::new));

      return 10 * digits.getFirst() + digits.getLast();
    }).sum();
  }

  private long solveA(Stream<String> lines) {
    return solve(lines, CHAR2DIGIT);
  }

  private long solveB(Stream<String> lines) {
    return solve(lines, WORD2DIGIT);
  }

}
