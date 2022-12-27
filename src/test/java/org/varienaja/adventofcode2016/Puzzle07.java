package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(110L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(242L, result);
  }

  @Test
  public void testA() {
    assertTrue(supportsTLS("abba[mnop]qrst"));
    assertFalse(supportsTLS("abcd[bddb]xyyx"));
    assertFalse(supportsTLS("aaaa[qwer]tyui"));
    assertTrue(supportsTLS("ioxxoj[asdfgh]zxcvbn"));
    assertTrue(supportsTLS("vjqhodfzrrqjshbhx[lezezbbswydnjnz]ejcflwytgzvyigz[hjdilpgdyzfkloa]mxtkrysovvotkuyekba"));
  }

  @Test
  public void testB() {
    assertTrue(supportsSSL("aba[bab]xyz"));
    assertFalse(supportsSSL("xyx[xyx]xyx"));
    assertTrue(supportsSSL("aaa[kek]eke"));
    assertTrue(supportsSSL("zazbz[bzb]cdb"));
  }

  private boolean containsABBA(String inString) {
    for (int i = 0; i < inString.length() - 3; i++) {
      if (inString.charAt(i) == inString.charAt(i + 3) //
          && inString.charAt(i + 1) == inString.charAt(i + 2) //
          && inString.charAt(i) != inString.charAt(i + 1)) {
        return true;
      }
    }
    return false;
  }

  private long solveA(List<String> lines) {
    return lines.stream().filter(this::supportsTLS).count();
  }

  private long solveB(List<String> lines) {
    return lines.stream().filter(this::supportsSSL).count();
  }

  private boolean supportsSSL(String inString) {
    StringBuilder normal = new StringBuilder();
    StringBuilder hyper = new StringBuilder();
    boolean hyperNet = false;
    for (char c : inString.toCharArray()) {
      if (c == '[') {
        hyper.append("_");
        hyperNet = true;
        continue;
      }
      if (c == ']') {
        normal.append("_");
        hyperNet = false;
        continue;
      }
      if (hyperNet) {
        hyper.append(c);
      } else {
        normal.append(c);
      }
    }

    String normalS = normal.toString();
    String hyperS = hyper.toString();
    for (int i = 0; i < normalS.length() - 2; i++) {
      if (normalS.charAt(i) == normalS.charAt(i + 2) //
          && normalS.charAt(i) != normalS.charAt(i + 1)) {

        if (hyperS.contains("" + normalS.charAt(i + 1) + normalS.charAt(i) + normalS.charAt(i + 1))) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean supportsTLS(String inString) {
    StringBuilder normal = new StringBuilder();
    StringBuilder hyper = new StringBuilder();
    boolean hyperNet = false;
    for (char c : inString.toCharArray()) {
      if (c == '[') {
        hyper.append("_");
        hyperNet = true;
        continue;
      }
      if (c == ']') {
        normal.append("_");
        hyperNet = false;
        continue;
      }
      (hyperNet ? hyper : normal).append(c);
    }
    return containsABBA(normal.toString()) && !containsABBA(hyper.toString());
  }
}
