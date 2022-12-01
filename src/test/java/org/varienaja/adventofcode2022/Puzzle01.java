package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle01 extends PuzzleAbs {
  private NavigableSet<Long> elfCalories = new TreeSet<>((l1, l2) -> Long.compare(l2, l1));

  private long solveA(List<String> lines) {
    elfCalories.clear();

    long elfCals = 0L;
    for (String l : lines) {
      if (l.isEmpty()) {
        elfCalories.add(elfCals);
        elfCals = 0L;
      } else {
        elfCals += Long.parseLong(l);
      }
    }

    return elfCalories.iterator().next();
  }

  private long solveB(List<String> lines) {
    solveA(lines);

    Iterator<Long> it = elfCalories.iterator();
    return it.next() + it.next() + it.next();
  }

  @Test
  public void testDay01() {
    List<String> testInput = List.of( //
        "1000", //
        "2000", //
        "3000", //
        "", //
        "4000", //
        "", //
        "5000", //
        "6000", //
        "", //
        "7000", //
        "8000", //
        "9000", //
        "", //
        "10000", //
        "" //
    );
    assertEquals(24000, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(66186L, result);
    System.out.println(result);

    assertEquals(45000, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    assertEquals(196804L, result);
    System.out.println(result);
  }

}
