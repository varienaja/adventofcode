package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
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
public class Puzzle23 extends PuzzleAbs {
  private Point[][] toScan = new Point[][] { //
      new Point[] {
          Point.dNorth, Point.dNorthEast, Point.dNorthWest
      }, new Point[] {
          Point.dSouth, Point.dSouthEast, Point.dSouthWest
      }, new Point[] {
          Point.dWest, Point.dNorthWest, Point.dSouthWest
      }, new Point[] {
          Point.dEast, Point.dNorthEast, Point.dSouthEast
      }
  };

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(4247L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(1049L, result);
  }

  @Test
  public void testA() {
    assertEquals(110L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(20, solveB(getTestInput()));
  }

  private Point doCheck(int n, Point elf, Set<Point> elves) {
    for (Point scan : toScan[n]) {
      Point candidate = new Point(elf.x + scan.x, elf.y + scan.y);
      if (elves.contains(candidate)) {
        return null;
      }
    }
    return new Point(elf.x + toScan[n][0].x, elf.y + toScan[n][0].y);
  }

  private boolean doRound(int round, Set<Point> elves) {
    boolean moved = false;
    Map<Point, Set<Point>> movementPlan = new HashMap<>();
    for (Point elf : elves) {
      if (elf.getAllNeighbours().stream().filter(elves::contains).count() == 0) {
        continue; // Don't move if no neighbours
      }

      Point toAdd = null;
      int check = 0;
      while (toAdd == null && check < toScan.length) {
        toAdd = doCheck((round + check) % toScan.length, elf, elves);
        ++check;
      }
      if (toAdd != null) {
        movementPlan.compute(toAdd, (k, v) -> v == null ? new HashSet<>() : v).add(elf);
      }
    }

    for (Entry<Point, Set<Point>> e : movementPlan.entrySet()) {
      if (e.getValue().size() == 1) { // Only move if I am the only one planning to go there
        moved = true;
        elves.removeAll(e.getValue());
        elves.add(e.getKey());
      }
    }

    return moved;
  }

  private List<String> getTestInput() {
    return List.of( //
        "....#..", //
        "..###.#", //
        "#...#.#", //
        ".#...##", //
        "#.###..", //
        "##.#.##", //
        ".#..#..");
  }

  private Set<Point> parse(List<String> lines) {
    Set<Point> elves = new HashSet<>();
    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          elves.add(new Point(x, y));
        }
      }
    }
    return elves;
  }

  private long solveA(List<String> lines) {
    Set<Point> elves = parse(lines);

    for (int round = 0; round < 10; round++) {
      doRound(round, elves);
    }

    IntSummaryStatistics statsX = elves.stream().mapToInt(p -> p.x).summaryStatistics();
    IntSummaryStatistics statsY = elves.stream().mapToInt(p -> p.y).summaryStatistics();
    return (statsX.getMax() + 1 - statsX.getMin()) * (statsY.getMax() + 1 - statsY.getMin()) - elves.size();
  }

  private long solveB(List<String> lines) {
    Set<Point> elves = parse(lines);

    int round = 0;
    while (doRound(round, elves)) {
      round++;
    }

    return 1 + round;
  }

}
