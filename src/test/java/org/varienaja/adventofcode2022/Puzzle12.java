package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
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

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "Sabqponm", //
        "abcryxxl", //
        "accszExk", //
        "acctuvwj", //
        "abdefghi");
  }

  private Integer findWay(Set<Point> from, Point to, Map<Point, Character> world) {
    // Dijkstra... find route to 'to', going 1 step, and no more than one step up (but more than one down!)
    Map<Point, Integer> visited = from.stream().collect(Collectors.toMap(Function.identity(), p -> 0, (p1, p2) -> p1, HashMap::new));
    Queue<Point> queue = new PriorityQueue<>(Comparator.comparing(visited::get));
    queue.addAll(from);

    while (!queue.isEmpty()) {
      Point p = queue.poll();
      if (p.equals(to)) {
        return visited.get(to);
      }

      char h = world.get(p);
      int dist = visited.get(p) + 1;
      p.getNSWENeighbours().stream() //
          .filter(nb -> !visited.containsKey(nb)) //
          .filter(world::containsKey) //
          .filter(nb -> world.get(nb) - h <= 1) //
          .peek(possible -> visited.put(possible, dist)) //
          .forEach(queue::offer);
    }
    return null; // no way from 'from' to 'to'
  }

  private long solve(List<String> lines, boolean partB) {
    Map<Point, Character> world = new HashMap<>();
    Set<Point> start = new HashSet<>();
    Point target = null;

    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        Point p = new Point(x, y);
        char c = line.charAt(x);
        world.put(p, c);
        if (c == 'S') {
          world.put(p, 'a');
          start.add(p);
        } else if (c == 'E') {
          world.put(p, 'z');
          target = p;
        } else if (partB && c == 'a') {
          start.add(p);
        }
      }
    }

    return findWay(start, target, world);
  }

  private long solveA(List<String> lines) {
    return solve(lines, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, true);
  }

}
