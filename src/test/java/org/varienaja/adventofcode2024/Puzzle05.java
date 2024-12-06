package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(4905, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(6204, result);
  }

  @Test
  public void testA() {
    assertEquals(143, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(123, solveB(getTestInput()));
  }

  private Map<Boolean, Long> solve(List<String> lines) {
    Map<Boolean, Long> result = new HashMap<>();
    Map<String, Set<String>> page2previous = new HashMap<>();
    Iterator<String> it = lines.iterator();
    String line;

    while (!(line = it.next()).isEmpty()) { // rules
      String[] parts = line.split("\\|");
      page2previous.compute(parts[1], (k, v) -> v == null ? new HashSet<>() : v).add(parts[0]);
    }

    while (it.hasNext()) { // updates
      String[] update = it.next().split(",");
      String[] updateCorrect = Arrays.stream(update).sorted((a, b) -> page2previous.getOrDefault(a, Collections.emptySet()).contains(b) ? 1 : -1)
          .toArray(String[]::new);

      long mid = Long.parseLong(updateCorrect[updateCorrect.length / 2]);
      result.compute(Arrays.equals(update, updateCorrect), (k, v) -> v == null ? mid : v + mid);
    }

    return result;
  }

  private long solveA(List<String> lines) {
    return solve(lines).get(true);
  }

  private long solveB(List<String> lines) {
    return solve(lines).get(false);
  }

}
