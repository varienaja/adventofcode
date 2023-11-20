package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(2941952859L, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(66113, sum);
  }

  private long solveA(String input) {
    BlockingDeque<Long> in = new LinkedBlockingDeque<>();
    in.add(1L);
    BlockingDeque<Long> out = new LinkedBlockingDeque<>();
    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    assertEquals(1, out.size());
    return out.pollLast();
  }

  private long solveB(String input) {
    BlockingDeque<Long> in = new LinkedBlockingDeque<>();
    in.add(2L);
    BlockingDeque<Long> out = new LinkedBlockingDeque<>();
    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    assertEquals(1, out.size());
    return out.pollLast();
  }

}
