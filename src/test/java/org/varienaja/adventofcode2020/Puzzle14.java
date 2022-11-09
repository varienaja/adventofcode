package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {

  private long[] mask(long value, char[] mask, char exceptionChar) {
    char[] maskedValue = toBinary(value, mask.length);
    SortedSet<Integer> xPositions = new TreeSet<>();
    for (int i = 0; i < mask.length; i++) {
      if (mask[i] != exceptionChar) {
        maskedValue[i] = mask[i];
        if (maskedValue[i] == 'X') {
          xPositions.add(i);
        }
      }
    }

    long[] answers = new long[1 << xPositions.size()];
    char[] looper = null;
    for (int i = 0; i < answers.length; i++) {
      looper = toBinary(i, xPositions.size());

      int xIndex = 0;
      char[] maskedAddress = Arrays.copyOf(maskedValue, maskedValue.length);
      for (int xPosition : xPositions) {
        maskedAddress[xPosition] = looper[xIndex++];
      }
      answers[i] = Long.parseLong(new String(maskedAddress), 2);
    }
    return answers;
  }

  private long solve(List<String> lines, char exceptionChar) {
    Map<Long, Long> mem = new HashMap<>();
    char[] mask = null;

    for (String line : lines) {
      String[] parts = line.split("\\s+=\\s+");
      if ("mask".equals(parts[0])) {
        mask = parts[1].toCharArray();
      } else {
        long ix = Long.parseLong(parts[0].replaceAll("\\D+", ""));
        long value = Long.parseLong(parts[1]);

        if (exceptionChar == 'X') {
          Arrays.stream(mask(value, mask, exceptionChar)).forEach(masked -> mem.put(ix, masked));
        } else {
          Arrays.stream(mask(ix, mask, exceptionChar)).forEach(masked -> mem.put(masked, value));
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
  public void testDay14() {
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

  private char[] toBinary(long value, int length) {
    StringBuilder sb = new StringBuilder(Long.toBinaryString(value));
    while (sb.length() < length) {
      sb.insert(0, '0');
    }
    return sb.toString().toCharArray();
  }

}
