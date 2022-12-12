package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(437L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(430L, result);
  }

  @Test
  public void testA() {
    assertEquals(31L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(29L, solveB(getTestInput()));
  }

  private Integer findWay(Point from, Point to, Map<Point, Character> world) {
    // Dijkstra... find route to 'to', going 1 step, and no more than one step up (but more than one down!)
    Map<Point, Integer> visited = new HashMap<>();
    visited.put(from, 0);

    Queue<Point> queue = new PriorityQueue<>((p1, p2) -> visited.get(p1) - visited.get(p2));
    queue.add(from);

    while (!queue.isEmpty()) {
      Point p = queue.poll();
      if (p.equals(to)) {
        return visited.get(to);
      }

      if (world.containsKey(p)) {
        char h = world.get(p);

        Set<Point> next = p.getNSWENeighbours().stream() //
            .filter(nb -> !visited.containsKey(nb)) //
            .filter(world::containsKey) //
            .filter(nb -> world.get(nb) - h <= 1) //
            .collect(Collectors.toSet());
        for (Point possible : next) {
          int dist = visited.get(p);
          visited.put(possible, dist + 1);
          queue.offer(possible);
        }
      }
    }
    return null;
  }

  private List<String> getTestInput() {
    return List.of( //
        "Sabqponm", //
        "abcryxxl", //
        "accszExk", //
        "acctuvwj", //
        "abdefghi")// )
    ;
  }

  private long solveA(List<String> lines) {
    Point start = null;
    Point target = null;
    Map<Point, Character> world = new HashMap<>();

    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        Point p = new Point(x, y);
        world.put(p, line.charAt(x));
        if (line.charAt(x) == 'S') {
          world.put(p, 'a');
          start = p;
        } else if (line.charAt(x) == 'E') {
          world.put(p, 'z');
          target = p;
        }
      }
    }

    return findWay(start, target, world);

  }

  private long solveB(List<String> lines) {
    Set<Point> start = new HashSet<>();
    Point target = null;
    Map<Point, Character> world = new HashMap<>();

    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        Point p = new Point(x, y);
        world.put(p, line.charAt(x));
        if (line.charAt(x) == 'S') {
          world.put(p, 'a');
          start.add(p);
        } else if (line.charAt(x) == 'E') {
          world.put(p, 'z');
          target = p;
        } else if (line.charAt(x) == 'a') {
          start.add(p);
        }
      }
    }

    Point tt = target;
    return start.stream().map(s -> findWay(s, tt, world)).filter(Objects::nonNull).mapToInt(i -> i).min().orElseThrow();
  }

}
