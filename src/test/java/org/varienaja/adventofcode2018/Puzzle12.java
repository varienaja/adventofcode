package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(3725, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(3100000000293L, sum);
  }

  @Test
  public void testA() {
    assertEquals(325, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(3099999995594L, solveB(testInput()));
  }

  private long solve(List<String> input, int generations) {
    String generation = input.get(0).substring("initial state: ".length());

    Map<String, Character> rules = new HashMap<>();
    for (int i = 2; i < input.size(); i++) {
      String line = input.get(i);
      String[] parts = line.split(" => ");
      rules.put(parts[0], parts[1].charAt(0));
    }

    for (int i = 1; i <= generations; i++) {
      StringBuilder sb = new StringBuilder();

      for (int p = -1; p < generation.length() + 1; p++) {
        StringBuilder ppppp = new StringBuilder();
        for (int pp = -2; pp <= 2; pp++) {
          int ix = p + pp;
          char px = (ix >= 0 && ix < generation.length()) ? generation.charAt(ix) : '.';
          ppppp.append(px);
        }
        sb.append(rules.getOrDefault(ppppp.toString(), '.'));
      }

      generation = sb.toString();
    }

    long sum = 0;

    // after 20 generations, ix0 is not at ix20; or: first ix = -20
    int ix = -generations;
    for (int i = 0; i < generation.length(); i++) {
      if (generation.charAt(i) == '#') {
        sum += i + ix;
      }
    }

    return sum;
  }

  private long solveA(List<String> input) {
    return solve(input, 20);
  }

  private long solveB(List<String> input) {
    // From generation 90 on, the increment is always 62
    long sum = solve(input, 90);
    sum += (50000000000L - 90L) * 62L;
    return sum;
  }

  private List<String> testInput() {
    return List.of( //
        "initial state: #..#.#..##......###...###", //
        "", //
        "...## => #", //
        "..#.. => #", //
        ".#... => #", //
        ".#.#. => #", //
        ".#.## => #", //
        ".##.. => #", //
        ".#### => #", //
        "#.#.# => #", //
        "#.### => #", //
        "##.#. => #", //
        "##.## => #", //
        "###.. => #", //
        "###.# => #", //
        "####. => #");
  }

}
