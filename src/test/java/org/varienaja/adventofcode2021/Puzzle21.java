package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {
  private class GameState {
    int p1pos;
    int p2pos;
    long p1score;
    long p2score;

    public GameState(int pp1, int pp2, long sc1, long sc2) {
      p1pos = pp1;
      p2pos = pp2;
      p1score = sc1;
      p2score = sc2;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof GameState) {
        GameState other = (GameState)o;
        return other.p1pos == p1pos && other.p2pos == p2pos && //
            other.p1score == p1score && other.p2score == p2score;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(p1pos, p2pos, p1score, p2score);
    }
  }

  private Map<GameState, long[]> state2Outcome = new HashMap<>();
  private int[] dice = new int[] {
      1, 2, 3
  };

  private GameState parse(List<String> lines) {
    int p1pos = Integer.parseInt(lines.get(0).substring(lines.get(0).indexOf(": ") + 2)) - 1;
    int p2pos = Integer.parseInt(lines.get(1).substring(lines.get(1).indexOf(": ") + 2)) - 1;
    return new GameState(p1pos, p2pos, 0, 0);
  }

  private long playGameA(GameState s) {
    long rolls = 0;
    int diceVal = 1;
    while (true) {
      // Don't care about diceVal getting > 100, because 101 has the same effect as
      // 1 because our playing area is only 10 fields long.
      int r1 = diceVal++;
      int r2 = diceVal++;
      int r3 = diceVal++;
      rolls += 3;
      s.p1pos = (s.p1pos + r1 + r2 + r3) % 10;
      s.p1score += s.p1pos + 1;
      if (s.p1score >= 1000) {
        return rolls * s.p2score;
      }

      s = new GameState(s.p2pos, s.p1pos, s.p2score, s.p1score);
    }
  }

  /**
   * Plays the Game, starting with State s.
   *
   * @param s the Game state
   * @return an array containing the amount of universes in which player 1 and 2 win
   */
  private long[] playGameB(GameState s) {
    if (s.p1score >= 21) {
      return new long[] {
          1, 0
      };
    }
    if (s.p2score >= 21) {
      return new long[] {
          0, 1
      };
    }
    long[] result = state2Outcome.get(s);
    if (result != null) {
      return result;
    }
    result = new long[2];
    for (int d1 : dice) {
      for (int d2 : dice) {
        for (int d3 : dice) {
          int newp1pos = (s.p1pos + d1 + d2 + d3) % 10;
          long newp1score = s.p1score + newp1pos + 1;

          long[] p = playGameB(new GameState(s.p2pos, newp1pos, s.p2score, newp1score));
          result[0] += p[1];
          result[1] += p[0];
        }
      }
    }
    state2Outcome.put(s, result);
    return result;
  }

  private long solveA(List<String> lines) {
    return playGameA(parse(lines));
  }

  private long solveB(List<String> lines) {
    long[] result = playGameB(parse(lines));
    return Math.max(result[0], result[1]);
  }

  @Test
  public void testDay21() {
    List<String> testInput = Arrays.asList(//
        "Player 1 starting position: 4", //
        "Player 2 starting position: 8");
    assertEquals(739785, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(684495, result);

    result = solveB(testInput);
    assertEquals(444356092776315L, result);
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(152587196649184L, result);
  }

}
