package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  private long solve(List<String> input, int steps, boolean partB) {
    Set<Point> field = new HashSet<>();
    if (partB) {
      field.add(new Point(0, 0));
      field.add(new Point(0, input.size() - 1));
      field.add(new Point(input.size() - 1, 0));
      field.add(new Point(input.size() - 1, input.size() - 1));
    }
    for (int y = 0; y < input.size(); y++) {
      char[] line = input.get(y).toCharArray();
      for (int x = 0; x < input.size(); x++) {
        if (line[x] == '#') {
          field.add(new Point(x, y));
        }
      }
    }

    for (int s = 0; s < steps; s++) {
      Set<Point> newField = new HashSet<>();
      if (partB) {
        newField.add(new Point(0, 0));
        newField.add(new Point(0, input.size() - 1));
        newField.add(new Point(input.size() - 1, 0));
        newField.add(new Point(input.size() - 1, input.size() - 1));
      }

      for (int y = 0; y < input.size(); y++) {
        for (int x = 0; x < input.size(); x++) {
          Point p = new Point(x, y);

          long nbOn = p.getAllNeighbours().stream().filter(field::contains).count();
          if (field.contains(p)) { // ON
            if (nbOn == 2 || nbOn == 3) {
              newField.add(p);
            }
          } else { // OFF
            if (nbOn == 3) {
              newField.add(p);
            }
          }
        }
      }
      field = newField;
    }

    return field.size();
  }

  private long solveA(List<String> input, int steps) {
    return solve(input, steps, false);
  }

  private long solveB(List<String> input, int steps) {
    return solve(input, steps, true);
  }

  @Test
  public void testDay18() {
    List<String> testInput = List.of( //
        ".#.#.#", //
        "...##.", //
        "#....#", //
        "..#...", //
        "#.#..#", //
        "####..");
    assertEquals(4, solveA(testInput, 4));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines, 100);
    System.out.println(sum);
    assertEquals(821, sum);

    announceResultB();
    assertEquals(17, solveB(testInput, 5));
    sum = solveB(lines, 100);
    System.out.println(sum);
    assertEquals(886, sum);
  }

}
