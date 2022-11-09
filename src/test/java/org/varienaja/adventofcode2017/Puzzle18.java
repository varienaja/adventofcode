package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  private long solveA(List<String> input) {
    Map<Character, Long> register2Value = new HashMap<>();
    long freq = -1;

    int ip = 0;
    while (ip < input.size()) {
      String line = input.get(ip);
      String[] parts = line.split("\\s");
      char register = parts[1].charAt(0);
      long val = 0;
      if (parts.length == 3) {
        if (Character.isAlphabetic(parts[2].charAt(0))) {
          val = register2Value.getOrDefault(parts[2].charAt(0), 0L);
        } else {
          val = Integer.parseInt(parts[2]);
        }
      }
      long value = val;

      if ("snd".equals(parts[0])) {
        freq = register2Value.getOrDefault(register, 0L);
      } else if ("set".equals(parts[0])) {
        register2Value.put(register, value);
      } else if ("add".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 + value : v + value);
      } else if ("mul".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 : v * value);
      } else if ("mod".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 : v % value);
      } else if ("rcv".equals(parts[0])) {
        if (register2Value.getOrDefault(parts[1].charAt(0), 0L) != 0L) {
          return freq;
        }
      }

      if ("jgz".equals(parts[0])) {
        if (register2Value.getOrDefault(register, 0L) > 0L) {
          ip += value;
        } else {
          ip++;
        }
      } else {
        ip++;
      }
    }

    return -1;
  }

  private long solveB(List<String> input) {
    // Keep two sets of memory and program counter.
    // Run one instruction for p0, then one for p1, one for p0, etc...
    Map<Character, Long> p0 = new HashMap<>();
    p0.put('p', 0L);
    int ip0 = 0;
    Map<Character, Long> p1 = new HashMap<>();
    p1.put('p', 1L);
    int ip1 = 0;
    long p1Sends = 0;

    // Communications queues
    List<Long> p0to1 = new LinkedList<>();
    List<Long> p1to0 = new LinkedList<>();

    boolean p0Active = true;
    boolean waiting0 = false;
    boolean waiting1 = false;
    while (!waiting0 || !waiting1) { // If BOTH computers are waiting, we deadlock and exit.
      int ip = p0Active ? ip0 : ip1;
      Map<Character, Long> register2Value = p0Active ? p0 : p1;
      List<Long> sendQ = p0Active ? p0to1 : p1to0;
      List<Long> recQ = p0Active ? p1to0 : p0to1;

      String line = input.get(ip);
      String[] parts = line.split("\\s");
      char register = parts[1].charAt(0);
      long val = 0;
      if (parts.length == 3) {
        if (Character.isAlphabetic(parts[2].charAt(0))) {
          val = register2Value.getOrDefault(parts[2].charAt(0), 0L);
        } else {
          val = Integer.parseInt(parts[2]);
        }
      }
      long value = val;

      if ("snd".equals(parts[0])) {
        if (Character.isAlphabetic(parts[1].charAt(0))) {
          val = register2Value.getOrDefault(parts[1].charAt(0), 0L);
        } else {
          val = Integer.parseInt(parts[1]);
        }
        sendQ.add(val);
        if (!p0Active) {
          p1Sends++;
        }
      } else if ("set".equals(parts[0])) {
        register2Value.put(register, value);
      } else if ("add".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 + value : v + value);
      } else if ("mul".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 : v * value);
      } else if ("mod".equals(parts[0])) {
        register2Value.compute(register, (k, v) -> v == null ? 0 : v % value);
      } else if ("rcv".equals(parts[0])) {
        if (!recQ.isEmpty()) {
          register2Value.put(register, recQ.remove(0));
          if (p0Active) {
            waiting0 = false;
          } else {
            waiting1 = false;
          }
        } else {
          ip--; // retry
          if (p0Active) {
            waiting0 = true;
          } else {
            waiting1 = true;
          }
        }
      }

      if ("jgz".equals(parts[0])) {
        if (Character.isAlphabetic(register)) { // >aargh register may be numeric too!
          val = register2Value.getOrDefault(register, 0L);
        } else {
          val = Integer.parseInt(parts[1]);
        }
        if (val > 0L) {
          ip += value;
        } else {
          ip++;
        }
      } else {
        ip++;
      }

      if (p0Active) {
        ip0 = ip;
      } else {
        ip1 = ip;
      }

      p0Active = !p0Active;
    }

    return p1Sends;
  }

  @Test
  public void testDay18() {
    List<String> testInput = List.of( //
        "set a 1", //
        "add a 2", //
        "mul a a", //
        "mod a 5", //
        "snd a", //
        "set a 0", //
        "rcv a", //
        "jgz a -1", //
        "set a 1", //
        "jgz a -2");
    assertEquals(4, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(3423, result);

    testInput = List.of( //
        "snd 1", //
        "snd 2", //
        "snd p", //
        "rcv a", //
        "rcv b", //
        "rcv c", //
        "rcv d");
    assertEquals(3, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(7493, result);
  }

}
