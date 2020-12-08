package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  private IntSummaryStatistics solveA(List<String> lines) {
    return lines.stream().map(this::solveInternal).mapToInt(i -> i).summaryStatistics();
  }

  private int solveB(List<String> lines) {
    Set<Integer> allSeatIDsExceptMine = lines.stream().map(this::solveInternal).collect(Collectors.toSet());

    IntSummaryStatistics s = solveA(lines);
    return IntStream.rangeClosed(s.getMin(), s.getMax()) //
        .filter(id -> !allSeatIDsExceptMine.contains(id)) //
        .findFirst() //
        .orElse(-1);
  }

  private int solveInternal(String input) {
    String id = input.replaceAll("F|L", "0").replaceAll("B|R", "1");
    return Integer.parseInt(id, 2);
  }

  @Test
  public void testDay05() throws IOException, URISyntaxException {
    assertEquals(567, solveInternal("BFFFBBFRRR"));
    assertEquals(119, solveInternal("FFFBBBFRRR"));
    assertEquals(820, solveInternal("BBFFBBFRLL"));

    announceResultA();
    List<String> lines = getInput();
    int result = solveA(lines).getMax();
    assertEquals(922, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines);
    assertEquals(747, result);
    System.out.println(result);
  }

}
