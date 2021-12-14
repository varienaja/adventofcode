package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {

  private long solve(List<String> lines, int cnt) {
    Map<String, String> templates = new HashMap<>();
    for (int i = 2; i < lines.size(); i++) {
      String[] parts = lines.get(i).split("\\s+->\\s+");
      templates.put(parts[0], parts[1]);
    }

    Map<String, Long> freqMap = new HashMap<>();
    char[] pp = lines.get(0).toCharArray();
    for (int j = 0; j < pp.length - 1; j++) {
      String lookup = pp[j] + "" + pp[j + 1];
      freqMap.compute(lookup, (k, v) -> v == null ? 1 : v + 1);
    }
    for (String s : templates.keySet()) {
      freqMap.compute(s, (k, v) -> v == null ? 0 : v);
    }

    for (int i = 0; i < cnt; i++) {
      Map<String, Long> nfreqMap = new HashMap<>(freqMap);
      for (Entry<String, Long> e : freqMap.entrySet()) {
        String val = templates.get(e.getKey());
        if (e.getValue() > 0) {
          nfreqMap.compute(e.getKey(), (k, v) -> v - e.getValue());

          String one = val + e.getKey().charAt(1);
          nfreqMap.compute(one, (k, v) -> v + e.getValue());
          String two = e.getKey().charAt(0) + val;
          nfreqMap.compute(two, (k, v) -> v + e.getValue());
        }
      }
      freqMap = nfreqMap;
    }

    Map<String, Long> freq = new HashMap<>();
    for (Entry<String, Long> e : freqMap.entrySet()) {
      freq.compute("" + e.getKey().charAt(1), (k, v) -> v == null ? e.getValue() : v + e.getValue());
    }
    freq.compute("" + lines.get(0).charAt(0), (k, v) -> v + 1);

    LongSummaryStatistics stats = freq.values().stream().mapToLong(l -> l).summaryStatistics();
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
