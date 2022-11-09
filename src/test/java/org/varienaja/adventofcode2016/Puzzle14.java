package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {
  private String testInput = "abc";
  private String input = "jlmsuwbz";
  private MessageDigest digester;

  private Character containsEqualConsecutiveChars(String s, int cnt) {
    char lastChar = s.charAt(0);
    int runLength = 1;

    for (int i = 1; i < s.length(); i++) {
      char c = s.charAt(i);
      if (lastChar == c) {
        runLength++;

        if (runLength == cnt) {
          return lastChar;
        }
      } else {
        lastChar = c;
        runLength = 1;
      }

    }

    return null;
  }

  private String getMD5(String input, int ix, int repeat) {
    String tester = input + ix;

    for (int r = 0; r < repeat; r++) {
      byte[] digested = digester.digest((tester).getBytes(StandardCharsets.UTF_8));
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < digested.length; i++) {
        String hex = Integer.toHexString(0xff & digested[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      tester = hexString.toString();
    }

    return tester;
  }

  private long solve(String input, int repeat) {
    Map<Character, Set<Integer>> three2Index = new HashMap<>();
    Map<Character, Set<Integer>> five2Index = new HashMap<>();

    int ix = -1000;
    int keyNr = 0;
    while (keyNr != 64) {
      ix++;

      for (Entry<Character, Set<Integer>> e : five2Index.entrySet()) {
        e.getValue().remove(ix);
      }

      int ixx = ix + 1000;
      String md5 = getMD5(input, ixx, repeat);
      Character c = containsEqualConsecutiveChars(md5, 5);
      if (c != null) {
        five2Index.compute(c, (k, v) -> v == null ? new HashSet<>() : v).add(ixx);
      }

      if (ix > 0) {
        md5 = getMD5(input, ix, repeat);
        c = containsEqualConsecutiveChars(md5, 3);
        if (c != null) {
          three2Index.compute(c, (k, v) -> v == null ? new HashSet<>() : v).add(ix);
          Set<Integer> ns = five2Index.get(c);
          if (ns != null && !ns.isEmpty()) { // Found correct key
            System.out.print('.');
            keyNr++;
          }
        }
      }
    }

    return ix;
  }

  private long solveA(String input) {
    return solve(input, 1);
  }

  private long solveB(String input) {
    return solve(input, 2017);
  }

  @Test
  public void testDay14() throws NoSuchAlgorithmException {
    digester = MessageDigest.getInstance("MD5");

    assertEquals(Character.valueOf('a'), containsEqualConsecutiveChars("aaab", 3));
    assertEquals(Character.valueOf('8'), containsEqualConsecutiveChars("0034e0923cc38887a57bd7b1d4f953df", 3));
    assertEquals("a107ff634856bb300138cac6568c0f24", getMD5("abc", 0, 2017));

    assertEquals(22728, solveA(testInput));

    announceResultA();
    long sum = solveA(input);
    System.out.println(sum);
    assertEquals(35186, sum);

    assertEquals(22551, solveB(testInput));
    announceResultB();
    sum = solveB(input);
    System.out.println(sum);
    assertEquals(22429, sum);
  }

}
