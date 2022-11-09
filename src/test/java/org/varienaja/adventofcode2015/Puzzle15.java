package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
public class Puzzle15 extends PuzzleAbs {
  private static final String CAPACITY = "capacity";
  private static final String DURABILITY = "durability";
  private static final String FLAVOR = "flavor";
  private static final String TEXTURE = "texture";
  private static final String CALORIES = "calories";

  private long solve(List<String> input, boolean partB) {
    Map<String, Map<String, Integer>> ingredient2Properties = new HashMap<>();
    Pattern p = Pattern.compile("(\\w+): " + CAPACITY + " (-?\\d+), " + DURABILITY + " (-?\\d+), " + //
        FLAVOR + " (-?\\d+), " + TEXTURE + " (-?\\d+), " + CALORIES + " (-?\\d+)");
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        Map<String, Integer> properties = new HashMap<>();
        properties.put(CAPACITY, Integer.parseInt(m.group(2)));
        properties.put(DURABILITY, Integer.parseInt(m.group(3)));
        properties.put(FLAVOR, Integer.parseInt(m.group(4)));
        properties.put(TEXTURE, Integer.parseInt(m.group(5)));
        properties.put(CALORIES, Integer.parseInt(m.group(6)));
        ingredient2Properties.put(m.group(1), properties);
      }
    }

    // Now I want ingredient2Properties.size() integers that add up to 100:
    int maxResult = -1;
    int[] teaSpoons = new int[ingredient2Properties.size()];
    int max = (int)Math.pow(100, teaSpoons.length);
    for (int i = 0; i < max; i++) {
      teaSpoons[0]++;
      if (i >= 100 && i % 100 == 0) {
        teaSpoons[0] = 0;
        teaSpoons[1]++;

        if (i >= 10000 && i % 10000 == 0) {
          teaSpoons[0] = 0;
          teaSpoons[1] = 0;
          teaSpoons[2]++;

          if (i >= 1000000 && i % 1000000 == 0) {
            teaSpoons[0] = 0;
            teaSpoons[1] = 0;
            teaSpoons[2] = 0;
            teaSpoons[3]++;
          }
        }
      }

      if (IntStream.of(teaSpoons).sum() == 100) {
        int result = 1;
        for (String property : new String[] {
            CAPACITY, DURABILITY, FLAVOR, TEXTURE
        }) {
          int r = 0;
          Iterator<Entry<String, Map<String, Integer>>> it = ingredient2Properties.entrySet().iterator();
          for (int j = 0; j < teaSpoons.length; j++) {
            Entry<String, Map<String, Integer>> e = it.next();
            r += teaSpoons[j] * e.getValue().get(property);
          }
          result *= Math.max(0, r);
        }

        if (partB) {
          int r = 0;
          Iterator<Entry<String, Map<String, Integer>>> it = ingredient2Properties.entrySet().iterator();
          for (int j = 0; j < teaSpoons.length; j++) {
            Entry<String, Map<String, Integer>> e = it.next();
            r += teaSpoons[j] * e.getValue().get(CALORIES);
          }
          if (r == 500) {
            maxResult = Math.max(maxResult, result);
          }
        } else {
          maxResult = Math.max(maxResult, result);
        }
      }
    }

    return maxResult;
  }

  private long solveA(List<String> input) {
    return solve(input, false);
  }

  private long solveB(List<String> input) {
    return solve(input, true);
  }

  @Test
  public void testDay10() {
    List<String> testInput = List.of( //
        "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8", //
        "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3");
    assertEquals(62842880, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(13882464, sum);

    announceResultB();
    assertEquals(57600000, solveB(testInput));
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(11171160, sum);
  }

}
