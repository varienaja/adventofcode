package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(3390596, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(5083024, sum);
  }

  private long solveA(List<String> input) {
    long fuelNeeded = 0;
    for (String line : input) {
      long mass = Long.parseLong(line);
      long fuel = (mass / 3) - 2;
      fuelNeeded += fuel;
    }
    return fuelNeeded;
  }

  private long solveB(List<String> input) {
    long fuelNeeded = 0;
    for (String line : input) {
      long mass = Long.parseLong(line);
      long fuel = 0;
      while ((fuel = Math.max(0, (mass / 3) - 2)) != 0) {
        mass = fuel;
        fuelNeeded += fuel;
      }
    }
    return fuelNeeded;
  }

  @Test
  public void testA() {
    assertEquals(2 + 2 + 654 + 33583, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(2 + 2 + 966 + 50346, solveB(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
        "12", //
        "14", //
        "1969", //
        "100756");
  }

}
