package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(17330L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(171518260283767L, result);
  }

  @Test
  public void testA() {
    assertEquals(89L, extractHighestJolts("811111111111119", 2));
    assertEquals(357L, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(987654321111L, extractHighestJolts("987654321111111", 12));
    assertEquals(811111111119L, extractHighestJolts("811111111111119", 12));
    assertEquals(3121910778619L, solveB(getStreamingTestInput()));
  }

  /**
   * Recursively finds the jolts in <tt>batteries</tt>that form the highest possible value.
   *
   * @param batteries the jolt values of all batteries
   * @param c current value
   * @param ix the minimum index
   * @param todo how many digits still to append to <tt>vv</tt>
   * @return the highest possible value as a long
   */
  private long extractHighestJolts(int[] batteries, long c, int ix, int todo) {
    if (todo == 0) {
      return c; // We've found it
    }

    int ixHighest = -1;
    int highest = -1;
    todo--;
    for (int i = ix; i < batteries.length - todo; ++i) { // Must leave 'todo' batteries
      if (batteries[i] > highest) {
        highest = batteries[i];
        ixHighest = i;
      }
    }
    return extractHighestJolts(batteries, 10 * c + highest, ixHighest + 1, todo);
  }

  private long extractHighestJolts(String line, int todo) {
    int[] all = line.chars().map(c -> c - '0').toArray();
    return extractHighestJolts(all, 0, 0, todo);
  }

  private long solveA(Stream<String> lines) {
    return lines.mapToLong(line -> extractHighestJolts(line, 2)).sum();
  }

  private long solveB(Stream<String> lines) {
    return lines.mapToLong(line -> extractHighestJolts(line, 12)).sum();
  }

}
