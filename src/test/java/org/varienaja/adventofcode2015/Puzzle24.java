package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {
  private int partitionSize;
  private long smallestProduct;

  /**
   * Generate all possible partitions of the integers in 'input' in the array
   * 'group1'. If the sum of all numbers of 'group' equals partitionSize, the
   * product of all numbers in 'group1' is returned.
   *
   * @param input all numbers
   * @param ix index in group1 to fill
   * @param group1 the chosen numbers
   */
  private void generate(int[] input, int ix, int[] group1) {
    if (ix == group1.length) {
      return;
    }

    nexti: for (int i : input) {
      int sum = 0;
      for (int j = 0; j < ix; j++) {
        sum += group1[j];
        if (group1[j] == i) {
          continue nexti;
        }
      }

      sum += i;
      group1[ix] = i;
      if (sum == partitionSize) {
        long product = Arrays.stream(group1).mapToLong(l -> l).reduce((a, b) -> a * b).getAsLong();
        if (product < smallestProduct) {
          smallestProduct = product;
          return;
        }
      }
      group1[ix] = i;

      generate(input, ix + 1, group1);
    }

  }

  private long solve(List<String> input, int groupCount) {
    smallestProduct = Long.MAX_VALUE;
    int[] items = input.stream().mapToInt(Integer::parseInt).sorted().toArray();
    int sum = 0;
    for (int item : items) {
      sum += item;
    }
    partitionSize = sum / groupCount;

    int size = 1;
    while (smallestProduct == Long.MAX_VALUE) {
      generate(items, 0, new int[size++]);
    }

    return smallestProduct;
  }

  private long solveA(List<String> input) {
    return solve(input, 3);
  }

  private long solveB(List<String> input) {
    return solve(input, 4);
  }

  @Test
  public void testDay24() {
    List<String> testInput = List.of( //
        "1", //
        "2", //
        "3", //
        "4", //
        "5", //
        "7", //
        "8", //
        "9", //
        "10", //
        "11");
    // assertEquals(99, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(10723906903L, sum);

    announceResultB();
    assertEquals(44, solveB(testInput));
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(74850409, sum);
  }

}
