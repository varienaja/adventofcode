package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
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
public class Puzzle14 extends PuzzleAbs {
  private static final int GRIDSIZE = 128;

  private char[][] calcGrid(String input) {
    char[][] result = new char[GRIDSIZE][GRIDSIZE];

    Puzzle10 p = new Puzzle10();
    for (int i = 0; i < GRIDSIZE; i++) {
      String hashInput = input + "-" + i;
      String knotHash = p.solveB(256, hashInput);
      StringBuilder sb = new StringBuilder();
      for (int ix = 0; ix < knotHash.length(); ix++) {
        String hex = "" + knotHash.charAt(ix);
        String binary = Integer.toString(Integer.parseInt(hex, 16), 2);
        while (binary.length() < 4) {
          binary = "0" + binary;
        }
        sb.append(binary);
      }

      result[i] = sb.toString().toCharArray();
    }

    return result;
  }

  Set<Point> getGroup(Set<Point> ones) {
    Set<Point> group = new HashSet<>();
    group.add(ones.iterator().next());

    Set<Point> toAdd = new HashSet<>();
    boolean added = true;
    while (added) {
      for (Point p : group) {
        for (Point candidate : p.getNSWENeighbours()) {
          if (ones.contains(candidate)) {
            toAdd.add(candidate);
          }
        }
      }

      added = group.addAll(toAdd);
    }

    return group;
  }

  Set<Point> getOnes(char[][] grid) {
    Set<Point> ones = new HashSet<>();
    for (int y = 0; y < GRIDSIZE; y++) {
      for (int x = 0; x < GRIDSIZE; x++) {
        if (grid[x][y] == '1') {
          ones.add(new Point(x, y));
        }
      }
    }
    return ones;
  }

  private long solveA(String input) {
    char[][] grid = calcGrid(input);

    return getOnes(grid).size();
  }

  private long solveB(String input) {
    char[][] grid = calcGrid(input);

    int regionCount = 0;
    Set<Point> ones = getOnes(grid);
    while (!ones.isEmpty()) {
      // Find a one; find all it's direct neighboring ones. If no new ones; set to 0.
      // increase regionCount
      Set<Point> group = getGroup(ones);
      ones.removeAll(group);
      regionCount++;
    }

    return regionCount;
  }

  @Test
  public void testDay14() {
    String testInput = "flqrgnkx";
    assertEquals(8108, solveA(testInput));

    announceResultA();
    String input = "wenycdww";
    long result = solveA(input);
    System.out.println(result);
    assertEquals(8226, result);

    assertEquals(1242, solveB(testInput));
    announceResultB();
    result = solveB(input);
    System.out.println(result);
    assertEquals(1128, result);
  }

}
