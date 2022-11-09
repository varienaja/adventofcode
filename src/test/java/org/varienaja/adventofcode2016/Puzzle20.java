package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {

  private long solve(List<String> input, int max, boolean partB) {
    // I HATE JAVA, WHY IS THERE NO UNSIGNED INT? :-D
    BitSet bsLower = new BitSet(max);
    BitSet bsUpper = new BitSet(max);
    bsLower.set(0, max);
    bsUpper.set(0, max);

    Pattern p = Pattern.compile("(\\d+)-(\\d+)");
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        long from = Long.parseLong(m.group(1));
        long to = Long.parseLong(m.group(2));

        if (from >= max) {
          from -= max;
          to -= max;

          if (to > Integer.MAX_VALUE) {
            to -= 2;
          }

          bsUpper.set((int)from, (int)to + 1, false);
        } else {
          if (from < max) {
            if (to < max) {
              bsLower.set((int)from, (int)to + 1, false);
            } else {
              bsLower.set((int)from, max - 1, false);
              to -= max;
              bsUpper.set(0, (int)to + 1, false);
            }
          }
        }
      }
    }

    long result;
    if (partB) {
      result = bsLower.cardinality();
      if (max >= Integer.MAX_VALUE) {
        result += bsUpper.cardinality() - 1; // WTF -1?;
      }
    } else {
      result = bsLower.nextSetBit(0);
    }

    return result;
  }

  private long solveA(List<String> input, int max) {
    return solve(input, max, false);
  }

  private long solveB(List<String> input, int max) {
    return solve(input, max, true);
  }

  @Test
  public void testDay00() {
    List<String> testInput = List.of( //
        "5-8", //
        "0-2", //
        "4-7");
    assertEquals(3, solveA(testInput, 10));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines, Integer.MAX_VALUE);
    System.out.println(sum);
    assertEquals(32259706L, sum);

    announceResultB();
    assertEquals(2, solveB(testInput, 10));
    sum = solveB(lines, Integer.MAX_VALUE);
    System.out.println(sum);
    assertEquals(113, sum);
  }

}
