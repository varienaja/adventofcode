package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {
  private char[] directions = {
      'N', 'E', 'S', 'W'
  };
  private Map<Character, AtomicInteger> steps;
  private int currentDirectionIx;
  // boat position, when mode=true
  private int x;
  private int y;
  private boolean mode; // false: puzzleA; true: puzzleB

  private void forward(int dist) {
    if (mode) {
      x += (steps.get('E').get() - steps.get('W').get()) * dist;
      y += (steps.get('N').get() - steps.get('S').get()) * dist;
    } else {
      steps.get(directions[currentDirectionIx]).getAndAdd(dist);
    }
  }

  private void rotateL(int degrees) {
    for (int deg = 0; deg < degrees; deg += 90) {
      if (mode) {
        int i = 0;
        AtomicInteger t = steps.get(directions[i]);
        for (i = 0; i < directions.length - 1;) {
          steps.put(directions[i], steps.get(directions[++i]));
        }
        steps.put(directions[i], t);
      } else {
        currentDirectionIx--;
        if (currentDirectionIx < 0) {
          currentDirectionIx += directions.length;
        }
      }
    }
  }

  private void rotateR(int degrees) {
    rotateL(360 - degrees);
  }

  private int solve(List<String> lines) {
    x = 0;
    y = 0;

    for (String line : lines) {
      char d = line.charAt(0);
      int dist = Integer.parseInt(line.substring(1));

      if (d == 'F') {
        forward(dist);
      } else if (d == 'L') {
        rotateL(dist);
      } else if (d == 'R') {
        rotateR(dist);
      } else {
        steps.get(d).getAndAdd(dist);
      }
    }

    if (!mode) {
      x = steps.get('E').get() - steps.get('W').get();
      y = steps.get('N').get() - steps.get('S').get();
    }
    return Math.abs(x) + Math.abs(y);
  }

  private int solveA(List<String> lines) {
    mode = false;
    currentDirectionIx = 1;
    steps = new HashMap<>();
    for (char c : directions) {
      steps.put(c, new AtomicInteger());
    }

    return solve(lines);
  }

  private int solveB(List<String> lines) {
    mode = true;
    steps = new HashMap<>();
    steps.put('N', new AtomicInteger(1));
    steps.put('E', new AtomicInteger(10));
    steps.put('S', new AtomicInteger());
    steps.put('W', new AtomicInteger());

    return solve(lines);
  }

  @Test
  public void testDay12() {
    List<String> input = Arrays.asList( //
        "F10", //
        "N3", //
        "F7", //
        "R90", //
        "F11");
    assertEquals(25, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    int result = solveA(lines);
    assertEquals(1294L, result);
    System.out.println(result);

    assertEquals(286L, solveB(input));
    announceResultB();
    result = solveB(lines);
    assertEquals(20592L, result);
    System.out.println(result);
  }

}
