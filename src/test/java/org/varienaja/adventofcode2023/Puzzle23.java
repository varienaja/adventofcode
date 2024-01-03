package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
public class Puzzle23 extends PuzzleAbs {
  record To(Point p, int distance) {
  }

  private Map<Point, Set<To>> from2tos;
  private Point start = new Point(1, 0);
  private Point goal = null;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(2238L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(6398L, result);
  }

  @Test
  public void testA() {
    assertEquals(94, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(154, solveB(getTestInput()));
  }

  private void calcDistances(Map<Point, Character> world, boolean partB) {
    Map<Point, Set<Point>> f2t = new LinkedHashMap<>();
    for (Point p : world.keySet()) {
      char m = world.get(p);
      for (Point candidate : p.getNSWENeighbours()) {
        if (!partB && "v<>^".indexOf(m) >= 0) { // check
          if (p.add(Point.dWest).equals(candidate) && m != '<') {
            continue;
          }
          if (p.add(Point.dSouth).equals(candidate) && m != 'v') {
            continue;
          }
          if (p.add(Point.dEast).equals(candidate) && m != '>') {
            continue;
          }
          if (p.add(Point.dNorth).equals(candidate) && m != '^') {
            continue;
          }
        }
        if (world.getOrDefault(candidate, '#') != '#') {
          f2t.compute(p, (k, v) -> v == null ? new HashSet<>() : v).add(candidate);
        }
      }
    }

    from2tos = new LinkedHashMap<>();
    Queue<Point> toCheck = new LinkedList<>(world.keySet());
    while (!toCheck.isEmpty()) {
      Point p = toCheck.poll();
      for (Point n : f2t.get(p)) {
        from2tos.compute(p, (k, v) -> v == null ? new HashSet<>() : v).add(compact(toCheck, f2t, p, n, 1));
      }
    }
  }

  private To compact(Queue<Point> toCheck, Map<Point, Set<Point>> e, Point p, Point n, int d) {
    while (e.get(n).size() == 2) {
      Set<Point> pts = new HashSet<>(e.get(n));
      pts.remove(p);
      toCheck.remove(p);
      p = n;
      n = pts.iterator().next();
      ++d;
    }
    return new To(n, d);
  }

  private void parse(List<String> lines, boolean partB) {
    Map<Point, Character> world = new LinkedHashMap<>();
    for (int y = 0; y < lines.size(); ++y) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        char c = line.charAt(x);
        if (c != '#') {
          world.put(new Point(x, y), c);
        }
      }
    }

    goal = new Point(lines.get(0).length() - 2, lines.size() - 1);

    calcDistances(world, partB);
  }

  private int solve(Point node, int dist, int best, Set<Point> seen) {
    if (node.equals(goal)) {
      return dist;
    }
    if (seen.contains(node)) {
      return best;
    }

    seen.add(node);
    for (To to : from2tos.get(node)) {
      best = Math.max(best, solve(to.p, to.distance + dist, best, seen));
    }
    seen.remove(node);

    return best;
  }

  private long solveA(List<String> lines) {
    parse(lines, false);
    return solve(start, 0, 0, new HashSet<>());
  }

  private long solveB(List<String> lines) {
    parse(lines, true);
    return solve(start, 0, 0, new HashSet<>());
  }

}
