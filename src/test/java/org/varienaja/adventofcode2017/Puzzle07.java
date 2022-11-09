package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  private int isBalanced(Map<String, Set<String>> inTree, String inKey, Map<String, Integer> inNode2weight) throws Exception {
    Set<String> sub = inTree.get(inKey);
    if (sub.isEmpty()) {
      return inNode2weight.get(inKey);
    }

    Map<Integer, Set<String>> weight2Keys = new HashMap<>();
    for (String s : sub) {
      weight2Keys.computeIfAbsent(isBalanced(inTree, s, inNode2weight), HashSet::new).add(s);
    }
    if (weight2Keys.size() > 1) {
      int toBe = 0;
      int wrong = 0;
      String wrongKey = "";
      for (Entry<Integer, Set<String>> e : weight2Keys.entrySet()) {
        if (e.getValue().size() == 1) {
          wrong = e.getKey();
          wrongKey = e.getValue().iterator().next();
        } else {
          toBe = e.getKey();
        }
      }
      int toAdd = toBe - wrong;
      int result = inNode2weight.get(wrongKey) + toAdd;
      throw new Exception(Integer.toString(result)); // WTF return from recursion by exception...
    }

    Entry<Integer, Set<String>> e = weight2Keys.entrySet().iterator().next();
    return inNode2weight.get(inKey) + e.getKey() * e.getValue().size();
  }

  private String solveDay7a(List<String> inLines) {
    Pattern p = Pattern.compile("(\\w+)\\s+\\((\\d+)\\)(\\s+->(.*))*");
    Pattern p2 = Pattern.compile("\\W+");

    Map<String, Set<String>> tree = new HashMap<>();

    for (String line : inLines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        String src = m.group(1);
        String dest = m.group(3);
        Set<String> targets = dest == null ? Collections.emptySet() : p2.splitAsStream(dest.substring(4)).collect(Collectors.toCollection(HashSet::new));
        tree.put(src, targets);
      }
    }

    while (tree.size() > 1) {
      Iterator<Entry<String, Set<String>>> it = tree.entrySet().iterator();
      Set<String> endpoints = new HashSet<>();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        if (entry.getValue().isEmpty()) {
          it.remove();
          endpoints.add(entry.getKey());
        }
      }

      it = tree.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        entry.getValue().removeAll(endpoints);
      }
    }

    return tree.entrySet().iterator().next().getKey();
  }

  private int solveDay7b(List<String> inLines) {
    Pattern p = Pattern.compile("(\\w+)\\s+\\((\\d+)\\)(\\s+->(.*))*");
    Pattern p2 = Pattern.compile("\\W+");
    Map<String, Integer> node2Weight = new HashMap<>();

    Map<String, Set<String>> tree = new HashMap<>();
    Map<String, Set<String>> treeCpy = new HashMap<>();

    for (String line : inLines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        String src = m.group(1);
        String dest = m.group(3);
        Set<String> targets = dest == null ? Collections.emptySet() : p2.splitAsStream(dest.substring(4)).collect(Collectors.toCollection(HashSet::new));
        tree.put(src, targets);
        node2Weight.put(src, Integer.parseInt(m.group(2)));
      }
    }
    for (Entry<String, Set<String>> entry : tree.entrySet()) {
      treeCpy.put(entry.getKey(), new HashSet<>(entry.getValue()));
    }

    while (tree.size() > 1) {
      Iterator<Entry<String, Set<String>>> it = tree.entrySet().iterator();
      Set<String> endpoints = new HashSet<>();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        if (entry.getValue().isEmpty()) {
          it.remove();
          endpoints.add(entry.getKey());
        }
      }

      it = tree.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        entry.getValue().removeAll(endpoints);
      }
    }

    String top = tree.entrySet().iterator().next().getKey();

    // Now verify Tree's weight:

    try {
      isBalanced(treeCpy, top, node2Weight);
    } catch (Exception e) {
      return Integer.parseInt(e.getMessage());
    }
    return -1;

    /*
     * while (tree.size() > 1) {
     * Iterator<Entry<String, Set<String>>> it = tree.entrySet().iterator();
     * Set<String> endpoints = new HashSet<>();
     * while (it.hasNext()) {
     * Entry<String, Set<String>> entry = it.next();
     * if (entry.getValue().isEmpty()) {
     * it.remove();
     * endpoints.add(entry.getKey());
     * }
     * }
     * it = tree.entrySet().iterator();
     * while (it.hasNext()) {
     * Entry<String, Set<String>> entry = it.next();
     * if (endpoints.containsAll(entry.getValue())) {
     * Map<Integer, Set<String>> weights = new HashMap<>();
     * for (String s : entry.getValue()) {
     * weights.computeIfAbsent(node2weight.remove(s), HashSet::new).add(s);
     * }
     * if (weights.size() == 1) {
     * int sum = node2weight.get(entry.getKey()) + entry.getValue().size() *
     * weights.entrySet().iterator().next().getKey();
     * entry.getValue().clear();
     * node2weight.put(entry.getKey(), sum);
     * } else {
     * int toBe = 0;
     * int wrong = 0;
     * String wrongKey = "";
     * for (Entry<Integer, Set<String>> e : weights.entrySet()) {
     * if (e.getValue().size() == 1) {
     * wrong = e.getKey();
     * wrongKey = e.getValue().iterator().next();
     * } else {
     * toBe = e.getKey();
     * }
     * }
     * int toAdd = toBe - wrong;
     * return originalnode2weight.get(wrongKey) + toAdd;
     * }
     * }
     * }
     * }
     */

  }

  @Test
  public void testDay7() throws Exception {
    List<String> testLines = Arrays.asList("pbga (66)", "xhth (57)", "ebii (61)", "havc (66)", "ktlj (57)", "fwft (72) -> ktlj, cntj, xhth", "qoyq (66)",
        "padx (45) -> pbga, havc, qoyq", "tknk (41) -> ugml, padx, fwft", "jptl (61)", "ugml (68) -> gyxo, ebii, jptl", "gyxo (61)", "cntj (57)");
    assertEquals("tknk", solveDay7a(testLines));

    System.out.print("Solution 7a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day07.txt").toURI()));
    System.out.println(solveDay7a(lines));

    assertEquals(60, solveDay7b(testLines));
    System.out.print("Solution 7b: ");
    System.out.println(solveDay7b(lines));
  }

}
