package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {
  private Point startPosition;
  private Point startDirection;
  private Map<Point, Point> turns = Map.of(Point.dNorth, Point.dEast, Point.dEast, Point.dSouth, Point.dSouth, Point.dWest, Point.dWest, Point.dNorth);

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(4722, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(1602, result);
  }

  public Set<Point> parse(List<String> lines) {
    Set<Point> obstacles = new HashSet<>();
    for (int y = 0; y < lines.size(); ++y) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        char c = line.charAt(x);
        if (c == '#') {
          obstacles.add(new Point(x, y));
        } else if (c != '.') {
          startPosition = new Point(x, y);
          startDirection = Point.directionFromChar(c);
        }
      }
    }

    return obstacles;
  }

  @Test
  public void testA() {
    assertEquals(41, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(6, solveB(getTestInput()));
  }

  private Set<Point> solve(Set<Point> obstacles, int maxX, int maxY, Point me, Point myDirection) {
    Map<Point, Set<Point>> seenLocation2Direction = new HashMap<>();
    while (me.x >= 0 && me.x < maxX && me.y >= 0 && me.y < maxY) {
      if (!seenLocation2Direction.compute(me, (k, v) -> v == null ? new HashSet<>() : v).add(myDirection)) {
        return Collections.emptySet(); // Loop detected
      }
      Point next = me.add(myDirection);
      while (obstacles.contains(next)) { // Turn 90° until free again
        myDirection = turns.get(myDirection);
        next = me.add(myDirection);
      }
      me = next;
    }

    return seenLocation2Direction.keySet();
  }

  private long solveA(List<String> lines) {
    return solve(parse(lines), lines.get(0).length(), lines.size(), startPosition, startDirection).size();
  }

  private long solveB(List<String> lines) {
    Set<Point> obstacles = parse(lines);
    int maxX = lines.get(0).length();
    int maxY = lines.size();

    // Put an obstacle at any unused (x,y); Simulate to know if a loop occurs.
    // Speed improvement: only put obstacles at positions that were visited during A.
    Set<Point> visited = solve(parse(lines), lines.get(0).length(), lines.size(), startPosition, startDirection);
    visited.remove(startPosition);

    return visited.stream().parallel().filter((v) -> {
      Set<Point> oo = new HashSet<>(obstacles);
      oo.add(v);
      return solve(oo, maxX, maxY, startPosition, startDirection).isEmpty();
    }).count();
  }

}
