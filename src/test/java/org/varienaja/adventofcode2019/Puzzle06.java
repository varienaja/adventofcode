package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle06 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(110190, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(343, sum);
  }

  private Map<String, String> parse(List<String> input) {
    Map<String, String> o2parent = new HashMap<>();
    for (String line : input) {
      String[] parts = line.split("\\)");
      o2parent.put(parts[1], parts[0]);
    }
    return o2parent;
  }

  private long solveA(List<String> input) {
    Map<String, String> o2parent = parse(input);

    long totalOrbits = 0;
    Set<String> satellites = o2parent.keySet();
    for (String s : satellites) {
      int orbits = -1; // Don't count the super parent
      String parent = s;
      while (parent != null) {
        parent = o2parent.get(parent);
        orbits++;
      }

      totalOrbits += orbits;
    }

    return totalOrbits;
  }

  private long solveB(List<String> input) {
    Map<String, String> o2parent = parse(input);
    String from = o2parent.get("YOU");
    String to = o2parent.get("SAN");

    Map<String, Integer> santoParent2dist = new HashMap<>();
    int dist = 1;
    while (to != null) {
      to = o2parent.get(to);
      santoParent2dist.put(to, dist++);
    }

    Map<String, Integer> youtoParent2dist = new HashMap<>();
    dist = 1;
    while (from != null) {
      from = o2parent.get(from);
      youtoParent2dist.put(from, dist++);
    }

    // Select first satellite contained by both paths to parent, add those two distances
    return youtoParent2dist.entrySet().stream() //
        .filter(e -> santoParent2dist.containsKey(e.getKey())) //
        .mapToInt(e -> e.getValue() + santoParent2dist.get(e.getKey())) //
        .min() //
        .orElse(-1);
  }

  @Test
  public void testA() {
    assertEquals(42, solveA(testInput()));
  }

  @Test
  public void testB() {
    List<String> input = new LinkedList<>(testInput());
    input.add("K)YOU");
    input.add("I)SAN");

    assertEquals(4, solveB(input));
  }

  private List<String> testInput() {
    return List.of( //
        "COM)B", //
        "B)C", //
        "C)D", //
        "D)E", //
        "E)F", //
        "B)G", //
        "G)H", //
        "D)I", //
        "E)J", //
        "J)K", //
        "K)L");
  }

}
