package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  private long getMostCommonPattern(List<String> lines, boolean inverse) {
    Set<String> linesSet = new HashSet<>(lines);
    int ix = 0;
    while (linesSet.size() > 1) {
      Set<String> ones = new HashSet<>();
      Set<String> zeroes = new HashSet<>();

      for (String line : linesSet) {
        (line.charAt(ix) == '1' ? ones : zeroes).add(line);
      }
      ix++;

      if (inverse) {
        linesSet = ones.size() >= zeroes.size() ? zeroes : ones;
      } else {
        linesSet = ones.size() >= zeroes.size() ? ones : zeroes;
      }
    }
    return Integer.parseInt(linesSet.iterator().next(), 2);
  }

  private long solveA(List<String> lines) {
    StringBuilder gamma = new StringBuilder();
    StringBuilder epsilon = new StringBuilder();

    IntStream.range(0, lines.get(0).length()).forEach(ix -> {
      long oneCount = lines.stream().filter(line -> line.charAt(ix) == '1').count();

      boolean moreOnes = oneCount > lines.size() - oneCount;
      gamma.append(moreOnes ? "1" : "0");
      epsilon.append(moreOnes ? "0" : "1");
    });

    return Integer.parseInt(gamma.toString(), 2) * Integer.parseInt(epsilon.toString(), 2);
  }

  private long solveB(List<String> lines) {
    long oxy = getMostCommonPattern(lines, false);
    long co2Scrub = getMostCommonPattern(lines, true);
    return oxy * co2Scrub;
  }

  @Test
  public void testDay01() {
    List<String> testInput = Arrays.asList("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010");
    assertEquals(198, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(3895776, result);
    System.out.println(result);

    assertEquals(230, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    assertEquals(7928162, result);
    System.out.println(result);
  }

}
