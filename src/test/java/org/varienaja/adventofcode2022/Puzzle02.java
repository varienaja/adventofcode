package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(11666L, result);
    System.out.println(result);
  }

  @Test
  public void doB() {
    announceResultB();
    List<String> lines = getInput();
    long result = solveB(lines);
    assertEquals(12767L, result);
    System.out.println(result);
  }

  private List<String> getTestInput() {
    return List.of( //
        "A Y", //
        "B X", //
        "C Z");
  }

  private long solve(List<String> lines, boolean optionB) {
    long totalScore = 0L;
    for (String game : lines) {
      int opponent = game.charAt(0) - 'A'; // A for Rock, B for Paper, and C for Scissors -> 0,1,2
      int me = game.charAt(2) - 'X'; // 0,1,2

      if (optionB) {// Modify my turn, to win, loose or draw
        if (me == 2) { // Need to win
          me = opponent + 1;
        } else if (me == 1) { // Draw
          me = opponent;
        } else { // Need to loose
          me = opponent - 1;
        }
        me = (3 + me) % 3; // Make sure me is 0, 1 or 2
      }

      // The score for a single round is the score for the shape you selected (1 for Rock, 2 for Paper, and 3 for
      // Scissors) plus the score for the outcome of the round (0 if you lost, 3 if the round was a draw, and 6 if you
      // won).
      int result = (3 + opponent - me) % 3; // Make sure result is 0, 1 or 2
      long score = result == 1 ? 0L // opponent wins
          : result == 2 ? 6L // I win
              : 3L; // draw
      score += 1 + me;

      totalScore += score;
    }
    return totalScore;
  }

  private long solveA(List<String> lines) {
    return solve(lines, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, true);
  }

  @Test
  public void testA() {
    assertEquals(15L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(12L, solveB(getTestInput()));
  }

}
