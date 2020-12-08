package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  private int solveA(List<String> lines) {
    int matches = 0;
    Pattern p = Pattern.compile("(\\d+)-(\\d+)\\s+(.):\\s+(.*)");
    for (String line : lines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int min = Integer.valueOf(m.group(1));
        int max = Integer.valueOf(m.group(2));
        char theChar = m.group(3).charAt(0);
        String pwd = m.group(4);

        int charCount = 0;
        for (char c : pwd.toCharArray()) {
          if (c == theChar) {
            charCount++;
          }
        }
        if (charCount >= min && charCount <= max) {
          matches++;
        }
      }
    }
    return matches;
  }

  private int solveB(List<String> lines) {
    int matches = 0;
    Pattern p = Pattern.compile("(\\d+)-(\\d+)\\s+(.):\\s+(.*)");
    for (String line : lines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int pos1 = Integer.valueOf(m.group(1)) - 1;
        int pos2 = Integer.valueOf(m.group(2)) - 1;
        char theChar = m.group(3).charAt(0);
        String pwd = m.group(4);

        if (pwd.charAt(pos1) == theChar ^ pwd.charAt(pos2) == theChar) {
          matches++;
        }
      }
    }
    return matches;
  }

  @Test
  public void testDay02() throws IOException, URISyntaxException {
    List<String> input = Arrays.asList("1-3 a: abcde", "1-3 b: cdefg", "2-9 c: ccccccccc");
    assertEquals(2, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    System.out.println(solveA(lines));

    assertEquals(1, solveB(input));
    announceResultB();
    System.out.println(solveB(lines));
  }

}
