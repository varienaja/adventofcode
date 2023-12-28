package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(562772L, result);
  }

  @Test
  public void testA() {
    assertEquals(54, solveA(getTestInput()));
  }

  private long solveA(List<String> lines) {
    NavigableMap<String, List<String>> connections = new TreeMap<>();
    for (String line : lines) {
      String[] parts = line.split(":\\s+");
      String[] pp = parts[1].split("\\s+");
      for (String p : pp) {
        connections.compute(parts[0], (k, v) -> v == null ? new LinkedList<>() : v).add(p);
        connections.compute(p, (k, v) -> v == null ? new LinkedList<>() : v).add(parts[0]);
      }
    }

    // https://en.wikipedia.org/wiki/Girvan%E2%80%93Newman_algorithm
    // 1. Calc betweenness of all edges
    // 2. remove edge with highest betweenness
    // 3. recalc betweenness of all edged affected by removal
    // until we have two parts (so: 3 steps?)

    // Calc edge betweenness
    Map<String, Integer> line2weight = new TreeMap<>(); // The edge betweenness
    for (String starter : connections.keySet()) {
      Set<String> seen = new HashSet<>();
      List<String> todo = List.of(starter);
      while (!todo.isEmpty()) {
        for (String s : todo) {
          String[] parts = s.split("-");
          seen.add(parts[parts.length - 1]);
        }
        List<String> toAdd = new LinkedList<>();
        for (String nodes : todo) {
          String[] parts = nodes.split("-");
          String[] partsNew = new String[parts.length + 1];
          int lastIx = parts.length;
          System.arraycopy(parts, 0, partsNew, 0, parts.length);

          for (String to : connections.get(parts[parts.length - 1])) {
            if (!seen.contains(to)) {
              partsNew[lastIx] = to;
              toAdd.add(nodes + "-" + to);

              for (int ix = 0; ix < lastIx; ++ix) {
                String f = partsNew[ix];
                String t = partsNew[ix + 1];
                String line = f.compareTo(t) < 0 ? f + "-" + t : t + "-" + f;
                line2weight.compute(line, (k, v) -> v == null ? 1 : v + 1);
              }
            }
          }
        }
        todo = toAdd;
      }
    }

    List<Entry<String, Integer>> l = line2weight.entrySet().stream().sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())).toList();
    // System.out.println(l);

    // Remove 3 highest-weighted lines (According to Girvan-Newman we should only remove 1 line, and then recalculate
    // the edge betweenness!)
    for (int i = 0; i < 3; ++i) {
      String line = l.get(i).getKey();
      String[] parts = line.split("-");
      connections.get(parts[0]).remove(parts[1]);
      connections.get(parts[1]).remove(parts[0]);
    }

    // Calc the groups
    Map<String, Set<String>> key2reachable = new HashMap<>();
    nextStarter: for (String starter : connections.keySet()) {
      for (Set<String> candidate : key2reachable.values()) {
        if (candidate.contains(starter)) {
          continue nextStarter;
        }
      }

      Set<String> seen = new HashSet<>();
      Queue<String> todo = new LinkedList<>();
      todo.add(starter);
      while (!todo.isEmpty()) {
        String node = todo.poll();
        seen.add(node);
        for (String to : connections.get(node)) {
          if (!seen.contains(to)) {
            todo.add(to);
          }
        }
      }
      key2reachable.put(starter, seen);
    }

    // Return product of sum of nodes in groups
    Set<Integer> vs = key2reachable.values().stream().map(v -> v.size()).collect(Collectors.toSet());
    return vs.stream().reduce((a, b) -> a * b).get();
  }

}
