package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  private long solveA(long[] lines, int preamble) {
    for (int i = preamble; i < lines.length; i++) {
      boolean matchFound = false;
      next: for (int j = i - preamble; j < i - 1; j++) {
        for (int k = j + 1; k < i; k++) {
          if (lines[j] + lines[k] == lines[i]) {
            matchFound = true;
            break next;
          }
        }
      }
      if (!matchFound) {
        return lines[i];
      }
    }
    return -1;
  }

  private long solveB(long[] lines, long toFind) {
    int end = 0;
    int sum = 0;
    int start = 0;
    while (sum != toFind) {
      if (sum < toFind) {
        sum += lines[end++];
      } else if (sum > toFind) {
        sum -= lines[start++];
      }
    }

    long[] matchingRun = new long[end - start];
    System.arraycopy(lines, start, matchingRun, 0, end - start);
    LongSummaryStatistics lss = LongStream.of(matchingRun).summaryStatistics();
    return lss.getMin() + lss.getMax();
  }

  @Test
  public void testDay09() {
    long[] input = {
        35L, 20L, 15L, 25L, 47L, 40L, 62L, 55L, 65L, 95L, 102L, 117L, 150L, 182L, 127L, 219L, 299L, 277L, 309L, 576L
    };
    assertEquals(127, solveA(input, 5));

    announceResultA();
    List<String> lines = getInput();
    long[] numbers = lines.stream().map(Long::parseLong).mapToLong(l -> l).toArray();
    long result = solveA(numbers, 25);
    assertEquals(36845998L, result);
    System.out.println(result);

    announceResultB();
    assertEquals(62, solveB(input, 127));
    result = solveB(numbers, result);
    assertEquals(4830226L, result);
    System.out.println(result);
  }

}
