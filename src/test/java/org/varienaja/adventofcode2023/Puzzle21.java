package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {
  record Item(int steps, Point p) {
  }

  private int maxY = 0;
  private Point starter = null;

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInput(), 64);
    System.out.println(result);
    assertEquals(3658L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInput(), 26501365);
    System.out.println(result);
    assertEquals(608193767979991L, result);
  }

  @Test
  public void testA() {
    assertEquals(16, solve(getTestInput(), 6));
  }

  @Test
  public void testB() {
    assertEquals(16, solve(getTestInput(), 6));
    assertEquals(50, solve(getTestInput(), 10));
    assertEquals(1594, solve(getTestInput(), 50));
  }

  private Set<Point> parse(List<String> lines) {
    Set<Point> world = new HashSet<>();
    for (int y = 0; y < lines.size(); ++y) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        char c = line.charAt(x);
        if (c != '.') {
          if (c == 'S') {
            starter = new Point(x, y);
          } else {
            world.add(new Point(x, y));
          }
        }
      }
    }

    maxY = lines.size();

    return world;
  }

  private long solve(List<String> lines, int steps) {
    return solve(parse(lines), starter, steps);
  }

  // For B: I saw that the maze fills and starts 'blinking' after a certain amount of steps
  // My goal was to keep a Map of stepCount to number of reachable garden plots of a maze-tile
  // I'd need a few of them, because only the starter starts in the middle, new maze-tiles are
  // entered from the middle bottom or middle top exclusively.
  // It would be easy to calculate how many tiles we need after x steps. The tiles in the middle
  // all have had time to fill up entirely until their blinking state, so their number of
  // reachable gardens is easy to lookup. The maze tiles at the brim are the ones that should be
  // 'calculated'. At the moment, I don't have much desire to implement this.
  // So this is from https://gist.github.com/maksverver/d2f24eca8471fd66f25218ed1e15f304
  private long solve(Set<Point> rocks, Point start, int n) {
    int ln = maxY;
    int steps = 0;
    List<Integer> cycle = new ArrayList<>();
    Set<Point>[] seen = new Set[] {
        new HashSet<Point>(), new HashSet<Point>()
    };
    Deque<Item> queue = new LinkedList<>();
    queue.add(new Item(0, start));

    while (!queue.isEmpty()) {
      Item item = queue.poll();
      int new_steps = item.steps;
      if (new_steps != steps) {
        if (steps % (ln * 2) == n % (ln * 2)) {
          // System.out.println(steps + ", " + seen[steps % 2].size());
          cycle.add((seen[steps % 2].size()));
          if (cycle.size() == 3) {
            long p2 = cycle.get(0);
            long offset = cycle.get(1) - cycle.get(0);
            long increment = (cycle.get(2) - cycle.get(1)) - (cycle.get(1) - cycle.get(0));
            long x = n / (ln * 2);
            p2 += x * offset + x * (x - 1) / 2 * increment;
            return p2;
          }
        }
      }
      steps = new_steps;
      int next_steps = new_steps + 1;
      for (Point c : item.p.getNSWENeighbours()) {
        Point mod = new Point(c.x % ln, c.y % ln);
        if (mod.x < 0) {
          mod = new Point(ln + mod.x, mod.y);
        }
        if (mod.y < 0) {
          mod = new Point(mod.x, ln + mod.y);
        }
        if (!rocks.contains(mod) && !seen[next_steps % 2].contains(c)) {
          seen[next_steps % 2].add(c);
          queue.offer(new Item(next_steps, c));
        }
      }
    }
    throw new IllegalStateException("???");
  }

}
