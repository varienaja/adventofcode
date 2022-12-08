package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle02 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(6696, sum);

  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getInput());
    System.out.println(result);
    assertEquals("bvnfawcnyoeyudzrpgslimtkj", result);
  }

  private long solveA(List<String> input) {
    long twoCount = 0;
    long threeCount = 0;

    for (String s : input) {
      Map<Character, Long> char2count = s.chars().mapToObj(c -> (char)c).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

      for (Long l : new HashSet<>(char2count.values())) {
        if (l == 2L) {
          twoCount++;
        } else if (l == 3L) {
          threeCount++;
        }
      }
    }

    return twoCount * threeCount;
  }

  private String solveB(List<String> input) {
    for (int i = 0; i < input.size(); i++) {
      String line = input.get(i);
      char[] x = line.toCharArray();
      for (int j = i; j < input.size(); j++) {
        char[] y = input.get(j).toCharArray();

        int diffCount = 0;
        int diffPos = -1;
        for (int z = 0; z < x.length; z++) {
          if (x[z] != y[z]) {
            diffCount++;
            diffPos = z;
          }
        }

        if (diffCount == 1) {
          return line.substring(0, diffPos) + line.substring(diffPos + 1, line.length());
        }
      }
    }

    return "";
  }

  @Test
  public void testA() {
    List<String> testInput = List.of( //
        "abcdef", //
        "bababc", //
        "abbcde", //
        "abcccd", //
        "aabcdd", //
        "abcdee", //
        "ababab");
    assertEquals(12, solveA(testInput));
  }

  @Test
  public void testB() {
    List<String> testInput = List.of( //
        "abcde", //
        "fghij", //
        "klmno", //
        "pqrst", //
        "fguij", //
        "axcye", //
        "wvxyz");
    assertEquals("fgij", solveB(testInput));
  }

}
