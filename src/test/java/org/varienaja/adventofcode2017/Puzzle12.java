package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  private Set<Integer> getProgramElements(int program, Map<Integer, Set<Integer>> p2ps) {
    Set<Integer> reachable = new HashSet<>();
    Set<Integer> newReachable = Set.of(program);

    do {
      Set<Integer> toInspect = new HashSet<>();
      for (Integer r : newReachable) {
        if (reachable.add(r)) {
          toInspect.addAll(p2ps.get(r));
        }
      }
      newReachable = toInspect;
    } while (!newReachable.isEmpty());

    return reachable;
  }

  private Map<Integer, Set<Integer>> parse(List<String> input) {
    Map<Integer, Set<Integer>> p2ps = new HashMap<>();
    for (String line : input) {
      String[] parts = line.split(" <-> ");
      int key = Integer.parseInt(parts[0]);
      Set<Integer> value = new HashSet<>();
      for (String p : parts[1].split(",\\s+")) {
        value.add(Integer.parseInt(p));
      }
      p2ps.put(key, value);
    }
    return p2ps;
  }

  private long solveA(int program, List<String> input) {
    Map<Integer, Set<Integer>> p2ps = parse(input);

    return getProgramElements(0, p2ps).size();
  }

  private long solveB(List<String> input) {
    int groupCount = 0;
    Map<Integer, Set<Integer>> p2ps = parse(input);

    while (!p2ps.isEmpty()) {
      Set<Integer> groupElements = getProgramElements(p2ps.keySet().iterator().next(), p2ps);
      groupCount++;
      for (Integer i : groupElements) {
        p2ps.remove(i);
      }
    }

    return groupCount;
  }

  @Test
  public void testDay10() {
    List<String> testInput = List.of( //
        "0 <-> 2", //
        "1 <-> 1", //
        "2 <-> 0, 3, 4", //
        "3 <-> 2, 4", //
        "4 <-> 2, 3, 6", //
        "5 <-> 6", //
        "6 <-> 4, 5");
    assertEquals(6, solveA(0, testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(0, lines);
    System.out.println(result);
    assertEquals(288, result);

    assertEquals(2, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(211, result);
  }

}
