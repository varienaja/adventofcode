package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {
  private int skipSize;
  private int currentPosition;

  private String getLengths(String input) {
    StringBuilder sb = new StringBuilder();
    for (char c : input.toCharArray()) {
      int i = c;
      sb.append(i);
      sb.append(',');
    }
    sb.append("17,31,73,47,23");

    return sb.toString();
  }

  private void knotHash(int[] list, String lengths) {
    String[] inputs = lengths.split(",");
    for (String in : inputs) {
      // reverse l elements, beginning at ix
      int l = Integer.parseInt(in);

      for (int i = 0; i < l / 2; i++) {
        int ix1 = (currentPosition + i) % list.length;
        int ix2 = (currentPosition + l - 1 - i) % list.length;

        int t = list[ix1];
        list[ix1] = list[ix2];
        list[ix2] = t;
      }

      currentPosition = (currentPosition + l + skipSize) % list.length;
      skipSize++;
    }
  }

  private long solveA(int size, String input) {
    skipSize = 0;
    currentPosition = 0;

    int[] list = new int[size];
    for (int i = 0; i < size; i++) {
      list[i] = i;
    }

    knotHash(list, input);

    return list[0] * list[1];
  }

  protected String solveB(int size, String input) {
    skipSize = 0;
    currentPosition = 0;
    String lengths = getLengths(input);

    int[] list = new int[size];
    for (int i = 0; i < size; i++) {
      list[i] = i;
    }

    for (int i = 0; i < 64; i++) {
      knotHash(list, lengths);
    }

    int[] dense = new int[size / 16];
    for (int i = 0; i < dense.length; i++) {
      for (int j = 0; j < 16; j++) {
        dense[i] ^= list[16 * i + j];
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < dense.length; i++) {
      String hex = Integer.toString(dense[i], 16);
      if (hex.length() == 1) {
        sb.append('0');
      }
      sb.append(hex);
    }

    return sb.toString();
  }

  @Test
  public void testDay10() {
    assertEquals(12, solveA(5, "3,4,1,5"));
    announceResultA();
    List<String> lines = getInput();
    long result = solveA(256, lines.get(0));
    System.out.println(result);
    assertEquals(6909, result);

    assertEquals("49,44,50,44,51,17,31,73,47,23", getLengths("1,2,3"));
    assertEquals("a2582a3a0e66e6e86e3812dcb672a272", solveB(256, ""));
    assertEquals("33efeb34ea91902bb2f59c9920caa6cd", solveB(256, "AoC 2017"));
    assertEquals("3efbe78a8d82f29979031a4aa0b16a9d", solveB(256, "1,2,3"));
    assertEquals("63960835bcdc130f0b66d7ff4f6a5a8e", solveB(256, "1,2,4"));
    announceResultB();
    String resultB = solveB(256, lines.get(0));
    System.out.println(resultB);
    assertEquals("9d5f4561367d379cfbf04f8c471c0095", resultB);
  }

}
