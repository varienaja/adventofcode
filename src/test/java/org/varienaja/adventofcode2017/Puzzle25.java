
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
public class Puzzle25 extends PuzzleAbs {
  private Map<Integer, Boolean> tape;
  private int pointer;
  private int step;
  private int steps;

  private char a() {
    boolean b = tape.getOrDefault(pointer, false);
    if (!b) {
      tape.put(pointer, true);
      pointer++;
      return 'b';
    } else {
      tape.put(pointer, false);
      pointer--;
      return 'c';
    }
  }

  private char b() {
    boolean b = tape.getOrDefault(pointer, false);
    if (!b) {
      tape.put(pointer, true);
      pointer--;
      return 'a';
    } else {
      tape.put(pointer, true);
      pointer++;
      return 'd';
    }
  }

  private char c() {
    boolean b = tape.getOrDefault(pointer, false);
    if (!b) {
      tape.put(pointer, false);
      pointer--;
      return 'b';
    } else {
      tape.put(pointer, false);
      pointer--;
      return 'e';
    }
  }

  private char d() {
    boolean b = tape.getOrDefault(pointer, false);
    if (!b) {
      tape.put(pointer, true);
      pointer++;
      return 'a';
    } else {
      tape.put(pointer, false);
      pointer++;
      return 'b';
    }
  }

  private char e() {
    boolean b = tape.getOrDefault(pointer, false);
    if (!b) {
      tape.put(pointer, true);
      pointer--;
      return 'f';
    } else {
      tape.put(pointer, true);
      pointer--;
      return 'c';
    }
  }

  private char f() {
    boolean b = tape.getOrDefault(pointer, false);
    if (!b) {
      tape.put(pointer, true);
      pointer++;
      return 'd';
    } else {
      tape.put(pointer, true);
      pointer++;
      return 'a';
    }
  }

  private long solveA(List<String> input) {
    step = 0;
    pointer = 0;
    tape = new HashMap<>();

    char next = 'a';
    while (step < steps) {
      char result = ' ';
      if (next == 'a') {
        result = a();
      } else if (next == 'b') {
        result = b();
      } else if (next == 'c') {
        result = c();
      } else if (next == 'd') {
        result = d();
      } else if (next == 'e') {
        result = e();
      } else if (next == 'f') {
        result = f();
      }
      next = result;
      step++;
    }

    return tape.values().stream().mapToInt(b -> b ? 1 : 0).sum();
  }

  @Test
  public void testDay25() {
    steps = 12481997;
    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(3362, result);
  }

}
