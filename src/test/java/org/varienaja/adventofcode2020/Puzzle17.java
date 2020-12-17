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
  private int[] directions = {
      -1, 0, 1
  };

  private long solveA(List<String> lines, int cycles) {
    final int MAX = 200;
    final int H = MAX / 2;
    char[][][] world = new char[MAX][MAX][MAX];

    int z = 0;
    int x = 0;
    int y = 0;
    for (z = 0; z < MAX; z++) {
      for (x = 0; x < MAX; x++) {
        for (y = 0; y < MAX; y++) {
          world[x][y][z] = '.';
        }
      }
    }
    long result = 0;

    z = H;
    x = 0;
    y = 0;
    for (y = 0; y < lines.size(); y++) {
      for (x = 0; x < lines.get(0).length(); x++) {
        world[x + H][y + H][z] = lines.get(y).charAt(x);
        if (lines.get(y).charAt(x) == '#') {
          result++;
        }
      }
    }

    for (int c = 0; c < cycles; c++) {
      char[][][] world2 = new char[MAX][MAX][MAX];
      for (z = 0; z < MAX; z++) {
        // System.out.println(z);
        for (y = 0; y < MAX; y++) {
          for (x = 0; x < MAX; x++) {
            // System.out.print(world[x][y][z]);
            world2[x][y][z] = world[x][y][z];

          }
          // System.out.println();
        }
        // System.out.println();
      }

      // System.out.println(c + " " + result);

      for (z = 0; z < MAX; z++) {
        for (x = 0; x < MAX; x++) {
          for (y = 0; y < MAX; y++) {
            int nbCount = 0;
            for (int a : directions) {
              for (int b : directions) {
                for (int d : directions) {
                  if (a == 0 && b == 0 && d == 0) {
                    continue;
                  }
                  boolean inBounds = 0 <= x + a && x + a < MAX - 1 && 0 <= y + b && y + b < MAX - 1 && 0 < z + d && z + d < MAX - 1;
                  if (inBounds) {
                    if (world[x + a][y + b][z + d] == '#') {
                      nbCount++;
                    }
                  }
                }
              }
            }

            if (world[x][y][z] == '#') {
              if (nbCount < 2 || nbCount > 3) {
                world2[x][y][z] = '.';
                result--;
              }
            } else if (world[x][y][z] == '.') {
              if (nbCount == 3) {
                world2[x][y][z] = '#';
                result++;
              }
            }

          }
        }
      }

      world = world2;

    }

    return result;
  }

  private long solveB(List<String> lines, int cycles) {
    final int MAX = 100;
    final int H = MAX / 2;
    char[][][][] world = new char[MAX][MAX][MAX][MAX];

    int z = 0;
    int x = 0;
    int y = 0;
    int w = 0;
    for (z = 0; z < MAX; z++) {
      for (x = 0; x < MAX; x++) {
        for (y = 0; y < MAX; y++) {
          for (w = 0; w < MAX; w++) {
            world[x][y][z][w] = '.';
          }
        }
      }
    }
    long result = 0;

    z = H;
    x = 0;
    y = 0;
    w = H;
    for (y = 0; y < lines.size(); y++) {
      for (x = 0; x < lines.get(0).length(); x++) {
        world[x + H][y + H][z][w] = lines.get(y).charAt(x);
        if (lines.get(y).charAt(x) == '#') {
          result++;
        }
      }
    }

    for (int c = 0; c < cycles; c++) {
      char[][][][] world2 = new char[MAX][MAX][MAX][MAX];
      for (w = 0; w < MAX; w++) {
        for (z = 0; z < MAX; z++) {
          // System.out.println(z);
          for (y = 0; y < MAX; y++) {
            for (x = 0; x < MAX; x++) {
              // System.out.print(world[x][y][z]);
              world2[x][y][z][w] = world[x][y][z][w];

            }
            // System.out.println();
          }
          // System.out.println();
        }
      }

      // System.out.println(c + " " + result);

      for (w = 0; w < MAX; w++) {
        for (z = 0; z < MAX; z++) {
          for (x = 0; x < MAX; x++) {
            for (y = 0; y < MAX; y++) {
              int nbCount = 0;
              for (int a : directions) {
                for (int b : directions) {
                  for (int d : directions) {
                    for (int e : directions) {

                      if (a == 0 && b == 0 && d == 0 && e == 0) {
                        continue;
                      }
                      boolean inBounds = 0 <= x + a && x + a < MAX - 1 && 0 <= y + b && y + b < MAX - 1 && 0 < z + d && z + d < MAX - 1 && 0 < w + e
                          && w + e < MAX - 1;
                      if (inBounds) {
                        if (world[x + a][y + b][z + d][w + e] == '#') {
                          nbCount++;
                        }
                      }
                    }
                  }
                }
              }

              if (world[x][y][z][w] == '#') {
                if (nbCount < 2 || nbCount > 3) {
                  world2[x][y][z][w] = '.';
                  result--;
                }
              } else if (world[x][y][z][w] == '.') {
                if (nbCount == 3) {
                  world2[x][y][z][w] = '#';
                  result++;
                }
              }

            }
          }
        }
      }
      world = world2;

    }

    return result;
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
