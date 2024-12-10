package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(786, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(1722, result);
  }

  @Test
  public void testA() {
    assertEquals(36, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(81, solveB(getTestInput()));
  }

  private long solve(List<String> lines, BiFunction<Point, Collection<Point>, Collection<Point>> remapper) {
    Map<Point, Character> world = parseWorld(lines);
    Queue<Map.Entry<Point, Point>> trails = world.entrySet().stream() //
        .filter(e -> e.getValue() == '0') // gather all starting points
        .map(e -> Map.entry(e.getKey(), e.getKey())) // as Map.Entry<currentPosition, startPosition>
        .collect(Collectors.toCollection(LinkedList::new));

    Map<Point, Collection<Point>> trailHead2trailEnd = new HashMap<>();
    while (!trails.isEmpty()) {
      Point currentPos = trails.peek().getKey();
      Point startPos = trails.poll().getValue();
      char height = world.get(currentPos);
      if (height == '9') { // Reached a 9: add to possible end points
        trailHead2trailEnd.compute(startPos, remapper).add(currentPos);
      } else { // Grow existing possible trails by each reachable next step
        currentPos.getNSWENeighbours().stream() //
            .filter(nb -> world.getOrDefault(nb, '.') == height + 1) //
            .forEach(nb -> trails.offer(Map.entry(nb, startPos)));
      }
    }

    return trailHead2trailEnd.values().stream().mapToLong(ways -> ways.size()).sum();
  }

  private long solveA(List<String> lines) {
    return solve(lines, (k, v) -> v == null ? new HashSet<>() : v);
  }

  private long solveB(List<String> lines) {
    return solve(lines, (k, v) -> v == null ? new LinkedList<>() : v);
  }

}
