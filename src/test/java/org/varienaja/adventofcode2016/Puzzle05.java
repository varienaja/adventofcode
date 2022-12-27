package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    String result = solveA(getInputString());
    System.out.println(result);
    assertEquals("801b56a7", result);
  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getInputString());
    System.out.println(result);
    assertEquals("424a0197", result);
  }

  @Test
  public void testA() {
    assertEquals("18f47a30", solveA("abc"));
  }

  @Test
  public void testB() {
    assertEquals("05ace8e3", solveB("abc"));
  }

  @Override
  protected String getInputString() {
    return "abbhdwsy";
  }

  private String solveA(String input) {
    int i = 0;
    StringBuilder sb = new StringBuilder();

    while (sb.length() < 8) {
      try {
        String md5;
        MessageDigest digester = MessageDigest.getInstance("MD5");
        do {
          String tester = input + i++;
          byte[] digested = digester.digest(tester.getBytes(Charset.forName("UTF-8")));
          StringBuffer hexString = new StringBuffer();
          for (int j = 0; j < digested.length; j++) {
            String hex = Integer.toHexString(0xff & digested[j]);
            if (hex.length() == 1) {
              hexString.append('0');
            }
            hexString.append(hex);
          }

          md5 = hexString.toString();
        } while (!md5.startsWith("00000"));
        sb.append(md5.charAt(5));
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    }

    return sb.toString();
  }

  private String solveB(String input) {
    int i = 0;
    Set<Integer> usedIndices = new HashSet<>();
    char[] pwd = new char[8];
    Arrays.fill(pwd, ' ');

    while (usedIndices.size() < 8) {
      try {
        String md5;
        MessageDigest digester = MessageDigest.getInstance("MD5");
        do {
          String tester = input + i++;
          byte[] digested = digester.digest(tester.getBytes(Charset.forName("UTF-8")));
          StringBuffer hexString = new StringBuffer();
          for (int j = 0; j < digested.length; j++) {
            String hex = Integer.toHexString(0xff & digested[j]);
            if (hex.length() == 1) {
              hexString.append('0');
            }
            hexString.append(hex);
          }

          md5 = hexString.toString();
        } while (!md5.startsWith("00000"));

        if (Character.isDigit(md5.charAt(5))) {
          int index = Integer.parseInt("" + md5.charAt(5));
          if (index >= 0 && index < pwd.length && pwd[index] == ' ') {
            usedIndices.add(index);
            pwd[index] = md5.charAt(6);
            // System.out.println(Arrays.toString(pwd));
          }
        }
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    }

    return new String(pwd);
  }

}
