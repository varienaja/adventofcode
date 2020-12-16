package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {

  private long solve(List<Integer> lines, int round) { // credits: wintergb
    int[] spoken2Rounds = new int[round];
    int r = 1;
    int nowSpoken = 0;
    int lastSpoken = 0;

    while (r <= round) {
      lastSpoken = r <= lines.size() ? lines.get(r - 1) : nowSpoken;
      nowSpoken = spoken2Rounds[lastSpoken] > 0 ? r - spoken2Rounds[lastSpoken] : 0;
      spoken2Rounds[lastSpoken] = r++;
    }
    return lastSpoken;
  }

  private long solveA(List<Integer> lines) {
    return solve(lines, 2020);
  }

  private long solveB(List<Integer> lines) {
    return solve(lines, 30000000);
  }

  private long solveSlow(List<Integer> lines, int round) {
    Map<Integer, Queue<Integer>> spoken2Rounds = IntStream.range(0, lines.size()) //
        .boxed() //
        .collect(Collectors.toMap(lines::get, i -> new LinkedList<>(Arrays.asList(i))));
    int r = lines.size();
    int lastSpoken = lines.get(r - 1);

    while (r < round) {
      Queue<Integer> rounds = spoken2Rounds.computeIfAbsent(lastSpoken, k -> new LinkedList<>());
      lastSpoken = 0;
      if (rounds.size() > 1) {
        int secondLastRound = rounds.poll();
        lastSpoken = r - 1 - secondLastRound;
      }
      spoken2Rounds.computeIfAbsent(lastSpoken, k -> new LinkedList<>()).offer(r++);
    }
    return lastSpoken;
  }

  @Test
  public void testDay15() {
    List<Integer> input = Arrays.asList(0, 3, 6);
    assertEquals(436L, solveA(input));
    assertEquals(436L, solveSlow(input, 2020));

    announceResultA();
    List<Integer> lines = Arrays.asList(0, 5, 4, 1, 10, 14, 7);
    long result = solveA(lines);
    assertEquals(203L, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines);
    assertEquals(9007186L, result);
    System.out.println(result);
  }

  @Test
  @Ignore
  public void testExample1() {
    assertEquals(175594L, solveB(Arrays.asList(0, 3, 6)));
  }

  @Test
  @Ignore
  public void testExample2() {
    assertEquals(2578L, solveB(Arrays.asList(1, 3, 2)));
  }

  @Test
  @Ignore
  public void testExample3() {
    assertEquals(3544142L, solveB(Arrays.asList(2, 1, 3)));
  }

  @Test
  @Ignore
  public void testExample4() {
    assertEquals(261214L, solveB(Arrays.asList(1, 2, 3)));
  }

  @Test
  @Ignore
  public void testExample5() {
    assertEquals(6895259L, solveB(Arrays.asList(2, 3, 1)));
  }

  @Test
  @Ignore
  public void testExample6() {
    assertEquals(18L, solveB(Arrays.asList(3, 2, 1)));
  }

  @Test
  @Ignore
  public void testExample7() {
    assertEquals(362L, solveB(Arrays.asList(3, 1, 2)));
  }

}
