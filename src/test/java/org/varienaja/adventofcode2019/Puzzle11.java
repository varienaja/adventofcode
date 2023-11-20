package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
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
public class Puzzle11 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(2276, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    solveB(getInputString());
    // CBLPJZCU
  }

  private Map<Point, Long> solve(String input, boolean partB) {
    Map<Point, Long> panel2Color = new HashMap<>();
    Point pos = new Point(0, 0);
    if (partB) {
      panel2Color.put(pos, 1L);
    }
    String directions = "nesw";
    int direction = 0;

    BlockingQueue<Long> in = new LinkedBlockingDeque<>();
    BlockingQueue<Long> out = new LinkedBlockingDeque<>();
    Future<Long> f = Intcode.run(input, Map.of(), in, out);

    while (!f.isDone()) {
      try {
        long color = panel2Color.getOrDefault(pos, 0L);
        in.put(color);

        Long paintedTo = out.poll(1, TimeUnit.SECONDS);
        if (paintedTo == null) {
          continue; // It seems that the call to f.isDone is just a tad too quick to detect that IntCode has stopped
        }
        long turn = out.take(); // 0==turn left, 1 == turn right
        panel2Color.put(pos, paintedTo);

        direction = (direction + (turn == 0 ? 3 : 1)) % directions.length();
        char d = directions.charAt(direction);
        if (d == 's') {
          pos = pos.getSouth();
        } else if (d == 'w') {
          pos = pos.getWest();
        } else if (d == 'n') {
          pos = pos.getNorth();
        } else if (d == 'e') {
          pos = pos.getEast();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return panel2Color;
  }

  private long solveA(String input) {
    Map<Point, Long> panel2Color = solve(input, false);
    return panel2Color.size();
  }

  private void solveB(String input) {
    Map<Point, Long> panel2Color = solve(input, true);
    IntSummaryStatistics w = panel2Color.keySet().stream().mapToInt(p -> p.x).summaryStatistics();
    IntSummaryStatistics h = panel2Color.keySet().stream().mapToInt(p -> p.y).summaryStatistics();
    System.out.println();

    for (int y = h.getMin(); y <= h.getMax(); y++) {
      for (int x = w.getMin(); x <= w.getMax(); x++) {
        Point p = new Point(x, y);
        System.out.print(panel2Color.getOrDefault(p, 0L) == 0L ? ' ' : '#');
      }
      System.out.println();
    }
  }

}
