package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {

  private long solveA(List<String> input) {
    List<String[]> replacements = new LinkedList<>();

    for (int y = 0; y < input.size() - 2; y++) {
      String line = input.get(y);
      replacements.add(line.split(" => "));
    }
    String molecule = input.get(input.size() - 1);

    Set<String> molecules = new HashSet<>();
    for (String[] replacement : replacements) {
      int start = 0;
      do {
        start = molecule.indexOf(replacement[0], start);
        if (start != -1) {
          String generated = molecule.substring(0, start) + replacement[1] + molecule.substring(start + replacement[0].length());
          molecules.add(generated);

          start += replacement[0].length();
        }

      } while (start != -1);

    }

    return molecules.size();
  }

  private long solveB(List<String> input) {
    Map<String, String> replacements = new HashMap<>();

    for (int y = 0; y < input.size() - 2; y++) {
      String line = input.get(y);
      String[] parts = line.split(" => ");
      replacements.put(parts[1], parts[0]);
    }
    String molecule = input.get(input.size() - 1);

    // Replace longest match with the shorter inverse replacement until we reach e
    long steps = 0;
    while (!"e".equals(molecule)) {
      int ll = 0;
      Entry<String, String> longest = null;
      for (Entry<String, String> e : replacements.entrySet()) {
        if (molecule.indexOf(e.getKey()) >= 0) {
          if (e.getKey().length() > ll) {
            ll = e.getKey().length();
            longest = e;
          }
        }
      }

      molecule = molecule.replaceFirst(longest.getKey(), longest.getValue());
      steps++;
    }

    return steps;
  }

  @Test
  public void testDay00() {
    List<String> testInput = List.of(//
        "H => HO", //
        "H => OH", //
        "O => HH", //
        "", //
        "HOH");
    assertEquals(4, solveA(testInput));
    testInput = List.of(//
        "H => HO", //
        "H => OH", //
        "O => HH", //
        "", //
        "HOHOHO");
    assertEquals(7, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(518, sum);

    announceResultB();
    testInput = List.of(//
        "e => H", //
        "e => O", //
        "H => HO", //
        "H => OH", //
        "O => HH", //
        "", //
        "HOH");
    assertEquals(3, solveB(testInput));
    testInput = List.of(//
        "e => H", //
        "e => O", //
        "H => HO", //
        "H => OH", //
        "O => HH", //
        "", //
        "HOHOHO");
    assertEquals(6, solveB(testInput));

    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(200, sum);
  }

}
