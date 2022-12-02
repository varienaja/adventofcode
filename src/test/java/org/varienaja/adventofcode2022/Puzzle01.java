package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    assertEquals(66186L, result);
    System.out.println(result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    assertEquals(196804L, result);
    System.out.println(result);
  }

  private long solveA(List<String> lines) {
    return sumTop(lines, 1);
  }

  private long solveB(List<String> lines) {
    return sumTop(lines, 3);
  }

  private long sumTop(List<String> lines, int count) {
    NavigableSet<Long> elfCalories = new TreeSet<>((l1, l2) -> Long.compare(l2, l1));

    long elfCals = 0L;
    for (String l : lines) {
      if (l.isEmpty()) {
        elfCalories.add(elfCals);
        if (elfCalories.size() > count) { // Optimization for very large inputs
          elfCalories.pollLast();
        }
        elfCals = 0L;
      } else {
        elfCals += Long.parseLong(l);
      }
    }

    return elfCalories.stream().mapToLong(l -> l).sum();
  }

  @Test
  public void testAB() {
    List<String> testInput = List.of( //
        "1000", //
        "2000", //
        "3000", //
        "", //
        "4000", //
        "", //
        "5000", //
        "6000", //
        "", //
        "7000", //
        "8000", //
        "9000", //
        "", //
        "10000", //
        "" //
    );
    assertEquals(24000, solveA(testInput));
    assertEquals(45000, solveB(testInput));
  }

}
