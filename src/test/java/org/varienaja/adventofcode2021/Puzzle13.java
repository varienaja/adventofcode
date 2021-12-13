package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {

  private void fold(Set<Point> grid, char axis, int w) {
    Set<Point> modified = new HashSet<>();
    Iterator<Point> it = grid.iterator();
    while (it.hasNext()) {
      Point p = it.next();

      if (axis == 'x') { // horizontal
        if (p.x >= w) {
          it.remove();
          int tosub = 2 * (p.x - w);
          modified.add(new Point(p.x - tosub, p.y));
        }
      }
      if (axis == 'y') { // vertical
        if (p.y >= w) {
          it.remove();
          int tosub = 2 * (p.y - w);
          modified.add(new Point(p.x, p.y - tosub));
        }
      }
    }

    grid.addAll(modified);
  }

  private long solve(List<String> lines, boolean once) {
    Set<Point> grid = new HashSet<>();
    List<String> operations = new LinkedList<>();

    boolean parseDots = true;
    for (String line : lines) {
      if (line.isEmpty()) {
        parseDots = false;
        continue;
      }
      if (parseDots) {
        String[] parts = line.split(",");
        grid.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
      } else {
        operations.add(line.replace("fold along ", ""));
      }
    }

    int foldCount = once ? 1 : operations.size();
    for (int i = 0; i < foldCount; i++) {
      String operation = operations.get(i);
      char axis = operation.charAt(0);
      int w = Integer.parseInt(operation.substring(2));

      fold(grid, axis, w);
    }

    System.out.println();
    for (int y = 0; y < 6; y++) {
      for (int x = 0; x < 39; x++) {
        System.out.print(grid.contains(new Point(x, y)) ? '#' : '.');
      }
      System.out.println();
    }

    return grid.size();
  }

  @Test
  public void testDay13() {
    List<String> testInput = Arrays.asList( //
        "6,10", //
        "0,14", //
        "9,10", //
        "0,3", //
        "10,4", //
        "4,11", //
        "6,0", //
        "6,12", //
        "4,1", //
        "0,13", //
        "10,12", //
        "3,4", //
        "3,0", //
        "8,4", //
        "1,10", //
        "2,14", //
        "8,10", //
        "9,0", //
        "", //
        "fold along y=7", //
        "fold along x=5");
    assertEquals(17, solve(testInput, true));

    announceResultA();
    List<String> lines = getInput();
    long result = solve(lines, true);
    System.out.println(result);
    assertEquals(788, result, 1);

    solve(testInput, false); // Square on StdOut
    announceResultB();
    solve(lines, false); // KJBKEUBG on StdOut
  }

}
