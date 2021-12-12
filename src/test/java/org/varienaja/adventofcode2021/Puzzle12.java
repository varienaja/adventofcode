package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  private Collection<String> increaseDepth(Collection<String> paths, List<String[]> map, boolean twiceOK) {
    Collection<String> newPaths = new TreeSet<>();
    boolean added = false;

    for (String path : paths) {
      int ix = path.lastIndexOf(',');
      String lastCave = path.substring(ix + 1);
      if ("end".equals(lastCave)) {
        newPaths.add(path); // keep finished path
      } else {
        for (String[] fromTo : map) {
          for (int i = 0; i < fromTo.length; i++) {
            if (fromTo[i].equals(lastCave)) {
              String to = fromTo[1 - i];
              if (!"start".equals(to)) {
                if (Character.isUpperCase(to.charAt(0)) || addOK(path, to, twiceOK)) {
                  String newPath = path + "," + to;
                  newPaths.add(newPath);
                  added = true;
                }
              }
            }
          }
        }
      }
    }

    if (added) {
      return increaseDepth(newPaths, map, twiceOK);
    }
    return newPaths;
  }

  private boolean addOK(String path, String toAdd, boolean twiceAllowed) {
    if (path.contains(toAdd)) {
      if (twiceAllowed) {
        Map<String, Integer> cave2Visits = new HashMap<>();
        for (String s : (path + "," + toAdd).split(",")) {
          if (!s.isEmpty() && Character.isLowerCase(s.charAt(0)) && !"start".equals(s) && !"end".equals(s)) {
            cave2Visits.compute(s, (k, v) -> v == null ? 1 : v + 1);
          }
        }

        long max = cave2Visits.values().stream().mapToInt(i -> i).max().orElse(0);
        if (max == 2) { // We already visited one or more caves twice
          return cave2Visits.values().stream().filter(i -> i == 2).count() <= 1;
        }
        return max <= 1;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  private long solveA(List<String> lines) {
    List<String[]> map = lines.stream().map(line -> line.split("-")).collect(Collectors.toList());
    return increaseDepth(Collections.singleton(",start"), map, false).size();
  }

  private long solveB(List<String> lines) {
    List<String[]> map = lines.stream().map(line -> line.split("-")).collect(Collectors.toList());
    return increaseDepth(Collections.singleton(",start"), map, true).size();
  }

  @Test
  public void testDay12() {
    List<String> testInput = Arrays.asList( //
        "start-A", //
        "start-b", //
        "A-c", //
        "A-b", //
        "b-d", //
        "A-end", //
        "b-end");
    assertEquals(10, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(5958, result);

    assertEquals(36, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(150426, result);
  }

}
