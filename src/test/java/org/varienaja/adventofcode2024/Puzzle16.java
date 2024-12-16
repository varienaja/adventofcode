package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.Point3D;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {

  record QueueElement(Point position, int direction, int cost, List<Point> way) {
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(85432, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(465, result);
  }

  @Test
  public void testA() {
    assertEquals(7036, solveA(getTestInput('a')));
    assertEquals(11048, solveA(getTestInput('b')));
  }

  @Test
  public void testB() {
    assertEquals(45, solveB(getTestInput('a')));
    assertEquals(64, solveB(getTestInput('b')));
  }

  private Point find(Map<Point, Character> world, char c) {
    for (Entry<Point, Character> e : world.entrySet()) {
      if (e.getValue() == c) { // found
        return e.getKey();
      }
    }
    throw new IllegalStateException("No " + c + " in world");
  }

  private Set<QueueElement> findShortestWays(Point from, int direction, Point to, Map<Point, Character> world) {
    // Dijkstra... find route to 'to', going 1 step, or do a costly turn
    Map<Integer, Point> dirc2dir = Map.of(1, Point.dNorth, 2, Point.dSouth, 3, Point.dEast, 4, Point.dWest);

    Map<Point3D, Integer> visited2dist = new HashMap<>();
    Queue<QueueElement> queue = new PriorityQueue<>(Comparator.comparing(qe -> qe.cost));
    queue.offer(new QueueElement(from, direction, 0, List.of(from)));

    Set<QueueElement> result = new HashSet<>();
    int minCost = Integer.MAX_VALUE;

    while (!queue.isEmpty()) {
      QueueElement qe = queue.poll();
      // print(world, qe.way);
      if (qe.position.equals(to)) {
        // print(world, qe.way);
        if (qe.cost <= minCost) {
          minCost = qe.cost;
          result.add(qe);
        } else {
          return result;
        }
      }

      for (Map.Entry<Integer, Point> e : dirc2dir.entrySet()) {
        if ((qe.direction == 1 && e.getKey() == 2) || (qe.direction == 2 && e.getKey() == 1) || (qe.direction == 3 && e.getKey() == 4)
            || (qe.direction == 4 && e.getKey() == 3)) {// n )
          // TODO Don't go back to where you came from... that is stupid //FIXME nicer solution!
          continue;
        }
        List<Point> way = qe.way;
        Point3D nb;
        int cost = qe.cost;
        if (qe.direction == e.getKey()) {
          nb = new Point3D(qe.position.x + e.getValue().x, qe.position.y + e.getValue().y, qe.direction);
          way = new LinkedList<>(qe.way);
          way.add(qe.position.add(e.getValue()));
          cost += 1;
        } else {
          if (world.get(qe.position.add(e.getValue())) == '#') {
            continue; // Don't turn into wall
          }
          nb = new Point3D(qe.position.x, qe.position.y, e.getKey());
          cost += 1000;
        }
        if (cost <= visited2dist.getOrDefault(nb, Integer.MAX_VALUE)) {
          // We've never visited this point in this direction ever, or we did but this one is just as costly or cheaper
          if (world.get(new Point(nb.x, nb.y)) == '.') {
            visited2dist.put(nb, cost);
            queue.offer(new QueueElement(new Point(nb.x, nb.y), e.getKey(), cost, way));
          }
        }
      }
    }
    return Collections.emptySet(); // no way from 'from' to 'to'
  }

  private Set<QueueElement> getShortestWays(List<String> input) {
    Map<Point, Character> world = parseWorld(input);
    Point me = find(world, 'S');
    Point target = find(world, 'E');
    world.put(me, '.');
    world.put(target, '.');
    return findShortestWays(me, 3, target, world);
  }

  private long solveA(List<String> input) {
    return getShortestWays(input).stream().mapToLong(qe -> qe.cost).findFirst().orElseGet(() -> Long.MAX_VALUE);
  }

  private long solveB(List<String> input) {
    return getShortestWays(input).stream().flatMap(qe -> qe.way().stream()).distinct().count();
  }

}
