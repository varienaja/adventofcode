package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {

  private long solve(List<String> lines, int cnt) {
    Map<String, String> rules = lines.stream() //
        .skip(2) //
        .map(l -> l.split("\\s+->\\s+")) //
        .collect(Collectors.toMap(p -> p[0], p -> p[1]));

    String polygon = lines.get(0);
    Map<String, Long> pair2insertion = new HashMap<>();
    for (int j = 0; j < polygon.length() - 1; j++) {
      pair2insertion.compute(polygon.substring(j, j + 2), (k, v) -> v == null ? 1 : v + 1);
    }
    Map<String, Long> elt2count = polygon.chars() //
        .mapToObj(Character::toString) //
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (int i = 0; i < cnt; i++) {
      Map<String, Long> nfreqMap = new HashMap<>(pair2insertion);
      for (Entry<String, Long> e : pair2insertion.entrySet()) {
        String insertion = rules.get(e.getKey()); // Example: AB -> C (so AB becomes ACB)

        nfreqMap.compute(e.getKey(), (k, v) -> v - e.getValue()); // Decrease count for AB
        BiFunction<String, Long, Long> increase = (k, v) -> e.getValue() + (v == null ? 0 : v);
        nfreqMap.compute(e.getKey().charAt(0) + insertion, increase); // Increase count for AC
        nfreqMap.compute(insertion + e.getKey().charAt(1), increase); // Increase count for CB
        elt2count.compute(insertion, increase); // Increase count for C
      }
      pair2insertion = nfreqMap;
    }

    LongSummaryStatistics stats = elt2count.values().stream().mapToLong(l -> l).summaryStatistics();
    return stats.getMax() - stats.getMin();
  }

  @Test
  public void testDay14() {
    List<String> testInput = Arrays.asList( //
        "NNCB", //
        "", //
        "CH -> B", //
        "HH -> N", //
        "CB -> H", //
        "NH -> C", //
        "HB -> C", //
        "HC -> B", //
        "HN -> C", //
        "NN -> C", //
        "BH -> H", //
        "NC -> B", //
        "NB -> B", //
        "BN -> B", //
        "BB -> N", //
        "BC -> B", //
        "CC -> N", //
        "CN -> C");
    assertEquals(1588, solve(testInput, 10));

    announceResultA();
    List<String> lines = getInput();
    long result = solve(lines, 10);
    System.out.println(result);
    assertEquals(4244, result);

    assertEquals(2188189693529L, solve(testInput, 40));
    announceResultB();
    result = solve(lines, 40);
    System.out.println(result);
    assertEquals(4807056953866L, result);
  }

}
