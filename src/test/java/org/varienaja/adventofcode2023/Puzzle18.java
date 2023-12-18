package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(74074L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(112074045986829L, result);
  }

  @Test
  public void testA() {
    assertEquals(62, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(952408144115L, solveB(getTestInput()));
  }

  private long solve(List<String> lines, boolean partB) {
    Map<Character, Point> val2Direction = Map.of('0', Point.dNorth, '1', Point.dEast, '2', Point.dSouth, '3', Point.dWest);
    long borderLength = 0;
    int x = 0;
    int y = 0;

    List<Point> points = new ArrayList<>();
    for (String line : lines) {
      String[] parts = line.split("\\s+");
      Point direction = partB ? val2Direction.get(parts[2].substring(7, 8).charAt(0)) : Point.directionFromChar(parts[0].charAt(0));
      int distance = partB ? Integer.parseInt(parts[2].substring(2, 7), 16) : Integer.parseInt(parts[1]);
      borderLength += distance;
      x += distance * direction.x;
      y += distance * direction.y;
      points.add(new Point(x, y));
    }

    // Shoelace algorithm A = .5 * SUM over i=1..n : y(i)*( x(i-1)-x(i+1) )
    int sz = points.size();
    long area = 0;
    for (int i = 0; i < sz; ++i) {
      area += points.get(i).y * ((long)points.get((sz + i - 1) % sz).x - points.get((i + 1) % sz).x);
    }
    area = Math.abs(area) / 2;

    // Pick's theorem: area = innerArea + borderLength/2 -1
    // long innerArea = area - borderLength / 2 + 1;
    // long totalArea = innerArea + borderLength; // Now add the border again to get total
    // return totalArea;

    // Simpler:
    return area + borderLength / 2 + 1;
  }

  private long solveA(List<String> lines) {
    return solve(lines, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, true);
  }
}
