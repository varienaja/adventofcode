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
public class Puzzle05 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    System.out.println(result);
    assertEquals(13933662, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    System.out.println(result);
    assertEquals(2369720, result);
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
    return l.getLast();
  }

  private long solveB(String input) {
    BlockingQueue<Long> in = new ArrayBlockingQueue<>(1);
    in.add(5L);
    BlockingQueue<Long> out = new ArrayBlockingQueue<>(100);
    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }
    LinkedList<Long> l = new LinkedList<>();
    out.drainTo(l);
    return l.getLast();
  }

  @Test
  public void testA() {
    assertEquals(1, solveA(testInput()));
  }

  private String testInput() {
    return "3,0,4,0,99";
  }

}
