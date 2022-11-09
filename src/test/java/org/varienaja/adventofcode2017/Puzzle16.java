package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {

  private String solveA(int programCount, String input) {
    char[] programs = new char[programCount];
    for (char i = 0; i < programCount; i++) {
      int p = 'a' + i;
      programs[i] = (char)p;
    }

    String[] moves = input.split(",");
    transpose(programs, moves);
    return String.valueOf(programs);
  }

  private String solveB(int repeatCount, int programCount, String input) {
    char[] programs = new char[programCount];
    for (char i = 0; i < programCount; i++) {
      int p = 'a' + i;
      programs[i] = (char)p;
    }

    Map<String, Integer> transpositions = new HashMap<>();
    String[] moves = input.split(",");
    for (int i = 0; i < repeatCount; i++) {
      String transposed = String.valueOf(programs);
      if (transpositions.get(transposed) == null) {
        transpositions.put(transposed, i);
      } else { // Found cycle with length transpositions.size();
        int offset = repeatCount % transpositions.size();

        for (Entry<String, Integer> e : transpositions.entrySet()) {
          if (e.getValue() == offset) {
            return e.getKey();
          }
        }
      }

      transpose(programs, moves);
    }

    // Repeats!!, So I've programmed cycle-detection
    // 1000 ibmchklnofjpdeag
    // 2000 obdmnjpeakfgchil
    // 3000 abcdefghijklmnop
    // 4000 ibmchklnofjpdeag
    // 5000 obdmnjpeakfgchil
    // 6000 abcdefghijklmnop
    // 7000 ibmchklnofjpdeag
    // 8000 obdmnjpeakfgchil
    // 9000 abcdefghijklmnop
    // 10000 ibmchklnofjpdeag

    return String.valueOf(programs);
  }

  @Test
  public void testDay14() {
    String testInput = "s1,x3/4,pe/b";

    assertEquals("baedc", solveA(5, testInput));

    announceResultA();
    List<String> input = getInput();
    String result = solveA(16, input.get(0));
    System.out.println(result);
    assertEquals("namdgkbhifpceloj", result);

    assertEquals("ceadb", solveB(2, 5, testInput));
    announceResultB();
    result = solveB(10000000, 16, input.get(0));
    System.out.println(result);
    assertEquals("ibmchklnofjpdeag", result);
  }

  private void transpose(char[] programs, String[] moves) {
    for (String m : moves) {
      if (m.startsWith("s")) {
        int spinSize = Integer.parseInt(m.substring(1));

        String suffix = String.valueOf(programs, programs.length - spinSize, spinSize);
        String prefix = String.valueOf(programs, 0, programs.length - spinSize);
        System.arraycopy((suffix + prefix).toCharArray(), 0, programs, 0, programs.length);
      } else if (m.startsWith("x")) {
        String[] parts = m.substring(1).split("/");
        int ix1 = Integer.parseInt(parts[0]);
        int ix2 = Integer.parseInt(parts[1]);

        char t = programs[ix1];
        programs[ix1] = programs[ix2];
        programs[ix2] = t;
      } else if (m.startsWith("p")) {
        int ix1 = String.valueOf(programs).indexOf(m.charAt(1));
        int ix2 = String.valueOf(programs).indexOf(m.charAt(3));

        char t = programs[ix1];
        programs[ix1] = programs[ix2];
        programs[ix2] = t;
      }
    }
  }

}
