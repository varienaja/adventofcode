package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(27502L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(31947L, result);
  }

  @Test
  public void testA() {
    assertEquals(405, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(400, solveB(getTestInput()));
  }

  long checkForSmudgedMirror(List<String> part) {
    long old = checkForMirror(part, -1); // Check previous mirror position

    // Brute force all possibilities until we find another mirror position
    for (int y = 0; y < part.size(); ++y) {
      for (int x = 0; x < part.get(y).length(); ++x) {
        List<String> partSmudgeRemoved = new LinkedList<>(part);
        StringBuilder sb = new StringBuilder(partSmudgeRemoved.get(y));
        sb.setCharAt(x, sb.charAt(x) == '.' ? '#' : '.');
        partSmudgeRemoved.set(y, sb.toString());

        long checkNew = checkForMirror(partSmudgeRemoved, old);
        if (checkNew != -1) {
          return checkNew;
        }
      }
    }

    throw new IllegalStateException("No mirror found after removing all possible smudges " + part);
  }

  private long checkForMirror(List<String> lines, long forbidden) {
    long result = checkVertical(lines, forbidden % 100);
    return result == -1 ? checkHorizontal(lines, forbidden - forbidden % 100) : result;
  }

  private long checkHorizontal(List<String> lines, long forbidden) {
    return IntStream.range(1, lines.size()) //
        .filter(h -> IntStream.range(0, Math.min(h, lines.size() - h)) // Check if 'h'...
            .allMatch(i -> lines.get(h - 1 - i).equals(lines.get(h + i)))) // ...is valid mirror position
        .map(m -> m * 100) // horizontal mirrors * 100
        .filter(m -> m != forbidden) // don't match any previous mirror position
        .findFirst() // return it...
        .orElse(-1); // ...or mirror position found
  }

  private long checkVertical(List<String> lines, long forbidden) {
    List<String> rotated = new LinkedList<>();

    for (int x = 0; x < lines.get(0).length(); ++x) {
      StringBuilder sb = new StringBuilder();
      for (int y = 0; y < lines.size(); ++y) {
        sb.append(lines.get(y).charAt(x));
      }
      rotated.add(sb.toString());
    }

    long result = checkHorizontal(rotated, forbidden == -1 ? -1 : forbidden * 100);
    return result == -1 ? -1 : result / 100;
  }

  private long solve(List<String> lines, boolean partB) {
    long sum = 0;

    List<String> part = new LinkedList<>();
    for (String line : lines) {
      if (line.isEmpty()) {
        sum += partB ? checkForSmudgedMirror(part) : checkForMirror(part, -1);
        part.clear();
      } else {
        part.add(line);
      }
    }

    return sum;
  }

  private long solveA(List<String> lines) {
    return solve(lines, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, true);
  }

}
