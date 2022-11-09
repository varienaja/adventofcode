package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {

  private long solve(List<String> input, boolean partB) {
    Map<Integer, Map<String, Integer>> aunt2Properties = new HashMap<>();

    Pattern p = Pattern.compile("Sue (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)");
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        Map<String, Integer> properties = new HashMap<>();
        properties.put(m.group(2), Integer.parseInt(m.group(3)));
        properties.put(m.group(4), Integer.parseInt(m.group(5)));
        properties.put(m.group(6), Integer.parseInt(m.group(7)));
        aunt2Properties.put(Integer.parseInt(m.group(1)), properties);
      }
    }

    long result = -1;
    for (Entry<Integer, Map<String, Integer>> e : aunt2Properties.entrySet()) {
      Integer children = e.getValue().get("children");
      Integer cats = e.getValue().get("cats");
      Integer samoyeds = e.getValue().get("samoyeds");
      Integer pomeranians = e.getValue().get("pomeranians");
      Integer akitas = e.getValue().get("akitas");
      Integer vizslas = e.getValue().get("vizslas");
      Integer goldfish = e.getValue().get("goldfish");
      Integer trees = e.getValue().get("trees");
      Integer cars = e.getValue().get("cars");
      Integer perfumes = e.getValue().get("perfumes");

      if (partB) {
        if (children == null || children == 3) {
          if (cats == null || cats > 7) {
            if (samoyeds == null || samoyeds == 2) {
              if (pomeranians == null || pomeranians < 3) {
                if (akitas == null || akitas == 0) {
                  if (vizslas == null || vizslas == 0) {
                    if (goldfish == null || goldfish < 5) {
                      if (trees == null || trees > 3) {
                        if (cars == null || cars == 2) {
                          if (perfumes == null || perfumes == 1) {
                            result = e.getKey();
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } else {
        if (children == null || children == 3) {
          if (cats == null || cats == 7) {
            if (samoyeds == null || samoyeds == 2) {
              if (pomeranians == null || pomeranians == 3) {
                if (akitas == null || akitas == 0) {
                  if (vizslas == null || vizslas == 0) {
                    if (goldfish == null || goldfish == 5) {
                      if (trees == null || trees == 3) {
                        if (cars == null || cars == 2) {
                          if (perfumes == null || perfumes == 1) {
                            result = e.getKey();
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return result;
  }

  private long solveA(List<String> input) {
    return solve(input, false);
  }

  private long solveB(List<String> input) {
    return solve(input, true);
  }

  @Test
  public void testDay16() {
    List<String> testInput = List.of();
    assertEquals(-1, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(373, sum);

    announceResultB();
    assertEquals(-1, solveB(testInput));
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(260, sum);
  }

}
