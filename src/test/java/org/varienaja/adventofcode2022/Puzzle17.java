package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class Puzzle17 extends PuzzleAbs {
  private Set<Point> piece1 = Set.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0));
  private Set<Point> piece2 = Set.of(new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2), new Point(1, 0));
  private Set<Point> piece3 = Set.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2));
  private Set<Point> piece4 = Set.of(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3));
  private Set<Point> piece5 = Set.of(new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1));
  private List<Set<Point>> pieces = List.of(piece1, piece2, piece3, piece4, piece5);
  private Set<Point> world;
  private int maxFloorHeight = 0;
  private int blockBottom = 0;
  private int blockLeft = 2;
  private Map<String, long[]> topWorld2height;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    System.out.println(result);
    assertEquals(3106L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    System.out.println(result);
    assertEquals(1537175792495L, result);
  }

  @Test
  public void testA() {
    assertEquals(3068L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(1514285714288L, solveB(getTestInput()));
  }

  private String getTestInput() {
    return ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";
  }

  private String getTopWorldKey(int blockIx, int inputPos) {
    StringBuilder sb = new StringBuilder();
    sb.append(inputPos);
    for (int y = maxFloorHeight; y > Math.max(0, maxFloorHeight - 10); y--) {
      for (int x = -1; x < 8; x++) {
        sb.append(world.contains(new Point(x, y)) ? '#' : ' ');
      }
    }
    sb.append(blockIx);
    return sb.toString();
  }

  private boolean moveDown(Set<Point> piece) {
    boolean ok = true;
    for (Point p : piece) {
      ok &= !world.contains(new Point(p.x + blockLeft, p.y + blockBottom - 1));
    }
    if (ok) {
      --blockBottom;
    } else { // add block to world
      for (Point p : piece) {
        int newY = p.y + blockBottom;
        maxFloorHeight = Math.max(maxFloorHeight, newY);
        world.add(new Point(p.x + blockLeft, newY));
      }
    }
    return ok;
  }

  private boolean moveLeft(Set<Point> piece) {
    boolean ok = true;
    for (Point p : piece) {
      ok &= !world.contains(new Point(p.x + blockLeft - 1, p.y + blockBottom));
      ok &= p.x + blockLeft - 1 >= 0;
    }
    if (ok) {
      --blockLeft;
    }
    return ok;
  }

  private boolean moveRight(Set<Point> piece) {
    boolean ok = true;
    for (Point p : piece) {
      ok &= !world.contains(new Point(p.x + blockLeft + 1, p.y + blockBottom));
      ok &= p.x + blockLeft + 1 < 7;
    }
    if (ok) {
      ++blockLeft;
    }
    return ok;
  }

  private long solve(String input, long amount) {
    world = new HashSet<>();
    for (int x = 0; x < 7; x++) {
      world.add(new Point(x, 0));
    }
    topWorld2height = new HashMap<>();
    maxFloorHeight = 0;

    Set<Point> piece;
    int inputPos = 0;
    long blocks = 0;
    long calculatedFloorHeight = 0;

    while (blocks < amount) {
      piece = pieces.get((int)(blocks % pieces.size()));
      blockLeft = 2;
      blockBottom = maxFloorHeight + 4;

      do { // simulate block falling
        char c = input.charAt(inputPos++);
        if (inputPos >= input.length()) {
          inputPos = 0;
        }

        if (c == '<') {
          moveLeft(piece);
        } else {
          moveRight(piece);
        }
      } while (moveDown(piece));
      blocks++;

      String top = getTopWorldKey((int)(blocks % pieces.size()), inputPos);
      long[] knownResult = topWorld2height.get(top);
      if (knownResult != null) {
        long dBlocks = blocks - knownResult[0];
        long dHeight = maxFloorHeight - knownResult[1];
        long periods = (amount - blocks) / dBlocks;
        calculatedFloorHeight += periods * dHeight;
        blocks += periods * dBlocks;
      }
      topWorld2height.put(top, new long[] {
          blocks, maxFloorHeight
      });
    }

    return maxFloorHeight + calculatedFloorHeight;
  }

  private long solveA(String input) {
    return solve(input, 2022L);
  }

  private long solveB(String input) {
    return solve(input, 1000000000000L);
  }

}
