package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Stack;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {
  private int garbageCounter;

  private long solveA(String input) {
    garbageCounter = 0;
    int garbageStart = -1;
    long result = 0;
    int braceDepth = 0;
    Stack<Character> s = new Stack<>();

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c == '!') {
        i += 1;
        if (garbageStart >= 0) {
          garbageStart += 2;
        }
        continue;
      } else if (c == '{') {
        if (s.isEmpty() || s.peek() != '>') {
          s.push('}');
          braceDepth++;

          result += braceDepth;
        }
      } else if (c == '<') {
        if (s.isEmpty() || s.peek() != '>') {
          s.push('>');
          garbageStart = i;
        }
      } else if (c == '>') {
        s.pop();
        int garbageLength = i - garbageStart - 1;
        garbageCounter += garbageLength;
        garbageStart = -1;
      } else if (c == '}') {
        if (s.peek() == '}') {
          braceDepth--;
          s.pop();
        }
      }
    }

    return result;
  }

  private long solveB(String input) {
    solveA(input);
    return garbageCounter;
  }

  @Test
  public void testDay08() {
    assertEquals(1, solveA("{}"));
    assertEquals(6, solveA("{{{}}}"));
    assertEquals(5, solveA("{{},{}}"));
    assertEquals(16, solveA("{{{},{},{{}}}}"));
    assertEquals(1, solveA("{<a>,<a>,<a>,<a>}"));
    assertEquals(9, solveA("{{<ab>},{<ab>},{<ab>},{<ab>}}"));
    assertEquals(9, solveA("{{<!!>},{<!!>},{<!!>},{<!!>}}"));
    assertEquals(3, solveA("{{<a!>},{<a!>},{<a!>},{<ab>}}"));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines.get(0));
    System.out.println(result);
    assertEquals(8337, result);

    assertEquals(0, solveB("<>"));
    assertEquals(17, solveB("<random characters>"));
    assertEquals(3, solveB("<<<<>"));
    assertEquals(2, solveB("<{!>}>"));
    assertEquals(0, solveB("<!!>"));
    assertEquals(0, solveB("<!!!>>"));
    assertEquals(10, solveB("<{o\"i!a,<{i<a>"));
    announceResultB();
    result = solveB(lines.get(0));
    System.out.println(result);
    assertEquals(4330, result);
  }

}
