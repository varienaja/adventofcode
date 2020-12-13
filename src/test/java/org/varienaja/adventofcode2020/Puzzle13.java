package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {

  private long solveA(String lines, int start) {
    String[] busLines = lines.split(",");

    int diff = 0;
    while (true) {
      for (int c = 0; c < busLines.length; c++) {
        if (!"x".contentEquals(busLines[c])) {
          int bl = Integer.parseInt(busLines[c]);
          if ((start + diff) % bl == 0) {
            return diff * bl;
          }
        }
      }

      diff++;
    }
  }

  private long solveB(String input) {
    String[] busLines = input.split(",");
    int i = 0;
    SortedMap<Integer, Integer> bls = new TreeMap<>();
    for (String bl : busLines) {
      if (!"x".equals(bl)) {
        bls.put(i, Integer.parseInt(bl));
      }
      i++;
    }
    return solveBRecursive(bls, bls.entrySet().iterator(), 1, 1);
  }

  private long solveBRecursive(SortedMap<Integer, Integer> busLines, Iterator<Entry<Integer, Integer>> it, long lastResult, long step) {
    long result = lastResult;
    if (it.hasNext()) {
      Entry<Integer, Integer> e = it.next();
      int timeDiff = e.getKey();
      int line = e.getValue();

      while ((result + timeDiff) % line != 0) {
        result += step;
      }

      return solveBRecursive(busLines, it, result, line * step);
    }
    return result;
  }

  @Test
  public void testDay12() {
    String input = "7,13,x,x,59,x,31,19";
    assertEquals(295, solveA(input, 939));

    announceResultA();
    String lines = "29,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,631,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,13,19,x,x,x,23,x,x,x,x,x,x,x,383,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,17";
    long result = solveA(lines, 1000507);
    System.out.println(result);
    assertEquals(370L, result);

    assertEquals(1068781L, solveB(input));
    assertEquals(754018L, solveB("67,7,59,61"));
    assertEquals(779210L, solveB("67,x,7,59,61"));
    assertEquals(1261476L, solveB("67,7,x,59,61"));
    assertEquals(1202161486L, solveB("1789,37,47,1889"));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(894954360381385L, result);
  }

}
