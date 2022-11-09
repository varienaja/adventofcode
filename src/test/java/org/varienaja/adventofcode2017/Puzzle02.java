package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  private int solveA(List<String> inInput) {
    int checksum = 0;
    for (String line : inInput) {
      Pattern pattern = Pattern.compile("\\s+");
      IntSummaryStatistics stats = pattern.splitAsStream(line).map(Integer::parseInt).collect(Collectors.summarizingInt(Integer::intValue));
      int diff = stats.getMax() - stats.getMin();
      checksum += diff;
    }

    return checksum;
  }

  private int solveB(List<String> inInput) {
    int checksum = 0;
    outer: for (String line : inInput) {
      Pattern pattern = Pattern.compile("\\s+");
      Integer[] numbers = pattern.splitAsStream(line).map(Integer::parseInt).toArray(Integer[]::new);
      // find divisors
      for (int i = 0; i < numbers.length; i++) {
        for (int j = 0; j < numbers.length; j++) {
          if (numbers[i] > numbers[j] && numbers[i] % numbers[j] == 0) {
            int diff = numbers[i] / numbers[j];
            checksum += diff;
            continue outer;
          }
        }
      }
    }

    return checksum;
  }

  @Test
  public void testDay2() throws Exception {
    List<String> testLines = Arrays.asList("5 1 9 5", "7 5 3", "2 4 6 8");
    assertEquals(18, solveA(testLines));

    System.out.print("Solution 2a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day02.txt").toURI()));
    System.out.println(solveA(lines));

    testLines = Arrays.asList("5 8 2 8", "9 4 7 3", "3 8 6 5");
    assertEquals(9, solveB(testLines));

    System.out.print("Solution 2b: ");
    System.out.println(solveB(lines));
  }

}
