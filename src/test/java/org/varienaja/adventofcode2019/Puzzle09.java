package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
    BlockingQueue<Long> in = new ArrayBlockingQueue<>(1);
    in.add(1L);
    BlockingQueue<Long> out = new ArrayBlockingQueue<>(100);
    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    LinkedList<Long> l = new LinkedList<>();
    out.drainTo(l);

    assertEquals(1, l.size());
    return l.getLast();
  }

  private long solveB(String input) {
    BlockingQueue<Long> in = new ArrayBlockingQueue<>(1);
    in.add(2L);
    BlockingQueue<Long> out = new ArrayBlockingQueue<>(100);
    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    LinkedList<Long> l = new LinkedList<>();
    out.drainTo(l);

    assertEquals(1, l.size());
    return l.getLast();
  }

}
