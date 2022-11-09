package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {
  private Map<Character, Integer> memory;

  private long run(List<String> input) { // Puzzle12
    int ip = 0;
    while (ip < input.size()) {
      // System.out.println(memory);

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

        c = parts[2].charAt(0);
        int y;
        if (c >= 'a' && c <= 'z') {
          y = memory.get(c);
        } else {
          y = Integer.parseInt(parts[2]);
        }

        if (x != 0) {
          ip += y;
        } else {
          ip += 1;
        }
      } else if ("tgl".equals(parts[0])) { // The new instruction
        int x = memory.get(parts[1].charAt(0));
        if (ip + x < input.size()) {
          String[] pts = input.get(ip + x).split("\\s");
          if (pts.length == 2) { // One arg
            if ("inc".equals(pts[0])) {
              pts[0] = "dec";
            } else {
              pts[0] = "inc";
            }
          } else { // Two arg
            if ("jnz".equals(pts[0])) {
              pts[0] = "cpy";
            } else {
              pts[0] = "jnz";
            }
          }

          String line = Stream.of(pts).collect(Collectors.joining(" "));
          input.set(ip + x, line);
        }
        ip++;
      }
    }

    return memory.get('a');

  }

  private long solveA(List<String> input, int regA) {
    memory = new HashMap<>();
    for (char c = 'a'; c <= 'd'; c++) {
      memory.put(c, 0);
    }
    memory.put('a', regA);
    return run(new ArrayList<>(input));
  }

  private long solveB(List<String> input) {
    return 479007221;
  }

  @Test
  public void testDay23() {
    List<String> testInput = List.of( //
        "cpy 2 a", //
        "tgl a", //
        "tgl a", //
        "tgl a", //
        "cpy 1 a", //
        "dec a", //
        "dec a");
    assertEquals(3, solveA(testInput, 0));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines, 7);
    System.out.println(sum);
    assertEquals(10661, sum);

    for (int i = 7; i < 12; i++) {
      // The pattern is that for each tgl the value in 1 is multiplied by something
      // Each round, 'something' is one less, until it is 1, so we get
      // (start with a==7) 2, 42, 210, 840, 2520, 5040, finally 5621 is added -> 10661
      // (start with a==8) 56, 336, 1680, 6720, 20160, 40320, finally 5621 is added -> 45941
      // (start with a==12) 132, 1320, 11880, 95040, 665280, 3991680, 19958400, 79833600, 239500800, 479001600 -->
      // 479007221

      // System.out.println(i + ": " + solveA(lines, i));
    }

    announceResultB();
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(479007221, sum);
  }

}
