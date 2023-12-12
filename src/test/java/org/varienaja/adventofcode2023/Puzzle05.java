package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  class Transformation {
    long destStart;
    long sourceStart;
    long length;
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(323142486L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(79874951L, result);
  }

  @Test
  public void testA() {
    assertEquals(35, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(46, solveB(getTestInput()));
  }

  private Map<String, List<Transformation>> parse(List<String> lines) {
    Map<String, List<Transformation>> rules = new HashMap<>();

    String name = "";
    for (int y = 1; y < lines.size(); ++y) {
      String line = lines.get(y);
      if (line.isEmpty()) {
      } else {
        if (line.endsWith(":")) {
          name = line;
          rules.put(line, new LinkedList<>());
        } else {
          List<Long> nrs = parseNumbers(line, "\\s+");
          Transformation t = new Transformation();
          t.destStart = nrs.get(0);
          t.sourceStart = nrs.get(1);
          t.length = nrs.get(2);
          rules.get(name).add(t);
        }
      }
    }

    return rules;
  }

  private long solveA(List<String> lines) {
    List<String> transformations = List.of("seed-to-soil map:", "soil-to-fertilizer map:", "fertilizer-to-water map:", "water-to-light map:",
        "light-to-temperature map:", "temperature-to-humidity map:", "humidity-to-location map:");

    List<Long> seeds = parseNumbers(lines.get(0).split(":\\s+")[1], "\\s+");
    Map<String, List<Transformation>> rules = parse(lines);

    List<Long> seedss = new LinkedList<>();
    for (long seed : seeds) {
      long result = seed;
      for (String t : transformations) {

        for (Transformation rule : rules.get(t)) {
          if (result >= rule.sourceStart && result < rule.sourceStart + rule.length) {
            result += rule.destStart - rule.sourceStart;
            break;
          }

        }

      }
      seedss.add(result);
    }

    return seedss.stream().mapToLong(Long::longValue).min().orElseThrow();
  }

  // Do the reverse to find first seed FIXME calculate this with Ranges, should be much faster
  private long solveB(List<String> lines) {
    List<String> transformations = List.of("humidity-to-location map:", "temperature-to-humidity map:", "light-to-temperature map:", "water-to-light map:",
        "fertilizer-to-water map:", "soil-to-fertilizer map:", "seed-to-soil map:");

    List<Long> seedsAndCounts = parseNumbers(lines.get(0).split(":\\s+")[1], "\\s+");
    Map<String, List<Transformation>> rules = parse(lines);

    for (long seed = 0; seed < Long.MAX_VALUE; ++seed) {
      long result = seed;
      for (String t : transformations) {

        for (Transformation rule : rules.get(t)) {
          if (result >= rule.destStart && result < rule.destStart + rule.length) {
            result -= rule.destStart - rule.sourceStart;
            break;
          }
        }
      }
      if (seed % 100000 == 0) {
        System.out.println(seed);
      }

      // check if result is a valid seed; if yes:return it
      for (int ix = 0; ix < seedsAndCounts.size(); ix += 2) {
        long seedNr = seedsAndCounts.get(ix);
        long count = seedsAndCounts.get(ix + 1);
        if (result >= seedNr && result < seedNr + count) {
          return seed;
        }
      }
    }

    throw new IllegalStateException("No seed found");
  }

}
