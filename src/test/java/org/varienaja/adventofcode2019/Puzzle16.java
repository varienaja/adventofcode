package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {

  private StringBuilder calcPhase(StringBuilder input, int index) {
    int[] pattern = new int[] {
        0, 1, 0, -1
    };
    if (index == 0) {
      for (int ix = 0; ix < input.length(); ix++) {
        int[] thisPattern = new int[input.length() + 1];
        int pIx = 0;

        for (int i = 0; i < thisPattern.length; i++) {
          thisPattern[i] = pattern[pIx % 4];
          if ((i + 1) % (ix + 1) == 0) {
            pIx++;
          }
        }

        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
          int p = thisPattern[i + 1];
          sum += p * (input.charAt(i) - '0');
        }

        int last = Math.abs(sum) % 10;
        char lastC = ("" + last).charAt(0);
        input.setCharAt(ix, lastC);
      }
    } else {
      // IF index is huge, we only have 0 and 1 in 'thisPattern', since the puzzle inputs are such that
      // thisPattern[index]==1 and thisPattern[index-1]==0, we only have to add
      // input[index]...input[input.length-1] to get the number at index.
      // If we start at the end, we can even avoid doing many add-operations twice or more.
      int sum = 0;
      for (int ix = input.length() - 1; ix >= index; ix--) {
        sum += (input.charAt(ix) - '0');

        int last = Math.abs(sum) % 10;
        char lastC = ("" + last).charAt(0);
        input.setCharAt(ix, lastC);
      }
    }

    return input;
  }

  @Test
  public void doA() {
    announceResultA();
    String sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals("50053207", sum);
  }

  @Test
  public void doB() {
    announceResultB();
    String sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals("32749588", sum);
  }

  private String solve(String input, int ix) {
    StringBuilder sb = new StringBuilder(input);
    for (int i = 0; i < 100; i++) {
      calcPhase(sb, ix);
    }
    return sb.substring(ix, ix + 8);
  }

  private String solveA(String input) {
    return solve(input, 0);
  }

  private String solveB(String input) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 10000; i++) {
      sb.append(input);
    }

    int ix = Integer.parseInt(input.substring(0, 7));
    return solve(sb.toString(), ix);
  }

  @Test
  public void testA() {
    StringBuilder sb = new StringBuilder("12345678");
    assertEquals("48226158", calcPhase(sb, 0).toString());

    assertEquals("24176176", solveA("80871224585914546619083218645595"));
    assertEquals("73745418", solveA("19617804207202209144916044189917"));
    assertEquals("52432133", solveA("69317163492948606335995924319873"));
  }

  @Test
  public void testB() {
    assertEquals("84462026", solveB("03036732577212944063491565474664"));
    assertEquals("78725270", solveB("02935109699940807407585447034323"));
    assertEquals("53553731", solveB("03081770884921959731165446850517"));
  }

}
