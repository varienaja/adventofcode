package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
    assertEquals(1849L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(201600L, result);
  }

  private List<String> getTestInput() {
    return List.of( //
        "30373", //
        "25512", //
        "65332", //
        "33549", //
        "35390");
  }

  private long solveA(List<String> lines) {
    int maxX = lines.size() - 1;
    int maxY = lines.get(0).length() - 1;
    long visibleTreeCount = maxX * 2 + maxY * 2; // Trees at border are always visible

    for (int x = 1; x < maxX; x++) {
      nextTree: for (int y = 1; y < maxY; y++) {

        for (Point.Direction d : Point.Direction.values()) {
          Point tree = new Point(x, y);
          boolean visible = true;
          char height = lines.get(tree.y).charAt(tree.x);

          while (visible && tree.x > 0 && tree.y > 0 && tree.x < maxX && tree.y < maxY) {
            tree = tree.add(d);
            char hh = lines.get(tree.y).charAt(tree.x);
            if (hh < height) {
              hh = height;
            } else {
              visible = false;
            }
          }
          if (visible) {
            ++visibleTreeCount;
            continue nextTree;
          }
        }

      }
    }

    return visibleTreeCount;
  }

  private long solveB(List<String> lines) {
    int maxX = lines.size() - 1;
    int maxY = lines.get(0).length() - 1;
    long maxScenic = Long.MIN_VALUE;

    for (int x = 1; x < maxX; x++) {
      for (int y = 1; y < maxY; y++) {

        long score = 1;
        for (Point.Direction d : Point.Direction.values()) {
          Point treeHouseCandidate = new Point(x, y);
          Point tree = new Point(x, y);
          boolean visible = true;
          char height = lines.get(tree.y).charAt(tree.x);

          while (visible && tree.x > 0 && tree.y > 0 && tree.x < maxX && tree.y < maxY) {
            tree = tree.add(d);
            char hh = lines.get(tree.y).charAt(tree.x);
            visible = hh < height;
          }
          score *= treeHouseCandidate.manhattanDistance(tree);
        }
        maxScenic = Math.max(score, maxScenic);

      }
    }

    return maxScenic;
  }

  @Test
  public void testA() {
    assertEquals(21L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(8L, solveB(getTestInput()));
  }

}
