package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput(), 1024, 70);
    System.out.println(result);
    assertEquals(272, result);
  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getInput(), 70);
    System.out.println(result);
    assertEquals("16,44", result);
  }

  @Test
  public void testA() {
    assertEquals(22, solveA(getTestInput(), 12, 6));
  }

  @Test
  public void testB() {
    assertEquals("6,1", solveB(getTestInput(), 6));
  }

  private long findShortestWay(Point from, Point to, Map<Point, Character> world) {
    record QueueElement(Point position, int dist) {
    }

    // Dijkstra... find route to 'to'
    Map<Point, Integer> visited2dist = new HashMap<>();
    Queue<QueueElement> queue = new PriorityQueue<>(Comparator.comparing(qe -> qe.dist));
    queue.offer(new QueueElement(from, 0));

    while (!queue.isEmpty()) {
      QueueElement qe = queue.poll();
      if (qe.position.equals(to)) {
        return qe.dist;
      }

      for (Point nb : qe.position.getNSWENeighbours()) {
        if (nb.x >= 0 && nb.x <= to.x && nb.y >= 0 && nb.y <= to.y) {
          if (!visited2dist.containsKey(nb)) {
            visited2dist.put(nb, qe.dist + 1); // Never try this point again (prevent equal objects in queue)
            if (world.getOrDefault(nb, '.') == '.') {
              queue.offer(new QueueElement(nb, qe.dist + 1));
            }
          }
        }
      }
    }
    return -1; // no way from 'from' to 'to'
  }

  private long solveA(List<String> input, int count, int target) {
    Map<Point, Character> world = new HashMap<>();
    for (String line : input) {
      String[] parts = line.split(",");
      world.put(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])), '#');
      if (world.size() == count) {
        break;
      }
    }

    return findShortestWay(new Point(0, 0), new Point(target, target), world);
  }

  private String solveB(List<String> input, int target) {
    int min = 1;
    int max = input.size() - 1;

    while (min != max) {
      int pivot = (max + min) / 2;
      if (solveA(input, pivot, target) == -1) {
        max = pivot;
      } else {
        min = pivot + 1;
      }
    }

    return input.get(min - 1);
  }

}
