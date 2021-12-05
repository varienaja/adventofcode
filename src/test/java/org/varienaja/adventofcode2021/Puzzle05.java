package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  private long solveA(List<String> lines) {
    return solveInternal(lines, true);
  }

  private long solveB(List<String> lines) {
    return solveInternal(lines, false);
  }

  private long solveInternal(List<String> lines, boolean excludeDiagonal) {
    Pattern p = Pattern.compile("(\\d+),(\\d+)\\s+->\\s+(\\d+),(\\d+)");
    Map<String, Integer> coord2value = new HashMap<>();

    for (String line : lines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int x1 = Integer.parseInt(m.group(1));
        int y1 = Integer.parseInt(m.group(2));
        int x2 = Integer.parseInt(m.group(3));
        int y2 = Integer.parseInt(m.group(4));

        int lengthX = x2 - x1;
        int lengthY = y2 - y1;

        if (excludeDiagonal && x1 != x2 && y1 != y2) {
          continue;
        }

        int x = x1;
        int y = y1;
        for (int i = 0; i <= Math.max(Math.abs(lengthX), Math.abs(lengthY)); i++) {
          String coord = x + "," + y;
          coord2value.compute(coord, (k, v) -> v == null ? 1 : v + 1);
          x += Integer.signum(lengthX);
          y += Integer.signum(lengthY);
        }
      }
    }

    return coord2value.values().stream().filter(v -> v > 1).count();
  }

  @Test
  public void testDay01() {
    List<String> testInput = Arrays.asList( //
        "0,9 -> 5,9", //
        "8,0 -> 0,8", //
        "9,4 -> 3,4", //
        "2,2 -> 2,1", //
        "7,0 -> 7,4", //
        "6,4 -> 2,0", //
        "0,9 -> 2,9", //
        "3,4 -> 1,4", //
        "0,0 -> 8,8", //
        "5,5 -> 8,2");
    assertEquals(5, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines); // 7576
    System.out.println(result);
    assertEquals(7318, result);

    assertEquals(12, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(19939, result);
  }

}
