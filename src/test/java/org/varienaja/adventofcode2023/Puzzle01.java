package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
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
    long result = solveA(getInput().stream());
    System.out.println(result);
    assertEquals(55108L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput().stream());
    System.out.println(result);
    assertEquals(56324L, result);
  }

  private Stream<String> getTestInputA() {
    return Stream.of( //
        "1abc2", //
        "pqr3stu8vwx", //
        "a1b2c3d4e5f", //
        "treb7uchet" //
    );
  }

  private Stream<String> getTestInputB() {
    return Stream.of( //
        "two1nine", //
        "eightwothree", //
        "abcone2threexyz", //
        "xtwone3four", //
        "4nineeightseven2", //
        "zoneight234", //
        "7pqrstsixteen" //
    );
  }

  private long solve(Stream<String> lines, Map<String, Integer> word2digit) {
    return lines.mapToInt(line -> {
      Deque<Integer> digits = new LinkedList<>();

      AtomicInteger ix = new AtomicInteger(0);
      do {
        word2digit.entrySet().stream() //
            .filter(e -> line.indexOf(e.getKey(), ix.get()) == ix.get()) //
            .mapToInt(Entry::getValue) //
            .findFirst() //
            .ifPresent(digits::add);
      } while (ix.incrementAndGet() < line.length());

      return 10 * digits.peekFirst() + digits.peekLast();
    }).sum();
  }

  private long solveA(Stream<String> lines) {
    return solve(lines, CHAR2DIGIT);
  }

  private long solveB(Stream<String> lines) {
    return solve(lines, WORD2DIGIT);
  }

  @Test
  public void testA() {
    assertEquals(142, solveA(getTestInputA()));
  }

  @Test
  public void testB() {
    assertEquals(281, solveB(getTestInputB()));
  }

}
