package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    String result = solveA(getInput());
    System.out.println(result);
    assertEquals("gyvwpxaz", result);
  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getInput());
    System.out.println(result);
    assertEquals("jucfoary", result);
  }

  @Test
  public void testA() {
    assertEquals("easter", solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals("advent", solveB(getTestInput()));
  }

  private List<String> getTestInput() {
    return List.of( //
        "eedadn", //
        "drvtee", //
        "eandsr", //
        "raavrd", //
        "atevrs", //
        "tsrnev", //
        "sdttsa", //
        "rasrtv", //
        "nssdts", //
        "ntnada", //
        "svetve", //
        "tesnvt", //
        "vntsnd", //
        "vrdear", //
        "dvrsen", //
        "enarar");
  }

  private String solve(List<String> lines, int m) {
    StringBuilder sb = new StringBuilder();
    int mx = lines.get(0).length();
    int i = 0;
    while (i < mx) {
      Map<Character, Integer> frequency = new HashMap<>();
      for (String line : lines) {
        char c = line.charAt(i);
        Integer count = frequency.getOrDefault(c, 0);
        frequency.put(c, ++count);
      }
      List<Character> res = frequency.entrySet().stream().sorted((e1, e2) -> m * e2.getValue().compareTo(e1.getValue())).map(e -> e.getKey()).limit(1)
          .collect(Collectors.toList());
      sb.append(res.get(0));
      i++;
    }
    return sb.toString();

  }

  private String solveA(List<String> lines) {
    return solve(lines, 1);
  }

  private String solveB(List<String> lines) {
    return solve(lines, -1);
  }

}
