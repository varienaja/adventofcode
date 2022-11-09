package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  private int solveA(List<String> instructions) {
    final int size = 1000;
    BitSet[] field = new BitSet[size];
    for (int i = 0; i < size; i++) {
      field[i] = new BitSet(size);
    }
    Pattern p = Pattern.compile(".*?(\\d+),(\\d+) through (\\d+),(\\d+)");

    for (String instruction : instructions) {
      Matcher m = p.matcher(instruction);
      if (m.matches()) {
        int sX = Integer.parseInt(m.group(1));
        int sY = Integer.parseInt(m.group(2));
        int eX = Integer.parseInt(m.group(3));
        int eY = Integer.parseInt(m.group(4));

        boolean set = instruction.startsWith("turn on");
        boolean reset = instruction.startsWith("turn off");
        boolean flip = instruction.startsWith("toggle");

        for (int x = sX; x <= eX; x++) {
          if (set) {
            field[x].set(sY, eY + 1);
          } else if (reset) {
            field[x].clear(sY, eY + 1);
          } else if (flip) {
            field[x].flip(sY, eY + 1);
          }
        }
      }
    }

    int counter = 0;
    for (int i = 0; i < size; i++) {
      counter += field[i].cardinality();
    }

    return counter;
  }

  private long solveB(List<String> instructions) {
    final int size = 1000;
    int[][] field = new int[size][size];
    Pattern p = Pattern.compile(".*?(\\d+),(\\d+) through (\\d+),(\\d+)");

    for (String instruction : instructions) {
      Matcher m = p.matcher(instruction);
      if (m.matches()) {
        int sX = Integer.parseInt(m.group(1));
        int sY = Integer.parseInt(m.group(2));
        int eX = Integer.parseInt(m.group(3));
        int eY = Integer.parseInt(m.group(4));

        boolean set = instruction.startsWith("turn on");
        boolean reset = instruction.startsWith("turn off");
        boolean flip = instruction.startsWith("toggle");

        for (int x = sX; x <= eX; x++) {
          for (int y = sY; y <= eY; y++) {
            if (set) {
              field[x][y]++;
            } else if (reset) {
              field[x][y]--;
              if (field[x][y] < 0) {
                field[x][y] = 0;
              }
            } else if (flip) {
              field[x][y] += 2;
            }
          }
        }
      }
    }

    long counter = 0;
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        counter += field[x][y];
      }
    }
    return counter;
  }

  @Test
  public void testDay06() {
    assertEquals(1000000, solveA(List.of("turn on 0,0 through 999,999")));
    assertEquals(1000, solveA(List.of("turn on 0,0 through 999,0")));
    assertEquals(1000000 - 4, solveA(List.of("turn on 0,0 through 999,999", "turn off 499,499 through 500,500")));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(377891, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines);
    assertEquals(14110788, result);
    System.out.println(result);
  }

}
