package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {
  private final static String START = "start";

  private Collection<String> increaseDepth(Collection<String> paths, List<String[]> fromTos, boolean twiceOK) {
    Collection<String> newPaths = new HashSet<>();
    boolean added = false;

    for (String path : paths) {
      String lastCave = path.substring(path.lastIndexOf(',') + 1);
      if ("end".equals(lastCave)) {
        newPaths.add(path); // keep finished path
      } else {
        for (String[] fromTo : fromTos) {
          for (int i = 0; i < fromTo.length; i++) {
            if (fromTo[i].equals(lastCave)) {
              String to = fromTo[1 - i];
              if (!START.equals(to)) {

                if (Character.isUpperCase(to.charAt(0)) || !path.contains(to)) {
                  newPaths.add(path + "," + to);
                  added = true;
                } else {
                  if (twiceOK && START.equals(path.substring(1, path.indexOf(',', 1)))) {
                    // Mark path with ! to indicate one double visit to a lowercase cave
                    newPaths.add("!" + path + "," + to);
                    added = true;
                  }
                }

              }
            }
          }
        }
      }
    }

    if (added) {
      return increaseDepth(newPaths, fromTos, twiceOK);
    }
    return newPaths;
  }

  private long solveA(List<String> lines) {
    List<String[]> map = lines.stream().map(line -> line.split("-")).collect(Collectors.toList());
    return increaseDepth(Collections.singleton("," + START), map, false).size();
  }

  private long solveB(List<String> lines) {
    List<String[]> map = lines.stream().map(line -> line.split("-")).collect(Collectors.toList());
    return increaseDepth(Collections.singleton("," + START), map, true).size();
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
