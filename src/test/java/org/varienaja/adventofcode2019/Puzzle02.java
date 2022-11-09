package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString(), Map.of(1, 12L, 2, 2L));
    System.out.println(sum);
    assertEquals(4462686, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(5936, sum);
  }

  private long solveA(String input, Map<Integer, Long> initialValues) {
    return Intcode.run(input, initialValues);
  }

  private long solveB(String input) {
    for (long noun = 0; noun <= 99; noun++) {
      for (long verb = 0; verb <= 99; verb++) {
        if (solveA(input, Map.of(1, noun, 2, verb)) == 19690720) {
          return 100 * noun + verb;
        }
      }
    }
    return -1;
  }

  @Test
  public void testA() {
    assertEquals(3500, solveA("1,9,10,3,2,3,11,0,99,30,40,50", Collections.emptyMap()));
    assertEquals(2, solveA("1,0,0,0,99", Collections.emptyMap()));
    assertEquals(2, solveA("2,4,4,5,99,0", Collections.emptyMap()));
    assertEquals(30, solveA("1,1,1,4,99,5,6,0,99", Collections.emptyMap()));
  }

}
