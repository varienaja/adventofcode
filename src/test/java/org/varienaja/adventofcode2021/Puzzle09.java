package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {
  private int maxX;
  private int maxY;
  private List<String> grid;

  private long calcBasinSize(Set<Point> basin) {
    Set<Point> toAdd;
    do {
      toAdd = basin.stream().flatMap(p -> p.getNeighbours().stream()) //
          .filter(nb -> getDepth(nb) < '9').collect(Collectors.toSet());
    } while (basin.addAll(toAdd));
    return basin.size();
  }

  private List<Point> findLows(List<String> lines) {
    List<Point> lows = new LinkedList<>();

    for (int x = 0; x < maxX; x++) {
      for (int y = 0; y < maxY; y++) {
        Point p = new Point(x, y);
        char h = getDepth(p);

        if (!p.getNeighbours().stream().mapToInt(this::getDepth).anyMatch(d -> d <= h)) {
          lows.add(p);
        }
      }
    }
    return lows;
  }

  private char getDepth(Point p) {
    if (0 <= p.y && p.y < maxY && 0 <= p.x && p.x < maxX) {
      return grid.get(p.y).charAt(p.x);
    }
    return 'A'; // 'A' is bigger than '9'
  }

  private long solveA(List<String> lines) {
    grid = lines;
    maxX = lines.get(0).length();
    maxY = lines.size();

    // '/' is just before 0, so the +1 to get the risk from the depth is incorporated here
    return findLows(lines).stream().mapToInt(p -> getDepth(p) - '/').sum();
  }

  private long solveB(List<String> lines) {
    grid = lines;
    maxX = lines.get(0).length();
    maxY = lines.size();

    List<Long> sizes = findLows(lines).stream() //
        .map(p -> calcBasinSize(new HashSet<>(Collections.singleton(p)))) //
        .sorted().collect(Collectors.toList());

    return sizes.stream().skip(sizes.size() - 3).reduce((a, b) -> a * b).get();
  }

  @Test
  public void testDay09() {
    List<String> testInput = Arrays.asList( //
        "2199943210", //
        "3987894921", //
        "9856789892", //
        "8767896789", //
        "9899965678");
    assertEquals(15, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(532, result);

    assertEquals(1134, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(1110780, result);
  }

}
