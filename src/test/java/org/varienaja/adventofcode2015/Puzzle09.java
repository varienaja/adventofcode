package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
public class Puzzle09 extends PuzzleAbs {
  private int solveA(List<String> lines) {
    // find places; try all orderings, output minimum traveling distance
    Pattern desc = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");
    Set<String> p = new HashSet<>();
    Map<String, Integer> distances = new HashMap<>();
    for (String line : lines) {
      Matcher m = desc.matcher(line);
      if (m.matches()) {
        p.add(m.group(1));
        p.add(m.group(2));

        distances.put(m.group(1) + "_" + m.group(2), Integer.parseInt(m.group(3)));
        distances.put(m.group(2) + "_" + m.group(1), Integer.parseInt(m.group(3)));
      }
    }

    Map<String, Integer> route2distance = new HashMap<>();
    solveDay9aOrderings(p, distances, route2distance, new LinkedList<String>());

    int result = Integer.MAX_VALUE;
    for (Entry<String, Integer> entry : route2distance.entrySet()) {
      if (entry.getValue() < result) {
        result = entry.getValue();
        // System.out.println(entry.getKey());
      }
    }

    return result;
  }

  private int solveB(List<String> lines) {
    // find places; try all orderings, output maximum traveling distance
    Pattern desc = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");
    Set<String> p = new HashSet<>();
    Map<String, Integer> distances = new HashMap<>();
    for (String line : lines) {
      Matcher m = desc.matcher(line);
      if (m.matches()) {
        p.add(m.group(1));
        p.add(m.group(2));

        distances.put(m.group(1) + "_" + m.group(2), Integer.parseInt(m.group(3)));
        distances.put(m.group(2) + "_" + m.group(1), Integer.parseInt(m.group(3)));
      }
    }

    Map<String, Integer> route2distance = new HashMap<>();
    solveDay9aOrderings(p, distances, route2distance, new LinkedList<String>());

    int result = Integer.MIN_VALUE;
    for (Entry<String, Integer> entry : route2distance.entrySet()) {
      if (entry.getValue() > result) {
        result = entry.getValue();
        // System.out.println(entry.getKey());
      }
    }

    return result;
  }

  private void solveDay9aOrderings(Set<String> places, Map<String, Integer> distances, Map<String, Integer> route2distance, List<String> route) {
    if (places.isEmpty()) {
      // System.out.print(route);
      int cost = 0;
      String lastPlace = null;
      for (String p : route) {
        if (lastPlace == null) {
          lastPlace = p;
        } else {
          cost += distances.get(lastPlace + "_" + p);
          lastPlace = p;
        }
      }
      // System.out.print(" ");
      // System.out.println(cost);
      route2distance.put(route.toString(), cost);
    }

    for (String p : places) {
      route.add(p);
      Set<String> pcs = new HashSet<>(places);
      pcs.remove(p);
      solveDay9aOrderings(pcs, distances, route2distance, route);
      route.remove(p);
    }

  }

  @Test
  public void testDay09() {
    assertEquals(605, solveA(List.of("London to Dublin = 464", "London to Belfast = 518", "Dublin to Belfast = 141")));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(251, result);
    System.out.println(result);

    announceResultB();
    result = solveB(lines);
    assertEquals(898, result);
    System.out.println(result);
  }

}
