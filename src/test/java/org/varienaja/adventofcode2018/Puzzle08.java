package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {

  private long calcSum(Iterator<Integer> it) {
    long sum = 0;

    int childCount = it.next();
    int metaDataSize = it.next();
    for (int c = 0; c < childCount; c++) {
      sum += calcSum(it);
    }
    for (int m = 0; m < metaDataSize; m++) {
      sum += it.next();
    }

    return sum;
  }

  private long calcValue(Iterator<Integer> it) {
    long val = 0;

    int childCount = it.next();
    int metaDataSize = it.next();
    Map<Integer, Long> child2Value = new HashMap<>();
    for (int c = 0; c < childCount; c++) {
      child2Value.put(c + 1, calcValue(it));
    }
    if (childCount == 0) {
      for (int m = 0; m < metaDataSize; m++) {
        val += it.next();
      }
    } else {
      for (int m = 0; m < metaDataSize; m++) {
        int childNo = it.next();
        val += child2Value.getOrDefault(childNo, 0L);
      }
    }

    return val;
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(46096, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(24820, sum);
  }

  private long solveA(String input) {
    List<String> parts = Arrays.asList(input.split("\\s"));
    List<Integer> data = parts.stream().map(Integer::parseInt).collect(Collectors.toList());

    return calcSum(data.iterator());
  }

  private long solveB(String input) {
    List<String> parts = Arrays.asList(input.split("\\s"));
    List<Integer> data = parts.stream().map(Integer::parseInt).collect(Collectors.toList());

    return calcValue(data.iterator());
  }

  @Test
  public void testA() {
    assertEquals(138, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(66, solveB(testInput()));
  }

  private String testInput() {
    return "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";
  }
}
