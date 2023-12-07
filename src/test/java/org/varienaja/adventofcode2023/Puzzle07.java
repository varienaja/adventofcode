package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {
  class Hand implements Comparable<Hand> {
    private String cardVals;

    Hand(String cards, String cardValues) {
      Map<Character, Long> c2count = cards.chars().mapToObj(i -> (char)i).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
      // Rewrite cards such that it becomes easy to sort. Use a-m instead of 2-9,T,J,Q,K,A
      cardVals = cards.chars().mapToObj(i -> "" + ((char)('a' + cardValues.indexOf((char)i)))).collect(Collectors.joining());

      if (cardValues.startsWith("J") && c2count.containsKey('J')) { // PartB: Move Joker to card type that occurs most
        long jCount = c2count.put('J', 0L);
        char mostOccurring = Collections.max(c2count.entrySet(), Map.Entry.comparingByValue()).getKey();
        c2count.compute(mostOccurring, (k, v) -> v + jCount);
      }

      // Prefix with hand-type while keeping the easy-to-sort-property:
      // Five of a kind: 55555, Four of a kind: 44441, Full house 33322, etc.
      cardVals = c2count.values().stream().map(Long::valueOf).sorted(Collections.reverseOrder()) //
          .mapToInt(Long::intValue).mapToObj(i -> new String(new char[i]).replace('\0', (char)('0' + i))) //
          .collect(Collectors.joining()) + cardVals;
    }

    @Override
    public int compareTo(Hand o) {
      return cardVals.compareTo(o.cardVals);
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(251106089L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(249620106L, result);
  }

  @Test
  public void testA() {
    assertEquals(6440, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(5905, solveB(getStreamingTestInput()));
  }

  private long solve(Stream<String> lines, String cardValues) {
    NavigableMap<Hand, Long> hand2bid = lines //
        .map(line -> line.split("\\s+")) //
        .collect(Collectors.toMap(a -> new Hand(a[0], cardValues), a -> Long.parseLong(a[1]), (v1, v2) -> v1, TreeMap::new));

    AtomicLong rank = new AtomicLong(1);
    return hand2bid.entrySet().stream() //
        .mapToLong(e -> e.getValue() * rank.getAndIncrement()) //
        .sum();
  }

  private long solveA(Stream<String> lines) {
    return solve(lines, "23456789TJQKA");
  }

  private long solveB(Stream<String> lines) {
    return solve(lines, "J23456789TQKA");
  }

}
