package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {

  private int solveA(List<String> input, int time) {
    Pattern p = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");
    List<Object[]> constraints = new LinkedList<>();
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        constraints.add(new Object[] {
            m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))
        });
      }
    }

    // Simulate
    int max = 0;
    for (Object[] c : constraints) {
      int t = time;
      int dist = 0;
      while (t > 0) {
        int deltaT = Math.min(t, (int)c[2]);
        t -= deltaT;
        dist += deltaT * (int)c[1];

        deltaT = Math.min(t, (int)c[3]);
        t -= deltaT;
      }
      max = Math.max(max, dist);
    }

    return max;
  }

  private int solveB(List<String> input, int time) {
    Pattern p = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");
    List<Object[]> constraints = new LinkedList<>();
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        constraints.add(new Object[] {
            m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))
        });
      }
    }

    int[][] simulation = new int[constraints.size()][time];
    // Simulate
    for (int i = 0; i < constraints.size(); i++) {
      Object[] c = constraints.get(i);
      int t = time;
      int dist = 0;
      int ix = 0;
      while (t > 0) {
        int deltaT = Math.min(t, (int)c[2]);
        t -= deltaT;

        for (int j = 0; j < deltaT; j++) {
          dist += (int)c[1];
          simulation[i][ix + j] = dist;
        }
        ix += deltaT;

        deltaT = Math.min(t, (int)c[3]);
        t -= deltaT;
        for (int j = 0; j < deltaT; j++) {
          simulation[i][ix + j] = dist;
        }
        ix += deltaT;
      }
    }

    // Score
    int[] scores = new int[constraints.size()];
    for (int j = 0; j < time; j++) {
      int max = 0;
      for (int i = 0; i < constraints.size(); i++) {
        if (simulation[i][j] > max) {
          max = simulation[i][j];
        }
      }

      for (int i = 0; i < constraints.size(); i++) {
        if (simulation[i][j] == max) {
          scores[i]++;
        }
      }
    }

    return IntStream.of(scores).max().orElse(-1);
  }

  @Test
  public void testDay10() {
    List<String> testInput = List.of( //
        "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.", //
        "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.");
    assertEquals(1120, solveA(testInput, 1000));

    announceResultA();
    List<String> lines = getInput();
    int sum = solveA(lines, 2503);
    System.out.println(sum);
    assertEquals(2655, sum);

    announceResultB();
    assertEquals(689, solveB(testInput, 1000));
    sum = solveB(lines, 2503);
    System.out.println(sum);
    assertEquals(1059, sum);
  }

}
