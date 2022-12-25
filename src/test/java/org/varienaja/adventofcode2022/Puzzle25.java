package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {
  private Map<Character, Integer> snafu2decimal = Map.of('2', 2, '1', 1, '0', 0, '-', -1, '=', -2);
  private Map<Integer, Character> decimal2snafu = snafu2decimal.entrySet().stream().collect(Collectors.toMap(Entry::getValue, Entry::getKey));

  @Test
  public void doA() {
    announceResultA();
    String result = solveA(getInput());
    System.out.println(result);
    assertEquals("2-=2-0=-0-=0200=--21", result);
  }

  @Test
  public void testA() {
    assertEquals("2=-1=0", solveA(getTestInput()));
  }

  private String addSnafu(String s1, String s2) {
    StringBuilder a = new StringBuilder(s1).reverse();
    StringBuilder b = new StringBuilder(s2).reverse();
    char[] result = new char[Math.max(a.length(), b.length()) + 1];
    int carry = 0;

    for (int p = 0; p < result.length; p++) {
      int digit1 = p < a.length() ? snafu2decimal.get(a.charAt(p)) : 0;
      int digit2 = p < b.length() ? snafu2decimal.get(b.charAt(p)) : 0;
      int sum = digit1 + digit2 + carry;

      if (sum < -2) {
        sum += 5;
        carry = -1;
      } else if (sum > 2) {
        sum -= 5;
        carry = 1;
      } else {
        carry = 0;
      }

      result[result.length - 1 - p] = decimal2snafu.get(sum);
    }

    int x = result[0] == '0' ? 1 : 0; // Remove prefix-zero, if exists
    return String.copyValueOf(result, x, result.length - x);
  }

  private List<String> getTestInput() {
    return List.of("1=-0-2", //
        "12111", //
        "2=0=", //
        "21", //
        "2=01", //
        "111", //
        "20012", //
        "112", //
        "1=-1=", //
        "1-12", //
        "12", //
        "1=", //
        "122");
  }

  private String solveA(List<String> lines) {
    return lines.stream().reduce(this::addSnafu).orElseThrow();
  }

}
