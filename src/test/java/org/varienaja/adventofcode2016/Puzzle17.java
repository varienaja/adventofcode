package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {
  private MessageDigest digester;
  private String input = "pxxbnzuo";

  private Map<String, Point> findNewStates(Map<String, Point> statesToCheck, Point target) {
    Map<String, Point> newStates = new HashMap<>();

    for (Entry<String, Point> e : statesToCheck.entrySet()) {
      if (target.equals(e.getValue())) {
        newStates.put(e.getKey(), e.getValue());
      } else {
        String md5 = getMD5(e.getKey());

        for (int i = 0; i < 4; i++) { // up, down, left, right
          if (md5.charAt(i) >= 'b' && md5.charAt(i) <= 'f') {
            String newInput;
            Point newPoint;
            if (i == 0) {
              newInput = e.getKey() + "U";
              newPoint = e.getValue().getNorth();
            } else if (i == 1) {
              newInput = e.getKey() + "D";
              newPoint = e.getValue().getSouth();
            } else if (i == 2) {
              newInput = e.getKey() + "L";
              newPoint = e.getValue().getWest();
            } else { // i==3
              newInput = e.getKey() + "R";
              newPoint = e.getValue().getEast();
            }
            if (0 <= newPoint.x && newPoint.x < 4 && 0 <= newPoint.y && newPoint.y < 4) {
              newStates.put(newInput, newPoint);
            }
          }
        }
      }
    }

    return newStates;
  }

  private String getMD5(String input) {
    byte[] digested = digester.digest(input.getBytes(StandardCharsets.UTF_8));
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < digested.length; i++) {
      String hex = Integer.toHexString(0xff & digested[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  private String solve(Map<String, Point> statesToCheck, Point target) {
    for (Entry<String, Point> e : statesToCheck.entrySet()) {
      if (target.equals(e.getValue())) {
        int ix = 0;
        for (char c : e.getKey().toCharArray()) {
          if (Character.isLowerCase(c)) {
            ix++;
          }
        }
        return e.getKey().substring(ix);
      }
    }

    return solve(findNewStates(statesToCheck, target), target);
  }

  private String solveA(String input) {
    return solve(Map.of(input, new Point(0, 0)), new Point(3, 3));
  }

  private long solveB(String input) {
    Point target = new Point(3, 3);

    Map<String, Point> states;
    Map<String, Point> nextStates = Map.of(input, new Point(0, 0));
    do {
      states = nextStates;
      nextStates = findNewStates(states, target); // don't calc next states when state is in target
    } while (!states.equals(nextStates));

    long maxDepth = 0;
    for (Entry<String, Point> e : nextStates.entrySet()) {
      if (target.equals(e.getValue())) {
        maxDepth = Math.max(maxDepth, e.getKey().length() - input.length());
      }
    }
    return maxDepth;
  }

  @Test
  public void testDay00() throws NoSuchAlgorithmException {
    digester = MessageDigest.getInstance("MD5");

    assertEquals("DDRRRD", solveA("ihgpwlah"));
    assertEquals("DDUDRLRRUDRD", solveA("kglvqrro"));
    assertEquals("DRURDRUDDLLDLUURRDULRLDUUDDDRR", solveA("ulqzkmiv"));

    announceResultA();
    String answer = solveA(input);
    System.out.println(answer);
    assertEquals("RDULRDDRRD", answer);

    assertEquals(370, solveB("ihgpwlah"));
    assertEquals(492, solveB("kglvqrro"));
    assertEquals(830, solveB("ulqzkmiv"));
    announceResultB();
    long ansB = solveB(input);
    System.out.println(ansB);
    assertEquals(752, ansB);
  }

}
