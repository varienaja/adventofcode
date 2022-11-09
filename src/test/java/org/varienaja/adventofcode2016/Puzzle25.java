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
public class Puzzle25 extends PuzzleAbs {
  private Map<Character, Integer> memory;

  private String run(List<String> input) {
    StringBuilder sb = new StringBuilder();

    int ip = 0;
    while (ip < input.size() && sb.length() < 20) {
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
      } else if ("out".equals(parts[0])) { // Additional instruction
        int x;
        char c = parts[1].charAt(0);
        if (c >= 'a' && c <= 'z') {
          x = memory.get(c);
        } else {
          x = Integer.parseInt(parts[1]);
        }

        sb.append(x);
      }
    }

    return sb.toString();
  }

  private long solveA(List<String> input) {
    for (int i = 0; i < 10000; i++) {
      StringBuilder sb = new StringBuilder();

      int a = i;
      int b = 0;

      int d = a + 2532;
      while (sb.length() < 20) {
        a = d;
        while (a != 0 && sb.length() < 20) {
          b = a % 2;
          a = a / 2;
          sb.append(b);
        }
      }
      if (sb.toString().equals("01010101010101010101")) {
        System.out.println(i);
      }

    }

    // FIXME WTF my run does only generate 1111111 or 000000???

    int startA = 0;
    String result;
    do {
      startA++;
      memory = new HashMap<>();
      for (char c = 'a'; c <= 'd'; c++) {
        memory.put(c, 0);
      }

      System.out.print(startA + ": ");
      memory.put('a', startA);
      result = run(input);
      System.out.println(result);
    } while (!"0101010101010101010101010".equals(result));
    return startA;
  }

  private long solveB(List<String> input) {
    return -1;
  }

  @Test
  public void testDay25() {
    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(-1, sum); // 2060 too high

    announceResultB();
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(-1, sum);
  }

}
