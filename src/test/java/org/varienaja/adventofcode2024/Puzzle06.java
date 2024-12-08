package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
public class Puzzle06 extends PuzzleAbs {
  private Point startPosition;
  private Point startDirection;
  private Map<Point, Point> turns = Map.of(Point.dNorth, Point.dEast, Point.dEast, Point.dSouth, Point.dSouth, Point.dWest, Point.dWest, Point.dNorth);
  private Map<Point, Integer> direction2nr = Map.of(Point.dNorth, 0, Point.dEast, 1, Point.dSouth, 2, Point.dWest, 3);
  private Map<Integer, Point> nr2direction = Map.of(0, Point.dNorth, 1, Point.dEast, 2, Point.dSouth, 3, Point.dWest);

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

  @Test
  public void testA() {
    assertEquals(41, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(6, solveB(getTestInput()));
  }

  private Set<Point> parse(List<String> lines) {
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

  private Set<Point3D> solve(Set<Point> obstacles, int maxX, int maxY, Point me, Point myDirection) {
    Set<Point> visited = new HashSet<>();
    Set<Point3D> seenLocationsAndDirections = new HashSet<>();
    while (me.x >= 0 && me.x < maxX && me.y >= 0 && me.y < maxY) {
      if (visited.add(me)) {
        seenLocationsAndDirections.add(new Point3D(me.x, me.y, direction2nr.get(myDirection)));
      }
      Point next = me.add(myDirection); // Move
      if (obstacles.contains(next)) { // We hit an obstacle
        myDirection = turns.get(myDirection); // Turn 90°
        next = me; // Undo move
      } else {
        next = me.add(myDirection); // We could move
      }
      me = next;
    }

    return seenLocationsAndDirections;
  }

  private long solveA(List<String> lines) {
    return solve(parse(lines), lines.get(0).length(), lines.size(), startPosition, startDirection).size();
  }

  private long solveB(List<String> lines) {
    int maxX = lines.get(0).length();
    int maxY = lines.size();

    // Store positions of obstacles in a sorted Set per row and column, so we can quickly lookup the the next obstacle
    // in any direction. Credits to https://github.com/maksverver/AdventOfCode/blob/master/2024/day06/solve.cc
    List<SortedSet<Integer>> hObstacles = new ArrayList<>(maxY);
    List<SortedSet<Integer>> vObstacles = new ArrayList<>(maxX);
    for (int y = 0; y < maxY; ++y) {
      String line = lines.get(y);
      for (int x = 0; x < maxX; ++x) {
        if (line.charAt(x) == '#') {
          while (hObstacles.size() <= y) {
            hObstacles.add(new TreeSet<>());
          }
          hObstacles.get(y).add(x);

          while (vObstacles.size() <= x) {
            vObstacles.add(new TreeSet<>());
          }
          vObstacles.get(x).add(y);
        }
      }
    }

    Set<Point3D> seenLocationsAndDirections = solve(parse(lines), lines.get(0).length(), lines.size(), startPosition, startDirection);
    seenLocationsAndDirections.remove(new Point3D(startPosition.x, startPosition.y, direction2nr.get(startDirection)));

    long result = 0;
    Set<Point3D> seen = new HashSet<>();

    for (Point3D blocked : seenLocationsAndDirections) {
      hObstacles.get(blocked.y).add(blocked.x);
      vObstacles.get(blocked.x).add(blocked.y);
      Point myDirection = nr2direction.get(blocked.z);
      // Calc previous position of myself just before I entered 'blocked' (==180° against my direction at that time)
      Point previous = turns.get(turns.get(myDirection));
      Point me = new Point(blocked.x, blocked.y).add(previous);

      while (true) {
        if (!seen.add(new Point3D(me.x, me.y, direction2nr.get(myDirection)))) { // Loop detected
          result++;
          break;
        }

        if (Point.dNorth.equals(myDirection)) {
          SortedSet<Integer> s = vObstacles.get(me.x).headSet(me.y);
          if (s.isEmpty()) {
            break; // We left the maze
          }
          me = new Point(me.x, s.last() + 1);
        } else if (Point.dEast.equals(myDirection)) {
          SortedSet<Integer> s = hObstacles.get(me.y).tailSet(me.x);
          if (s.isEmpty()) {
            break; // We left the maze
          }
          me = new Point(s.first() - 1, me.y);
        } else if (Point.dSouth.equals(myDirection)) {
          SortedSet<Integer> s = vObstacles.get(me.x).tailSet(me.y);
          if (s.isEmpty()) {
            break; // We left the maze
          }
          me = new Point(me.x, s.first() - 1);
        } else if (Point.dWest.equals(myDirection)) { // dWest
          SortedSet<Integer> s = hObstacles.get(me.y).headSet(me.x);
          if (s.isEmpty()) {
            break; // We left the maze
          }
          me = new Point(s.last() + 1, me.y);
        }

        myDirection = turns.get(myDirection);
      }

      hObstacles.get(blocked.y).remove(blocked.x);
      vObstacles.get(blocked.x).remove(blocked.y);
      seen.clear();
    }

    return result;
  }

}
