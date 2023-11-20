package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(20372, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(13334L, sum);
  }

  public long solveA(String input) {
    Map<Integer, BlockingQueue<Long>> ins = new HashMap<>();
    Map<Integer, BlockingQueue<Long>> outs = new HashMap<>();
    Map<Integer, Future<Long>> pcs = new HashMap<>();

    for (int i = 0; i < 50; i++) {
      BlockingQueue<Long> in = new LinkedBlockingDeque<>(2000);
      in.offer((long)i);
      ins.put(i, in);
      BlockingQueue<Long> out = new LinkedBlockingDeque<>(2000);
      outs.put(i, out);

      pcs.put(i, Intcode.run(input, Map.of(), in, out));
    }
    try {
      while (true) {
        for (BlockingQueue<Long> q : outs.values()) {
          if (q.size() >= 3) { // Full packet sent
            int address = q.poll().intValue();
            long x = q.poll();
            long y = q.poll();
            // System.out.println(address + ", " + x + ", " + y);
            if (address == 255) {
              return y;
            }

            BlockingQueue<Long> target = ins.get(address);
            target.offer(x);
            target.offer(y);
          }
        }

        for (BlockingQueue<Long> q : ins.values()) {
          if (q.isEmpty()) {
            q.offer(-1L);
          }
        }
      }
    } finally {
      for (Future<Long> f : pcs.values()) {
        f.cancel(true);
      }
    }
  }

  public long solveB(String input) {
    Map<Integer, BlockingQueue<Long>> ins = new HashMap<>();
    Map<Integer, BlockingQueue<Long>> outs = new HashMap<>();
    Map<Integer, Future<Long>> pcs = new HashMap<>();
    long natX = Long.MIN_VALUE;
    long natY = Long.MIN_VALUE;
    long lastNattedY = Long.MIN_VALUE;

    for (int i = 0; i < 50; i++) {
      BlockingQueue<Long> in = new LinkedBlockingDeque<>();
      in.offer((long)i);
      ins.put(i, in);
      BlockingQueue<Long> out = new LinkedBlockingDeque<>();
      outs.put(i, out);

      pcs.put(i, Intcode.run(input, Map.of(), in, out));
    }
    try {
      while (true) {
        for (BlockingQueue<Long> q : outs.values()) {
          if (q.size() >= 3) { // Full packet sent
            int address = q.poll().intValue();
            long x = q.poll();
            long y = q.poll();
            if (address == 255) {
              natX = x;
              natY = y;
            } else {
              BlockingQueue<Long> target = ins.get(address);
              target.offer(x);
              target.offer(y);
            }
          }
        }

        boolean idle = true;
        for (BlockingQueue<Long> q : ins.values()) {
          if (q.isEmpty()) {
            q.offer(-1L);
          } else {
            idle = false;
          }
        }
        for (BlockingQueue<Long> q : outs.values()) {
          if (!q.isEmpty()) {
            idle = false;
          }
        }

        if (idle && natY != Long.MIN_VALUE) {
          if (natY == lastNattedY) {
            return natY;
          }
          lastNattedY = natY;

          BlockingQueue<Long> target = ins.get(0);
          target.offer(natX);
          target.offer(natY);
          natX = Long.MIN_VALUE;
          natY = Long.MIN_VALUE;
        }
      }
    } finally {
      for (Future<Long> f : pcs.values()) {
        f.cancel(true);
      }
    }

  }

}
