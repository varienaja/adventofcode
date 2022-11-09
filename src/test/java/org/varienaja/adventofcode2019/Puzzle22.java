package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    int[] result = solveA(getInput(), 10007);

    int ix = 0;
    while (result[ix] != 2019) {
      ix++;
    }
    System.out.println(ix);
    assertEquals(2558, ix);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(63967243502561L, sum);
  }

  private int[] solveA(List<String> input, int decksize) {
    LinkedList<Integer> cards = IntStream.range(0, decksize).boxed().collect(Collectors.toCollection(LinkedList::new));

    for (String operation : input) {
      if ("deal into new stack".equals(operation)) {
        Collections.reverse(cards);
      } else if (operation.startsWith("deal")) {
        int increment = Integer.parseInt(operation.substring(operation.lastIndexOf(' ') + 1));
        int[] tmp = new int[cards.size()];
        int target = 0;
        while (!cards.isEmpty()) {
          tmp[target] = cards.poll();
          target += increment;
          if (target >= tmp.length) {
            target -= tmp.length;
          }
        }
        cards = IntStream.of(tmp).boxed().collect(Collectors.toCollection(LinkedList::new));
      } else {
        int cut = Integer.parseInt(operation.substring(operation.lastIndexOf(' ') + 1));
        if (cut >= 0) {
          // Take first 'cut' cards, move them to the end
          for (int i = 0; i < cut; i++) {
            cards.addLast(cards.removeFirst());
          }
        } else {
          // Take last 'cut' cards, move them to the front
          for (int i = 0; i < Math.abs(cut); i++) {
            cards.addFirst(cards.removeLast());
          }
        }
      }
    }

    return cards.stream().mapToInt(i -> i).toArray();
  }

  private long solveB(List<String> input) {
    // Had to look at reddit: https://www.reddit.com/r/adventofcode/comments/ee0rqi/2019_day_22_solutions/
    long m = 119315717514047L;
    long n = 101741582076661L;
    long pos = 2020;

    long a = 1;
    long b = 0;
    for (String operation : input) {
      if ("deal into new stack".equals(operation)) {
        a = -a % m;
        b = (m - 1 - b) % m;
      } else if (operation.startsWith("deal")) {
        long increment = Long.parseLong(operation.substring(operation.lastIndexOf(' ') + 1));
        a = (a * increment) % m;
        b = (b * increment) % m;
      } else { // cut
        long cut = Long.parseLong(operation.substring(operation.lastIndexOf(' ') + 1));
        b = (b - cut) % m;
      }
    }

    // r = (b * pow(1-a, m-2, m)) % m
    BigInteger mm = BigInteger.valueOf(m);
    BigInteger r = BigInteger.valueOf(b).multiply(BigInteger.valueOf(1 - a).modPow(BigInteger.valueOf(m - 2), mm)).mod(BigInteger.valueOf(m));

    // return ((pos - r) * pow(a, n*(m-2), m) + r) % m
    return BigInteger.valueOf(pos - r.longValue()).multiply(BigInteger.valueOf(a).modPow(BigInteger.valueOf(n).multiply(BigInteger.valueOf(m - 2)), mm)).add(r)
        .mod(BigInteger.valueOf(m)).longValue();
  }

  @Test
  public void testA() {
    assertArrayEquals(new int[] {
        0, 3, 6, 9, 2, 5, 8, 1, 4, 7
    }, solveA(testInput1(), 10));

    assertArrayEquals(new int[] {
        3, 0, 7, 4, 1, 8, 5, 2, 9, 6
    }, solveA(testInput2(), 10));

    assertArrayEquals(new int[] {
        6, 3, 0, 7, 4, 1, 8, 5, 2, 9
    }, solveA(testInput3(), 10));

    assertArrayEquals(new int[] {
        9, 2, 5, 8, 1, 4, 7, 0, 3, 6
    }, solveA(testInput4(), 10));
  }

  private List<String> testInput1() {
    return List.of( //
        "deal with increment 7", //
        "deal into new stack", //
        "deal into new stack");
  }

  private List<String> testInput2() {
    return List.of( //
        "cut 6", //
        "deal with increment 7", //
        "deal into new stack");
  }

  private List<String> testInput3() {
    return List.of( //
        "deal with increment 7", //
        "deal with increment 9", //
        "cut -2");
  }

  private List<String> testInput4() {
    return List.of( //
        "deal into new stack", //
        "cut -2", //
        "deal with increment 7", //
        "cut 8", //
        "cut -4", //
        "deal with increment 7", //
        "cut 3", //
        "deal with increment 9", //
        "deal with increment 3", //
        "cut -1");
  }

}
