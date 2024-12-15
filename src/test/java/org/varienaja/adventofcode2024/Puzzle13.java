package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {
  class ClawMachine {
    private long xA;
    private long xB;
    private long yA;
    private long yB;
    private long xPrize;
    private long yPrize;

    ClawMachine(String ba, String bb, String p, long skew) {
      Pattern buttonPattern = Pattern.compile("Button\\s+.:\\s+X\\+(\\d+),\\s+Y\\+(\\d+)");
      Matcher m = buttonPattern.matcher(ba);
      assert (m.matches());
      xA = Long.parseLong(m.group(1));
      yA = Long.parseLong(m.group(2));

      m = buttonPattern.matcher(bb);
      assert (m.matches());
      xB = Long.parseLong(m.group(1));
      yB = Long.parseLong(m.group(2));

      Pattern prizePattern = Pattern.compile("Prize:\\s+X=(\\d+),\\s+Y=(\\d+)");
      m = prizePattern.matcher(p);
      assert (m.matches());
      xPrize = Long.parseLong(m.group(1)) + skew;
      yPrize = Long.parseLong(m.group(2)) + skew;
    }

    @Override
    public String toString() {
      return "" + xA + " " + yA;
    }

    long minCost() {
      // Suppose yA = 34, xA = 94, yB = 67, xB = 22
      // We make two simple functions (f: y= a*x + bb): f1: y = 34/94 * x and f2: : y = 67/22 * x
      long numerator1 = yA;
      long demoninator1 = xA;
      long numerator2 = yB;
      long denominator2 = xB;

      // f1 begins in (0,0). That is already good. f2 should go through (px,py) (8400,5400): we have to add a value for
      // that, so f2 becomes: y = 67/22 * x - 20181.82
      long bb = yPrize * denominator2 - xPrize * numerator2; // (Still has to be divided by xB but we do that later so
                                                             // we can use a long instead of a double)

      // We multiply the fractions with denominator of the other, so we can subtract them
      numerator1 *= denominator2;
      demoninator1 *= denominator2;
      numerator2 *= demoninator1;
      denominator2 *= demoninator1;

      // Now we calculate where these two functions cross
      // 748/2068 * x = 6298/2068 * x - 20181.82 --> 5550/2068 * x = 20181.82 --> x = 7520 -> a = 80, b = 40
      numerator1 -= numerator2;
      long x = (bb * demoninator1 / (numerator1 * xB /* now is later */));
      long a = x / xA;
      long b = (xPrize - x) / xB;

      // Check if result is precise as we can't do fractional button presses
      if (a * xA + b * xB == xPrize && a * yA + b * yB == yPrize) {
        return COSTA * a + COSTB * b;
      }
      return 0;
    }
  }

  private static final int COSTA = 3;
  private static final int COSTB = 1;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(38714, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(74015623345775L, result);
  }

  @Test
  public void testA() {
    assertEquals(480, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(875318608908L, solveB(getTestInput()));
  }

  private long solve(List<String> input, long skew) {
    long result = 0;

    for (int i = 0; i < input.size(); ++i) {
      result += new ClawMachine(input.get(i++), input.get(i++), input.get(i++), skew).minCost();
    }

    return result;
  }

  private long solveA(List<String> input) {
    return solve(input, 0);
  }

  private long solveB(List<String> input) {
    return solve(input, 10000000000000L);
  }

}
