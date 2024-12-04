package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(534L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(577L, result);
  }

  @Test
  public void testA() {
    assertEquals(2, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(4, solveB(getStreamingTestInput()));
  }

  private boolean isSafe(List<Integer> report) {
    int maxStep = 3;
    int sign = Integer.signum(report.get(1) - report.get(0));

    for (int i = 1; i < report.size(); ++i) {
      int step = report.get(i) - report.get(i - 1);
      if (Integer.signum(step) != sign || Math.abs(step) > maxStep) {
        return false;
      }
    }
    return true;
  }

  private long solveA(Stream<String> lines) {
    return lines.filter(l -> {
      String[] parts = l.split("\s+");
      List<Integer> report = Stream.of(parts).map(Integer::parseInt).collect(Collectors.toList());
      return isSafe(report);
    }).count();
  }

  private long solveB(Stream<String> lines) {
    return lines.filter(l -> {
      String[] parts = l.split("\s+");
      List<Integer> report = Stream.of(parts).map(Integer::parseInt).collect(Collectors.toList());
      if (isSafe(report)) {
        return true;
      }
      for (int i = 0; i < report.size(); ++i) {
        List<Integer> rr = new ArrayList<>(report);
        rr.remove(i);
        if (isSafe(rr)) {
          return true;
        }
      }
      return false;
    }).count();
  }

}
