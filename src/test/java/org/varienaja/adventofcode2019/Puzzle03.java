package org.varienaja.adventofcode2019;

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
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(232, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(6084, sum);
  }

  private long solve(List<String> input, boolean partA) {
    Map<Integer, Map<Point, Integer>> line2Points = new HashMap<>();
    Set<Point> crossing = new HashSet<>();

    for (int i = 0; i < input.size(); i++) {
      line2Points.put(i, new HashMap<>());
      String line = input.get(i);
      int steps = 0;
      Point p = new Point(0, 0);
      String[] parts = line.split(",");
      for (String d : parts) {
        int len = Integer.parseInt(d.substring(1));
        char direction = d.charAt(0);
        for (int l = 0; l < len; l++) {
          switch (direction) {
            case 'R':
              p = p.getEast();
              break;
            case 'D':
              p = p.getSouth();
              break;
            case 'L':
              p = p.getWest();
              break;
            case 'U':
              p = p.getNorth();
              break;
          }

          steps++;
          line2Points.get(i).putIfAbsent(p, steps);
          if (i == 1 && line2Points.get(0).containsKey(p)) {
            crossing.add(p);
          }
        }
      }
    }
    if (partA) {
      return crossing.stream().mapToInt(p -> Math.abs(p.x) + Math.abs(p.y)).min().orElse(-1);
    }
    return crossing.stream().mapToInt(p -> line2Points.get(0).get(p) + line2Points.get(1).get(p)).min().orElse(-1);
  }

  private long solveA(List<String> input) {
    return solve(input, true);
  }

  private long solveB(List<String> input) {
    return solve(input, false);
  }

  @Test
  public void testA() {
    assertEquals(6, solveA(testInput1()));
    assertEquals(159, solveA(testInput2()));
    assertEquals(135, solveA(testInput3()));
  }

  @Test
  public void testB() {
    assertEquals(30, solveB(testInput1()));
    assertEquals(610, solveB(testInput2()));
    assertEquals(410, solveB(testInput3()));
  }

  private List<String> testInput1() {
    return List.of( //
        "R8,U5,L5,D3", //
        "U7,R6,D4,L4");
  }

  private List<String> testInput2() {
    return List.of( //
        "R75,D30,R83,U83,L12,D49,R71,U7,L72", //
        "U62,R66,U55,R34,D71,R55,D58,R83");
  }

  private List<String> testInput3() {
    return List.of( //
        "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", //
        "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7");
  }

}
