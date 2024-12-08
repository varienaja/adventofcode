package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class Puzzle08 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(247, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(861, result);
  }

  @Test
  public void testA() {
    assertEquals(14, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(34, solveB(getTestInput()));
  }

  private Set<Point> calcAntiNodes(List<String> lines, boolean partB) {
    int maxX = lines.get(0).length();
    int maxY = lines.size();
    Map<Character, Set<Point>> antennaType2Locations = parse(lines);

    Set<Point> antiNodes = new HashSet<>();
    for (Set<Point> antennae : antennaType2Locations.values()) {
      Set<Point> candidates = new HashSet<>();
      for (Point a1 : antennae) {
        for (Point a2 : antennae) {
          if (a1.equals(a2)) {
            continue;
          }

          int multiplicator = partB ? 0 : 1;
          int dx = a1.x - a2.x;
          int dy = a1.y - a2.y;
          int newX = a1.x + multiplicator * dx;
          int newY = a1.y + multiplicator * dy;
          while (newX >= 0 && newX < maxX && newY >= 0 && newY < maxY) {
            candidates.add(new Point(newX, newY));
            newX += dx;
            newY += dy;
            if (!partB) {
              break;
            }
          }
        }
      }
      antiNodes.addAll(candidates);
    }

    return antiNodes;
  }

  private Map<Character, Set<Point>> parse(List<String> lines) {
    Map<Character, Set<Point>> antennaType2Locations = new HashMap<>();
    for (int y = 0; y < lines.size(); ++y) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); ++x) {
        char c = line.charAt(x);
        if (c != '.') {
          antennaType2Locations.compute(c, (k, v) -> v == null ? new HashSet<>() : v).add(new Point(x, y));
        }
      }
    }
    return antennaType2Locations;
  }

  private long solveA(List<String> lines) {
    return calcAntiNodes(lines, false).size();
  }

  private long solveB(List<String> lines) {
    return calcAntiNodes(lines, true).size();
  }

}
