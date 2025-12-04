package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1351L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(8345L, result);
  }

  @Test
  public void testA() {
    assertEquals(13L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(43L, solveB(getTestInput()));
  }

  private Set<Point> getAccessiblePaperRolls(Set<Point> coords) {
    return coords.stream() //
        .filter(p -> p.getAllNeighbours().stream() //
            .filter(coords::contains) //
            .count() < 4) // Check that each point has less than 4 neighbours
        .collect(Collectors.toSet());
  }

  private Set<Point> parseWorldToPaperRollCoordinates(List<String> lines) {
    Character at = '@';
    return parseWorld(lines).entrySet().stream() //
        .filter(e -> at.equals(e.getValue())) //
        .map(Entry::getKey) //
        .collect(Collectors.toSet());
  }

  private long solveA(List<String> lines) {
    return getAccessiblePaperRolls(parseWorldToPaperRollCoordinates(lines)).size();
  }

  private long solveB(List<String> lines) {
    Set<Point> coords = parseWorldToPaperRollCoordinates(lines);
    long paperRollsStart = coords.size();
    while (coords.removeAll(getAccessiblePaperRolls(coords))) {
    }
    return paperRollsStart - coords.size();
  }

}
