package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(1223326L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(21070419L, result);
  }

  @Test
  public void testA() {
    assertEquals(11, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(31, solveB(getStreamingTestInput()));
  }

  private long solveA(Stream<String> lines) {
    List<Long> as = new ArrayList<>();
    List<Long> bs = new ArrayList<>();

    lines.forEach(l -> {
      String[] parts = l.split("\s+");
      as.add(Long.parseLong(parts[0]));
      bs.add(Long.parseLong(parts[1]));
    });

    Collections.sort(as);
    Collections.sort(bs);

    return IntStream.range(0, as.size()).mapToLong(i -> Math.abs(as.get(i) - bs.get(i))).sum();
  }

  private long solveB(Stream<String> lines) {
    List<Integer> as = new ArrayList<>();
    Map<Integer, Integer> nr2count = new HashMap<>();

    lines.forEach(l -> {
      String[] parts = l.split("\s+");
      as.add(Integer.parseInt(parts[0]));
      nr2count.compute(Integer.parseInt(parts[1]), (k, v) -> v == null ? 1 : v + 1);
    });

    return as.stream().mapToLong(a -> a * nr2count.getOrDefault(a, 0)).sum();
  }

}
