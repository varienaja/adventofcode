package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  private int solveA(List<String> lines) {
    int sum = 0;

    Set<Integer> s = new HashSet<>();
    for (String line : lines) {
      sum += line.chars().boxed().filter(s::add).count();
      if (line.isEmpty()) {
        s.clear();
      }
    }
    return sum;
  }

  private int solveB(List<String> lines) {
    int sum = 0;

    Set<Set<Integer>> ss = new HashSet<>();
    for (String line : lines) {
      if (line.isEmpty()) { // count
        Set<Integer> result = ss.iterator().next();
        ss.stream().skip(1).forEach(result::retainAll);
        sum += result.size();
        ss.clear();
      } else {
        ss.add(line.chars().boxed().collect(Collectors.toSet()));
      }
    }
    return sum;
  }

  @Test
  public void testDay06() throws IOException, URISyntaxException {
    assertEquals(6, solveA(Arrays.asList( //
        "abcx", //
        "abcy", //
        "abcz", //
        "")));

    assertEquals(11, solveA(Arrays.asList( //
        "abc", //
        "", //
        "a", //
        "b", //
        "c", //
        "", //
        "ab", //
        "ac", //
        "", //
        "a", //
        "a", //
        "a", //
        "a", //
        "", //
        "b", //
        "")));

    announceResultA();
    List<String> lines = getInput();
    System.out.println(solveA(lines));

    assertEquals(6, solveB(Arrays.asList( //
        "abc", //
        "", //
        "a", //
        "b", //
        "c", //
        "", //
        "ab", //
        "ac", //
        "", //
        "a", //
        "a", //
        "a", //
        "a", //
        "", //
        "b", //
        "")));

    announceResultB();
    System.out.println(solveB(lines));
  }

}
