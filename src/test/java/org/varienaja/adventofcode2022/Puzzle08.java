package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.IntStream;

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

  @Test
  public void testA() {
    assertEquals(21L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(8L, solveB(getTestInput()));
  }

  private long calcScenicScore(List<String> lines, Point treeHouseCandidate, long maxX, long maxY) {
    long score = 1;
    for (Point.Direction d : Point.Direction.values()) {
      Point tree = new Point(treeHouseCandidate.x, treeHouseCandidate.y);
      boolean visible = true;
      char height = lines.get(tree.y).charAt(tree.x);

      while (visible && tree.x > 0 && tree.y > 0 && tree.x < maxX && tree.y < maxY) {
        tree = tree.add(d);
        char hh = lines.get(tree.y).charAt(tree.x);
        visible = hh < height;
      }
      score *= treeHouseCandidate.manhattanDistance(tree);
    }
    return score;
  }

  private List<String> getTestInput() {
    return List.of( //
        "30373", //
        "25512", //
        "65332", //
        "33549", //
        "35390");
  }

  private boolean isVisible(List<String> lines, Point start, long maxX, long maxY) {
    for (Point.Direction d : Point.Direction.values()) {
      Point tree = new Point(start.x, start.y);
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
        return true;
      }
    }
    return false;
  }

  private long solveA(List<String> lines) {
    int maxX = lines.size() - 1;
    int maxY = lines.get(0).length() - 1;

    return IntStream.rangeClosed(0, maxX).parallel().mapToLong( //
        x -> IntStream.rangeClosed(0, maxY).filter( //
            y -> isVisible(lines, new Point(x, y), maxX, maxY)).count() //
    ).sum();
  }

  private long solveB(List<String> lines) {
    int maxX = lines.size() - 1;
    int maxY = lines.get(0).length() - 1;

    return IntStream.range(1, maxX).parallel().mapToLong( //
        x -> IntStream.range(1, maxY).mapToLong( //
            y -> calcScenicScore(lines, new Point(x, y), maxX, maxY)).max().orElseThrow() //
    ).max().orElseThrow();
  }

}
