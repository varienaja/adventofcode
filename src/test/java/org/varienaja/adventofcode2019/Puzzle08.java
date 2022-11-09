package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString(), 25, 6);
    System.out.println(sum);
    assertEquals(1064, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    solveB(getInputString(), 25, 6);
  }

  private long solveA(String input, int width, int height) {
    Set<Map<Integer, Integer>> layers = new HashSet<>();

    Map<Integer, Integer> digit2count;
    int i = 0;
    while (i < input.length()) {
      if (i % (width * height) == 0) {
        digit2count = new HashMap<>();
        for (int ii = 0; ii < width * height; ii++) {
          int digit = Integer.parseInt("" + input.charAt(i + ii));
          digit2count.compute(digit, (k, v) -> v == null ? 1 : v + 1);
        }
        layers.add(digit2count);
        i += width * height;
      }
    }

    Map<Integer, Integer> leastZeroes = layers.stream().min(Comparator.comparing(l -> l.getOrDefault(0, 0))).orElse(Map.of());
    return leastZeroes.get(1) * leastZeroes.get(2);
  }

  private void solveB(String input, int width, int height) {
    int size = width * height;
    int layerCount = input.length() / (size);

    System.out.println();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        char p;
        for (int l = 0; l < layerCount; l++) {
          p = input.charAt(y * width + x + l * size);
          if (p == '0') {
            System.out.print(' ');
            break;
          } else if (p == '1') {
            System.out.print('#');
            break;
          }
        }
      }
      System.out.println();
    }
  }

  @Test
  public void testA() {
    assertEquals(1, solveA("123456789012", 3, 2));
  }

}
