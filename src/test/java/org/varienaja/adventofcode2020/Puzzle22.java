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

  private LinkedList<Integer> playGameB(LinkedList<Integer> p1, LinkedList<Integer> p2) {
    Set<LinkedList<Integer>> seenP1 = new HashSet<>();
    Set<LinkedList<Integer>> seenP2 = new HashSet<>();

    while (p1.size() != 0 && p2.size() != 0) {
      if (seenP1.contains(p1) && seenP2.contains(p2)) {
        return p1;
      }
      seenP1.add(new LinkedList<>(p1));
      seenP2.add(new LinkedList<>(p2));

      int cp1 = p1.poll();
      int cp2 = p2.poll();

      LinkedList<Integer> winner = null;
      if (p1.size() >= cp1 && p2.size() >= cp2) {
        LinkedList<Integer> s1 = new LinkedList<>();
        for (int i = 0; i < cp1; i++) {
          s1.add(p1.get(i));
        }

        LinkedList<Integer> s2 = new LinkedList<>();
        for (int i = 0; i < cp2; i++) {
          s2.add(p2.get(i));
        }

        winner = playGameB(s1, s2);
        if (winner == s1) {
          p1.offer(cp1);
          p1.offer(cp2);
        } else {
          p2.offer(cp2);
          p2.offer(cp1);
        }
      } else {

        if (cp1 > cp2) {
          p1.offer(cp1);
          p1.offer(cp2);
        } else {
          p2.offer(cp2);
          p2.offer(cp1);
        }
      }
    }
    return p1.size() == 0 ? p2 : p1;
  }

  private long solveA(LinkedList<Integer> p1, LinkedList<Integer> p2) {
    while (p1.size() != 0 && p2.size() != 0) {
      int cp1 = p1.poll();
      int cp2 = p2.poll();
      if (cp1 > cp2) { // cards to player1
        p1.offer(cp1);
        p1.offer(cp2);
      } else { // cards to player2
        p2.offer(cp2);
        p2.offer(cp1);
      }
    }
    LinkedList<Integer> winning = p1.size() == 0 ? p2 : p1;
    long result = 0L;
    int multiplier = winning.size();
    for (Integer card : winning) {
      result += card * multiplier--;
    }
    return result;
  }

  private long solveB(LinkedList<Integer> p1, LinkedList<Integer> p2) {

    // play normally
    LinkedList<Integer> winning = playGameB(p1, p2);

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
    assertEquals(306L, solveA(p1, p2));

    LinkedList<Integer> r1 = new LinkedList<>(Arrays.asList(41, 33, 20, 32, 7, 45, 2, 12, 14, 29, 49, 37, 6, 11, 39, 46, 47, 38, 23, 22, 28, 10, 36, 35, 24));
    LinkedList<Integer> r2 = new LinkedList<>(Arrays.asList(17, 4, 44, 9, 27, 18, 30, 42, 21, 26, 16, 48, 8, 15, 34, 50, 19, 43, 25, 1, 13, 31, 3, 5, 40));
    announceResultA();
    long result = solveA(r1, r2);
    assertEquals(32815L, result);
    System.out.println(result);

    p1 = new LinkedList<>(Arrays.asList(9, 2, 6, 3, 1));
    p2 = new LinkedList<>(Arrays.asList(5, 8, 4, 7, 10));
    assertEquals(291L, solveB(p1, p2));
    announceResultB();
    r1 = new LinkedList<>(Arrays.asList(41, 33, 20, 32, 7, 45, 2, 12, 14, 29, 49, 37, 6, 11, 39, 46, 47, 38, 23, 22, 28, 10, 36, 35, 24));
    r2 = new LinkedList<>(Arrays.asList(17, 4, 44, 9, 27, 18, 30, 42, 21, 26, 16, 48, 8, 15, 34, 50, 19, 43, 25, 1, 13, 31, 3, 5, 40));
    result = solveB(r1, r2);
    assertEquals(30695L, result);
    System.out.println(result);
  }

}
