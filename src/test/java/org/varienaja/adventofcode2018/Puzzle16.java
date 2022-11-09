package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {
  private Map<Integer, Set<Integer>> op2instructions;
  private int[] registers;

  private int[] addi(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] + b;
    return result;
  }

  private int[] addr(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] + registers[b];
    return result;
  }

  private int[] bani(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] & b;
    return result;
  }

  private int[] banr(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] & registers[b];
    return result;
  }

  private int[] bori(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] | b;
    return result;
  }

  private int[] borr(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] | registers[b];
    return result;
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(588, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(627, sum);
  }

  private int[] doOp(int op, int a, int b, int c) {
    switch (op) {
      case 0:
        return addr(a, b, c);
      case 1:
        return addi(a, b, c);
      case 2:
        return mulr(a, b, c);
      case 3:
        return muli(a, b, c);
      case 4:
        return banr(a, b, c);
      case 5:
        return bani(a, b, c);
      case 6:
        return borr(a, b, c);
      case 7:
        return bori(a, b, c);
      case 8:
        return setr(a, b, c);
      case 9:
        return seti(a, b, c);
      case 10:
        return gtir(a, b, c);
      case 11:
        return gtri(a, b, c);
      case 12:
        return gtrr(a, b, c);
      case 13:
        return eqir(a, b, c);
      case 14:
        return eqri(a, b, c);
      case 15:
        return eqrr(a, b, c);
    }
    return null;
  }

  private int[] eqir(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = a == registers[b] ? 1 : 0;
    return result;
  }

  private int[] eqri(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] == b ? 1 : 0;
    return result;
  }

  private int[] eqrr(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] == registers[b] ? 1 : 0;
    return result;
  }

  private int[] gtir(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = a > registers[b] ? 1 : 0;
    return result;
  }

  private int[] gtri(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] > b ? 1 : 0;
    return result;
  }

  private int[] gtrr(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] > registers[b] ? 1 : 0;
    return result;
  }

  private int[] muli(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] * b;
    return result;
  }

  private int[] mulr(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a] * registers[b];
    return result;
  }

  private int[] parseLine(String line) {
    String[] parts = line.split("\\D+");
    int[] result = new int[4];

    int t = 0;
    for (int i = 0; i < parts.length; i++) {
      try {
        result[t] = Integer.parseInt(parts[i]);
        t++;
      } catch (NumberFormatException e) { // Ignore
      }
    }

    return result;
  }

  private int[] seti(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = a;
    return result;
  }

  private int[] setr(int a, int b, int c) {
    int[] result = new int[4];
    System.arraycopy(registers, 0, result, 0, registers.length);
    result[c] = registers[a];
    return result;
  }

  private long solveA(List<String> input) {
    long result = 0;
    op2instructions = new HashMap<>();

    int i = 0;
    String line = null;
    do {
      line = input.get(i);
      if (line.startsWith("Before")) {
        registers = parseLine(line);

        int[] instruction = parseLine(input.get(i + 1));
        int[] after = parseLine(input.get(i + 2));

        int matches = 0;
        for (int op = 0; op < 16; op++) {
          int[] tester = doOp(op, instruction[1], instruction[2], instruction[3]);
          if (Arrays.equals(tester, after)) {
            matches++;
            op2instructions.compute(instruction[0], (k, v) -> v == null ? new HashSet<>() : v).add(op);
          }
        }

        result += matches >= 3 ? 1 : 0;
        i += 4;
      }
    } while (i < input.size() && !line.isEmpty());
    return result;
  }

  private long solveB(List<String> input) {
    solveA(input);

    int[] translation = new int[16];
    Arrays.fill(translation, -1);

    while (!op2instructions.isEmpty()) {
      Iterator<Entry<Integer, Set<Integer>>> it = op2instructions.entrySet().iterator();

      Set<Integer> added = new HashSet<>();
      while (it.hasNext()) {
        Entry<Integer, Set<Integer>> e = it.next();
        if (e.getValue().size() == 1) {
          translation[e.getKey()] = e.getValue().iterator().next();
          it.remove();
          added.add(translation[e.getKey()]);
        }
      }

      for (Set<Integer> v : op2instructions.values()) {
        v.removeAll(added);
      }
    }
    // We now know what opcode is which instruction

    int start = 3;
    while (!input.get(start).isEmpty() && !input.get(start + 1).isEmpty() && !input.get(start + 2).isEmpty()) {
      start++;
    }

    Arrays.fill(registers, 0);
    for (int i = start; i < input.size(); i++) {
      int[] instruction = parseLine(input.get(i));
      registers = doOp(translation[instruction[0]], instruction[1], instruction[2], instruction[3]);
    }

    return registers[0];
  }

  @Test
  public void testA() {
    assertEquals(1, solveA(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
        "Before: [3, 2, 1, 1]", //
        "9 2 1 2", //
        "After:  [3, 2, 2, 1]");
  }
}
