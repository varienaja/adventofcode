package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

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
public class Puzzle19 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(126L, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(11351625L, sum);
  }

  public long solveA(String input) {
    long oneCount = 0;
    BlockingQueue<Long> in = new ArrayBlockingQueue<>(2);
    BlockingQueue<Long> out = new ArrayBlockingQueue<>(1);

    for (long y = 0; y < 50; y++) {
      for (long x = 0; x < 50; x++) {
        in.offer(x);
        in.offer(y);

        try {
          Intcode.run(input, Map.of(), in, out).get();
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (out.poll() == 1L) {
          // System.out.println(x + "," + y);
          oneCount++;
        }
      }
    }
    return oneCount;
  }

  public long solveB(String input) {
    // Walk (x,y) along left edge of the beam. If the position x,y-99 is also '#' and x+99,y too, we've found where the
    // square fits

    // Last coordinates of solveA
    long x = 33;
    long y = 49;

    boolean found = false;
    while (!found) {
      BlockingQueue<Long> in = new ArrayBlockingQueue<>(2);
      BlockingQueue<Long> out = new ArrayBlockingQueue<>(1);

      // find next left-edge
      y++;
      boolean edgeFound = false;
      do {
        in.offer(x);
        in.offer(y);
        try {
          Intcode.run(input, Map.of(), in, out).get();
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (out.poll() == 0L) {
          x++;
        } else {
          edgeFound = true;
        }
      } while (!edgeFound);
      // System.out.println(x + "," + y);

      // Try x,y-99
      in.offer(x);
      in.offer(y - 99);
      try {
        Intcode.run(input, Map.of(), in, out).get();
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (out.poll() == 1L) {
        // Try x+99,y-99
        in.offer(x + 99);
        in.offer(y - 99);
        try {
          Intcode.run(input, Map.of(), in, out).get();
        } catch (Exception e) {
          e.printStackTrace();
        }
        if (out.poll() == 1L) {
          found = true;
        }
      }
    }

    return 10000L * x + (y - 99);
  }

}
