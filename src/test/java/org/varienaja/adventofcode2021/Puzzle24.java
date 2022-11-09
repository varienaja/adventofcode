package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {
  private int[] itup = new int[] {
      1, 2, 3, 4, 5, 6, 7, 8, 9
  };
  private int[] itdown = new int[] {
      9, 8, 7, 6, 5, 4, 3, 2, 1
  };

  private int len = 14;
  private int[] dxs = new int[len];
  private int[] dys = new int[len];
  private int[] dzs = new int[len];

  private long solve(List<String> lines, boolean smallestFirst) {
    for (int i = 0; i < len; i++) {
      String lineDIVZ = lines.get(18 * i + 4);
      int dZ = Integer.parseInt(lineDIVZ.split("\\s")[2]);
      String lineADDX = lines.get(18 * i + 5);
      int dX = Integer.parseInt(lineADDX.split("\\s")[2]);
      String lineADDY = lines.get(18 * i + 15);
      int dY = Integer.parseInt(lineADDY.split("\\s")[2]);

      dys[i] = dY;
      dxs[i] = dX;
      dzs[i] = dZ;
    }

    String result = solveStep(len - 1, Set.of(0), "", smallestFirst);
    return Long.parseLong(result);
  }

  private String solveStep(int step, Set<Integer> zOK, String suffix, boolean smallestFirst) {
    if (step < 0) {
      return suffix;
    }

    int zMin;
    int zMax;
    if (dzs[step] == 26) {
      zMin = zOK.iterator().next() * 26;
      zMax = zMin + 26;
    } else {
      zMin = zOK.iterator().next() / 26;
      zMax = zMin;
    }

    for (int w : smallestFirst ? itup : itdown) {
      Set<Integer> zCandidates = new HashSet<>();
      for (int zCandidate = zMin; zCandidate <= zMax; zCandidate++) {
        int x = (zCandidate % 26) + dxs[step];
        int z = zCandidate;
        z /= dzs[step];
        if (x != w) {
          z = 26 * z + w + dys[step];
        }
        if (zOK.contains(z)) {
          zCandidates.add(zCandidate);
        }
      }
      if (!zCandidates.isEmpty()) {
        String result = solveStep(step - 1, zCandidates, Integer.toString(w) + suffix, smallestFirst);
        if (result != null) {
          return result;
        }
      }
    }

    return null;
  }

  @Test
  public void testDay24() {
    announceResultA();
    List<String> lines = getInput();
    long result = solve(lines, false);
    System.out.println(result);
    assertEquals(94992992796199L, result);

    announceResultB();
    result = solve(lines, true);
    System.out.println(result);
    assertEquals(11931881141161L, result);
  }

}
