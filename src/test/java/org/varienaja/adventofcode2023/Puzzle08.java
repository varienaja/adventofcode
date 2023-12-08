package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(13019L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(13524038372771L, result);
  }

  @Test
  public void testA() {
    assertEquals(2, solveA(getTestInput('a')));
  }

  @Test
  public void testB() {
    assertEquals(6, solveB(getTestInput('b')));
  }

  private long solve(List<String> lines, String startSuffix, String endSuffix) {
    String instructions = lines.get(0);

    Map<String, String[]> from2to = lines.stream().skip(2) //
        .map(line -> line.split("\\W+")) //
        .collect(Collectors.toMap(a -> a[0], a -> new String[] {
            a[1], a[2]
        }));

    return from2to.keySet().stream().filter(k -> k.endsWith(startSuffix)).mapToLong(pos -> {
      int steps = 0;
      for (;;) {
        if (pos.endsWith(endSuffix)) {
          return steps;
        }

        char c = instructions.charAt(steps++ % instructions.length());
        pos = from2to.get(pos)[c == 'L' ? 0 : 1];
      }
    }).reduce(this::lcm).orElseThrow();
  }

  private long solveA(List<String> lines) {
    return solve(lines, "AAA", "ZZZ");
  }

  private long solveB(List<String> lines) {
    return solve(lines, "A", "Z");
  }

}
