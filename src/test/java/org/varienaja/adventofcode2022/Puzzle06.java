package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    assertEquals(1287L, result);
    System.out.println(result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    assertEquals(3716L, result);
    System.out.println(result);
  }

  private long solve(String line, int distinctCount) {
    for (int ix = distinctCount; ix < line.length(); ix++) {
      if (IntStream.range(ix - distinctCount, ix).map(i -> line.charAt(i)).distinct().count() == distinctCount) {
        return ix;
      }
    }
    throw new IllegalArgumentException("invalid input");
  }

  private long solveA(String line) {
    return solve(line, 4);
  }

  private long solveB(String line) {
    return solve(line, 14);
  }

  @Test
  public void testA() {
    assertEquals(5, solveA("bvwbjplbgvbhsrlpgdmjqwftvncz"));
    assertEquals(6, solveA("nppdvjthqldpwncqszvftbrmjlhg"));
    assertEquals(10, solveA("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"));
    assertEquals(11, solveA("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"));
  }

  @Test
  public void testB() {
    assertEquals(19, solveB("mjqjpqmgbljsphdztnvjfqwrcgsmlb"));
    assertEquals(23, solveB("bvwbjplbgvbhsrlpgdmjqwftvncz"));
    assertEquals(23, solveB("nppdvjthqldpwncqszvftbrmjlhg"));
    assertEquals(29, solveB("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"));
    assertEquals(26, solveB("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"));
  }

}
