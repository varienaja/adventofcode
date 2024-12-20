package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInput(), 100, 2);
    System.out.println(result);
    assertEquals(1485, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInput(), 100, 20);
    System.out.println(result);
    assertEquals(1027501, result);
  }

  @Test
  public void testA() {
    assertEquals(44, solve(getTestInput(), 1, 2));
  }

  @Test
  public void testB() {
    assertEquals(285, solve(getTestInput(), 50, 20));
  }

  private Map<Point, Integer> calcDistances(Point to, Map<Point, Character> world) {
    record QueueElement(Point position, int dist) {
    }

    // DFS, à la Dijkstra: calculates the distances from each reachable point to 'to'
    Map<Point, Integer> visited2dist = new HashMap<>();
    Set<Point> visited = new HashSet<>();
    Queue<QueueElement> queue = new PriorityQueue<>(Comparator.comparing(qe -> qe.dist));
    queue.offer(new QueueElement(to, 0));

    while (!queue.isEmpty()) {
      QueueElement qe = queue.poll();
      visited.add(qe.position);
      visited2dist.put(qe.position, qe.dist);

      for (Point nb : qe.position.getNSWENeighbours()) {
        if (visited.add(nb) && world.get(nb) != '#') {
          queue.offer(new QueueElement(nb, qe.dist + 1));
        }
      }
    }
    return visited2dist;
  }

  private long solve(List<String> input, int minimalCutoff, int maxCheatLength) {
    Map<Point, Character> world = parseWorld(input);
    Point end = find(world, 'E');
    NavigableMap<Point, Integer> cheatlessDistances = new TreeMap<>(Comparator.comparing(p -> p.x * 10000 + p.y));
    cheatlessDistances.putAll(calcDistances(end, world));

    return cheatlessDistances.entrySet().stream().parallel() // parallel gives quite a speedup
        // tailmap gives another 2x speedup
        .mapToLong(e1 -> cheatlessDistances.tailMap(e1.getKey(), false).entrySet().stream().filter(e2 -> {
          int d = e1.getKey().manhattanDistance(e2.getKey());
          return d <= maxCheatLength &&
          // some magic with max and min to make sure we always consider the shortcut (and no detour)
              Math.max(e1.getValue(), e2.getValue()) - d - Math.min(e1.getValue(), e2.getValue()) >= minimalCutoff;
        }).count()).sum();
  }

}
