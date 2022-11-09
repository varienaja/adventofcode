package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {
  private Set<Set<Integer>> solutions;

  private void solve(List<Integer> input, int liters, Set<Set<Integer>> solutions, Set<Integer> used) {
    if (input.isEmpty()) {
      return;
    }

    if (liters < 0) {
      return;
    }

    if (liters == 0) {
      solutions.add(used);
    }

    for (int i = 0; i < input.size(); i++) {
      if (!used.contains(i)) {
        int sumRest = input.subList(i, input.size()).stream().mapToInt(in -> in).sum();
        if (sumRest >= liters) { // Don't calc stuff that will never give a result
          Set<Integer> newUsed = new LinkedHashSet<>(used);
          newUsed.add(i);
          solve(input, liters - input.get(i), solutions, newUsed);
        }
      }
    }
  }

  private long solveA(List<Integer> input, int liters) {
    solutions = new HashSet<>();
    Collections.sort(input);
    Collections.reverse(input);
    solve(input, liters, solutions, Collections.emptySet());
    return solutions.size();
  }

  private long solveB() {
    NavigableMap<Integer, Integer> containers2Configs = new TreeMap<>();
    for (Set<Integer> s : solutions) {
      containers2Configs.compute(s.size(), (k, v) -> v == null ? 1 : v + 1);
    }
    return containers2Configs.firstEntry().getValue();
  }

  @Test
  public void testDay17() {
    List<Integer> testInput = Arrays.asList(20, 15, 10, 5, 5);
    assertEquals(4, solveA(testInput, 25));
    assertEquals(3, solveB());

    announceResultA();
    List<Integer> lines = getInput().stream().map(Integer::parseInt).collect(Collectors.toList());
    long sum = solveA(lines, 150);
    System.out.println(sum);
    assertEquals(1638, sum);

    announceResultB();
    sum = solveB();
    System.out.println(sum);
    assertEquals(17, sum);
  }

}
