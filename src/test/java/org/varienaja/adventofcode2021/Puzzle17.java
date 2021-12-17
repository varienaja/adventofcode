package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
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
public class Puzzle17 extends PuzzleAbs {

  private long solveA(int x1, int x2, int y1, int y2) {
    // target area: x=236..262, y=-78..-58
    // fire from 0,0, direction x+?, y+?, speed s
    // when do I hit the target area?
    // each step, x 1 towards 0, , y minus 1
    long maxy = 0;
    Set<Point> hits = new HashSet<>();

    for (int sx = -1500; sx < 1500; sx++) {
      for (int sy = -1500; sy < 1500; sy++) {
        int localMaxy = 0;
        int speedX = sx;
        int speedY = sy;
        int posX = 0;
        int posY = 0;
        for (int s = 0; s < 1100; s++) {

          posX = posX + speedX;
          posY = posY + speedY;

          if (speedX > 0) {
            speedX--;
          } else if (speedX < 0) {
            speedX++;
          }
          speedY--;

          if (posY > localMaxy) {
            localMaxy = posY;
          }

          if (x1 <= posX && posX <= x2 && y1 <= posY && posY <= y2) {
            hits.add(new Point(sx, sy));
            System.out.println("Hit for x" + sx + " y" + sy + " height" + localMaxy);
            if (localMaxy > maxy) {
              maxy = localMaxy;
            }

          }
        }

      }

    }
    System.out.println("hits: " + hits.size());

    return maxy;
  }

  private long solveB(List<String> lines) {

    return -1;
  }

  @Test
  public void testDay17() {
    List<String> testInput = Arrays.asList("");
    assertEquals(45, solveA(20, 30, -10, -5));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(236, 262, -78, -58);
    System.out.println(result);
    assertEquals(-1, result);

    assertEquals(-1, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(-1, result);
  }

}
