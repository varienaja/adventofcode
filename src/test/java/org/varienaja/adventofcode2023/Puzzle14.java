package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {
  private static boolean DEBUG = false;
  private int maxX = 0;
  private int maxY = 0;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(106186L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(106390L, result);
  }

  @Test
  public void testA() {
    assertEquals(136, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(64, solveB(getTestInput()));
  }

  private long calcLoad(Map<Point, Character> world) {
    return world.entrySet().stream() //
        .filter(e -> e.getValue() == 'O') //
        .map(Entry::getKey) //
        .mapToLong(p -> maxY - p.y) //
        .sum();
  }

  private void move(Map<Point, Character> world, Point direction) {
    // Move 'O's as far as they can in the given direction

    int moved = 0;
    do {
      moved = 0;
      for (int y = -direction.y; y < maxY - direction.y; ++y) {
        for (int x = -direction.x; x < maxX - direction.x; ++x) {
          Point p = new Point(x, y);
          if (world.getOrDefault(p, '.') == 'O') {
            Point pp = p.add(direction);
            if (world.getOrDefault(pp, '.') == '.') {
              world.remove(p);
              world.put(pp, 'O');
              ++moved;
            }
          }
        }
      }
      if (DEBUG) {
        print(world);
        System.out.println("moved: " + moved);
      }
    } while (moved != 0);
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

  private long solveA(List<String> lines) {
    Map<Point, Character> world = parse(lines);
    move(world, Point.dNorth);
    return calcLoad(world);
  }

  private long solveB(List<String> lines) {
    int cycles = 1000000000;
    Map<Point, Character> world = parse(lines);
    Map<Map<Point, Character>, Integer> seen2Cycle = new HashMap<>();

    // find cyclic behaviour
    for (int i = 0; i < cycles; ++i) {
      for (char d : "NWSE".toCharArray()) {
        move(world, Point.directionFromChar(d));
      }

      Integer seenCycle = seen2Cycle.put(new HashMap<>(world), i);
      if (seenCycle != null) {
        int endCycle = seenCycle + (cycles - i - 1 /* cycles remaining */) % (i - seenCycle /* cycle length */);

        return seen2Cycle.entrySet().stream() //
            .filter(e -> e.getValue() == endCycle) //
            .mapToLong(e -> calcLoad(e.getKey())) //
            .findFirst() //
            .orElseThrow(); // Cannot happen: if we detect a cycle, the endCycle should be present
      }
    }
    throw new IllegalStateException("Apparently, we waited for the universe to end");
  }

}
