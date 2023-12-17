package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {

  class Way implements Comparable<Way> {
    Point p;
    int dist;
    char lastDirection;

    public Way(Point p, int dist, char lastDirection) {
      this.p = p;
      this.dist = dist;
      this.lastDirection = lastDirection;
    }

    @Override
    public int compareTo(Way o) {
      int result = Integer.compare(dist, o.dist);
      if (result == 0) {
        result = Character.compare(lastDirection, o.lastDirection);
      }
      if (result == 0) {
        result = Integer.compare(p.x, o.p.x);
      }
      if (result == 0) {
        result = Integer.compare(p.y, o.p.y);
      }
      return result;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Way) {
        Way w = (Way)o;
        return p.equals(w.p) && lastDirection == w.lastDirection;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(p, lastDirection);
    }

    public List<Way> nextPossibleWays(int min, int max) {
      List<Way> result = new LinkedList<>();

      for (char c : "NSWE".toCharArray()) {
        if ((lastDirection == c) || // Don't continue direction
            (lastDirection == 'N' && c == 'S') || //
            (lastDirection == 'S' && c == 'N') || //
            (lastDirection == 'E' && c == 'W') || // and don't reverse
            (lastDirection == 'W' && c == 'E')) {
          continue;
        }

        Point cur = p;
        int d = dist;
        for (int i = 1; i <= max; ++i) {
          cur = cur.add(Point.directionFromChar(c));
          if (cur.x >= 0 && cur.x <= maxX && cur.y >= 0 && cur.y <= maxY) {
            d += getVal(cur);

            if (i >= min) {
              result.add(new Way(cur, d, c));
            }
          }
        }
      }

      return result;
    }
  }

  private List<String> input;
  private int maxX;
  private int maxY;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(767L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(904L, result);
  }

  @Test
  public void testA() {
    assertEquals(102, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(94, solveB(getTestInput()));
  }

  int getVal(Point p) {
    return input.get(p.y).charAt(p.x) - '0';
  }

  private long solve(List<String> lines, int min, int max) {
    input = lines;
    maxX = lines.get(0).length() - 1;
    maxY = lines.size() - 1;

    Point end = new Point(maxX, maxY);
    Map<Way, Integer> visited = new HashMap<>();
    visited.put(new Way(new Point(0, 0), 0, '.'), 0);

    // Dijkstra
    Queue<Way> q = new PriorityQueue<>(visited.keySet());
    while (!q.isEmpty()) {
      Way w = q.poll();

      if (w.p.equals(end)) {
        return w.dist;
      }

      for (Way candidate : w.nextPossibleWays(min, max)) {
        if (candidate.dist < visited.getOrDefault(candidate, Integer.MAX_VALUE)) {
          visited.put(candidate, candidate.dist);
          q.add(candidate);
        }
      }
    }

    throw new IllegalStateException("no way to exit found");
  }

  private long solveA(List<String> lines) {
    return solve(lines, 0, 3);
  }

  private long solveB(List<String> lines) {
    return solve(lines, 4, 10);
  }

}
