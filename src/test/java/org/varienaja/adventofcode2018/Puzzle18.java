package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

import com.google.common.base.Functions;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {
  private static final char OPEN = '.';
  private static final char TREE = '|';
  private static final char LUMBER = '#';
  private static final Map<Long, Long> round2Value = new HashMap<>();
  /**
   * Arbitrary value of rounds after which we hope to have found a repeating pattern. With my input this is around 600.
   */
  private static final long MAX = 650;

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(506385, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(215404, sum);
  }

  private long solve(List<String> input, long rounds) {
    long period = 0;
    int maxX = input.get(0).length();
    int maxY = input.size();

    Map<Point, Character> grid1 = new HashMap<>();
    List<Long> answers = new LinkedList<>();
    answers.add(0L);

    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        grid1.put(new Point(x, y), line.charAt(x));
      }
    }

    for (long i = 1; i <= Math.min(rounds, MAX); i++) {
      Map<Point, Character> grid2 = new HashMap<>();
      for (int y = 0; y < maxY; y++) {
        for (int x = 0; x < maxX; x++) {
          Point p = new Point(x, y);
          char c = grid1.get(p);

          Map<Character, Long> adjacent2Count = p.getAllNeighbours().stream() //
              .map(grid1::get) //
              .filter(Objects::nonNull) //
              .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()));

          if (c == OPEN) {
            // Becomes trees if >=3 are trees, otherwise nothing happens
            grid2.put(p, adjacent2Count.getOrDefault(TREE, 0L) >= 3 ? TREE : c);
          } else if (c == TREE) {
            // becomes lumberyard if >=3 are lumberyard, otherwise nothing happens
            grid2.put(p, adjacent2Count.getOrDefault(LUMBER, 0L) >= 3 ? LUMBER : c);
          } else if (c == LUMBER) {
            // remains lumberyard if >=1 lumberyard and >=1 trees, otherwise becomes open
            grid2.put(p, adjacent2Count.getOrDefault(LUMBER, 0L) >= 1 && adjacent2Count.getOrDefault(TREE, 0L) >= 1 ? LUMBER : OPEN);
          }
        }
      }
      grid1 = grid2;

      long treeCount = 0;
      long lumberCount = 0;
      for (char c : grid1.values()) {
        if (c == LUMBER) {
          lumberCount++;
        } else if (c == TREE) {
          treeCount++;
        }
      }
      long answer = treeCount * lumberCount;
      answers.add(answer);
      Long seen = round2Value.put(answer, i);
      if (seen != null) {
        period = i - seen;
      }
    }
    if (period != 0) {
      // TDO The MAX constant is ridiculous, use https://en.wikipedia.org/wiki/Cycle_detection#Floyd's_Tortoise_and_Hare
      // to find cycles!
      Map<Long, Long> ix2Answer = new HashMap<>();
      for (long i = answers.size() - period; i < answers.size(); i++) {
        ix2Answer.put(i % period, answers.get((int)i));
      }
      long ix = rounds % period;
      return ix2Answer.get(ix);
    }

    long treeCount = 0;
    long lumberCount = 0;
    for (char c : grid1.values()) {
      if (c == LUMBER) {
        lumberCount++;
      } else if (c == TREE) {
        treeCount++;
      }
    }

    return treeCount * lumberCount;
  }

  private long solveA(List<String> input) {
    return solve(input, 10);
  }

  private long solveB(List<String> input) {
    // After an amount of rounds, the resource value keeps alternating between the same few values
    return solve(input, 1000000000L);
  }

  @Test
  public void testA() {
    assertEquals(1147, solveA(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
        ".#.#...|#.", //
        ".....#|##|", //
        ".|..|...#.", //
        "..|#.....#", //
        "#.#|||#|#|", //
        "...#.||...", //
        ".|....|...", //
        "||...#|.#|", //
        "|.||||..|.", //
        "...#.|..|.");
  }
}
