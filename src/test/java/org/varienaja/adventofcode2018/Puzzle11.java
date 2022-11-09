package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {

  private int calcPowerLevel(int serialNo, int x, int y) {
    int rackID = x + 10;
    int powerlevel = (y * rackID + serialNo) * rackID;
    powerlevel = powerlevel % 1000;
    powerlevel = powerlevel / 100 - 5;
    return powerlevel;
  }

  @Test
  public void doA() {
    announceResultA();
    Point answer = solveA(3999);
    System.out.println(answer);
    assertEquals(new Point(21, 77), answer);
  }

  @Test
  public void doB() {
    announceResultB();
    int[] answer = solveB(3999);
    System.out.println(Arrays.toString(answer));
    assertArrayEquals(new int[] {
        224, 222, 27
    }, answer);
  }

  private int[][] makeGrid(int serialNo) {
    int[][] grid = new int[301][301];
    for (int x = 1; x <= 300; x++) {
      for (int y = 1; y <= 300; y++) {
        grid[x][y] = calcPowerLevel(serialNo, x, y);
      }
    }
    return grid;
  }

  private Point solveA(int serialNo) {
    int[][] grid = makeGrid(serialNo);

    int z = 3;
    int maxSum = Integer.MIN_VALUE;
    Point best = null;
    for (int x = 1; x <= 300 - z; x++) {
      for (int y = 1; y <= 300 - z; y++) {
        int sum = 0;
        for (int xx = x; xx < x + z; xx++) {
          for (int yy = y; yy < y + z; yy++) {
            sum += grid[xx][yy];
          }
        }
        if (sum > maxSum) {
          maxSum = sum;
          best = new Point(x, y);
        }
      }
    }

    return best;
  }

  private int[] solveB(int serialNo) {
    int[][] grid = makeGrid(serialNo);

    int maxSum = Integer.MIN_VALUE;
    int[] best = new int[3];
    for (int z = 1; z <= 30; z++) { // Should be z<=300, but that is slow
      for (int x = 1; x <= 300 - z; x++) {
        for (int y = 1; y <= 300 - z; y++) {

          int sum = 0;
          for (int xx = x; xx < x + z; xx++) {
            for (int yy = y; yy < y + z; yy++) {
              sum += grid[xx][yy];
            }
          }

          if (sum > maxSum) {
            maxSum = sum;
            best[0] = x;
            best[1] = y;
            best[2] = z;
          }
        }
      }
    }

    return best;
  }

  @Test
  public void testA() {
    assertEquals(4, calcPowerLevel(8, 3, 5));
    assertEquals(-5, calcPowerLevel(57, 122, 79));
    assertEquals(0, calcPowerLevel(39, 217, 196));
    assertEquals(4, calcPowerLevel(71, 101, 153));

    assertEquals(new Point(33, 45), solveA(18));
    assertEquals(new Point(21, 61), solveA(42));
  }

  @Test
  public void testB() {
    assertArrayEquals(new int[] {
        90, 269, 16
    }, solveB(18));

    assertArrayEquals(new int[] {
        232, 251, 12
    }, solveB(42));
  }

}
