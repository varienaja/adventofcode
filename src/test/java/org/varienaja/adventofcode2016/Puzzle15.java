package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {

  private long solveA(List<String> input) {
    List<int[]> discs = new LinkedList<>();
    Pattern p = Pattern.compile("Disc #\\d has (\\d+) positions; at time=0, it is at position (\\d+).");
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        discs.add(new int[] {
            Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))
        });
      }
    }

    int t = 0;
    boolean success = false;
    while (!success) {
      int depth = 0;
      int deltaT = t + 1;
      for (int[] disc2 : discs) {
        int pos = (disc2[1] + deltaT) % disc2[0];
        if (pos == 0) {
          depth++;
        } else { // Don't bother calculating if no success
          break;
        }
        deltaT++;
      }

      if (depth == discs.size()) {
        return t;
      }
      t++;
    }
    return -1;
  }

  private long solveB(List<String> input) {
    input.add("Disc #7 has 11 positions; at time=0, it is at position 0.");
    return solveA(input);
  }

  @Test
  public void testDay15() {
    List<String> testInput = List.of( //
        "Disc #1 has 5 positions; at time=0, it is at position 4.", //
        "Disc #2 has 2 positions; at time=0, it is at position 1.");
    assertEquals(5, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(400589, sum);

    announceResultB();
    lines = new LinkedList<>(lines);
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(3045959, sum);
  }

}
