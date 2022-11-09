package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {
  class Item implements Comparable<Item> {
    int distance;
    int item;
    Point p;

    Item(int d, Point p, int item) {
      this.distance = d;
      this.p = p;
      this.item = item;
    }

    @Override
    public int compareTo(Item o) {
      return Integer.compare(distance, o.distance);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(distance);
      sb.append(" ");
      sb.append(p);
      if (item == NOTHING) {
        sb.append(" NOTHING");
      }
      if (item == TORCH) {
        sb.append(" TORCH");
      }
      if (item == GEAR) {
        sb.append(" CLIMBING_GEAR");
      }
      return sb.toString();
    }

  }

  class Visit {
    Point p;
    int item;

    Visit(Point p, int tool) {
      this.p = p;
      item = tool;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Visit) {
        Visit other = (Visit)o;
        return p.equals(other.p) && item == other.item;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return item * p.hashCode();
    }

  }

  private static final int NOTHING = 1;
  private static final int TORCH = 2;
  private static final int GEAR = 4;

  private Map<Point, Integer> p2Erosion = new HashMap<>();
  private Map<Point, Character> maze = new HashMap<>();

  private int computeErosion(Point p, Point target, int depth) {
    Integer erosion = p2Erosion.get(p);
    if (erosion == null) {
      int geoIndex;
      if ((p.x == 0 && p.y == 0) || (p.x == target.x && p.y == target.y)) {
        geoIndex = 0;
      } else if (p.y == 0) {
        geoIndex = p.x * 16807;
      } else if (p.x == 0) {
        geoIndex = p.y * 48271;
      } else {
        geoIndex = computeErosion(p.getWest(), target, depth) * computeErosion(p.getNorth(), target, depth);
      }
      erosion = (geoIndex + depth) % 20183;
      p2Erosion.put(p, erosion);
    }
    return erosion;
  }

  private Character computeType(Point p, Point target, int depth) {
    return maze.computeIfAbsent(p, (k) -> {
      int rest = computeErosion(p, target, depth) % 3;
      char c = ' ';
      if (rest == 0) {
        c = '.';
      } else if (rest == 1) {
        c = '=';
      } else if (rest == 2) {
        c = '|';
      }
      return c;
    });
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(11810, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(1015, sum); // 1027 too high; 1011 too low, 1020 too high FIXME I must have an error somewhere, I can't
                             // find it.. :-/
  }

  private long findShortestPath(Point start, Point dest, int depth) {
    Map<Visit, Integer> visited = new HashMap<>();
    visited.put(new Visit(start, TORCH), 0);

    Queue<Item> queue = new PriorityQueue<>();
    queue.add(new Item(0, start, TORCH));

    while (true) {
      Item item = queue.poll();
      System.out.println(item);
      if (dest.equals(item.p) && item.item == TORCH) {
        return item.distance;
      }

      for (int tool : new int[] {
          NOTHING, GEAR, TORCH
      }) {
        int cost = item.item == tool ? 1 : 8;
        visitNeigbours(visited, item.distance + cost, item.p, tool, queue, dest, depth);
      }
    }
  }

  private long solveA(List<String> input) {
    int depth = Integer.parseInt(input.get(0).split(" ")[1]);
    String[] xy = input.get(1).split(" ")[1].split(",");
    Point target = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));

    for (int y = 0; y <= target.y; y++) {
      for (int x = 0; x <= target.x; x++) {
        computeType(new Point(x, y), target, depth);
      }
    }

    for (int y = 0; y <= target.y; y++) {
      for (int x = 0; x <= target.x; x++) {
        Point p = new Point(x, y);
        System.out.print(maze.get(p));
      }
      System.out.println();
    }

    long riskLevel = 0;
    for (Character c : maze.values()) {
      if (c == '.') { // Rocky
        riskLevel += 0;
      } else if (c == '=') { // Wet
        riskLevel += 1;
      } else if (c == '|') { // Narrow
        riskLevel += 2;
      }
    }

    return riskLevel;
  }

  private long solveB(List<String> input) {
    int depth = Integer.parseInt(input.get(0).split(" ")[1]);
    String[] xy = input.get(1).split(" ")[1].split(",");
    Point target = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    Point start = new Point(0, 0);

    // Do Dijkstra shortest path with cost-constraints: one move costs 1 minute; change of equipment costs 7 minutes

    // In rocky regions, you can use the climbing gear or the torch. You cannot use neither (you'll likely slip and
    // fall).
    // In wet regions, you can use the climbing gear or neither tool. You cannot use the torch (if it gets wet, you
    // won't have a light source).
    // In narrow regions, you can use the torch or neither tool. You cannot use the climbing gear (it's too bulky to
    // fit).

    // start at (0,0) with torch
    return findShortestPath(start, target, depth);
    // And make a random-access variant of the maze in part A.
  }

  @Test
  public void testA() {
    assertEquals(114, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(45, solveB(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
        "depth: 510", //
        "target: 10,10");
  }

  private void visitNeigbours(Map<Visit, Integer> visited, int distance, Point p, int item, Queue<Item> queue, Point dest, int depth) {
    for (Point nb : p.getNSWENeighbours()) {
      if (nb.x < 0 || nb.y < 0) {
        continue;
      }

      char situation = computeType(nb, dest, depth);
      if (situation == '.' && item == NOTHING) {
        continue;
      }
      if (situation == '=' && item == TORCH) {
        continue;
      }
      if (situation == '|' && item == GEAR) {
        continue;
      }

      Visit v = new Visit(nb, item);
      Integer dist = visited.get(v);
      if (dist != null && dist <= distance) {
        continue;
      }

      visited.put(v, distance);
      queue.add(new Item(distance, nb, item));
    }
  }

}
