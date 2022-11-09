package org.varienaja.adventofcode2015;

import static java.util.Collections.swap;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {
  private Map<String, Integer> couple2Happiness;
  private Map<String, Integer> order2Happiness;
  private Set<String> names = new HashSet<>();

  private int calcHappiness(List<String> order) {
    int sum = 0;
    for (int i = 0; i < order.size(); i++) {
      String nameA = order.get(i);
      String nameB = order.get(i + 1 >= order.size() ? 0 : i + 1);
      String couple = nameA.compareTo(nameB) < 0 ? nameA + nameB : nameB + nameA;

      sum += couple2Happiness.get(couple);
    }

    // System.out.print(order);
    // System.out.print(" ");
    // System.out.println(sum);

    order2Happiness.put(order.toString(), sum);

    return sum;
  }

  void permute(List<String> arr, int k) {
    for (int i = k; i < arr.size(); i++) {
      swap(arr, i, k);
      permute(arr, k + 1);
      swap(arr, k, i);
    }
    if (k == arr.size() - 1) {
      calcHappiness(arr);
    }
  }

  private int solveA(List<String> input) {
    couple2Happiness = new TreeMap<>();
    order2Happiness = new HashMap<>();
    names = new HashSet<>();

    for (String l : input) {
      int firstSpace = l.indexOf(' ');
      int lastSpace = l.lastIndexOf(' ');
      boolean positive = l.contains("gain");

      String nameA = l.substring(0, firstSpace);
      String nameB = l.substring(lastSpace + 1, l.length() - 1);

      int happiness = 0;
      for (char c : l.toCharArray()) {
        if (Character.isDigit(c)) {
          happiness = 10 * happiness + Integer.parseInt("" + c);
        }
      }
      if (!positive) {
        happiness = -happiness;
      }

      names.add(nameA);

      String couple = nameA.compareTo(nameB) < 0 ? nameA + nameB : nameB + nameA;
      Integer h = couple2Happiness.get(couple);
      if (h == null) {
        h = 0;
      }

      h += happiness;
      couple2Happiness.put(couple, h);
    }
    // System.out.println(couple2Happiness);

    permute(new LinkedList<>(names), 0);

    return order2Happiness.values().stream().mapToInt(i -> i).max().orElse(-1);
  }

  private int solveB(List<String> input) {
    for (String name : names) {
      input.add("Varienaja would gain 0 happiness units by sitting next to " + name + ".");
      input.add(name + " would gain 0 happiness units by sitting next to Varienaja.");
    }

    return solveA(input);
  }

  @Test
  public void testDay10() {
    List<String> testInput = List.of( //
        "Alice would gain 54 happiness units by sitting next to Bob.", //
        "Alice would lose 79 happiness units by sitting next to Carol.", //
        "Alice would lose 2 happiness units by sitting next to David.", //
        "Bob would gain 83 happiness units by sitting next to Alice.", //
        "Bob would lose 7 happiness units by sitting next to Carol.", //
        "Bob would lose 63 happiness units by sitting next to David.", //
        "Carol would lose 62 happiness units by sitting next to Alice.", //
        "Carol would gain 60 happiness units by sitting next to Bob.", //
        "Carol would gain 55 happiness units by sitting next to David.", //
        "David would gain 46 happiness units by sitting next to Alice.", //
        "David would lose 7 happiness units by sitting next to Bob.", //
        "David would gain 41 happiness units by sitting next to Carol.");
    assertEquals(330, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    int sum = solveA(lines);
    System.out.println(sum);
    assertEquals(618, sum);

    announceResultB();
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(601, sum);
  }

}
