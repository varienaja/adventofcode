package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {
  private NavigableSet<Long> answers;

  @Test
  public void doA() {
    answers = new TreeSet<>();
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(22012, sum);
  }

  @Test
  public void doB() {
    answers = new TreeSet<>();
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(4039164, sum);
  }

  private long solveA(String input) {
    List<Long> phases = List.of(3L, 1L, 2L, 4L, 0L);
    solveA(input, phases, 0, new long[5]);
    return answers.last();
  }

  private void solveA(String input, List<Long> phases, int ix, long[] inputs) {
    if (ix == 5) {
      try {
        BlockingQueue<Long> inA = new LinkedBlockingDeque<>();
        inA.addAll(List.of(inputs[0], 0L));
        BlockingQueue<Long> outA = new LinkedBlockingDeque<>();
        Intcode.run(input, Map.of(), inA, outA).get();

        BlockingQueue<Long> inB = new LinkedBlockingDeque<>();
        inB.add(inputs[1]);
        inB.add(outA.poll());
        BlockingQueue<Long> outB = new LinkedBlockingDeque<>();
        Intcode.run(input, Map.of(), inB, outB).get();

        BlockingQueue<Long> inC = new LinkedBlockingDeque<>();
        inC.add(inputs[2]);
        inC.add(outB.poll());
        BlockingQueue<Long> outC = new LinkedBlockingDeque<>();
        Intcode.run(input, Map.of(), inC, outC).get();

        BlockingQueue<Long> inD = new LinkedBlockingDeque<>();
        inD.add(inputs[3]);
        inD.add(outC.poll());
        BlockingQueue<Long> outD = new LinkedBlockingDeque<>();
        Intcode.run(input, Map.of(), inD, outD).get();

        BlockingQueue<Long> inE = new LinkedBlockingDeque<>();
        inE.add(inputs[4]);
        inE.add(outD.poll());
        BlockingQueue<Long> outE = new LinkedBlockingDeque<>();
        Intcode.run(input, Map.of(), inE, outE).get();

        answers.add(outE.poll());
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      for (long i : phases) {
        List<Long> newPossibleInputs = new LinkedList<>(phases);
        newPossibleInputs.remove(Long.valueOf(i));
        inputs[ix] = i;
        solveA(input, newPossibleInputs, ix + 1, inputs);
      }
    }
  }

  private long solveB(String input) {
    List<Long> phases = List.of(5L, 6L, 7L, 8L, 9L);
    solveB(input, phases, 0, new long[5]);
    return answers.last();
  }

  private void solveB(String input, List<Long> phases, int ix, long[] inputs) {
    if (ix == 5) {
      BlockingQueue<Long> inA = new LinkedBlockingDeque<>();
      inA.add(inputs[0]);
      inA.add(0L);

      BlockingQueue<Long> inB = new LinkedBlockingDeque<>();
      inB.add(inputs[1]);

      BlockingQueue<Long> inC = new LinkedBlockingDeque<>();
      inC.add(inputs[2]);

      BlockingQueue<Long> inD = new LinkedBlockingDeque<>();
      inD.add(inputs[3]);

      BlockingQueue<Long> inE = new LinkedBlockingDeque<>();
      inE.add(inputs[4]);

      Intcode.run(input, Map.of(), inA, inB);
      Intcode.run(input, Map.of(), inB, inC);
      Intcode.run(input, Map.of(), inC, inD);
      Intcode.run(input, Map.of(), inD, inE);
      try {
        Intcode.run(input, Map.of(), inE, inA).get(); // Wait for Amp E to finish
        long answer = inA.poll();
        answers.add(answer);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      for (long i : phases) {
        List<Long> newPossibleInputs = new LinkedList<>(phases);
        newPossibleInputs.remove(Long.valueOf(i));
        inputs[ix] = i;
        solveB(input, newPossibleInputs, ix + 1, inputs);
      }
    }
  }

}
