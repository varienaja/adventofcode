package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  private long solve(String input, boolean complicatedFuelFormula) {
    List<Integer> poss = Arrays.stream(input.split(",")).map(s -> Integer.parseInt(s)).collect(Collectors.toList());

    IntSummaryStatistics stats = poss.stream().mapToInt(i -> i).summaryStatistics();

    long minFuel = Long.MAX_VALUE;
    for (int hpos = stats.getMin(); hpos < stats.getMax(); hpos++) {
      int fuel = 0;
      for (Integer p : poss) {
        int dist = Math.abs((p - hpos));
        if (complicatedFuelFormula) {
          fuel += (dist * (1 + dist)) / 2;
        } else {
          fuel += dist;
        }
      }

      if (fuel < minFuel) {
        minFuel = fuel;
      }
    }

    return minFuel;
  }

  @Test
  public void testDay07() {
    String testInput = "16,1,2,0,4,2,7,1,2,14";
    assertEquals(37, solve(testInput, false));

    announceResultA();
    String lines = getInput().get(0);
    long result = solve(lines, false);
    System.out.println(result);
    assertEquals(343605, result);

    assertEquals(168, solve(testInput, true));
    announceResultB();
    result = solve(lines, true);
    System.out.println(result);
    assertEquals(96744904, result);
  }

}
