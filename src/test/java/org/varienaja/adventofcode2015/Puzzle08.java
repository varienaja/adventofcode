package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {

  private int solveA(List<String> lines) {
    int charCount = 0;
    int memCount = 0;
    for (String line : lines) {
      StringBuilder sb = new StringBuilder();
      charCount += line.length();
      String decoded = line.replaceAll("^\"", "").replaceAll("\"$", "");
      int i = 0;
      while (i < decoded.length()) {
        if (decoded.charAt(i) == '\\') {
          if (decoded.charAt(i + 1) == '"') {
            sb.append('"');
            i += 2;
            continue;
          } else if (decoded.charAt(i + 1) == '\\') {
            sb.append('\\');
            i += 2;
            continue;
          } else if (decoded.charAt(i + 1) == 'x') {
            try {
              String tester = decoded.substring(i + 2, i + 4);
              sb.append((char)Integer.parseInt(tester, 16));
              i += 4;
              continue;
            } catch (NumberFormatException e) { // No hex
            }
          }
        }
        sb.append(decoded.charAt(i));
        i++;
      }

      decoded = sb.toString();
      memCount += decoded.length();
      // System.out.println(line + " --> " + decoded);
    }

    return charCount - memCount;
  }

  private int solveB(List<String> lines) {
    int charCount = 0;
    int memCount = 0;
    for (String line : lines) {
      charCount += line.length();
      String encoded = "\"" + line.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
      // System.out.println(line + " --> " + encoded);
      memCount += encoded.length();
    }
    return memCount - charCount;
  }

  @Test
  public void testDay08() {
    assertEquals(2, solveA(List.of("\"\"")));
    assertEquals(2, solveA(List.of("\"abc\"")));
    assertEquals(3, solveA(List.of("\"aaa\\\"aaa\"")));
    assertEquals(5, solveA(List.of("\"\\x26\"")));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(1333, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines);
    assertEquals(2046, result);
    System.out.println(result);
  }

}
