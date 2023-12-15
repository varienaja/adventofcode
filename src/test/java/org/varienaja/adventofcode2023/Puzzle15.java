package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    System.out.println(result);
    assertEquals(506269L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    System.out.println(result);
    assertEquals(264021L, result);
  }

  @Test
  public void testA() {
    assertEquals(1320, solveA(getTestInputString()));
  }

  @Test
  public void testB() {
    assertEquals(145, solveB(getTestInputString()));
  }

  private int hash(String s) {
    return ('\0' + s).chars().reduce((a, b) -> (a + b) * 17 % 256).getAsInt(); // Muhaha, dirty one-liner
  }

  private long solveA(String line) {
    return Stream.of(line.split(",")).mapToInt(this::hash).sum();
  }

  private long solveB(String line) {
    Map<Integer, Map<String, Integer>> hash2box = new HashMap<>();

    for (String part : line.split(",")) {
      if (part.endsWith("-")) { // remove lens with given label from relevant box, moving any remaining lenses forward
        String label = part.substring(0, part.length() - 1);
        hash2box.computeIfPresent(hash(label), (hash, box) -> {
          box.remove(label);
          return box;
        });
      } else { // put lens into relevant box, replacing lenses with same label, otherwise put at end
        String[] pts = part.split("=");
        hash2box.computeIfAbsent(hash(pts[0]), h -> new LinkedHashMap<>()).put(pts[0], Integer.parseInt(pts[1]));
      }
    }

    long sum = 0;
    for (Entry<Integer, Map<String, Integer>> e : hash2box.entrySet()) {
      int ix = 1;
      for (Entry<String, Integer> lens : e.getValue().entrySet()) {
        sum += (1 + e.getKey()) * ix++ * lens.getValue();
      }
    }
    return sum;
  }

}
