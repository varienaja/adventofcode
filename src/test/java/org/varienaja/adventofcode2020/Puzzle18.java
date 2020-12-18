package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {
  char[] line;
  int pos;

  private long parseTerm() {
    long result = 0;
    char op = 'N';
    long nr = 0;
    while (pos < line.length) {
      if (op == '+' || op == '*') {
        result = nr;
        nr = 0L;
      }
      while (pos < line.length && Character.isDigit(line[pos])) {
        nr = nr * 10;
        nr += Integer.parseInt("" + line[pos++]);
      }
      if (pos < line.length && line[pos] == '(') {
        pos++;
        nr = parseTerm();
      }

      if (op == '+') {
        nr += result;
        op = 'N';
      } else if (op == '*') {
        nr *= result;
        op = 'N';
      }

      if (pos == line.length) {
        return nr;
      }

      if (line[pos] == ')') {
        pos++;
        return nr;
      } else if (line[pos] == '+') {
        op = '+';
        pos++;
      } else if (line[pos] == '*') {
        op = '*';
        pos++;
      }
    }
    return result;
  }

  private long parseTermB() {
    long result = 0;
    char op = 'N';
    long nr = 0;
    while (true) {
      if (op == '+' || op == '*') {
        result = nr;
        nr = 0L;
      }
      while (pos < line.length && Character.isDigit(line[pos])) {
        nr = nr * 10;
        nr += Integer.parseInt("" + line[pos++]);
      }
      if (pos < line.length && line[pos] == '(') {
        pos++;
        nr = parseTermB();
      }

      if (op == '+') {
        nr += result;
        op = 'N';
      } else if (op == '*') {
        nr *= result;
        op = 'N';
      }

      if (pos == line.length) {
        return nr;
      }

      if (line[pos] == ')') {
        pos++;
        return nr;
      } else if (line[pos] == '+') {
        op = '+';
        pos++;
      } else if (line[pos] == '*') {
        pos++;
        nr *= parseTermB();
        return nr;
      }
    }
  }

  private long solveA(List<String> lines) {
    long result = 0;
    for (String line : lines) {
      long s = solveA(line);
      // System.out.print(s);
      // System.out.println(": " + line);
      result += s;
    }

    return result;
  }

  private long solveA(String line) {
    this.line = line.replaceAll(" ", "").toCharArray();
    pos = 0;
    return parseTerm();
  }

  private long solveB(List<String> lines) {
    long result = 0;
    for (String line : lines) {
      long s = solveB(line);
      // System.out.print(s);
      // System.out.println(": " + line);
      result += s;
    }

    return result;
  }

  private long solveB(String line) {
    this.line = line.replaceAll(" ", "").toCharArray();
    pos = 0;
    return parseTermB();
  }

  @Test
  public void testDay18() {
    assertEquals(26, solveA("2 * 3 + (4 * 5)"));
    assertEquals(437, solveA("5 + (8 * 3 + 9 + 3 * 4 * 3)"));
    assertEquals(12240, solveA("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));
    assertEquals(13632, solveA("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
    assertEquals(809928672L, solveA("(9 + 7 * 8 * 8 * (7 * 4 * 7 * 2 * 6 + 2) + 6) * (2 + (2 + 2 + 2 * 5 + 5 * 9) + 5 + 8 + 6)"));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(8298263963837L, result);
    System.out.println(result);

    assertEquals(23340, solveB("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
    assertEquals(51, solveB("1 + (2 * 3) + (4 * (5 + 6))"));
    assertEquals(46, solveB("2 * 3 + (4 * 5)"));
    assertEquals(1445, solveB("5 + (8 * 3 + 9 + 3 * 4 * 3)"));
    assertEquals(669060, solveB("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));

    announceResultB();
    result = solveB(lines);
    assertEquals(145575710203332L, result);
    System.out.println(result);
  }

}
