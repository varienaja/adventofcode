package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Queue;
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
public class Puzzle21 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(19355862L, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(1140470745L, sum);
  }

  private void programLine(String line, Queue<Long> q) {
    for (char c : line.toCharArray()) {
      q.offer((long)c);
    }
    q.offer(10L);
  }

  public long solveA(String input) {
    BlockingDeque<Long> in = new LinkedBlockingDeque<>(2000);
    BlockingDeque<Long> out = new LinkedBlockingDeque<>(2000);

    // ABCD
    // ??.# !C && D --> Jump
    // .??? or !A --> Jump

    programLine("NOT C J", in);
    programLine("AND D J", in);
    programLine("NOT A T", in);
    programLine("OR T J", in);
    programLine("WALK", in);

    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return out.peekLast();
  }

  public long solveB(String input) {
    BlockingDeque<Long> in = new LinkedBlockingDeque<>(2000);
    BlockingDeque<Long> out = new LinkedBlockingDeque<>(2000);

    programLine("NOT C J", in);
    programLine("AND D J", in);
    programLine("AND H J", in);
    programLine("NOT B T", in);
    programLine("AND D T", in);
    programLine("OR T J", in);
    programLine("NOT A T", in);
    programLine("OR T J", in);
    programLine("RUN", in);

    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return out.peekLast();
  }

}
