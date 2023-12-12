package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class Puzzle10 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(6725L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(383L, result);
  }

  @Test
  public void testA() {
    assertEquals(4, solveA(getTestInput('a')));
    assertEquals(8, solveA(getTestInput('b')));
  }

  @Test
  public void testB() {
    assertEquals(4, solveB(getTestInput('c')));
    assertEquals(8, solveB(getTestInput('d')));
    assertEquals(10, solveB(getTestInput('e')));
  }

  private Map<Point, Character> retrieveBorder(List<String> lines) {
    Map<Point, Character> point2dist = new HashMap<>();

    Point start = null;
    Map<Point, Character> current = new HashMap<>();
    Map<Point, Character> world = new HashMap<>();
    for (int y = 0; y < lines.size(); ++y) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        char c = line.charAt(x);
        if (c == 'S') {
          start = new Point(x, y);
        }
        world.put(new Point(x, y), c);
      }
    }

    long distance = 0L;

    current.put(start, 'S');
    point2dist.put(start, 'S');
    Set<Point> seen = new HashSet<>();
    //
    // | is a vertical pipe connecting north and south.
    // - is a horizontal pipe connecting east and west.
    // L is a 90-degree bend connecting north and east.
    // J is a 90-degree bend connecting north and west.
    // 7 is a 90-degree bend connecting south and west.
    // F is a 90-degree bend connecting south and east.

    // How far until we meet at the other end of the loop?
    do {
      Map<Point, Character> nbs = new HashMap<>();
      Iterator<Point> it = current.keySet().iterator();
      while (it.hasNext()) {
        Point here = it.next();
        seen.add(here);
        char c = world.get(here);

        it.remove();

        // FIXME crap code below can certainly be nicer
        char n = world.getOrDefault(here.getNorth(), '.');
        if (Set.of('S', '|', 'L', 'J').contains(c)) {
          if (Set.of('|', 'F', '7').contains(n)) {
            nbs.put(here.getNorth(), n);
          }
        }

        char w = world.getOrDefault(here.getWest(), '.');
        if (Set.of('S', '-', 'J', '7').contains(c)) {
          if (Set.of('-', 'L', 'F').contains(w)) {
            nbs.put(here.getWest(), w);
          }
        }

        char s = world.getOrDefault(here.getSouth(), '.');
        if (Set.of('S', '|', 'F', '7').contains(c)) {
          if (Set.of('|', 'J', 'L').contains(s)) {
            nbs.put(here.getSouth(), s);
          }
        }

        char e = world.getOrDefault(here.getEast(), '.');
        if (Set.of('S', '-', 'L', 'F').contains(c)) {
          if (Set.of('-', 'J', '7').contains(e)) {
            nbs.put(here.getEast(), e);
          }
        }

        if (distance == 0) { // We have the nbs of 'S'; check what S should be
          char ss = '.';
          if ("|7F".indexOf(n) >= 0 && "|LJ".indexOf(s) >= 0) {
            ss = '|';
          } else if ("|7F".indexOf(n) >= 0 && "-7J".indexOf(w) >= 0) {
            ss = 'L';
          } else if ("|7F".indexOf(n) >= 0 && "-LF".indexOf(e) >= 0) {
            ss = 'J';
          } else if ("|LJ".indexOf(s) >= 0 && "-7J".indexOf(e) >= 0) {
            ss = 'F';
          } else if ("|7F".indexOf(s) >= 0 && "-LF".indexOf(w) >= 0) {
            ss = '7';
          } else if ("-LF".indexOf(w) >= 0 && "-7J".indexOf(e) >= 0) {
            ss = '-';
          }
          point2dist.put(start, ss);
        }
      }
      nbs.keySet().removeAll(seen);
      if (!nbs.isEmpty()) {
        distance++;
        point2dist.putAll(nbs);
      }
      current = nbs;
    } while (!current.isEmpty());
    return point2dist;
  }

  private long solveA(List<String> lines) {
    return retrieveBorder(lines).size() / 2;
  }

  private long solveB(List<String> lines) {
    Map<Point, Character> borderPoints = retrieveBorder(lines);

    // iterate over world, count my line-crossings, when odd nr: count dots
    long area = 0L;

    for (int y = 0; y < lines.size(); ++y) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        if (!borderPoints.containsKey(new Point(x, y))) {

          int crossings = 0;
          for (int i = 1; i < Math.min(x, y) + 1; ++i) {
            Point tester = new Point(x - i, y - i);
            Character b = borderPoints.get(tester);
            if (b != null && "|-FJ".indexOf(b) != -1) {
              ++crossings;
            }
          }

          if (crossings % 2 == 1) {
            ++area;
          }

        }
      }
    }

    return area;
  }

}
