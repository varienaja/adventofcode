package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  private long solve(String input, int days) {
    List<Integer> timers = Arrays.stream(input.split(",")).map(s -> Integer.parseInt(s)).collect(Collectors.toList());
    NavigableMap<Integer, Long> tmp = new TreeMap<>();
    timers.forEach(i -> tmp.compute(i, (k, v) -> v == null ? 1 : v + 1));

    Map<AtomicInteger, Long> age2Count = tmp.entrySet().stream().collect(Collectors.toMap(e -> new AtomicInteger(e.getKey()), Map.Entry::getValue));
    age2Count.put(new AtomicInteger(0), 0L);
    for (int i = tmp.lastKey() + 1; i <= 8; i++) {
      age2Count.put(new AtomicInteger(i), 0L);
    }

    for (int i = 0; i < days; i++) {
      long nw = 0;
      for (Entry<AtomicInteger, Long> e : age2Count.entrySet()) {
        if (e.getKey().get() == 0) {
          nw = e.getValue();
        }
      }

      for (Entry<AtomicInteger, Long> e : age2Count.entrySet()) {
        int newAge = e.getKey().decrementAndGet();
        if (newAge == -1) {
          e.getKey().set(8);
        } else if (newAge == 6) {
          e.setValue(e.getValue() + nw);
        }
      }
    }

    return age2Count.values().stream().mapToLong(l -> l).sum();
  }

  @Test
  public void testDay01() {
    String testInput = "3,4,3,1,2";
    assertEquals(26, solve(testInput, 18));
    assertEquals(5934, solve(testInput, 80));

    announceResultA();
    String lines = getInput().get(0);
    long result = solve(lines, 80);
    System.out.println(result);
    assertEquals(374927, result);

    assertEquals(26984457539L, solve(testInput, 256));
    announceResultB();
    result = solve(lines, 256);
    System.out.println(result);
    assertEquals(1687617803407L, result);
  }

}
