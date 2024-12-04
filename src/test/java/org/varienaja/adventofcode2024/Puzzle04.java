package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(2517, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(1960, result);
  }

  @Test
  public void testA() {
    assertEquals(18, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(9, solveB(getTestInput()));
  }

  private long solveA(List<String> lines) {
    char[] matcher = "XMAS".toCharArray();
    Map<Point, Character> w = parseWorld(lines);

    // For each 'X' go in all 8 directions for 4 steps, check how often it forms 'XMAS'
    return w.entrySet().stream() //
        .filter(e -> e.getValue().equals('X')) //
        .mapToLong(e -> {
          long xmasCount = 0;
          for (Point d : Point.ALLDIRECTIONS) {
            Point here = e.getKey();
            int ix = 1;
            while (ix < matcher.length) {
              here = here.add(d);
              if (w.getOrDefault(here, '.') == matcher[ix]) {
                if (++ix == matcher.length) {
                  xmasCount++;
                }
              } else {
                break;
              }
            }
          }
          return xmasCount;
        }).sum();
  }

  private long solveB(List<String> lines) {
    Map<Point, Character> w = parseWorld(lines);

    // For each 'A' check if both diagonals form SAM or MAS
    return w.entrySet().stream() //
        .filter(e -> e.getValue().equals('A')) //
        .filter(e -> {
          char c1 = w.getOrDefault(e.getKey().add(Point.dNorthWest), '.');
          char c2 = w.getOrDefault(e.getKey().add(Point.dSouthEast), '.');
          char c3 = w.getOrDefault(e.getKey().add(Point.dNorthEast), '.');
          char c4 = w.getOrDefault(e.getKey().add(Point.dSouthWest), '.');

          return (((c1 == 'M' && c2 == 'S') || (c1 == 'S' && c2 == 'M')) //
              && ((c3 == 'M' && c4 == 'S') || (c3 == 'S' && c4 == 'M')));
        }).count();
  }

}
