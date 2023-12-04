package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(13440L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    solveB(getInput()); // PBZGRAZA
  }

  @Test
  @Ignore
  public void testA() {
    assertEquals(0, solveA(getTestInput()));
  }

  @Test
  @Ignore
  public void testB() {
    assertEquals(-1, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of();
  }

  private Map<Integer, Integer> solve(List<String> lines) {
    Map<Integer, Integer> cycle2x = new HashMap<>();
    int cycle = 1;
    int x = 1;
    cycle2x.put(cycle, x);

    for (String line : lines) {
      cycle2x.put(++cycle, x); // 'noop' or 1st cycle of 'addx'
      if (line.startsWith("addx")) {
        x += Integer.parseInt(line.substring(line.indexOf(' ') + 1));
        cycle2x.put(++cycle, x);
      }
    }

    return cycle2x;
  }

  private long solveA(List<String> lines) {
    Map<Integer, Integer> cycle2x = solve(lines);
    return Stream.of(20, 60, 100, 140, 180, 220).mapToInt(c -> c * cycle2x.get(c)).sum();
  }

  private long solveB(List<String> lines) {
    final int height = 6;
    final int width = 40;
    Map<Integer, Integer> cycle2x = solve(lines);
    System.out.println();

    for (int y = 0; y < height; y++) {
      for (int x = 1; x <= width; x++) {
        int spritePos = cycle2x.get(y * width + x);
        boolean visible = x >= spritePos && x < spritePos + 3;
        System.out.print(visible ? '#' : '.');
      }
      System.out.println();
    }

    return -1;
  }

}
