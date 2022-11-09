package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {
  private Map<Point, Set<Point>> maze;
  private Point currentPosition;

  /**
   * If I go from (0,0) to the farthest point, how many steps did I take?
   *
   * @param partB whether to do the calculation for partB: How many rooms have a shortest path from your current
   *          location that pass through at least 1000 doors?
   * @return maximum amount of steps, possible from (0,0)
   */
  private long calcMazeSize(boolean partB) {
    long stepCount = 0;
    Map<Long, Set<Point>> steps2Reached = new HashMap<>();
    steps2Reached.put(stepCount, Set.of(new Point(0, 0)));
    Set<Point> toVisit = new HashSet<>(maze.keySet());
    for (Set<Point> nbs : maze.values()) {
      toVisit.addAll(nbs);
    }

    while (!toVisit.isEmpty()) {
      Set<Point> reachableInThisStep = new HashSet<>();
      for (Point p : steps2Reached.get(stepCount)) {
        toVisit.remove(p);
        Set<Point> nbs = maze.get(p);
        if (nbs != null) {
          for (Point candidate : p.getNSWENeighbours()) {
            if (nbs.contains(candidate) && toVisit.contains(candidate)) {
              reachableInThisStep.add(candidate);
            }
          }
        }
      }
      if (!reachableInThisStep.isEmpty()) {
        stepCount++;
        steps2Reached.put(stepCount, reachableInThisStep);
      }
    }

    if (partB) {
      long roomCount = 0;
      for (Entry<Long, Set<Point>> e : steps2Reached.entrySet()) {
        if (e.getKey() >= 1000) {
          roomCount += e.getValue().size();
        }
      }
      return roomCount;
    }

    return stepCount;
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(4274, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(8547, sum);
  }

  void parseRegex(char[] regex, AtomicInteger ix) {
    Point startPosition = currentPosition;
    Point np;

    while (regex[ix.get()] != '$') {
      switch (regex[ix.getAndIncrement()]) {
        case 'N':
          np = currentPosition.getNorth();
          maze.compute(currentPosition, (k, v) -> v == null ? v = new HashSet<>() : v).add(np);
          currentPosition = np;
          break;
        case 'S':
          np = currentPosition.getSouth();
          maze.compute(currentPosition, (k, v) -> v == null ? v = new HashSet<>() : v).add(np);
          currentPosition = np;
          break;
        case 'W':
          np = currentPosition.getWest();
          maze.compute(currentPosition, (k, v) -> v == null ? v = new HashSet<>() : v).add(np);
          currentPosition = np;
          break;
        case 'E':
          np = currentPosition.getEast();
          maze.compute(currentPosition, (k, v) -> v == null ? v = new HashSet<>() : v).add(np);
          currentPosition = np;
          break;
        case '(':
          Point myPosition = currentPosition;
          parseRegex(regex, ix);
          currentPosition = myPosition;
          break;
        case '|':
          currentPosition = startPosition;
          break;
        case ')':
          return;
      }
    }
  }

  private long solve(String input, boolean partB) {
    maze = new HashMap<>();
    currentPosition = new Point(0, 0);
    char[] regex = input.toCharArray();

    parseRegex(regex, new AtomicInteger(1));

    return calcMazeSize(partB);
  }

  private long solveA(String input) {
    return solve(input, false);
  }

  private long solveB(String input) {
    return solve(input, true);
  }

  @Test
  public void testA() {
    assertEquals(3, solveA(testInput1()));
    assertEquals(10, solveA(testInput2()));
    assertEquals(18, solveA(testInput3()));
  }

  @Test
  public void testB() {

  }

  private String testInput1() {
    return "^WNE$";
  }

  private String testInput2() {
    return "^ENWWW(NEEE|SSE(EE|N))$";
  }

  private String testInput3() {
    return "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$";
  }

}
