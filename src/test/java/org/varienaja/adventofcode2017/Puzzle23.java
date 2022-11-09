
package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {

  private long solveA(List<String> input) {
    long mulCount = 0;

    Map<Character, Long> register2Value = new HashMap<>();

    int ip = 0;
    while (ip < input.size()) {
      String line = input.get(ip);
      String[] parts = line.split("\\s");
      char register = parts[1].charAt(0);
      long val = 0;
      if (parts.length == 3) {
        if (Character.isAlphabetic(parts[2].charAt(0))) {
          val = register2Value.getOrDefault(parts[2].charAt(0), 0L);
        } else {
          val = Integer.parseInt(parts[2]);
        }
      }
      long value = val;

      if ("set".equals(parts[0])) {
        register2Value.put(register, value);
      } else if ("sub".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 : v - value);
      } else if ("mul".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 : v * value);
        mulCount++;
      }

      if ("jnz".equals(parts[0])) {
        if (Character.isAlphabetic(register)) { // >aargh register may be numeric too!
          val = register2Value.getOrDefault(register, 0L);
        } else {
          val = Integer.parseInt(parts[1]);
        }
        if (val != 0L) {
          ip += value;
        } else {
          ip++;
        }
      } else {
        ip++;
      }
    }

    return mulCount;
  }

  private long solveB(List<String> input) {
    int a = 1;
    int b = 0;
    int c = 0;
    int d = 0;
    int e = 0;
    int f = 0;
    int g = 0;
    int h = 0;

    b = 67;
    c = b;
    if (a != 0) {
      b = b * 100 + 100000;
      c = b + 17000;
    }

    do {
      f = 1;
      d = 2;
      e = 2;
      for (d = 2; d * d <= b; d++) {
        if (b % d == 0) {
          f = 0;
          break;
        }
      }
      if (f == 0) {
        h++;
      }
      g = b - c;
      b += 17;
    } while (g != 0);

    return h;
  }

  @Test
  public void testDay22() {
    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(4225, result);

    announceResultB();
    long stepCount = solveB(lines);
    System.out.println(stepCount);
    assertEquals(905, stepCount);
  }

}
