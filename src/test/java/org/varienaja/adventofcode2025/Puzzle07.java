package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1717L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(231507396180012L, result);
  }

  @Test
  public void testA() {
    assertEquals(21L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(40L, solveB(getTestInput()));
  }

  private long solveA(List<String> lines) {
    long splits = 0L;
    Map<Point, Character> world = parseWorld(lines);

    Queue<Point> heads = new LinkedList<>();
    heads.offer(world.entrySet().stream().filter(e -> e.getValue().equals('S')).findFirst().get().getKey());
    while (!heads.isEmpty()) {
      Queue<Point> todo = new LinkedList<>(heads);
      heads.clear();
      Set<Point> toAdd = new HashSet<>();
      for (Point current : todo) {
        Point next = current.getSouth();
        Character c = world.get(next);
        if (c == null) {
          // Reached bottom
        } else {
          if (c.equals('.')) {
            toAdd.add(next);
          } else if (c.equals('^')) {
            ++splits;
            toAdd.add(next.getEast());
            toAdd.add(next.getWest());
          }
        }
      }
      heads.addAll(toAdd);
    }

    return splits;
  }

  private long solveB(List<String> lines) {
    Map<Point, Character> world = parseWorld(lines);

    Map<Point, Long> head2ways = new HashMap<>();
    head2ways.put(world.entrySet().stream().filter(e -> e.getValue().equals('S')).findFirst().get().getKey(), 1L);
    while (!head2ways.isEmpty()) {
      Map<Point, Long> todo = new HashMap<>(head2ways);
      head2ways.clear();
      for (Entry<Point, Long> current : todo.entrySet()) {
        Point next = current.getKey().getSouth();
        Character c = world.get(next);
        if (c == null) {
          return todo.values().stream().mapToLong(l -> l).sum(); // Reached bottom
        }
        if (c.equals('.')) {
          head2ways.merge(next, current.getValue(), Long::sum);
        } else if (c.equals('^')) {
          head2ways.merge(next.getEast(), current.getValue(), Long::sum);
          head2ways.merge(next.getWest(), current.getValue(), Long::sum);
        }
      }
    }

    throw new IllegalStateException("");
  }

}
