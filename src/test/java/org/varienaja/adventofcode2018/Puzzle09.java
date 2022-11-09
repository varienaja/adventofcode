package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(403, 71920);
    System.out.println(sum);
    assertEquals(439089, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveA(403, 7192000);
    System.out.println(sum);
    assertEquals(3668541094L, sum);
  }

  private long solveA(int players, int marbles) {
    Map<Integer, Long> elf2Score = new HashMap<>();
    List<Integer> circle = new LinkedList<>();
    circle.add(0);
    int currentMarble = 1;

    ListIterator<Integer> it = circle.listIterator();
    while (currentMarble <= marbles) {
      if (currentMarble % 23 == 0) {
        long score = currentMarble;
        int currentPlayer = currentMarble % players;
        elf2Score.compute(currentPlayer, (k, v) -> v == null ? score : v + score);

        // Move 7 marbles counter-clockwise
        int a = 0;
        for (int i = 0; i <= 7; i++) {
          if (!it.hasPrevious()) {
            it = circle.listIterator(circle.size());
          }
          a = it.previous();
        }
        int additional = a;
        elf2Score.compute(currentPlayer, (k, v) -> v + additional);
        it.remove();

        if (!it.hasNext()) {
          it = circle.listIterator();
        }
        it.next();
      } else {
        if (!it.hasNext()) {
          it = circle.listIterator();
        }
        it.next();
        it.add(currentMarble);
      }

      currentMarble++;
    }

    return elf2Score.values().stream().mapToLong(l -> l).max().orElse(-1L);
  }

  @Test
  public void testA() {
    assertEquals(32, solveA(9, 25));
    assertEquals(8317, solveA(10, 1618));
    assertEquals(146373, solveA(13, 7999));
    assertEquals(2764, solveA(17, 1104));
    assertEquals(54718, solveA(21, 6111));
    assertEquals(37305, solveA(30, 5807));
  }

}
