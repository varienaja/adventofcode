package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    System.out.println(result);
    assertEquals(123908L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    System.out.println(result);
    assertEquals(10755693147L, result);
  }

  @Test
  public void testA() {
    assertEquals("ADVENT", decompress("ADVENT"));
    assertEquals("ABBBBBC", decompress("A(1x5)BC"));
    assertEquals("ABCBCDEFEFG", decompress("A(2x2)BCD(2x2)EFG"));
    assertEquals("(1x3)A", decompress("(6x1)(1x3)A"));
    assertEquals("X(3x3)ABC(3x3)ABCY", decompress("X(8x2)(3x3)ABCY"));
  }

  @Test
  public void testB() {
    assertEquals(20, decompressB(decompress("X(8x2)(3x3)ABCY")));
    assertEquals(241920, decompressB(decompress("(27x12)(20x12)(13x14)(7x10)(1x12)A")));
    assertEquals(445, decompressB(decompress("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN")));
  }

  private String decompress(String inCompressed) {
    StringBuilder sb = new StringBuilder();

    char[] arr = inCompressed.toCharArray();
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == '(') { // find x and l
        StringBuilder l = new StringBuilder();
        while (arr[++i] != 'x') {
          l.append(arr[i]);
        }
        int length = Integer.parseInt(l.toString());

        StringBuilder x = new StringBuilder();
        while (arr[++i] != ')') {
          x.append(arr[i]);
        }
        i++;
        int times = Integer.parseInt(x.toString());

        for (int j = 0; j < times; j++) {
          for (int k = 0; k < length; k++) {
            sb.append(arr[i + k]);
          }
        }
        i += length - 1;
      } else {
        sb.append(arr[i]);
      }
    }

    return sb.toString();
  }

  private long decompressB(String inUncompressed) {
    long result = 0;
    String rest = inUncompressed;
    while (!rest.isEmpty()) {
      int start = rest.indexOf('(');
      if (start == -1) {
        return result + rest.length();
      }

      result += start;

      StringBuilder l = new StringBuilder();
      while (rest.charAt(++start) != 'x') {
        l.append(rest.charAt(start));
      }
      int length = Integer.parseInt(l.toString());

      StringBuilder x = new StringBuilder();
      while (rest.charAt(++start) != ')') {
        x.append(rest.charAt(start));
      }
      int times = Integer.parseInt(x.toString());
      start++;

      long subLength = decompressB(rest.substring(start, start + length));
      for (int j = 0; j < times; j++) {
        result += subLength;
      }

      rest = rest.substring(start + length);
    }

    return result;
  }

  private long solveA(String input) {
    String decompressed = decompress(input);
    long counter = 0L;
    for (char c : decompressed.toCharArray()) {
      if (c != ' ') {
        counter++;
      }
    }
    return counter;
  }

  private long solveB(String input) {
    String decompressed = decompress(input);
    return decompressB(decompressed);
  }

}
