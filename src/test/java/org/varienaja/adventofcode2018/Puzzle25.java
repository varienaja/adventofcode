package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(386, sum);
  }

  private long solveA(List<String> input) {
    List<int[]> points = new LinkedList<>();
    for (String line : input) {
      String[] parts = line.split(",");
      int[] point = new int[] {
          Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])
      };
      points.add(point);
    }

    // Count constellations
    List<List<int[]>> constellations = new LinkedList<>();
    for (int[] point : points) {
      List<List<int[]>> matchingConstellations = new LinkedList<>();

      for (List<int[]> constellation : constellations) {
        for (int[] p : constellation) {
          int dist = 0;
          for (int d = 0; d < 4; d++) {
            dist += Math.abs(p[d] - point[d]);
          }

          if (dist <= 3) {
            // If any point is closed than 3, the new point belongs to this constellation
            matchingConstellations.add(constellation);
            break;
          }
        }
      }

      List<int[]> newc;
      if (matchingConstellations.isEmpty()) {
        newc = new LinkedList<>();
        newc.add(point);
        constellations.add(newc);
      } else if (matchingConstellations.size() == 1) {
        matchingConstellations.iterator().next().add(point);
      } else {
        constellations.removeAll(matchingConstellations);
        newc = matchingConstellations.stream().flatMap(c -> c.stream()).collect(Collectors.toList());
        newc.add(point);
        constellations.add(newc);
      }
    }
    return constellations.size();
  }

  @Test
  public void testA() {
    assertEquals(2, solveA(testInput1()));
  }

  private List<String> testInput1() {
    return List.of( //
        "0,0,0,0", //
        "3,0,0,0", //
        "0,3,0,0", //
        "0,0,3,0", //
        "0,0,0,3", //
        "0,0,0,6", //
        "9,0,0,0", //
        "12,0,0,0");
  }

}
