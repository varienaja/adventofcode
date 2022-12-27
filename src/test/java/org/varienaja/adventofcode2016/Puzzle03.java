package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(862L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(1577L, result);
  }

  @Test
  public void testA() {
    assertFalse(isValidTriangle("5 10 25"));
  }

  private boolean isValidTriangle(String inLine) {
    String[] parts = inLine.trim().split("\\s+");
    assertEquals(3, parts.length);
    int[] sides = new int[] {
        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])
    };

    // x y z : x+y>z
    // x y z : x+z>y
    // x y z : y+z>x

    return sides[0] + sides[1] > sides[2] && sides[0] + sides[2] > sides[1] && sides[1] + sides[2] > sides[0];
  }

  private long solveA(List<String> lines) {
    return lines.stream().filter(this::isValidTriangle).count();
  }

  private long solveB(List<String> lines) {
    List<String> transformed = new LinkedList<>();
    int i = 0;
    do {
      String l1 = lines.get(i++);
      String l2 = lines.get(i++);
      String l3 = lines.get(i++);

      String[] parts1 = l1.trim().split("\\s+");
      String[] parts2 = l2.trim().split("\\s+");
      String[] parts3 = l3.trim().split("\\s+");

      transformed.add(parts1[0] + " " + parts2[0] + " " + parts3[0]);
      transformed.add(parts1[1] + " " + parts2[1] + " " + parts3[1]);
      transformed.add(parts1[2] + " " + parts2[2] + " " + parts3[2]);
    } while (i < lines.size());

    return solveA(transformed);
  }

}
