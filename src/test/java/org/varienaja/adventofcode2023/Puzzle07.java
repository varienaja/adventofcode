package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
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
    int myVal = 0;
    String cards;

    Hand(String cards, boolean methodB) {
      this.cards = cards;
      Map<Character, Integer> c2count = new HashMap<>();
      for (char c : cards.toCharArray()) {
        c2count.compute(c, (k, v) -> v == null ? 1 : v + 1);
      }

      if (methodB) {
        Integer jCount = c2count.remove('J');
        if (jCount != null) { // Move Joker to set of cards that occur most
          int max = Integer.MIN_VALUE;
          char mostOccurring = '0';
          for (Entry<Character, Integer> e : c2count.entrySet()) {
            if (e.getValue() > max) {
              max = e.getValue();
              mostOccurring = e.getKey();
            }
          }
          c2count.compute(mostOccurring, (k, v) -> v == null ? jCount : v + jCount);
        }
      }

      Map<Integer, Integer> count2count = new HashMap<>();
      for (int val : c2count.values()) {
        count2count.compute(val, (k, v) -> v == null ? 1 : v + 1);
      }

      if (count2count.getOrDefault(5, 0) == 1) { // five of a kind
        myVal = 1 << 7;
      } else if (count2count.getOrDefault(4, 0) == 1) { // four of a kind
        myVal = 1 << 6;
      } else if (count2count.getOrDefault(3, 0) == 1 && count2count.getOrDefault(2, 0) == 1) { // full house
        myVal = 1 << 5;
      } else if (count2count.getOrDefault(3, 0) == 1) { // three of a kind
        myVal = 1 << 4;
      } else if (count2count.getOrDefault(2, 0) == 2) { // two pair
        myVal = 1 << 3;
      } else if (count2count.getOrDefault(2, 0) == 1) { // one pair
        myVal = 1 << 2;
      } else { // high card
        myVal = 1 << 1;
      }
    }

    @Override
    public int compareTo(Hand o) {
      int result = Integer.compare(myVal, o.myVal);
      if (result == 0) { // check first different card
        for (int i = 0; i < cards.length(); ++i) {
          int v1 = CARD2VALUE.get(cards.charAt(i));
          int v2 = CARD2VALUE.get(o.cards.charAt(i));

          result = Integer.compare(v1, v2);
          if (result != 0) {
            return result;
          }
        }
      }

      return result;
    }

    @Override
    public String toString() {
      return cards + " " + myVal;
    }
  }

  static Map<Character, Integer> CARD2VALUE = new HashMap<>();
  static {
    for (int i = 2; i <= 9; ++i) {
      CARD2VALUE.put((char)('0' + i), i);
    }
    CARD2VALUE.put('T', 10);
    CARD2VALUE.put('J', 11);
    CARD2VALUE.put('Q', 12);
    CARD2VALUE.put('K', 13);
    CARD2VALUE.put('A', 14);
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

  private long solve(Stream<String> lines, boolean partB) {
    NavigableMap<Hand, Long> hands = lines //
        .map(line -> line.split("\\s+")) //
        .collect(Collectors.toMap(arr -> new Hand(arr[0], partB), arr -> Long.parseLong(arr[1]), (v1, v2) -> v1, TreeMap::new));

    AtomicLong rank = new AtomicLong(1);
    return hands.entrySet().stream() //
        .mapToLong(e -> e.getValue() * rank.getAndIncrement()) //
        .sum();
  }

  private long solveA(Stream<String> lines) {
    return solve(lines, false);
  }

  private long solveB(Stream<String> lines) {
    CARD2VALUE.put('J', 1);
    return solve(lines, true);
  }

}
