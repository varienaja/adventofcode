package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {

  class Computer {
    long a;
    long b;
    long c;
    int[] program;
    int ip;
    LinkedList<Integer> out;

    Computer(List<String> input) {
      a = Long.parseLong(input.get(0).substring(input.get(0).indexOf(':') + 2));
      b = Long.parseLong(input.get(1).substring(input.get(1).indexOf(':') + 2));
      c = Long.parseLong(input.get(2).substring(input.get(2).indexOf(':') + 2));
      String code = input.get(4).substring(input.get(4).indexOf(':') + 2);
      program = Arrays.stream(code.split(",")).mapToInt(Integer::parseInt).toArray();
      ip = 0;
      out = new LinkedList<>();
    }

    /**
     * The output in position length - ix corresponds to the three bits at position ix. Starting with the last index, we
     * test which one of the 8 possible numbers gives the correct number in position length - ix. If we find a match, we
     * try further with the next index.
     *
     * @return the value for register a such that the output equals the program
     */
    long findA() {
      record QueueElement(int ix, long a) {
      }

      Queue<QueueElement> q = new LinkedList<>();
      q.offer(new QueueElement(program.length - 1, 0));

      while (!q.isEmpty()) {
        QueueElement qe = q.poll();
        for (long a = qe.a; a < qe.a + 8; ++a) {
          reset(a);
          run();
          if (tailEqual(qe.ix)) {
            if (qe.ix == 0) {
              return a;
            }
            q.offer(new QueueElement(qe.ix - 1, a << 3));
          }
        }
      }
      throw new IllegalStateException();
    }

    void reset(long aA) {
      a = aA;
      b = 0;
      c = 0;
      ip = 0;
      out.clear();
    }

    List<Integer> run() {
      while (ip < program.length) {
        long op = program[ip + 1];
        if (op == 4) {
          op = a;
        } else if (op == 5) {
          op = b;
        } else if (op == 6) {
          op = c;
        }

        switch (program[ip]) {
          case 0: // adv
            a = a >> op;
            break;
          case 1: // bxl
            b = b ^ program[ip + 1];
            break;
          case 2: // bst
            b = op % 8;
            break;
          case 3: // jnz
            ip = a == 0 ? ip : program[ip + 1] - 2; // compensate the ip += 2 after the switch
            break;
          case 4: // bxc
            b = b ^ c;
            break;
          case 5: // out
            int result = (int)(op & 7); // & 7 works better than % 8 (for negative numbers)
            out.add(result);
            break;
          case 6: // bdv
            b = a >> op;
            break;
          case 7: // cdv
            c = a >> op;
            break;
          default:
            throw new IllegalStateException();
        }
        ip += 2;
      }
      return out;
    }

    boolean tailEqual(int ix) {
      Iterator<Integer> it = out.descendingIterator();
      for (int i = program.length - 1; i >= ix; --i) {
        if (!it.hasNext() || program[i] != it.next()) {
          return false;
        }
      }
      return !it.hasNext();
    }
  }

  @Test
  public void doA() {
    announceResultA();
    String result = solveA(getInput());
    System.out.println(result);
    assertEquals("3,1,5,3,7,4,2,7,5", result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(190593310997519L, result);
  }

  @Test
  public void testA() {
    assertEquals("4,6,3,5,6,3,5,2,1,0", solveA(getTestInput('a')));
  }

  @Test
  public void testB() {
    assertEquals(117440, solveB(getTestInput('b')));
  }

  private String solveA(List<String> input) {
    return new Computer(input).run().stream().map(i -> Integer.toString(i)).collect(Collectors.joining(","));
  }

  private long solveB(List<String> input) {
    return new Computer(input).findA();
  }

}
