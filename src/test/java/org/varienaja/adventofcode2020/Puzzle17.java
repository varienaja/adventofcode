package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {
  private char[][][][] world;
  private int[] directions = {
      -1, 0, 1
  };
  private int[] directionsA = {
      0
  };

  private int countNeighbors(int x, int y, int z, int w, boolean partA) {
    int nbCount = 0;
    for (int dX : directions) {
      for (int dY : directions) {
        for (int dZ : directions) {
          for (int dW : partA ? directionsA : directions) {
            if (dX == 0 && dY == 0 && dZ == 0 && dW == 0) {
              continue;
            }

            if (inBounds(x + dX, y + dY, z + dZ, w + dW) //
                && world[x + dX][y + dY][z + dZ][w + dW] == '#') {
              nbCount++;
            }
          }
        }
      }
    }
    return nbCount;
  }

  private boolean inBounds(int x, int y, int z, int w) {
    return 0 <= x && x < world.length //
        && 0 <= y && y < world[0].length //
        && 0 <= z && z < world[0][0].length //
        && 0 <= w && w < world[0][0][0].length;
  }

  private long initWorld(List<String> lines) {
    long result = 0L;

    world = new char[lines.get(0).length()][lines.size()][1][1];
    for (int y = 0; y < lines.size(); y++) {
      for (int x = 0; x < lines.get(0).length(); x++) {
        char ch = lines.get(y).charAt(x);
        world[x][y][0][0] = ch;
        if (ch == '#') {
          result++;
        }
      }
    }
    return result;
  }

  private long solve(List<String> lines, int cycles, boolean partA) {
    long result = initWorld(lines);
    int dWW = partA ? 0 : 1;

    for (int cy = 0; cy < cycles; cy++) {
      // Create new world that is one larger on each side, w-direction only grows when !partA
      int wStart = partA ? 0 : -1;
      int wEnd = partA ? 0 : world[0][0][0].length;
      char[][][][] newWorld = new char[world.length + 2][world[0].length + 2][world[0][0].length + 2][partA ? 1 : world[0][0][0].length + 2];

      for (int x = -1; x <= world.length; x++) {
        for (int y = -1; y <= world[0].length; y++) {
          for (int z = -1; z <= world[0][0].length; z++) {
            for (int w = wStart; w <= wEnd; w++) {

              int nbCount = countNeighbors(x, y, z, w, partA);

              if (inBounds(x, y, z, w) && world[x][y][z][w] == '#') {
                if (nbCount < 2 || nbCount > 3) {
                  newWorld[x + 1][y + 1][z + 1][w + dWW] = '.';
                  result--;
                } else {
                  newWorld[x + 1][y + 1][z + 1][w + dWW] = '#';
                }
              } else {
                if (nbCount == 3) {
                  newWorld[x + 1][y + 1][z + 1][w + dWW] = '#';
                  result++;
                } else {
                  newWorld[x + 1][y + 1][z + 1][w + dWW] = '.';
                }
              }
            }
          }
        }
      }

      world = newWorld;
    }

    return result;
  }

  private long solveA(List<String> lines, int cycles) {
    return solve(lines, cycles, true);
  }

  private long solveB(List<String> lines, int cycles) {
    return solve(lines, cycles, false);
  }

  @Test
  public void testDay17() {
    List<String> input = Arrays.asList( //
        ".#.", //
        "..#", //
        "###");
    assertEquals(112, solveA(input, 6));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines, 6);
    assertEquals(211L, result);
    System.out.println(result);

    assertEquals(848L, solveB(input, 6));
    announceResultB();
    result = solveB(lines, 6);
    assertEquals(1952L, result);
    System.out.println(result);
  }

}
