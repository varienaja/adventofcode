package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
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
public class Puzzle09 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(6030L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(2545L, result);
  }

  @Test
  public void testA() {
    assertEquals(13L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(1L, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "R 4", //
        "U 4", //
        "L 3", //
        "D 1", //
        "R 4", //
        "D 1", //
        "L 5", //
        "R 2");
  }

  private long solve(List<String> lines, int snakeLength) {
    Set<Point> visitedByTail = new HashSet<>();
    Point[] snake = new Point[snakeLength];
    for (int i = 0; i < snakeLength; i++) {
      snake[i] = new Point(0, 0);
    }

    for (String line : lines) {
      int dist = Integer.parseInt(line.substring(1 + line.indexOf(' ')));
      char dir = line.charAt(0); // U, D, L, R

      for (int i = 0; i < dist; i++) {
        snake[0] = snake[0].add(Point.directionFromChar(dir));

        // Move rest of snake
        for (int j = 0; j < snakeLength - 1; j++) {
          Point h = snake[j];
          Point t = snake[j + 1];
          if (h.manhattanDistance(t) > 1) {
            int toAddX = Integer.signum(h.x - t.x);
            int toAddY = Integer.signum(h.y - t.y);
            if (t.y == h.y) { // same row
              t = new Point(t.x + toAddX, t.y);
            } else if (t.x == h.x) { // same column
              t = new Point(t.x, t.y + toAddY);
            } else if (h.manhattanDistance(t) > 2) { // diagonal
              t = new Point(t.x + toAddX, t.y + toAddY);
            }
          }
          snake[j + 1] = t;
        }

        visitedByTail.add(snake[snakeLength - 1]);
      }
    }

    return visitedByTail.size();
  }

  private long solveA(List<String> lines) {
    return solve(lines, 2);
  }

  private long solveB(List<String> lines) {
    return solve(lines, 10);
  }

}
