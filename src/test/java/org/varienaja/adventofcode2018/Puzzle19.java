package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {
  private int[] registers = new int[6];

  @Test
  public void doA() {
    Arrays.fill(registers, 0);
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(1968, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(21211200, sum);
  }

  private long solveA(List<String> input) {
    int ipr = Integer.parseInt(input.remove(0).split("\\s")[1]);
    int ip = 0;

    while (ip < input.size()) {
      registers[ipr] = ip;

      if (ip == 3) {
        for (int i = 1; i <= registers[4]; i++) {
          if (registers[4] % i == 0) {
            registers[0] += i;
          }
        }
        return registers[0];

        // So we check if reg[4] is dividable by 1,2,3, etc. and we sum up those in reg[0]

        // do {
        // registers[1] = registers[5] * registers[2]; // mulr 5 2 1
        // if (registers[1] == registers[4]) {
        // registers[0] = registers[5] + registers[0]; // addr 5 0 0
        // }
        // registers[2] = registers[2] + 1; // addi 2 1 2
        //
        // System.out.println(registers[ipr] + " : " + Arrays.toString(registers));
        // } while (registers[2] <= registers[4]); // gtrr 2 4 1

        // 03 mulr 5 2 1
        // 04 eqrr 1 4 1
        // 05 addr 1 3 3
        // 06 addi 3 1 3
        // 07 addr 5 0 0
        // 08 addi 2 1 2
        // 09 gtrr 2 4 1
        // 10 addr 3 1 3
        // 11 seti 2 3 3
        // System.out.println(registers[ipr] + " : " + Arrays.toString(registers));
      } else {
        String instruction = input.get(registers[ipr]);
        String[] parts = instruction.split("\\s");
        int a = Integer.parseInt(parts[1]);
        int b = Integer.parseInt(parts[2]);
        int c = Integer.parseInt(parts[3]);
        if ("addr".equals(parts[0])) {
          registers[c] = registers[a] + registers[b];
        } else if ("addi".equals(parts[0])) {
          registers[c] = registers[a] + b;
        } else if ("mulr".equals(parts[0])) {
          registers[c] = registers[a] * registers[b];
        } else if ("muli".equals(parts[0])) {
          registers[c] = registers[a] * b;
        } else if ("banr".equals(parts[0])) {
          registers[c] = registers[a] & registers[b];
        } else if ("bani".equals(parts[0])) {
          registers[c] = registers[a] & b;
        } else if ("borr".equals(parts[0])) {
          registers[c] = registers[a] | registers[b];
        } else if ("bori".equals(parts[0])) {
          registers[c] = registers[a] | b;
        } else if ("setr".equals(parts[0])) {
          registers[c] = registers[a];
        } else if ("seti".equals(parts[0])) {
          registers[c] = a;
        } else if ("gtir".equals(parts[0])) {
          registers[c] = a > registers[b] ? 1 : 0;
        } else if ("gtri".equals(parts[0])) {
          registers[c] = registers[a] > b ? 1 : 0;
        } else if ("gtrr".equals(parts[0])) {
          registers[c] = registers[a] > registers[b] ? 1 : 0;
        } else if ("eqir".equals(parts[0])) {
          registers[c] = a == registers[b] ? 1 : 0;
        } else if ("eqri".equals(parts[0])) {
          registers[c] = registers[a] == b ? 1 : 0;
        } else if ("eqrr".equals(parts[0])) {
          registers[c] = registers[a] == registers[b] ? 1 : 0;
        }
      }

      ip = registers[ipr] + 1;
    }

    return registers[0];
  }

  private long solveB(List<String> input) {
    Arrays.fill(registers, 0);
    registers[0] = 1;
    return solveA(input);
  }

  @Test
  public void testA() {
    assertEquals(6, solveA(testInput()));
  }

  private List<String> testInput() {
    return new ArrayList<>(List.of( //
        "#ip 0", //
        "seti 5 0 1", //
        "seti 6 0 2", //
        "addi 0 1 0", //
        "addr 1 2 3", //
        "setr 1 0 0", //
        "seti 8 0 4", //
        "seti 9 0 5"));
  }
}
