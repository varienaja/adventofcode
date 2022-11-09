package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  private String solveA(String inString) {
    // iterate over chars; count them; if they change: output count + the char
    StringBuilder sb = new StringBuilder();
    char current = 0;
    int counter = 0;
    char[] arr = inString.toCharArray();

    for (int i = 0; i < arr.length; i++) {
      if (current != arr[i] || i == arr.length) {
        if (counter > 0) {
          sb.append(Integer.toString(counter));
          sb.append(current);
        }
        counter = 1;
        current = arr[i];
      } else {
        counter++;
      }
    }
    sb.append(Integer.toString(counter));
    sb.append(current);

    return sb.toString();
  }

  @Test
  public void testDay10() {
    assertEquals("11", solveA("1"));
    assertEquals("21", solveA("11"));
    assertEquals("1211", solveA("21"));
    assertEquals("111221", solveA("1211"));
    assertEquals("312211", solveA("111221"));

    announceResultA();
    String line = "1113222113";
    for (int i = 1; i <= 40; i++) {
      line = solveA(line);
    }
    long result = line.length();
    assertEquals(252594, result);
    System.out.println(result);

    announceResultB();
    line = "1113222113";
    for (int i = 1; i <= 50; i++) {
      line = solveA(line);
    }
    result = line.length();
    assertEquals(3579328, result);
    System.out.println(result);
  }

}
