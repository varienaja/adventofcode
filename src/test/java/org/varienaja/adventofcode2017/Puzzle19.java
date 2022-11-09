
package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {
  private long steps;

  private String solveA(List<String> input) {
    steps = 0;
    boolean next = true;
    int r = 0;
    int c = input.get(r).indexOf('|');
    char direction = 's';
    StringBuilder result = new StringBuilder();

    while (next) {
      steps++;
      if (direction == 's') {
        r++;
      } else if (direction == 'n') {
        r--;
      } else if (direction == 'w') {
        c--;
      } else if (direction == 'e') {
        c++;
      }

      char cc = input.get(r).charAt(c);
      if (Character.isAlphabetic(cc)) {
        result.append(cc);
      } else if ('+' == cc) { // find new direction
        Map<Character, Character> dir2Char = new HashMap<>();
        dir2Char.put('n', input.get(r - 1).charAt(c));
        dir2Char.put('s', input.get(r + 1).charAt(c));
        dir2Char.put('e', input.get(r).charAt(c + 1));
        dir2Char.put('w', input.get(r).charAt(c - 1));

        if (direction == 's') {
          dir2Char.remove('n');
        } else if (direction == 'n') {
          dir2Char.remove('s');
        } else if (direction == 'w') {
          dir2Char.remove('e');
        } else if (direction == 'e') {
          dir2Char.remove('w');
        }

        for (Entry<Character, Character> e : dir2Char.entrySet()) {
          if (!e.getValue().equals(' ')) {
            direction = e.getKey();
          }
        }
      } else if (' ' == cc) {
        next = false;
      }

    }

    return result.toString();
  }

  private long solveB(List<String> input) {
    solveA(input);
    return steps;
  }

  @Test
  public void testDay19() {
    List<String> testInput = List.of( //
        "        |          ", //
        "        |  +--+    ", //
        "        A  |  C    ", //
        "    F---|----E|--+ ", //
        "        |  |  |  D ", //
        "        +B-+  +--+ ", //
        "                   ");
    assertEquals("ABCDEF", solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    String result = solveA(lines);
    System.out.println(result);
    assertEquals("LOHMDQATP", result);

    assertEquals(38, solveB(testInput));
    announceResultB();
    long stepCount = solveB(lines);
    System.out.println(stepCount);
    assertEquals(16492, stepCount);
  }

}
