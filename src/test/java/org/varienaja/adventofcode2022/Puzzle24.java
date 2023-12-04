package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {
  private int maxX = 1;
  private int maxY = 1;
  private Point from = null;
  private Point to = null;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(283L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(883L, result);
  }

  @Test
  public void testA() {
    assertEquals(18L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(54, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "#.######", //
        "#>>.<^<#", //
        "#.<..<<#", //
        "#>v.><>#", //
        "#<^v^^>#", //
        "######.#");
  }

  private int findWay(Set<Point> from, Point to, Map<Point, Character> world, int startMinute) {
    int minute = startMinute;
    Set<Point> toAdd = new HashSet<>(from);
    while (!toAdd.isEmpty()) {
      if (toAdd.contains(to)) {
        return minute;
      }
      Set<Point> winds = getWinds(++minute, world);
      Set<Point> toCheck = toAdd;
      toAdd = new HashSet<>(toCheck); // Stay put is a possibility
      toCheck.stream().map(Point::getNSWENeighbours).forEach(toAdd::addAll); // Add all neighbours
      toAdd.removeAll(winds); // But not if not wind
      toAdd.retainAll(world.keySet()); // And within world
    }
    throw new IllegalStateException("Cannot find way out");
  }

  private Set<Point> getWinds(int minute, Map<Point, Character> world) {
    Set<Point> result = new HashSet<>();
    for (Entry<Point, Character> e : world.entrySet()) {
      int dx = 0;
      int dy = 0;
      if (e.getValue() == '>') {
        dx = minute;
      } else if (e.getValue() == '<') {
        dx = -minute;
      } else if (e.getValue() == '^') {
        dy = -minute;
      } else if (e.getValue() == 'v') {
        dy = minute;
      } else {
        continue;
      }

      int newX = (e.getKey().x + dx) % maxX;
      int newY = (e.getKey().y + dy) % maxY;
      result.add(new Point(newX < 0 ? newX + maxX : newX, newY < 0 ? newY + maxY : newY));
    }
    return result;
  }

  private Map<Point, Character> parse(List<String> lines) {
    maxY = lines.size() - 2;
    maxX = lines.get(0).length() - 2;
    Map<Point, Character> world = new HashMap<>();

    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        char c = line.charAt(x);
        if (c != '#') {
          Point p = new Point(x - 1, y - 1); // Shift everything so the winds blow nicely from (0,0) to (maxX,maxY)
          if (y == 0) {
            from = p;
          }
          if (y == lines.size() - 1) {
            to = p;
          }
          world.put(p, c);
        }
      }
    }
    return world;
  }

  private long solveA(List<String> lines) {
    Map<Point, Character> world = parse(lines);
    return findWay(Collections.singleton(from), to, world, 0);
  }

  private long solveB(List<String> lines) {
    Map<Point, Character> world = parse(lines);

    // from - to - from - to
    int time = findWay(Collections.singleton(from), to, world, 0);
    time = findWay(Collections.singleton(to), from, world, time);
    return findWay(Collections.singleton(from), to, world, time);
  }

}
