package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {
  class FirewallLayer {
    int depth;
    int range;
    int pos;
    boolean goingDown;

    FirewallLayer(int depth, int range) {
      this.depth = depth;
      this.range = range;
      goingDown = true;
      pos = 1;
    }

    void doStep() {
      if (goingDown) {
        if (pos == range) {
          goingDown = !goingDown;
          pos--;
        } else {
          pos++;
        }
      } else {
        if (pos == 1) {
          pos++;
          goingDown = !goingDown;
        } else {
          pos--;
        }
      }
    }
  }

  FirewallLayer[] parse(List<String> input) {
    NavigableMap<Integer, Integer> depth2range = new TreeMap<>();
    for (String line : input) {
      String[] parts = line.split(": ");
      int depth = Integer.parseInt(parts[0]);
      int range = Integer.parseInt(parts[1]);
      depth2range.put(depth, range);
    }

    FirewallLayer[] layers = new FirewallLayer[depth2range.lastKey() + 1];
    for (Entry<Integer, Integer> e : depth2range.entrySet()) {
      layers[e.getKey()] = new FirewallLayer(e.getKey(), e.getValue());
    }
    return layers;
  }

  long severity(FirewallLayer[] layers, boolean reportZero) {
    long severity = 0;

    for (int i = 0; i < layers.length; i++) {
      for (FirewallLayer l : layers) {
        if (l != null) {
          if (i == l.depth && l.pos == 1) {
            severity += reportZero ? (i + 1) * l.range : i * l.range;
          }
          l.doStep();
        }
      }
    }

    return severity;
  }

  private long solveA(List<String> input) {
    FirewallLayer[] layers = parse(input);
    return severity(layers, false);
  }

  private long solveB(List<String> input) {
    FirewallLayer[] layers = parse(input);

    int max = 10000000;
    BitSet bs = new BitSet(max);
    bs.set(0, max);
    for (int i = 0; i < layers.length; i++) {
      if (layers[i] != null) {
        int period = (layers[i].range - 1) * 2;
        int offset = i;
        // System.out.println("not divisible by " + period + "( minus " + offset + ")");
        for (int x = -offset; x < max; x += period) {
          if (x >= 0) {
            bs.set(x, false);
          }
        }
      }
    }

    return bs.nextSetBit(0);
  }

  @Test
  public void testDay13() {
    List<String> testInput = List.of( //
        "0: 3", // 1,2,3,2,1: 0,4,8,12,...not divisible by 4
        "1: 2", // 1,2,1: 0,2,4,6,8,..., not divisible by 2 (minus one)
        "4: 4", // 1,2,3,4,3,2,1: 0,6,12,18,... not divisible by 6 ( minus 4)
        "6: 4");// 1,2,3,4,3,2,1: 0,6,12,18,... not divisible by 6 ( minus 6)
    // Period is (range-1)*2
    assertEquals(24, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(2264, result);

    assertEquals(10, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(3875838, result);
  }

}
