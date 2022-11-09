package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {

  private boolean solveA(String inPassword) {
    if (inPassword.indexOf('i') >= 0) {
      return false;
    }
    if (inPassword.indexOf('o') >= 0) {
      return false;
    }
    if (inPassword.indexOf('l') >= 0) {
      return false;
    }

    boolean containsStraight = false;
    for (int i = 0; i < inPassword.length() - 2; i++) {
      if (inPassword.charAt(i + 2) - inPassword.charAt(i + 1) == 1 //
          && inPassword.charAt(i + 1) - inPassword.charAt(i) == 1) {
        containsStraight = true;
        break;
      }
    }
    if (!containsStraight) {
      return false;
    }

    int pairCount = 0;
    int i = 0;
    while (i < inPassword.length() - 1) {
      if (inPassword.charAt(i + 1) == inPassword.charAt(i)) {
        pairCount++;
        i += 2;
      } else {
        i++;
      }
    }

    return pairCount > 1;
  }

  private String solveDay11aInc(String inString) {
    StringBuilder increased = new StringBuilder();
    for (int i = inString.length() - 1; i >= 0; i--) {
      char c = (char)(inString.charAt(i) + 1);
      if (c > 'z') {
        c = 'a';
        increased.insert(0, c);
      } else {
        increased.insert(0, c);
        increased.insert(0, inString.substring(0, i));
        return increased.toString();
      }
    }

    return increased.toString();
  }

  @Test
  public void testDay10() {
    assertFalse(solveA("hijklmmn"));
    assertFalse(solveA("abbceffg"));
    assertFalse(solveA("abbcegjk"));
    assertFalse(solveA("abcdefgh"));
    assertTrue(solveA("abcdffaa"));
    assertFalse(solveA("ghijklmn"));
    assertTrue(solveA("ghjaabcc"));

    announceResultA();
    String line = "hxbxwxba";
    while (!solveA(line)) {
      line = solveDay11aInc(line);
    }
    System.out.println(line);
    assertEquals("hxbxxyzz", line);

    announceResultB();
    line = solveDay11aInc(line);
    while (!solveA(line)) {
      line = solveDay11aInc(line);
    }
    System.out.println(line);
    assertEquals("hxcaabcc", line);
  }

}
