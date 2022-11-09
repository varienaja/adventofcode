package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  private long solveA(String line) {
    long result = -1;
    try {
      String md5;
      MessageDigest digester = MessageDigest.getInstance("MD5");
      do {
        String key = line + ++result;
        byte[] digested = digester.digest(key.getBytes(Charset.forName("UTF-8")));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digested.length; i++) {
          String hex = Integer.toHexString(0xff & digested[i]);
          if (hex.length() == 1) {
            hexString.append('0');
          }
          hexString.append(hex);
        }

        md5 = hexString.toString();
      } while (!md5.startsWith("00000"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return result;
  }

  private long solveB(String line) {
    int result = -1;
    try {
      String md5;
      MessageDigest digester = MessageDigest.getInstance("MD5");
      do {
        String key = line + ++result;
        byte[] digested = digester.digest(key.getBytes(Charset.forName("UTF-8")));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digested.length; i++) {
          String hex = Integer.toHexString(0xff & digested[i]);
          if (hex.length() == 1) {
            hexString.append('0');
          }
          hexString.append(hex);
        }

        md5 = hexString.toString();
      } while (!md5.startsWith("000000"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return result;
  }

  @Test
  public void testDay04() {
    assertEquals(609043, solveA("abcdef"));
    assertEquals(1048970, solveA("pqrstuv"));

    announceResultA();
    String input = "yzbqklnj";
    long result = solveA(input);
    assertEquals(282749, result);
    System.out.println(result);

    announceResultB();
    result = solveB(input);
    assertEquals(9962624, result);
    System.out.println(result);
  }

}
