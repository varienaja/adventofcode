package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  private int solveA(int[] numbers) {
    for (int i = 0; i < numbers.length - 1; i++) {
      for (int j = i + 1; j < numbers.length; j++) {
        if (numbers[i] + numbers[j] == 2020) {
          return numbers[i] * numbers[j];
        }
      }
    }
    return -1;
  }

  private int solveB(int[] numbers) {
    for (int i = 0; i < numbers.length - 2; i++) {
      for (int j = i + 1; j < numbers.length - 1; j++) {
        for (int k = j + 1; k < numbers.length; k++) {
          if (numbers[i] + numbers[j] + numbers[k] == 2020) {
            return numbers[i] * numbers[j] * numbers[k];
          }
        }
      }
    }
    return -1;
  }

  @Test
  public void testDay01() {
    int[] testInput = {
        1721, 979, 366, 299, 675, 1456
    };
    assertEquals(514579, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    int[] numbers = lines.stream().map(Integer::parseInt).mapToInt(i -> i).toArray();
    int result = solveA(numbers);
    assertEquals(987339, result);
    System.out.println(result);

    assertEquals(241861950, solveB(testInput));
    announceResultB();
    result = solveB(numbers);
    assertEquals(259521570, result);
    System.out.println(result);
  }

}
