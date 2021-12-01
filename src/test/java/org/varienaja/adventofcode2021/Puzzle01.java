package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  private int solveA(int[] numbers) {
    int increases = 0;
    for (int i = 1; i < numbers.length; i++) {
      if (numbers[i] > numbers[i - 1]) {
        increases++;
      }
    }
    return increases;
  }

  private int solveB(int[] numbers) {
    int[] nrs = new int[numbers.length - 2];

    for (int i = 2; i < numbers.length; i++) {
      nrs[i - 2] = numbers[i] + numbers[i - 1] + numbers[i - 2];
    }

    return solveA(nrs);
  }

  @Test
  public void testDay01() {
    int[] testInput = {
        199, 200, 208, 210, 200, 207, 240, 269, 260, 263
    };
    assertEquals(7, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    int[] numbers = lines.stream().map(Integer::parseInt).mapToInt(i -> i).toArray();
    int result = solveA(numbers);
    assertEquals(1139, result);
    System.out.println(result);

    assertEquals(5, solveB(testInput));
    announceResultB();
    result = solveB(numbers);
    assertEquals(1103, result);
    System.out.println(result);
  }

}
