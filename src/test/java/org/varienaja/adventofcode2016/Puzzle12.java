package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {
  private Map<Character, Integer> memory;

  private long run(List<String> input) {
    int ip = 0;
    while (ip < input.size()) {
      String[] parts = input.get(ip).split("\\s");
      if ("cpy".equals(parts[0])) {
        int x;
        char c = parts[1].charAt(0);
        if (c >= 'a' && c <= 'z') {
          x = memory.get(c);
        } else {
          x = Integer.parseInt(parts[1]);
        }
        char y = parts[2].charAt(0);
        memory.put(y, x);
        ip++;
      } else if ("inc".equals(parts[0])) {
        memory.compute(parts[1].charAt(0), (k, v) -> v + 1);
        ip++;
      } else if ("dec".equals(parts[0])) {
        memory.compute(parts[1].charAt(0), (k, v) -> v - 1);
        ip++;
      } else if ("jnz".equals(parts[0])) {
        int x;
        char c = parts[1].charAt(0);
        if (c >= 'a' && c <= 'z') {
          x = memory.get(c);
        } else {
          x = Integer.parseInt(parts[1]);
        }
        int y = Integer.parseInt(parts[2]);
        if (x != 0) {
          ip += y;
        } else {
          ip += 1;
        }
      }
    }

    return memory.get('a');
  }

  private long solveA(List<String> input) {
    memory = new HashMap<>();
    for (char c = 'a'; c <= 'd'; c++) {
      memory.put(c, 0);
    }
    return run(input);
  }

  private long solveB(List<String> input) {
    memory = new HashMap<>();
    for (char c = 'a'; c <= 'd'; c++) {
      memory.put(c, 0);
    }
    memory.put('c', 1);
    return run(input);
  }

  @Test
  public void testDay12() {
    List<String> testInput = List.of( //
        "cpy 41 a", //
        "inc a", //
        "inc a", //
        "dec a", //
        "jnz a 2", //
        "dec a");
    assertEquals(42, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(318020, sum);

    announceResultB();
    assertEquals(42, solveB(testInput));
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(9227674, sum);
  }

}
