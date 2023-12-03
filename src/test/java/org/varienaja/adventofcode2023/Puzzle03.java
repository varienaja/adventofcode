package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.InfoPoint;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(527144L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(81463996L, result);
  }

  @Test
  public void testA() {
    assertEquals(4361, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(467835L, solveB(getTestInput()));
  }

  private List<String> getTestInput() {
    return List.of( //
        "467..114..", //
        "...*......", //
        "..35..633.", //
        "......#...", //
        "617*......", //
        ".....+.58.", //
        "..592.....", //
        "......755.", //
        "...$.*....", //
        ".664.598.." //
    );
  }

  private Stream<Entry<InfoPoint<Character>, Deque<Long>>> parse(List<String> lines) {
    Map<InfoPoint<Character>, Deque<Long>> symbol2Partnumbers = parseSymbols(lines);
    Pattern digits = Pattern.compile("\\d+");

    for (int y = 0; y < lines.size(); ++y) {
      Matcher m = digits.matcher(lines.get(y));
      while (m.find()) {
        String part = m.group();
        for (int ix = m.start() - 1; ix <= m.end(); ++ix) {
          for (int iy = y - 1; iy <= y + 1; ++iy) {
            Point tester = new Point(ix, iy);
            if (symbol2Partnumbers.containsKey(tester)) {
              symbol2Partnumbers.get(tester).add(Long.parseLong(part));
            }
          }
        }
      }
    }

    return symbol2Partnumbers.entrySet().stream();
  }

  private Map<InfoPoint<Character>, Deque<Long>> parseSymbols(List<String> lines) {
    Map<InfoPoint<Character>, Deque<Long>> symbol2Partnumbers = new HashMap<>();

    for (int y = 0; y < lines.size(); ++y) {
      int x = 0;
      for (char c : lines.get(y).toCharArray()) {
        if (c != '.' && !Character.isDigit(c)) {
          symbol2Partnumbers.put(new InfoPoint<>(x, y, c), new LinkedList<>());
        }
        ++x;
      }
    }

    return symbol2Partnumbers;
  }

  private long solveA(List<String> lines) {
    return parse(lines) //
        .flatMap(e -> e.getValue().stream()) //
        .mapToLong(Long::longValue) //
        .sum();
  }

  private long solveB(List<String> lines) {
    return parse(lines) //
        .filter(e -> e.getKey().getInfo() == '*') //
        .filter(e -> e.getValue().size() == 2) //
        .mapToLong(e -> e.getValue().pollFirst() * e.getValue().pollLast()) //
        .sum();
  }

}
