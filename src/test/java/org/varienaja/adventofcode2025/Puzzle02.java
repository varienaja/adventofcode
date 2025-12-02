package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    System.out.println(result);
    assertEquals(21139440284L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    System.out.println(result);
    assertEquals(38731915928L, result);
  }

  @Test
  public void testA() {
    assertTrue(isInvalidA("11"));
    assertTrue(isInvalidA("1010"));
    assertTrue(isInvalidA("123123"));
    assertFalse(isInvalidA("111"));
    assertFalse(isInvalidA("1112"));
    assertEquals(1227775554L, solveA(getTestInputString()));
  }

  @Test
  public void testB() {
    assertTrue(isInvalidB("11"));
    assertTrue(isInvalidB("111"));
    assertTrue(isInvalidB("2121212121"));
    assertFalse(isInvalidB("2121212122"));
    assertEquals(4174379265L, solveB(getTestInputString()));
  }

  /**
   * Checks whether the given id is invalid.
   *
   * @param id the id
   * @param partCount the number of parts to dive the id in
   * @return whether the 'id' consists of 'partcount' equal parts
   */
  private boolean isInvalid(String id, int partCount) {
    if (id.length() % partCount != 0) {
      return false;
    }

    int partLength = id.length() / partCount;
    for (int offset = partLength; offset < id.length(); offset += partLength) {
      for (int i = 0; i < partLength; ++i) {
        if (id.charAt(i) != id.charAt(offset + i)) {
          return false;
        }
      }
    }
    return true; // substring of partLength was found to be repeated throughout the whole id
  }

  /**
   * Check for part A.
   *
   * @param id the id
   * @return whether the given id consists of two equal halves
   */
  private boolean isInvalidA(String id) {
    return isInvalid(id, 2);
  }

  /**
   * Check for part B.
   *
   * @param id the id
   * @return whether the given id consists of two or more equal parts
   */
  private boolean isInvalidB(String id) {
    return IntStream.rangeClosed(2, id.length()).anyMatch(i -> isInvalid(id, i));
  }

  private long solve(String line, Function<String, Boolean> invalidChecker) {
    return Pattern.compile(",").splitAsStream(line) //
        .map(part -> part.split("-")) //
        .flatMapToLong(fromto -> LongStream.rangeClosed(Long.parseLong(fromto[0]), Long.parseLong(fromto[1])))//
        .filter(candidate -> invalidChecker.apply(String.valueOf(candidate))) //
        .sum();
  }

  private long solveA(String line) {
    return solve(line, this::isInvalidA);
  }

  private long solveB(String line) {
    return solve(line, this::isInvalidB);
  }

}
