package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {
  private static final long FACTORA = 16807;
  private static final long FACTORB = 48271;

  private long solveA(int rounds, List<String> input) {
    long a = Integer.parseInt(input.get(0).substring(input.get(0).lastIndexOf(' ') + 1));
    long b = Integer.parseInt(input.get(1).substring(input.get(1).lastIndexOf(' ') + 1));

    int matches = 0;
    for (int i = 0; i < rounds; i++) {
      a *= FACTORA;
      a %= 2147483647;

      b *= FACTORB;
      b %= 2147483647;

      long xorred = a ^ b;
      long lowest = Long.lowestOneBit(xorred);
      if (lowest >= 65535) {
        matches++;
      }
    }

    return matches;
  }

  private long solveB(int rounds, List<String> input) {
    long a = Integer.parseInt(input.get(0).substring(input.get(0).lastIndexOf(' ') + 1));
    long b = Integer.parseInt(input.get(1).substring(input.get(1).lastIndexOf(' ') + 1));

    int matches = 0;
    for (int i = 0; i < rounds; i++) {
      do {
        a *= FACTORA;
        a %= 2147483647;
      } while (a % 4 != 0);

      do {
        b *= FACTORB;
        b %= 2147483647;
      } while (b % 8 != 0);

      long xorred = a ^ b;
      long lowest = Long.lowestOneBit(xorred);
      if (lowest >= 65535) {
        matches++;
      }
    }

    return matches;
  }

  @Test
  public void testDay14() {
    List<String> testInput = List.of( //
        "Generator A starts with 65", //
        "Generator B starts with 8921");

    assertEquals(1, solveA(5, testInput));
    assertEquals(588, solveA(40000000, testInput));

    announceResultA();
    List<String> input = getInput();
    long result = solveA(40000000, input);
    System.out.println(result);
    assertEquals(569, result);

    assertEquals(1, solveB(1056, testInput));
    assertEquals(309, solveB(5000000, testInput));
    announceResultB();
    result = solveB(5000000, input);
    System.out.println(result);
    assertEquals(298, result);
  }

}
