package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
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

  private long countPaths(String path, List<String[]> fromTos, boolean twiceOK) {
    long result = 0;

    String lastCave = path.substring(path.lastIndexOf(',') + 1);
    if ("end".equals(lastCave)) {
      result++; // keep finished path
    } else {
      for (String[] fromTo : fromTos) {
        for (int i = 0; i < fromTo.length; i++) {
          if (fromTo[i].equals(lastCave)) {
            String to = fromTo[1 - i];
            if (!START.equals(to)) {

              if (Character.isUpperCase(to.charAt(0)) || !path.contains(to)) {
                result += countPaths(path + "," + to, fromTos, twiceOK);
              } else {
                if (twiceOK && START.equals(path.substring(1, path.indexOf(',', 1)))) {
                  // twiceOK is not OK anymore once we've used one lowercase cave twice
                  result += countPaths(path + "," + to, fromTos, false);
                }
              }

            }
          }
        }
      }
    }

    return result;
  }

  private long solveA(List<String> lines) {
    List<String[]> map = lines.stream().map(line -> line.split("-")).collect(Collectors.toList());
    return countPaths("," + START, map, false);
  }

  private long solveB(List<String> lines) {
    List<String[]> map = lines.stream().map(line -> line.split("-")).collect(Collectors.toList());
    return countPaths("," + START, map, true);
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
