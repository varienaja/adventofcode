
package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {

  private long solveA(List<String> input, int burstCount) {
    int newInfections = 0;

    Set<Point> infected = new HashSet<>();
    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          infected.add(new Point(x, y));
        }
      }
    }
    int m = (input.size() / 2);
    Point virusCarrier = new Point(m, m);
    String face = "nesw";
    int direction = 0; // Index in face

    for (int i = 0; i < burstCount; i++) {
      if (infected.contains(virusCarrier)) {
        // Turn right
        direction++;
        if (direction >= face.length()) {
          direction = 0;
        }

        // Becomes cleaned
        infected.remove(virusCarrier);
      } else {
        // Turn left
        direction--;
        if (direction < 0) {
          direction = face.length() - 1;
        }

        // Becomes infected
        infected.add(new Point(virusCarrier.x, virusCarrier.y));
        newInfections++;
      }

      // Move carrier forward
      switch (face.charAt(direction)) {
        case 'n':
          virusCarrier = virusCarrier.getNorth();
          break;
        case 's':
          virusCarrier = virusCarrier.getSouth();
          break;
        case 'e':
          virusCarrier = virusCarrier.getEast();
          break;
        case 'w':
          virusCarrier = virusCarrier.getWest();
          break;
      }

      // for (int y = -10; y < 10; y++) {
      // for (int x = -10; x < 10; x++) {
      // System.out.print(infected.contains(new Point(x, y)) ? '#' : '.');
      // }
      // System.out.println();
      // }
      // System.out.println();
    }

    return newInfections;
  }

  private long solveB(List<String> input, int burstCount) {
    int newInfections = 0;

    Set<Point> infected = new HashSet<>();
    Set<Point> weakened = new HashSet<>();
    Set<Point> flagged = new HashSet<>();
    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          infected.add(new Point(x, y));
        }
      }
    }
    int m = (input.size() / 2);
    Point virusCarrier = new Point(m, m);
    String face = "nesw";
    int direction = 0; // Index in face

    for (int i = 0; i < burstCount; i++) {
      if (weakened.contains(virusCarrier)) { // Weakened becomes infected
        // No turn
        weakened.remove(virusCarrier);
        infected.add(new Point(virusCarrier.x, virusCarrier.y));
        newInfections++;
      } else if (flagged.contains(virusCarrier)) {// Flagged becomes clean
        // Reverse
        direction += 2;
        if (direction >= face.length()) {
          direction -= face.length();
        }

        flagged.remove(virusCarrier);
      } else if (infected.contains(virusCarrier)) {// Infected becomes flagged
        // Turn right
        direction++;
        if (direction >= face.length()) {
          direction = 0;
        }

        infected.remove(virusCarrier);
        flagged.add(new Point(virusCarrier.x, virusCarrier.y));
      } else { // Clean becomes weakened
        // Turn left
        direction--;
        if (direction < 0) {
          direction = face.length() - 1;
        }

        weakened.add(new Point(virusCarrier.x, virusCarrier.y));
      }

      // Move carrier forward
      switch (face.charAt(direction)) {
        case 'n':
          virusCarrier = virusCarrier.getNorth();
          break;
        case 's':
          virusCarrier = virusCarrier.getSouth();
          break;
        case 'e':
          virusCarrier = virusCarrier.getEast();
          break;
        case 'w':
          virusCarrier = virusCarrier.getWest();
          break;
      }
    }

    return newInfections;
  }

  @Test
  public void testDay22() {
    List<String> testInput = List.of( //
        "..#", //
        "#..", //
        "...");
    assertEquals(5, solveA(testInput, 7));
    assertEquals(41, solveA(testInput, 70));
    assertEquals(5587, solveA(testInput, 10000));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines, 10000);
    System.out.println(result);
    assertEquals(5348, result);

    assertEquals(26, solveB(testInput, 100));
    assertEquals(2511944, solveB(testInput, 10000000));
    announceResultB();
    long stepCount = solveB(lines, 10000000);
    System.out.println(stepCount);
    assertEquals(2512225, stepCount);
  }

}
