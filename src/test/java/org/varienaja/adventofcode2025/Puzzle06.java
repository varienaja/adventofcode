package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInput(), true);
    System.out.println(result);
    assertEquals(6757749566978L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInput(), false);
    System.out.println(result);
    assertEquals(10603075273949L, result);
  }

  @Test
  public void testA() {
    assertEquals(4277556L, solve(getTestInput(), true));
  }

  @Test
  public void testB() {
    assertEquals(3263827L, solve(getTestInput(), false));
  }

  private long solve(List<List<Long>> inputs, List<String> ops) {
    long sum = 0;

    for (int i = 0; i < inputs.size(); ++i) {
      LongStream ls = inputs.get(i).stream().mapToLong(l -> l);
      if ("*".equals(ops.get(i))) { // mul
        sum += ls.reduce((l1, l2) -> l1 * l2).getAsLong();
      } else { // plus
        sum += ls.reduce(Long::sum).getAsLong();
      }
    }

    return sum;
  }

  private long solve(List<String> lines, boolean partA) {
    List<String> ops = Stream.of(lines.get(lines.size() - 1).split("\\s+")).toList();
    lines.remove(lines.size() - 1);

    List<List<Long>> input = new LinkedList<>();
    for (int i = 0; i < ops.size(); ++i) {
      input.add(new LinkedList<>());
    }

    if (partA) {
      for (String line : lines) {
        String[] parts = line.trim().split("\\s+");
        for (int i = 0; i < parts.length; ++i) {
          input.get(i).add(Long.parseLong(parts[i]));
        }
      }
    } else {
      int opix = ops.size() - 1;
      for (int ix = lines.get(0).length() - 1; ix >= -1; --ix) {
        boolean changed = false;
        long nr = 0;
        for (String line : lines) {
          char c = ix == -1 ? ' ' : line.charAt(ix);
          if (Character.isDigit(c)) {
            nr = nr * 10 + (c - '0');
            changed = true;
          }
        }
        if (changed) {
          input.get(opix).add(nr);
        } else {
          --opix;
        }
      }
    }

    return solve(input, ops);
  }

}
