package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {
  private static final int CARDSIZE = 5;

  /** Ordered Map of turnCount to a matching Bingo card. */
  private NavigableMap<Integer, int[][]> turns2Card;
  /** Quick lookup of the turn number for a drawn bingo number. */
  private Map<Integer, Integer> nr2turn;
  /** All drawn bingo numbers. */
  private List<Integer> numbers;

  private void playBingo(List<String> lines) {
    String[] nrs = lines.get(0).split(",");
    nr2turn = new HashMap<>();
    numbers = new LinkedList<>();
    for (int i = 0; i < nrs.length; i++) {
      int nr = Integer.parseInt(nrs[i]);
      nr2turn.put(nr, i);
      numbers.add(nr);
    }

    List<int[][]> cards = new LinkedList<>();
    for (int i = 2; i < lines.size(); i++) {
      int[][] card = new int[CARDSIZE][CARDSIZE];
      int ix = 0;
      do {
        String line = lines.get(i++).trim();
        card[ix++] = Stream.of(line.split("\\s+")).map(s -> Integer.parseInt(s)).mapToInt(j -> j).toArray();
      } while (i < lines.size() && !lines.get(i).isEmpty());
      cards.add(card);
    }

    turns2Card = new TreeMap<>();
    for (int[][] card : cards) { // For each Bingo card, check in how many turns it'd generate a Bingo
      int minTurns = nr2turn.size(); // turns needed to generate a Bingo in the whole card
      for (int a = 0; a < CARDSIZE; a++) {
        int minTurnsA = 0; // turns needed to generate a Bingo in row a
        int minTurnsB = 0; // turns needed to generate a Bingo in column a
        for (int b = 0; b < CARDSIZE; b++) {
          int turn = nr2turn.get(card[a][b]);
          if (turn > minTurnsA) {
            minTurnsA = turn;
          }

          turn = nr2turn.get(card[b][a]);
          if (turn > minTurnsB) {
            minTurnsB = turn;
          }
        }

        int score = Math.min(minTurnsA, minTurnsB);
        if (score < minTurns) {
          minTurns = score;
        }
      }

      turns2Card.put(minTurns, card);
    }
  }

  private long solveA() {
    return solveInternal(true);
  }

  private long solveB() {
    return solveInternal(false);
  }

  private long solveInternal(boolean findMinimum) {
    Entry<Integer, int[][]> best = findMinimum ? turns2Card.firstEntry() : turns2Card.lastEntry();
    Set<Integer> cardNrs = new HashSet<>();
    for (int r = 0; r < CARDSIZE; r++) {
      for (int c = 0; c < CARDSIZE; c++) {
        cardNrs.add(best.getValue()[r][c]);
      }
    }
    cardNrs.removeAll(numbers.subList(0, best.getKey() + 1));
    return cardNrs.stream().mapToInt(i -> i).sum() * numbers.get(best.getKey());
  }

  @Test
  public void testDay04() {
    List<String> testInput = Arrays.asList("7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1", //
        "", //
        "22 13 17 11  0", //
        " 8  2 23  4 24", //
        "21  9 14 16  7", //
        " 6 10  3 18  5", //
        " 1 12 20 15 19", //
        "", //
        " 3 15  0  2 22", //
        " 9 18 13 17  5", //
        "19  8  7 25 23", //
        "20 11 10 24  4", //
        "14 21 16 12  6", //
        "", "14 21 17 24  4", //
        "10 16 15  9 19", //
        "18  8 23 26 20", //
        "22 11 13  6  5", //
        " 2  0 12  3  7");
    playBingo(testInput);
    assertEquals(4512, solveA());
    assertEquals(1924, solveB());

    announceResultA();
    playBingo(getInput());
    long result = solveA();
    assertEquals(35711, result);
    System.out.println(result);

    announceResultB();
    result = solveB();
    assertEquals(5586, result);
    System.out.println(result);
  }

}
