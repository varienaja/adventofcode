package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  private long solveA(List<Integer> adapters) {
    List<Integer> sorted = new LinkedList<>(adapters);
    Collections.sort(sorted);
    sorted.add(sorted.get(sorted.size() - 1) + 3);
    sorted.add(0, 0);

    Map<Integer, AtomicInteger> diff2Count = new HashMap<>();
    for (int i = 0; i < sorted.size() - 1; i++) {
      diff2Count.computeIfAbsent(sorted.get(i + 1) - sorted.get(i), d -> new AtomicInteger()).incrementAndGet();
    }
    return diff2Count.get(1).get() * diff2Count.get(3).get();
  }

  private long solveB(List<Integer> adapters) {
    List<Integer> sortedJoltages = new LinkedList<>(adapters);
    Collections.sort(sortedJoltages);
    sortedJoltages.add(0, 0);

    return solveBRecursive(0, sortedJoltages, new HashMap<Integer, Long>());
  }

  private long solveBRecursive(int ix, List<Integer> adapters, Map<Integer, Long> ix2Sum) {
    if (ix == adapters.size() - 1) {
      return 1L;
    }
    if (ix2Sum.containsKey(ix)) {
      return ix2Sum.get(ix);
    }

    long sum = 0L;
    int j = ix + 1;
    while (j < adapters.size() && adapters.get(j) - adapters.get(ix) <= 3) {
      sum += solveBRecursive(j, adapters, ix2Sum);
      j++;
    }
    ix2Sum.put(ix, sum);
    return sum;
  }

  @Test
  public void testDay10() {
    List<Integer> input = Arrays.asList(16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4);
    assertEquals(7 * 5, solveA(input));

    List<Integer> input2 = Arrays.asList(28, 33, 18, 42, 31, 14, 46, 20, 48, 47, 24, 23, 49, 45, 19, 38, 39, 11, 1, 32, 25, 35, 8, 17, 7, 9, 4, 2, 34, 10, 3);
    assertEquals(22 * 10, solveA(input2));

    announceResultA();
    List<String> lines = getInput();
    List<Integer> numbers = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
    long result = solveA(numbers);
    assertEquals(2346L, result);
    System.out.println(result);

    assertEquals(8, solveB(input));
    assertEquals(19208, solveB(input2));

    announceResultB();
    result = solveB(numbers);
    assertEquals(6044831973376L, result);
    System.out.println(result);
  }

}
