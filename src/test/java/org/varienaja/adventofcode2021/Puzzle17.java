package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {
  private Map<String, List<Integer>> input2result = new HashMap<>();

  private List<Integer> calc(int x1, int x2, int y1, int y2) {
    List<Integer> result = new LinkedList<>();
    // The farthest we can shoot with velocity v = v*(v+1)/2. This gives us the
    // minimum x-speed. If we throw farther than x2, we never hit the target.
    int sxMin = (int)Math.sqrt(2 * x2) - 1;
    for (int sx = sxMin; sx <= x2; sx++) {
      // If we throw below y1, we'll never hit the target. If we'd throw
      // higher than -y1, the ball would drop so fast in the end that it
      // would fly through the target
      launchNextProbe: for (int sy = y1; sy < -y1; sy++) {
        int maxY = 0;
        int speedX = sx;
        int speedY = sy;
        int posX = 0;
        int posY = 0;

        while (posX < x2 && posY > y1) { // Don't simulate beyond target range
          posX += speedX;
          posY += speedY--;
          speedX -= Integer.signum(speedX);
          maxY = Math.max(maxY, posY);

          if (x1 <= posX && posX <= x2 && y1 <= posY && posY <= y2) { // target hit
            result.add(maxY);
            continue launchNextProbe; // Prevent double detection within target area
          }
        }
      }
    }
    return result;
  }

  private List<Integer> solve(String line) {
    return input2result.computeIfAbsent(line, k -> {
      String[] parts = line.replace("target area: ", "").split(",\\s+");
      String[] xx = parts[0].replace("x=", "").split("\\.\\.");
      String[] yy = parts[1].replace("y=", "").split("\\.\\.");
      return calc(Integer.parseInt(xx[0]), Integer.parseInt(xx[1]), Integer.parseInt(yy[0]), Integer.parseInt(yy[1]));
    });
  }

  private int solveA(String line) {
    List<Integer> r = solve(line);
    return r.stream().mapToInt(i -> i).max().orElse(0);
  }

  private int solveB(String line) {
    return solve(line).size();
  }

  @Test
  public void testDay17() {
    String testInput = "target area: x=20..30, y=-10..-5";
    assertEquals(45, solveA(testInput));

    announceResultA();
    String line = getInput().get(0);
    int result = solveA(line);
    System.out.println(result);
    assertEquals(3003, result);

    assertEquals(112, solveB(testInput));
    announceResultB();
    result = solveB(line);
    System.out.println(result);
    assertEquals(940, result);
  }

}
