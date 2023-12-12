package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(1702218515L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(925L, result);
  }

  @Test
  public void testA() {
    assertEquals(114, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(2, solveB(getStreamingTestInput()));
  }

  private long solve(Stream<String> lines, boolean partB) {
    return lines.mapToLong(line -> {
      List<Deque<Long>> all = new LinkedList<>();

      LinkedList<Long> l = new LinkedList<>(parseNumbers(line, "\\s+"));
      if (partB) {
        Collections.reverse(l);
      }

      all.add(l);
      boolean allZero = false;
      int ix = 0;
      while (!allZero) {
        LinkedList<Long> ll = new LinkedList<>();
        Iterator<Long> it = all.get(ix++).iterator();
        long a = it.next();
        while (it.hasNext()) {
          long b = it.next();
          ll.add(b - a);
          a = b;
        }
        all.add(ll);
        allZero = ll.stream().allMatch(i -> i == 0);
      }

      all.get(all.size() - 1).add(0L);
      for (int x = all.size() - 2; x >= 0; --x) {
        long toAdd = all.get(x + 1).peekLast();
        toAdd += all.get(x).peekLast();
        all.get(x).add(toAdd);
      }
      return all.get(0).peekLast();
    }).sum();
  }

  private long solveA(Stream<String> lines) {
    return solve(lines, false);
  }

  private long solveB(Stream<String> lines) {
    return solve(lines, true);
  }

}
