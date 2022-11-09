package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(28772955L, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput(), 200);
    System.out.println(sum);
    assertEquals(2023, sum);
  }

  private int getBugCount(Map<Integer, Map<Point, Character>> worlds, int level, Point p) {
    int bugCount = 0;
    Set<Point> nbs = p.getNSWENeighbours();
    Map<Point, Character> lowerWorld = worlds.getOrDefault(level - 1, new HashMap<>());
    Map<Point, Character> upperWorld = worlds.getOrDefault(level + 1, new HashMap<>());
    for (Point nb : nbs) {
      if (nb.x == -1) { // is level -1, 1,2 a bug?
        if (lowerWorld.getOrDefault(new Point(1, 2), '.') == '#') {
          bugCount++;
        }
      } else if (nb.x == 5) { // is level -1, 3,2 a bug?
        if (lowerWorld.getOrDefault(new Point(3, 2), '.') == '#') {
          bugCount++;
        }
      } else if (nb.y == -1) { // is level -1, 2,1 a bug?
        if (lowerWorld.getOrDefault(new Point(2, 1), '.') == '#') {
          bugCount++;
        }
      } else if (nb.y == 5) { // is level -1, 2,3 a bug?
        if (lowerWorld.getOrDefault(new Point(2, 3), '.') == '#') {
          bugCount++;
        }
      } else if (nb.x == 2 && nb.y == 2) {
        if (p.getNorth().equals(nb)) { // are level +1, count x=0..4,4 bugs?
          for (int x = 0; x < 5; x++) {
            if (upperWorld.getOrDefault(new Point(x, 4), '.') == '#') {
              bugCount++;
            }
          }
        } else if (p.getWest().equals(nb)) { // are level +1, count x=0,y=0..4 bugs?
          for (int y = 0; y < 5; y++) {
            if (upperWorld.getOrDefault(new Point(4, y), '.') == '#') {
              bugCount++;
            }
          }
        } else if (p.getEast().equals(nb)) {// are level +1, count x=4,y=0..4 bugs?
          for (int y = 0; y < 5; y++) {
            if (upperWorld.getOrDefault(new Point(0, y), '.') == '#') {
              bugCount++;
            }
          }
        } else if (p.getSouth().equals(nb)) {// are level +1, count x=0..4,0 bugs?
          for (int x = 0; x < 5; x++) {
            if (upperWorld.getOrDefault(new Point(x, 0), '.') == '#') {
              bugCount++;
            }
          }
        }
      } else {
        if (worlds.getOrDefault(level, new HashMap<>()).getOrDefault(nb, '.') == '#') {
          bugCount++;
        }
      }
    }

    return bugCount;
  }

  private long solveA(List<String> input) {
    Set<String> seen = new HashSet<>();

    Map<Point, Character> world = new HashMap<>();
    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        char c = line.charAt(x);
        if (c == '#') {
          world.put(new Point(x, y), c);
        }
      }
    }

    while (true) {
      Map<Point, Character> newWorld = new HashMap<>();
      StringBuilder sb = new StringBuilder();
      for (int y = 0; y < 5; y++) {
        for (int x = 0; x < 5; x++) {
          Point p = new Point(x, y);
          char c = world.getOrDefault(p, '.');
          int bugCount = 0;
          for (Point candidate : p.getNSWENeighbours()) {
            char nb = world.getOrDefault(candidate, '.');
            if (nb == '#') {
              bugCount++;
            }
          }

          char newC;
          if (c == '#') { // A bug dies, unless there is exactly one bug adjacent to it
            newC = bugCount == 1 ? c : '.';
          } else { // An empty space becomes infested with a bug if exactly one or two bugs are adjacent to it.
            newC = (bugCount == 1 || bugCount == 2) ? '#' : c;
          }
          if (newC == '#') {
            newWorld.put(p, newC);
          }
          sb.append(newC);
        }
        sb.append("\n");
      }
      String state = sb.toString();
      // System.out.println(state);
      world = newWorld;

      if (!seen.add(state)) {
        // Calc biodiversity
        long result = 0;
        int pow = 1;
        for (int y = 0; y < 5; y++) {
          for (int x = 0; x < 5; x++) {
            if (world.getOrDefault(new Point(x, y), '.') == '#') {
              result += pow;
            }
            pow <<= 1;
          }
        }

        return result;
      }
    }
  }

  private long solveB(List<String> input, int minutes) {
    Map<Integer, Map<Point, Character>> worlds = new HashMap<>();
    Map<Point, Character> world = new HashMap<>();
    worlds.put(0, world);

    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        char c = line.charAt(x);
        if (c == '#') {
          world.put(new Point(x, y), c);
        }
      }
    }

    for (int m = 0; m < minutes; m++) {
      Map<Integer, Map<Point, Character>> newWorlds = new HashMap<>();

      NavigableSet<Integer> levels = new TreeSet<>(worlds.keySet());
      levels.add(levels.first() - 1);
      levels.add(levels.last() + 1);

      for (Integer level : levels) {
        world = worlds.getOrDefault(level, new HashMap<>());

        Map<Point, Character> newWorld = new HashMap<>();
        for (int y = 0; y < 5; y++) {
          for (int x = 0; x < 5; x++) {
            Point p = new Point(x, y);
            char c = world.getOrDefault(p, '.');
            int bugCount = getBugCount(worlds, level, p);

            char newC;
            if (c == '#') { // A bug dies, unless there is exactly one bug adjacent to it
              newC = bugCount == 1 ? c : '.';
            } else { // An empty space becomes infested with a bug if exactly one or two bugs are adjacent to it.
              newC = (bugCount == 1 || bugCount == 2) ? '#' : c;
            }
            if (newC == '#' && !new Point(2, 2).equals(p)) {
              newWorld.put(p, newC);
            }
          }
        }
        newWorlds.put(level, newWorld);
      }

      worlds = newWorlds;
    }

    return worlds.values().stream().map(w -> w.size()).reduce((a, b) -> a + b).orElse(-1);
  }

  @Test
  public void testA() {
    assertEquals(2129920L, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(99, solveB(testInput(), 10));
  }

  private List<String> testInput() {
    return List.of( //
        "....#", //
        "#..#.", //
        "#..##", //
        "..#..", //
        "#....");
  }

}
