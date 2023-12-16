package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {
  private int maxX;
  private int maxY;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(7210L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(7673L, result);
  }

  @Test
  public void testA() {
    assertEquals(46, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(51, solveB(getTestInput()));
  }

  private void offer(Queue<Point[]> q, Set<String> seen, Point pos, Point direction) {
    Point next = pos.add(direction);
    if (!seen.contains("" + next + direction) && //
        next.x >= 0 && next.x < maxX && //
        next.y >= 0 && next.y < maxY) {
      q.add(new Point[] {
          next, direction
      });
    }
  }

  private Map<Point, Character> parse(List<String> lines) {
    Map<Point, Character> world = new HashMap<>();
    for (int y = 0; y < lines.size(); ++y) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        char c = line.charAt(x);
        if (c != '.') {
          world.put(new Point(x, y), c);
        }
      }
    }

    maxX = lines.get(0).length();
    maxY = lines.size();

    return world;
  }

  private long solve(Map<Point, Character> world, Point start, Point direction) {
    Set<String> seen = new HashSet<>();
    Set<Point> energized = new HashSet<>();
    Queue<Point[]> beamFrontAndDirection = new LinkedList<>();
    beamFrontAndDirection.add(new Point[] {
        start, direction
    });

    while (!beamFrontAndDirection.isEmpty()) {
      Point[] current = beamFrontAndDirection.poll();
      Point pos = current[0];
      Point dir = current[1];
      energized.add(pos);
      seen.add("" + pos + dir);

      // Calc new fronts
      char c = world.getOrDefault(pos, '.');
      Set<Point> directionsToAdd = Set.of(dir);
      if (c == '-') {
        if (Point.dNorth.equals(dir) || Point.dSouth.equals(dir)) {
          directionsToAdd = Set.of(Point.dWest, Point.dEast);
        }
      } else if (c == '|') {
        if (Point.dWest.equals(dir) || Point.dEast.equals(dir)) {
          directionsToAdd = Set.of(Point.dNorth, Point.dSouth);
        }
      } else if (c == '/') {
        if (Point.dNorth.equals(dir)) {
          directionsToAdd = Set.of(Point.dEast);
        } else if (Point.dEast.equals(dir)) {
          directionsToAdd = Set.of(Point.dNorth);
        } else if (Point.dSouth.equals(dir)) {
          directionsToAdd = Set.of(Point.dWest);
        } else if (Point.dWest.equals(dir)) {
          directionsToAdd = Set.of(Point.dSouth);
        }
      } else if (c == '\\') {
        if (Point.dNorth.equals(dir)) {
          directionsToAdd = Set.of(Point.dWest);
        } else if (Point.dEast.equals(dir)) {
          directionsToAdd = Set.of(Point.dSouth);
        } else if (Point.dSouth.equals(dir)) {
          directionsToAdd = Set.of(Point.dEast);
        } else if (Point.dWest.equals(dir)) {
          directionsToAdd = Set.of(Point.dNorth);
        }
      }
      directionsToAdd.stream().forEach(d -> offer(beamFrontAndDirection, seen, pos, d));
    }

    return energized.size();
  }

  private long solveA(List<String> lines) {
    return solve(parse(lines), new Point(0, 0), Point.dEast);
  }

  private long solveB(List<String> lines) {
    Map<Point, Character> world = parse(lines);

    Map<Point, Point> start2direction = new HashMap<>();
    for (int x = 0; x <= maxX; ++x) {
      start2direction.put(new Point(x, 0), Point.dSouth);
      start2direction.put(new Point(x, maxY), Point.dNorth);
    }
    for (int y = 0; y <= maxY; ++y) {
      start2direction.put(new Point(0, y), Point.dEast);
      start2direction.put(new Point(maxX, y), Point.dWest);
    }

    return start2direction.entrySet().stream().parallel() //
        .mapToLong(e -> solve(world, e.getKey(), e.getValue())) //
        .max().orElseThrow();
  }

}
