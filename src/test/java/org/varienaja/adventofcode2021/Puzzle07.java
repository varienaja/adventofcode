package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  private long solveA(String input) {
    List<Long> poss = Arrays.stream(input.split(",")).map(s -> Long.parseLong(s)).sorted().collect(Collectors.toList());
    long middleElt = poss.get(poss.size() / 2);
    // The middle element has just as many smaller number on the right as it has bigger on the left
    // Move all crabs to this position causes the least amount of steps

    return poss.stream().mapToLong(p -> Math.abs(p - middleElt)).sum();
  }

  private long solveB(String input) {
    List<Long> poss = Arrays.stream(input.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toList());
    double avg = poss.stream().mapToLong(l -> l).summaryStatistics().getAverage();
    long candidate1 = (long)Math.floor(avg);
    long candidate2 = (long)Math.ceil(avg);
    // When cost is not linear but cubic, we need to take the average. Since
    // this is a double we have to try two horizontal positions
    // For skeptics, change candidate1 and 2 to min(poss) and max(poss)

    return LongStream.rangeClosed(candidate1, candidate2).map(hpos -> {
      return poss.stream().mapToLong(p -> Math.abs(p - hpos)).map(dist -> (dist * (1 + dist)) / 2).sum();
    }).min().orElse(0L);
  }

  @Test
  public void testDay07() {
    String testInput = "16,1,2,0,4,2,7,1,2,14";
    assertEquals(37, solveA(testInput));

    announceResultA();
    String lines = getInput().get(0);
    long result = solveA(lines);
    System.out.println(result);

    assertEquals(168, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(96744904, result);
  }

}
