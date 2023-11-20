package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {
  private static final boolean DEBUG = false;
  private static final long TIMEOUT = 10;

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(180, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(8777, sum);
  }

  private Long read(BlockingQueue<Long> out) throws InterruptedException {
    return out.poll(TIMEOUT, TimeUnit.MILLISECONDS);
  }

  private long solveA(String input) {
    BlockingQueue<Long> in = new LinkedBlockingDeque<>();
    BlockingQueue<Long> out = new LinkedBlockingDeque<>();
    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Count blocks; every 3rd output that equals 2
    long blocks = 0;
    long counter = 0;
    Long polled = null;
    while ((polled = out.poll()) != null) {
      counter++;
      if (counter % 3 == 0) {
        if (polled == 2L) {
          blocks++;
        }
      }
    }

    return blocks;
  }

  private long solveB(String input) {
    Point scoreP = new Point(-1, 0);
    int maxX = 0;
    int maxY = 0;
    int score = 0;
    Point paddle = null;
    Point ball = null;

    String elements = " #*-x";
    // Run code, print outcome + score, read from keyboard, do again until won?
    BlockingQueue<Long> in = new LinkedBlockingDeque<>();
    BlockingQueue<Long> out = new LinkedBlockingDeque<>();
    Future<Long> f = Intcode.run(input, Map.of(0, 2L), in, out);

    Map<Point, Character> field = new HashMap<>();

    while (!f.isDone() || !out.isEmpty()) {
      try {
        Long X = 1L;
        while ((X = read(out)) != null) {
          Long Y = read(out);
          Long C = read(out);

          Point p = new Point(X.intValue(), Y.intValue());
          if (scoreP.equals(p)) {
            score = C.intValue();
          } else {
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
            char c = elements.charAt(C.intValue());
            field.put(p, c);
            if (c == '-') {
              paddle = p;
            } else if (c == 'x') {
              ball = p;
            }
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (DEBUG) {
        for (int y = 0; y <= maxY; y++) {
          for (int x = 0; x <= maxX; x++) {
            System.out.print(field.getOrDefault(new Point(x, y), ' '));
          }
          System.out.println();
        }
        System.out.println(score);
      }

      long joystick = Integer.signum(ball.x - paddle.x);
      in.offer(joystick);
    }

    return score;
  }

}
