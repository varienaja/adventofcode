package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getLongInput());
    System.out.println(result);
    assertEquals(536, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getLongInput());
    System.out.println(result);
    assertEquals(75108, result);
  }

  private long solveA(List<Long> input) {
    return input.stream().mapToLong(l -> l).sum();
  }

  private long solveB(List<Long> input) {
    long frequency = 0;
    Set<Long> seen = new HashSet<>();
    seen.add(frequency);

    while (true) {
      for (long l : input) {
        frequency += l;
        if (!seen.add(frequency)) {
          return frequency;
        }
      }
    }
  }

  @Test
  public void testA() {
    assertEquals(3L, solveA(List.of(1L, 1L, 1L)));
    assertEquals(0L, solveA(List.of(1L, 1L, -2L)));
    assertEquals(-6L, solveA(List.of(-1L, -2L, -3L)));
  }

  @Test
  public void testB() {
    assertEquals(0L, solveB(List.of(1L, -1L)));
    assertEquals(10L, solveB(List.of(3L, 3L, 4L, -2L, -4L)));
    assertEquals(5L, solveB(List.of(-6L, 3L, 8L, 5L, -6L)));
    assertEquals(14L, solveB(List.of(7L, 7L, -2L, -7L, -4L)));
  }

}
