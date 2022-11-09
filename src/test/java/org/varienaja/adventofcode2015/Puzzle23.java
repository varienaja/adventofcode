package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {

  private long solve(List<String> input, char register, boolean partB) {
    Map<Character, Integer> register2Value = new HashMap<>();
    register2Value.put('a', partB ? 1 : 0);
    register2Value.put('b', 0);

    int ip = 0;

    while (ip != input.size()) {
      String[] parts = input.get(ip).split("\\s");
      if ("hlf".equals(parts[0])) {
        register2Value.compute(parts[1].charAt(0), (k, v) -> v / 2);
        ip++;
      } else if ("tpl".equals(parts[0])) {
        register2Value.compute(parts[1].charAt(0), (k, v) -> v * 3);
        ip++;
      } else if ("inc".equals(parts[0])) {
        register2Value.compute(parts[1].charAt(0), (k, v) -> v + 1);
        ip++;
      } else if ("jmp".equals(parts[0])) {
        ip += Integer.parseInt(parts[1]);
      } else if ("jie".equals(parts[0])) {
        int v = register2Value.get(parts[1].charAt(0));
        ip += v % 2 == 0 ? Integer.parseInt(parts[2]) : 1;
      } else if ("jio".equals(parts[0])) {
        int v = register2Value.get(parts[1].charAt(0));
        ip += v == 1 ? Integer.parseInt(parts[2]) : 1;
      }
    }

    return register2Value.get(register);
  }

  private long solveA(List<String> input, char register) {
    return solve(input, register, false);
  }

  private long solveB(List<String> input, char register) {
    return solve(input, register, true);
  }

  @Test
  public void testDay23() {
    List<String> testInput = List.of( //
        "inc a", //
        "jio a, +2", //
        "tpl a", //
        "inc a");
    assertEquals(2, solveA(testInput, 'a'));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines, 'b');
    System.out.println(sum);
    assertEquals(184, sum);

    announceResultB();
    assertEquals(7, solveB(testInput, 'a'));
    sum = solveB(lines, 'b');
    System.out.println(sum);
    assertEquals(231, sum);
  }

}
