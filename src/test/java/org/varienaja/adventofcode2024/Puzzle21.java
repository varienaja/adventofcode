package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024. Got to part 1 all right, but had trouble getting the correct lengths for levels 3
 * and up. After pulling many hairs, I just translated Maks' Python solution into Java and it worked.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 * @see https://github.com/maksverver/AdventOfCode/blob/master/2024/21.py
 */
public class Puzzle21 extends PuzzleAbs {
  record PathKey(int keypadId, char src, char dst) {
  }

  record LengthKey(String s, int level, int robotCount) {
  }

  static String[][] KEYPADS;

  static {
    KEYPADS = new String[][] {
        {
            "789", //
            "456", //
            "123", //
            " 0A"
        }, {
            " ^A", //
            "<v>"
        }
    };
  }

  private Map<String, Long> s2c = new HashMap<>();
  private Map<PathKey, Set<String>> combo2path = new HashMap<>();;
  private Map<LengthKey, Long> l2length = new HashMap<>();

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInput(), 2);
    System.out.println(result);
    assertEquals(212488, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInput(), 25);
    System.out.println(result);
    assertEquals(258263972600402L, result);
  }

  @Test
  public void testA() {
    assertEquals(Set.of("<"), getPossiblePaths(0, 'A', '0'));
    assertEquals(Set.of("^"), getPossiblePaths(0, '0', '2'));
    assertEquals(Set.of(">^^", "^^>"), getPossiblePaths(0, '2', '9'));
    assertEquals(Set.of("vvv"), getPossiblePaths(0, '9', 'A'));

    assertEquals(Set.of("v<<"), getPossiblePaths(1, 'A', '<'));
    assertEquals(Set.of(">^"), getPossiblePaths(1, '<', '^'));

    assertEquals(12, length("029A", 0, 0));
    assertEquals(28, length("029A", 0, 1));
    assertEquals(68, length("029A", 0, 2));
    assertEquals(164, length("029A", 0, 3));

    assertEquals(126384, solve(getTestInput(), 2));
  }

  private Set<String> getPossiblePaths(int keypadId, char src, char dst) {
    PathKey pk = new PathKey(keypadId, src, dst);
    Set<String> result = combo2path.get(pk);
    if (result != null) {
      return result;
    }

    int xSrc = -1;
    int ySrc = -1;
    int xDst = -1;
    int yDst = -1;

    result = new HashSet<>();
    for (int y = 0; y < KEYPADS[keypadId].length; ++y) {
      for (int x = 0; x < KEYPADS[keypadId][y].length(); ++x) {
        char c = KEYPADS[keypadId][y].charAt(x);
        if (c == src) {
          xSrc = x;
          ySrc = y;
        }
        if (c == dst) {
          xDst = x;
          yDst = y;
        }
      }
    }

    int dx = xDst - xSrc;
    int dy = yDst - ySrc;

    String movementX = (Integer.signum(dx) > 0 ? ">" : "<").repeat(Math.abs(dx));
    String movementY = (Integer.signum(dy) > 0 ? "v" : "^").repeat(Math.abs(dy));

    result = new HashSet<>();
    if (KEYPADS[keypadId][yDst].charAt(xSrc) != ' ') {
      result.add(movementY + movementX); // First y then x, but only if path does not cross the gap
    }
    if (KEYPADS[keypadId][ySrc].charAt(xDst) != ' ') {
      result.add(movementX + movementY); // First x then y, but only if path does not cross the gap
    }
    combo2path.put(pk, result);
    return result;
  }

  private long length(String code, int level, int robotCount) {
    if (level > robotCount) {
      s2c.compute(code, (k, v) -> v == null ? 1 : v + 1);
      return code.length();
    }

    LengthKey lk = new LengthKey(code, level, robotCount);
    if (l2length.containsKey(lk)) {
      return l2length.get(lk);
    }

    int keypadId = level == 0 ? 0 : 1;
    long result = 0;
    char lastTyped = 'A';
    for (char c : code.toCharArray()) {
      Set<String> possiblePaths = getPossiblePaths(keypadId, lastTyped, c);
      long minLength = possiblePaths.stream().map(p -> p + "A").mapToLong(x -> length(x, level + 1, robotCount)).min().orElseThrow();
      result += minLength;
      lastTyped = c;
    }

    l2length.put(lk, result);
    return result;
  }

  private long solve(List<String> input, int robotCount) {
    long result = 0;
    for (String code : input) {
      result += Long.parseLong(extractDigits(code)) * length(code, 0, robotCount);
    }
    return result;
  }

}
