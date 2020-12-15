package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {

  private long solve(List<Long> lines, int round) {
    Map<Long, Queue<Integer>> spoken2Rounds = IntStream.range(0, lines.size()) //
        .boxed() //
        .collect(Collectors.toMap(lines::get, i -> new LinkedList<>(Arrays.asList(i))));
    int r = lines.size();
    long lastSpoken = lines.get(r - 1);

    while (r < round) {
      Queue<Integer> rounds = spoken2Rounds.computeIfAbsent(lastSpoken, k -> new LinkedList<>());
      lastSpoken = 0;
      if (rounds.size() > 1) {
        int secondLastRound = rounds.poll();
        int lastRound = rounds.peek();
        lastSpoken = lastRound - secondLastRound;
      }
      spoken2Rounds.computeIfAbsent(lastSpoken, k -> new LinkedList<>()).offer(r++);
    }
    return lastSpoken;
  }

  private long solveA(List<Long> lines) {
    return solve(lines, 2020);
  }

  private long solveB(List<Long> lines) {
    return solve(lines, 30000000);
  }

  @Test
  public void testDay12() {
    List<Long> input = Arrays.asList(0L, 3L, 6L);
    assertEquals(436L, solveA(input));

    announceResultA();
    List<Long> lines = Arrays.asList(0L, 5L, 4L, 1L, 10L, 14L, 7L);
    long result = solveA(lines);
    assertEquals(203L, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines);
    assertEquals(9007186L, result);
    System.out.println(result);
  }

  @Test
  public void testExample1() {
    assertEquals(175594L, solveB(Arrays.asList(0L, 3L, 6L)));
  }

  @Test
  public void testExample2() {
    assertEquals(2578L, solveB(Arrays.asList(1L, 3L, 2L)));
  }

  @Test
  public void testExample3() {
    assertEquals(3544142L, solveB(Arrays.asList(2L, 1L, 3L)));
  }

  @Test
  public void testExample4() {
    assertEquals(261214L, solveB(Arrays.asList(1L, 2L, 3L)));
  }

  @Test
  public void testExample5() {
    assertEquals(6895259L, solveB(Arrays.asList(2L, 3L, 1L)));
  }

  @Test
  public void testExample6() {
    assertEquals(18L, solveB(Arrays.asList(3L, 2L, 1L)));
  }

  @Test
  public void testExample7() {
    assertEquals(362L, solveB(Arrays.asList(3L, 1L, 2L)));
  }

}
