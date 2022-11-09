package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {
  private int[] registers = new int[6];

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(15823996, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(10199686, sum);
  }

  private long run(List<String> input, int reg0) {
    // #ip 1
    // 0 seti 123 0 4
    // 1 bani 4 456 4
    // 2 eqri 4 72 4
    // 3 addr 4 1 1
    // 4 seti 0 0 1
    // 5 seti 0 8 4
    // 6 bori 4 65536 3
    // 7 seti 16098955 8 4
    // 8 bani 3 255 5
    // 9 addr 4 5 4
    // 10 bani 4 16777215 4
    // 11 muli 4 65899 4
    // 12 bani 4 16777215 4
    // 13 gtir 256 3 5
    // 14 addr 5 1 1
    // 15 addi 1 1 1
    // 16 seti 27 3 1
    // 17 seti 0 7 5
    // 18 addi 5 1 2
    // 19 muli 2 256 2
    // 20 gtrr 2 3 2
    // 21 addr 2 1 1
    // 22 addi 1 1 1
    // 23 seti 25 1 1
    // 24 addi 5 1 5
    // 25 seti 17 6 1
    // 26 setr 5 4 3
    // 27 seti 7 5 1
    // 28 eqrr 4 0 5 -- Here is the equals-check with register 0. The value in register 4 is 15823996 at that point, so
    // this value must be the answer for part A
    // 29 addr 5 1 1
    // 30 seti 5 3 1

    long instructionCounter = 0;
    Arrays.fill(registers, 0);
    registers[0] = reg0;

    int ipr = Integer.parseInt(input.remove(0).split("\\s")[1]);
    int ip = 0;

    while (ip < input.size()) {
      if (ipr >= registers.length) {
        return Long.MAX_VALUE;
      }
      registers[ipr] = ip;

      String instruction = input.get(registers[ipr]);
      // System.out.println(ip + " " + instruction);
      // System.out.println(ip + " " + Arrays.toString(registers));
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

      ip = registers[ipr] + 1;
      instructionCounter++;
      if (instructionCounter > 10000000) {
        return Long.MAX_VALUE;
      }
    }

    return instructionCounter;
  }

  private int runCompiled(int a) {
    a |= 0x10000;
    int b = 16098955;
    b += a & 0xff;
    b &= 0xffffff;
    b *= 65899;
    b &= 0xffffff;
    b += (a >> 8) & 0xff;
    b &= 0xffffff;
    b *= 65899;
    b &= 0xffffff;
    b += (a >> 16) & 0xff;
    b &= 0xffffff;
    b *= 65899;
    b &= 0xffffff;
    return b;
  }

  private long solveA(List<String> input) {
    int i = 15823996;
    while (run(new ArrayList<>(input), i) == Long.MAX_VALUE) {
      i++;
    }
    return i;
  }

  private long solveB(List<String> input) {
    int n = runCompiled(0);
    Set<Integer> seen = new HashSet<>();
    while (true) {
      int n2 = runCompiled(n);
      if (seen.contains(n2)) {
        return n;
      }
      seen.add(n);
      n = n2;
    }
  }

}
