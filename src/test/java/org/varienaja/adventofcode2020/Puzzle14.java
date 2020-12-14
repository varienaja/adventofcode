package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {

  private List<Long> mask(long value, String mask, char exceptionChar) {
    char[] masked = new char[mask.length()];
    Arrays.fill(masked, '0');
    char[] xx = Long.toBinaryString(value).toCharArray();
    System.arraycopy(xx, 0, masked, masked.length - xx.length, xx.length);
    int floatCount = 0;
    for (int i = 0; i < mask.length(); i++) {
      if (mask.charAt(i) != exceptionChar) {
        masked[i] = mask.charAt(i);
        if (masked[i] == 'X') {
          floatCount++;
        }
      }
    }

    List<Long> answers = new LinkedList<>();
    int max = 1 << floatCount;
    for (int i = 0; i < max; i++) {
      char[] maskedAddress = new char[masked.length];
      System.arraycopy(masked, 0, maskedAddress, 0, masked.length);

      char[] looper = new char[floatCount];
      if (floatCount > 0) {
        Arrays.fill(looper, '0');
        char[] bb = Long.toBinaryString(i).toCharArray();
        System.arraycopy(bb, 0, looper, looper.length - bb.length, bb.length);
      }

      int pos = 0;
      for (int m = 0; m < maskedAddress.length; m++) {
        if (maskedAddress[m] == 'X') {
          maskedAddress[m] = looper[pos++];
        }
      }
      answers.add(Long.parseLong(new String(maskedAddress), 2));
    }
    return answers;
  }

  private long solve(List<String> lines, char exceptionChar) {
    Map<Long, Long> mem = new HashMap<>();
    String mask = "";

    for (String line : lines) {
      String[] parts = line.split("\\s+=\\s+");
      if ("mask".equals(parts[0])) {
        mask = parts[1];
      } else {
        long ix = Long.parseLong(parts[0].replaceAll("\\D+", ""));
        long value = Long.parseLong(parts[1]);

        if (exceptionChar == 'X') {
          mask(value, mask, exceptionChar).forEach(masked -> mem.put(ix, masked));
        } else {
          mask(ix, mask, '0').forEach(masked -> mem.put(masked, value));
        }
      }
    }

    return mem.values().stream().mapToLong(l -> l).summaryStatistics().getSum();
  }

  private long solveA(List<String> lines) {
    return solve(lines, 'X');
  }

  private long solveB(List<String> lines) {
    return solve(lines, '0');
  }

  @Test
  public void testDay12() {
    List<String> input = Arrays.asList( //
        "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X", //
        "mem[8] = 11", //
        "mem[7] = 101", //
        "mem[8] = 0"//
    );
    assertEquals(165, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(13496669152158L, result);
    System.out.println(result);

    input = Arrays.asList(//
        "mask = 000000000000000000000000000000X1001X", //
        "mem[42] = 100", //
        "mask = 00000000000000000000000000000000X0XX", //
        "mem[26] = 1"//
    );
    assertEquals(208L, solveB(input));
    announceResultB();
    result = solveB(lines);
    assertEquals(3278997609887L, result);
    System.out.println(result);
  }

}
