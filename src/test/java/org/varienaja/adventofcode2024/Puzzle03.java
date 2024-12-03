package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(179834255L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(80570939L, result);
  }

  @Test
  public void testA() {
    assertEquals(161, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(48, solveB(getStreamingTestInput()));
  }

  private long solveA(Stream<String> lines) {
    Pattern p = Pattern.compile("(mul\\((\\d{1,3}),(\\d{1,3})\\))");
    return lines.flatMapToLong(l -> p.matcher(l).results().mapToLong(m -> Long.parseLong(m.group(2)) * Long.parseLong(m.group(3)))).sum();
  }

  private long solveB(Stream<String> lines) {
    StringBuilder sb = new StringBuilder();
    lines.forEach(sb::append);

    // remove everything between don't() (inclusive) and do() (exclusive)
    int start = 0;
    while ((start = sb.indexOf("don't()", start)) >= 0) {
      int end = sb.indexOf("do()", start);
      sb.replace(start, end == -1 ? sb.length() : end, "");
    }

    return solveA(Stream.of(sb.toString()));
  }

}
