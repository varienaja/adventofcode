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
  private int pos;
  private boolean partA;

  private long parseExpr(String line, long lastT, char op) {
    long t = parseTerm(line);

    if (op == '+') {
      t += lastT;
    } else if (op == '*') {
      t *= lastT;
    }

    if (pos == line.length()) {
      return t;
    }

    if (line.charAt(pos) == ')') {
      pos++;
      return t;
    } else if (line.charAt(pos) == '+') {
      op = '+';
      pos++;
    } else if (line.charAt(pos) == '*') {
      pos++;
      if (partA) {
        op = '*';
      } else {
        return t * parseExpr(line, 0L, 'N');
      }
    }

    return parseExpr(line, t, op);
  }

  private long parseTerm(String line) {
    char c = line.charAt(pos++);
    if (Character.isDigit(c)) {
      return c - '0'; // char '0'-'9' to int 0-9
    } else if (c == '(') {
      return parseExpr(line, 0L, 'N');
    }
    return 0L;
  }

  private long solve(List<String> lines) {
    return lines.stream().map(this::solve).reduce((a, b) -> (a + b)).orElse(-1L);
  }

  private long solve(String line) {
    pos = 0;
    return parseExpr(line.replaceAll(" ", ""), 0L, 'N');
  }

  @Test
  public void testDay18() {
    partA = true;
    assertEquals(26, solve("2 * 3 + (4 * 5)"));
    assertEquals(437, solve("5 + (8 * 3 + 9 + 3 * 4 * 3)"));
    assertEquals(12240, solve("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));
    assertEquals(13632, solve("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
    assertEquals(809928672L, solve("(9 + 7 * 8 * 8 * (7 * 4 * 7 * 2 * 6 + 2) + 6) * (2 + (2 + 2 + 2 * 5 + 5 * 9) + 5 + 8 + 6)"));

    announceResultA();
    List<String> lines = getInput();
    long result = solve(lines);
    assertEquals(8298263963837L, result);
    System.out.println(result);

    partA = false;
    assertEquals(23340, solve("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
    assertEquals(51, solve("1 + (2 * 3) + (4 * (5 + 6))"));
    assertEquals(46, solve("2 * 3 + (4 * 5)"));
    assertEquals(1445, solve("5 + (8 * 3 + 9 + 3 * 4 * 3)"));
    assertEquals(669060, solve("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));

    announceResultB();
    result = solve(lines);
    assertEquals(145575710203332L, result);
    System.out.println(result);
  }

}
