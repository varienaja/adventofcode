
package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {
  private long globalMax;
  private List<List<Point>> longest;

  long generateMaxStrength(int startValue, long strength, Set<Point> ports, List<Point> used, boolean partB) {
    long max = strength;

    for (Point candidate : ports) {
      if (candidate.x == startValue || candidate.y == startValue) {

        long newStrength = strength + (partB ? 1 : candidate.x + candidate.y);
        Set<Point> newPorts = new HashSet<>(ports);
        newPorts.remove(candidate);
        used.add(candidate);
        int nextStartValue = candidate.x == startValue ? candidate.y : candidate.x;
        newStrength = generateMaxStrength(nextStartValue, newStrength, newPorts, used, partB);
        if (newStrength >= max) {
          max = newStrength;

          if (max >= globalMax) {
            if (max > globalMax) {
              longest.clear();
            }
            globalMax = max;
            if (used.size() == max) {
              longest.add(new LinkedList<>(used));
            }
          }
        }
        used.remove(candidate);
      }
    }

    return max;
  }

  private long solveA(List<String> input) {
    globalMax = 0;
    longest = new LinkedList<>();

    Set<Point> ports = new HashSet<>();
    for (String line : input) {
      String[] parts = line.split("/");
      ports.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
    }

    return generateMaxStrength(0, 0, ports, new LinkedList<>(), false);
  }

  private long solveB(List<String> input) {
    globalMax = 0;
    longest = new LinkedList<>();

    Set<Point> ports = new HashSet<>();
    for (String line : input) {
      String[] parts = line.split("/");
      ports.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
    }

    generateMaxStrength(0, 0, ports, new LinkedList<>(), true);

    long maxStrength = 0;
    for (List<Point> ps : longest) {
      long strength = 0;
      for (Point port : ps) {
        strength += port.x + port.y;
      }
      if (strength > maxStrength) {
        maxStrength = strength;
      }
    }

    return maxStrength;
  }

  @Test
  public void testDay24() {
    List<String> testInput = List.of("0/2", //
        "2/2", //
        "2/3", //
        "3/4", //
        "3/5", //
        "0/1", //
        "10/1", //
        "9/10");
    assertEquals(31, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(1695, result);

    assertEquals(19, solveB(testInput));
    announceResultB();
    long stepCount = solveB(lines);
    System.out.println(stepCount);
    assertEquals(1673, stepCount);
  }

}
