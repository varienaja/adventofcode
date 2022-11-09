package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    String answer = solveA(getInput());
    System.out.println(answer);
    assertEquals("JDEKPFABTUHOQSXVYMLZCNIGRW", answer);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput(), 5, 60);
    System.out.println(sum);
    assertEquals(1048, sum);
  }

  private String solveA(List<String> input) {
    Set<Character> letters = new HashSet<>();
    List<String> pairs = new LinkedList<>();
    for (String line : input) {
      String ab = line.substring(5, 6) + line.substring(36, 37);
      pairs.add(ab);
      letters.add(ab.charAt(0));
      letters.add(ab.charAt(1));
    }

    // Find start letter(s)
    Set<Character> first = new TreeSet<>(letters);
    Iterator<Character> it = first.iterator();
    while (it.hasNext()) {
      Character c = it.next();
      for (String ab : pairs) {
        if (ab.charAt(1) == c) {
          it.remove();
          break;
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    while (!first.isEmpty()) {
      it = first.iterator();
      char next = it.next();
      it.remove();
      sb.append(next);

      // Find next candidates
      for (String ab : pairs) {
        if (ab.charAt(0) == next) {
          first.add(ab.charAt(1));
        }
      }

      // Remove those that cannot be used yet because their prerequisite hasn't yet been used
      it = first.iterator();
      while (it.hasNext()) {
        char c = it.next();
        for (String ab : pairs) {
          if (c == ab.charAt(1)) {
            if (sb.indexOf("" + ab.charAt(0)) < 0) {
              it.remove();
              break;
            }
          }
        }
      }
    }

    return sb.toString();
  }

  private long solveB(List<String> input, int workerCount, int taskLength) {
    Set<Character> letters = new HashSet<>();
    List<String> pairs = new LinkedList<>();
    for (String line : input) {
      String ab = line.substring(5, 6) + line.substring(36, 37);
      pairs.add(ab);
      letters.add(ab.charAt(0));
      letters.add(ab.charAt(1));
    }

    // Find start letter(s)
    Set<Character> first = new TreeSet<>(letters);
    Iterator<Character> it = first.iterator();
    while (it.hasNext()) {
      Character c = it.next();
      for (String ab : pairs) {
        if (ab.charAt(1) == c) {
          it.remove();
          break;
        }
      }
    }

    Map<Character, Integer> workers = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    long t = 0;
    while (!first.isEmpty()) {
      it = first.iterator();

      while (workers.size() < workerCount && it.hasNext()) {
        char next = it.next();
        if (!workers.containsKey(next)) {
          workers.put(next, taskLength + 1 + next - 'A');
        }
      }

      Set<Character> done = new HashSet<>();
      t++;
      Iterator<Entry<Character, Integer>> it2 = workers.entrySet().iterator();
      while (it2.hasNext()) {
        Entry<Character, Integer> e = it2.next();
        e.setValue(e.getValue() - 1);
        if (e.getValue() == 0) {
          it2.remove();
          first.remove(e.getKey());
          done.add(e.getKey());
          sb.append(e.getKey());
        }
      }

      // Find next candidates
      for (String ab : pairs) {
        if (done.contains(ab.charAt(0))) {
          first.add(ab.charAt(1));
        }
      }

      // Remove those that cannot be used yet because their prerequisite hasn't yet been used
      it = first.iterator();
      while (it.hasNext()) {
        char c = it.next();
        for (String ab : pairs) {
          if (c == ab.charAt(1)) {
            if (sb.indexOf("" + ab.charAt(0)) < 0) {
              it.remove();
              break;
            }
          }
        }
      }
    }

    return t;
  }

  @Test
  public void testA() {
    assertEquals("CABDFE", solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(15, solveB(testInput(), 2, 0));
  }

  private List<String> testInput() {
    return List.of( //
        "Step C must be finished before step A can begin.", //
        "Step C must be finished before step F can begin.", //
        "Step A must be finished before step B can begin.", //
        "Step A must be finished before step D can begin.", //
        "Step B must be finished before step E can begin.", //
        "Step D must be finished before step E can begin.", //
        "Step F must be finished before step E can begin.");
  }

}
