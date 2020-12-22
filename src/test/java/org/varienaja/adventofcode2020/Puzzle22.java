package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {

  private LinkedList<Integer> playGame(LinkedList<Integer> p1, LinkedList<Integer> p2, boolean partA) {
    Set<LinkedList<Integer>> seenP1 = new HashSet<>();
    Set<LinkedList<Integer>> seenP2 = new HashSet<>();

    while (p1.size() != 0 && p2.size() != 0) {
      if (!partA) {
        if (seenP1.contains(p1) && seenP2.contains(p2)) {
          return p1;
        }
        seenP1.add(new LinkedList<>(p1));
        seenP2.add(new LinkedList<>(p2));
      }

      int cp1 = p1.poll();
      int cp2 = p2.poll();

      boolean p1Won = cp1 > cp2;

      if (!partA && p1.size() >= cp1 && p2.size() >= cp2) {
        LinkedList<Integer> s1 = new LinkedList<>(p1.subList(0, cp1));
        LinkedList<Integer> s2 = new LinkedList<>(p2.subList(0, cp2));
        p1Won = (playGame(s1, s2, partA) == s1);
      }
      if (p1Won) {
        p1.offer(cp1);
        p1.offer(cp2);
      } else {
        p2.offer(cp2);
        p2.offer(cp1);
      }
    }
    return p1.size() == 0 ? p2 : p1;
  }

  private long solve(LinkedList<Integer> p1, LinkedList<Integer> p2, boolean partA) {
    LinkedList<Integer> winning = playGame(p1, p2, partA);

    long result = 0L;
    int multiplier = winning.size();
    for (Integer card : winning) {
      result += card * multiplier--;
    }
    return result;
  }

  @Test
  public void testDay22() {
    LinkedList<Integer> p1 = new LinkedList<>(Arrays.asList(9, 2, 6, 3, 1));
    LinkedList<Integer> p2 = new LinkedList<>(Arrays.asList(5, 8, 4, 7, 10));
    assertEquals(306L, solve(p1, p2, true));

    LinkedList<Integer> r1 = new LinkedList<>(Arrays.asList(41, 33, 20, 32, 7, 45, 2, 12, 14, 29, 49, 37, 6, 11, 39, 46, 47, 38, 23, 22, 28, 10, 36, 35, 24));
    LinkedList<Integer> r2 = new LinkedList<>(Arrays.asList(17, 4, 44, 9, 27, 18, 30, 42, 21, 26, 16, 48, 8, 15, 34, 50, 19, 43, 25, 1, 13, 31, 3, 5, 40));
    announceResultA();
    long result = solve(r1, r2, true);
    assertEquals(32815L, result);
    System.out.println(result);

    p1 = new LinkedList<>(Arrays.asList(9, 2, 6, 3, 1));
    p2 = new LinkedList<>(Arrays.asList(5, 8, 4, 7, 10));
    assertEquals(291L, solve(p1, p2, false));
    announceResultB();
    r1 = new LinkedList<>(Arrays.asList(41, 33, 20, 32, 7, 45, 2, 12, 14, 29, 49, 37, 6, 11, 39, 46, 47, 38, 23, 22, 28, 10, 36, 35, 24));
    r2 = new LinkedList<>(Arrays.asList(17, 4, 44, 9, 27, 18, 30, 42, 21, 26, 16, 48, 8, 15, 34, 50, 19, 43, 25, 1, 13, 31, 3, 5, 40));
    result = solve(r1, r2, false);
    assertEquals(30695L, result);
    System.out.println(result);
  }

}
