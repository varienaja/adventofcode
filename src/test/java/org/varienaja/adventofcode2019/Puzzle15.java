package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {
  private static final boolean DEBUG = false;
  private Point target = null;
  private int minX = 0;
  private int maxX = 0;
  private int minY = 0;
  private int maxY = 0;

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(412, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(418, sum);
  }

  private Map<Point, Character> buildMaze(String input) {
    Point start = new Point(0, 0);
    minX = 0;
    maxX = 0;
    minY = 0;
    maxY = 0;
    Map<Point, Character> world = new HashMap<>();
    Queue<Point> unknown = new LinkedList<>();
    Point here = start;
    world.put(here, '.');

    BlockingQueue<Long> in = new LinkedBlockingDeque<>();
    BlockingQueue<Long> out = new LinkedBlockingDeque<>();
    Intcode.run(input, Map.of(), in, out);
    try {
      while (true) {
        Set<Point> toAdd = new HashSet<>(here.getNSWENeighbours());
        toAdd.removeAll(unknown);
        toAdd.removeAll(world.keySet());

        unknown.addAll(toAdd);
        if (unknown.isEmpty()) {
          break;
        }
        // I am 'here', I want to explore 'unknown', how to get there?
        List<Long> steps = findWay(here, unknown.poll(), world);

        // Go there
        for (Long l : steps) {
          Point oldPosition = here;
          if (l == 1L) {
            here = oldPosition.getNorth();
          } else if (l == 2L) {
            here = oldPosition.getSouth();
          } else if (l == 3L) {
            here = oldPosition.getWest();
          } else if (l == 4L) {
            here = oldPosition.getEast();
          }
          minX = Math.min(here.x, minX);
          maxX = Math.max(here.x, maxX);
          minY = Math.min(here.y, minY);
          maxY = Math.max(here.y, maxY);
          in.offer(l);

          long situation = out.poll(100, TimeUnit.MILLISECONDS);
          if (situation == 0L) { // Wall hit
            world.put(here, '#');
            here = oldPosition;
          } else if (situation == 2L) { // Target hit
            world.put(here, '.');
            target = here;
          } else {
            world.put(here, '.');
          }
        }

        if (DEBUG) {
          for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
              Point p = new Point(x, y);
              if (here.equals(p)) {
                System.out.print('D');
              } else {
                System.out.print(world.getOrDefault(p, ' '));
              }
            }
            System.out.println();
          }
          System.out.println(here);
          System.out.println(unknown);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return world;
  }

  private List<Long> findWay(Point from, Point to, Map<Point, Character> world) {
    // Dijkstra... find route to 'to', using dots
    Map<Point, List<Long>> visited = new HashMap<>();
    visited.put(from, new LinkedList<>());

    Queue<Point> queue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.manhattanDistance(to), p2.manhattanDistance(to)));
    queue.add(from);

    while (true) {
      Point p = queue.poll();
      if (p.equals(to)) {
        return visited.get(to);
      }

      if (world.containsKey(p)) {
        // I can have an unexplored point at the end,
        // but not in the middle of the road to 'to'
        Set<Point> next = p.getNSWENeighbours().stream() //
            .filter(nb -> !visited.containsKey(nb)) //
            .filter(nb -> world.getOrDefault(nb, '.') == '.') //
            .collect(Collectors.toSet());
        for (Point possible : next) {
          long go = 0L;
          if (possible.equals(p.getEast())) {
            go = 4L;
          } else if (possible.equals(p.getWest())) {
            go = 3L;
          } else if (possible.equals(p.getSouth())) {
            go = 2L;
          } else if (possible.equals(p.getNorth())) {
            go = 1L;
          }
          List<Long> road = new LinkedList<>(visited.get(p));
          road.add(go);
          visited.put(possible, road);
          queue.offer(possible);
        }
      }
    }
  }

  private long flood(Map<Point, Character> world, Point target) {
    long minutes = 0;

    Set<Point> visited = new HashSet<>();
    Queue<Set<Point>> queue = new LinkedList<>();
    world.put(target, ' ');
    queue.add(Collections.singleton(target));
    while (!queue.isEmpty()) {
      Set<Point> pts = queue.poll();
      if (pts.isEmpty()) {
        break;
      }

      Set<Point> nxt = new HashSet<>();
      for (Point p : pts) {
        Set<Point> next = p.getNSWENeighbours().stream() //
            .filter(nb -> !visited.contains(nb)) //
            .filter(nb -> world.get(nb) == '.') //
            .collect(Collectors.toSet());
        for (Point possible : next) {
          visited.add(possible);
          nxt.add(possible);
          world.put(possible, ' ');
        }
      }
      if (!nxt.isEmpty()) {
        queue.add(nxt);
        minutes++;
      }

      if (DEBUG) {
        print(world);
      }
    }

    return minutes;
  }

  private long solveA(String input) {
    Map<Point, Character> world = buildMaze(input);
    return findWay(new Point(0, 0), target, world).size();
  }

  private long solveB(String input) {
    Map<Point, Character> world = buildMaze(input);
    return flood(world, target);
  }

}
