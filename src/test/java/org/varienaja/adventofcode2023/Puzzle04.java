package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  class Card {
    int id;
    int winningNumberCount;

    Card(String line) {
      String[] parts = line.split(":\\s*");
      id = Integer.parseInt(parts[0].split("\\s+")[1]);

      String[] nrs = parts[1].split("\\s+\\|\\s+");
      Set<Long> myNumbers = new HashSet<>(parseNumbers(nrs[0], "\\s+"));
      myNumbers.retainAll(parseNumbers(nrs[1], "\\s+"));
      winningNumberCount = myNumbers.size();
    }

    int getValue() {
      return winningNumberCount == 0 ? 0 : 1 << winningNumberCount - 1;
    }

    int getWinningNumberCount() {
      return winningNumberCount;
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(22897L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(5095824L, result);
  }

  @Test
  public void testA() {
    assertEquals(13, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(30, solveB(getStreamingTestInput()));
  }

  private long solveA(Stream<String> lines) {
    return lines.map(Card::new).mapToInt(Card::getValue).sum();
  }

  private long solveB(Stream<String> lines) {
    Map<Integer, Integer> card2count = new HashMap<>();

    lines.map(Card::new).forEach(c -> {
      int toAdd = card2count.compute(c.id, (k, v) -> (v == null ? 0 : v) + 1);

      for (int ix = 1; ix <= c.getWinningNumberCount(); ++ix) {
        card2count.compute(c.id + ix, (k, v) -> (v == null ? 0 : v) + toAdd);
      }
    });

    return card2count.values().stream().mapToInt(Integer::intValue).sum();
  }

}
