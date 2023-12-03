package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    Map<InfoPoint<Character>, Deque<Long>> symbol2Partnumbers = new HashMap<>();

    int y = 0;
    for (String line : lines) {
      int x = 0;
      for (char c : line.toCharArray()) {
        if (c != '.' && !Character.isDigit(c)) {
          symbol2Partnumbers.put(new InfoPoint<>(x, y, c), new LinkedList<>());
        }
        ++x;
      }
      ++y;
    }

    y = 0;
    for (String line : lines) {
      StringBuilder sb = new StringBuilder();
      int x = 0;
      for (char c : (line + ".").toCharArray()) {
        if (Character.isDigit(c)) {
          sb.append(c);
        } else {
          if (!sb.isEmpty()) { // Scan around part number for symbols
            for (int ix = x - sb.length() - 1; ix <= x; ++ix) {
              for (int iy = y - 1; iy <= y + 1; ++iy) {
                Point tester = new Point(ix, iy);
                if (symbol2Partnumbers.containsKey(tester)) {
                  symbol2Partnumbers.get(tester).add(Long.parseLong(sb.toString()));
                }
              }
            }
            sb.setLength(0);
          }
        }
        ++x;
      }
      ++y;
    }

    return symbol2Partnumbers.entrySet().stream();
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
