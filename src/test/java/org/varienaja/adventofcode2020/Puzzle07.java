package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  private Map<String, Set<String>> extractRulesA(List<String> lines) {
    Pattern bagRule = Pattern.compile("\\s*(\\d+)\\s+(.*?)\\s+(bag)");
    return lines.stream() //
        .map(line -> line.split(" bags contain ")) //
        .collect(Collectors.toMap(parts -> parts[0], parts -> {
          Set<String> cols = new HashSet<>();
          Matcher m = bagRule.matcher(parts[1]);
          while (m.find()) {
            cols.add(m.group(2));
          }
          return cols;
        }));
  }

  private Map<String, Set<SimpleEntry<Integer, String>>> extractRulesB(List<String> lines) {
    Pattern bagRule = Pattern.compile("\\s*(\\d+)\\s+(.*?)\\s+(bag)");
    return lines.stream() //
        .map(line -> line.split(" bags contain ")) //
        .collect(Collectors.toMap(parts -> parts[0], parts -> {
          Set<SimpleEntry<Integer, String>> cols = new HashSet<>();
          Matcher m = bagRule.matcher(parts[1]);
          while (m.find()) {
            cols.add(new SimpleEntry<>(Integer.parseInt(m.group(1)), m.group(2)));
          }
          return cols;
        }));
  }

  private long solveA(List<String> lines) {
    Set<String> weSearch = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    weSearch.add("shiny gold");
    boolean repeat;
    do {
      repeat = false;
      for (Entry<String, Set<String>> entry : extractRulesA(lines).entrySet()) {
        for (String s : weSearch) {
          if (entry.getValue().contains(s)) {
            repeat |= weSearch.add((entry.getKey()));
          }
        }
      }
    } while (repeat);

    return weSearch.size() - 1;
  }

  private long solveB(List<String> lines) {
    return solveRecursive(extractRulesB(lines), "shiny gold") - 1;
  }

  private long solveRecursive(Map<String, Set<SimpleEntry<Integer, String>>> rules, String key) {
    Set<SimpleEntry<Integer, String>> bags = rules.get(key);

    long result = 1;
    for (SimpleEntry<Integer, String> b : bags) {
      result += b.getKey() * solveRecursive(rules, b.getValue());
    }
    return result;
  }

  @Test
  public void testDay07() {
    List<String> testInput = Arrays.asList( //
        "light red bags contain 1 bright white bag, 2 muted yellow bags.", //
        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.", //
        "bright white bags contain 1 shiny gold bag.", //
        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.", //
        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.", //
        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.", //
        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.", //
        "faded blue bags contain no other bags.", //
        "dotted black bags contain no other bags.");
    assertEquals(4, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(172L, result);
    System.out.println(result);

    assertEquals(32, solveB(testInput));
    testInput = Arrays.asList( //
        "shiny gold bags contain 2 dark red bags.", //
        "dark red bags contain 2 dark orange bags.", //
        "dark orange bags contain 2 dark yellow bags.", //
        "dark yellow bags contain 2 dark green bags.", //
        "dark green bags contain 2 dark blue bags.", //
        "dark blue bags contain 2 dark violet bags.", //
        "dark violet bags contain no other bags.");
    assertEquals(126, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    assertEquals(39645L, result);
    System.out.println(result);
  }

}
