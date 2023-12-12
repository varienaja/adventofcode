package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {
  private Map<String, Long> key2SolutionCount;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(7653L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(60681419004564L, result);
  }

  @Test
  public void testA() {
    assertEquals(1, countArrangements("???.### 1,1,3"));
    assertEquals(10, countArrangements("?###???????? 3,2,1"));
    assertEquals(4, countArrangements(".??..??...?##. 1,1,3"));
    assertEquals(3, countArrangements("?.??..#?##?# 1,6"));

    assertEquals(21, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(".#?.#?.#?.#?.# 1,1,1,1,1", repeat(".# 1", 5));
    assertEquals(506250, countArrangements(repeat("?###???????? 3,2,1", 5)));

    assertEquals(525152, solveB(getStreamingTestInput()));
  }

  long countArrangements(String s) {
    key2SolutionCount = new HashMap<>();

    String[] parts = s.split("\\s+");
    List<Long> lengths = parseNumbers(parts[1], ",");

    return solve(parts[0], lengths, 0, 0, 0);
  }

  private String repeat(String s, int count) {
    String[] parts = s.split("\\s+");

    String prefix = IntStream.range(0, count).mapToObj(i -> parts[0]).collect(Collectors.joining("?"));
    String suffix = IntStream.range(0, count).mapToObj(i -> parts[1]).collect(Collectors.joining(","));

    return prefix + " " + suffix;
  }

  private long solve(Stream<String> lines, boolean partB) {
    return lines //
        .map(line -> repeat(line, partB ? 5 : 1)) //
        .mapToLong(this::countArrangements) //
        .sum();
  }

  private long solve(String record, List<Long> tester, int recordIx, int testerIx, long currentRunLength) {
    String key = "" + recordIx + "," + testerIx + "," + currentRunLength;
    if (key2SolutionCount.containsKey(key)) {
      return key2SolutionCount.get(key);
    }

    if (recordIx == record.length()) {
      if (testerIx == tester.size() && currentRunLength == 0) {
        return 1;
      }
      if (testerIx == tester.size() - 1 && tester.get(testerIx) == currentRunLength) {
        return 1;
      }
      return 0;
    }

    long result = 0;
    for (char c : ".#".toCharArray()) {
      if (record.charAt(recordIx) == c || record.charAt(recordIx) == '?') {
        if (c == '.' && currentRunLength == 0) {
          result += solve(record, tester, recordIx + 1, testerIx, 0);
        } else if (c == '.' && currentRunLength > 0 && testerIx < tester.size() && tester.get(testerIx) == currentRunLength) {
          result += solve(record, tester, recordIx + 1, testerIx + 1, 0);
        } else if (c == '#') {
          result += solve(record, tester, recordIx + 1, testerIx, currentRunLength + 1);
        }
      }
    }
    key2SolutionCount.put(key, result);

    return result;
  }

  private long solveA(Stream<String> lines) {
    return solve(lines, false);
  }

  private long solveB(Stream<String> lines) {
    return solve(lines, true);
  }

}
