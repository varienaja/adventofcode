package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {
  private Set<Point> maze;
  private Map<Integer, Point> numbers;
  private Map<Point, Integer> distances;

  private int calcDist(int n1, int n2) {
    // Start at n1, go all (breath first) possible directions until reached n2, return distance
    Point target = numbers.get(n2);

    int distance = 0;
    Set<Point> reachable = Collections.singleton(numbers.get(n1));
    Set<Point> seen = new HashSet<>(reachable);
    while (!reachable.contains(target)) {
      Set<Point> nextRound = new HashSet<>();
      for (Point r : reachable) {
        for (Point candidate : r.getNSWENeighbours()) {
          if (!maze.contains(candidate)) {
            if (seen.add(candidate)) {
              nextRound.add(candidate);
            }
          }
        }
      }

      reachable = nextRound;
      distance++;
    }

    return distance;
  }

  private int generateOrders(Set<Integer> allNumbers, int[] ordering, int ix) {
    if (allNumbers.isEmpty()) {
      int distance = 0;
      for (int i = 1; i < ordering.length; i++) {
        int n1 = ordering[i - 1];
        int n2 = ordering[i];
        Point p = new Point(Math.min(n1, n2), Math.max(n1, n2));
        distance += distances.get(p);
      }
      return distance;
    }

    int distance = Integer.MAX_VALUE;
    for (int i : allNumbers) {
      ordering[ix] = i;
      Set<Integer> allNumbersMinusOne = new HashSet<>(allNumbers);
      allNumbersMinusOne.remove(i);

      int dist = generateOrders(allNumbersMinusOne, ordering, ix + 1);
      if (dist < distance) {
        distance = dist;
      }
    }
    return distance;
  }

  private void prepareMaze(List<String> input) {
    maze = new HashSet<>();
    numbers = new HashMap<>();
    distances = new HashMap<>();

    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        char c = line.charAt(x);
        if (c == '#') {
          maze.add(new Point(x, y));
        } else if (c >= '0' && c <= '9') {
          numbers.put(Integer.parseInt("" + c), new Point(x, y));
        }
      }
    }

    for (int n1 : numbers.keySet()) {
      for (int n2 : numbers.keySet()) {
        if (n1 < n2) {
          distances.put(new Point(n1, n2), calcDist(n1, n2));
        }
      }
    }
  }

  private long solveA(List<String> input) {
    prepareMaze(input);

    // Calc shortest distances between all pairs
    // calc each ordering of 1..7 (5040)
    // calc minimum dist from 0 to ordering

    Set<Integer> allNumbers = new HashSet<>(numbers.keySet());
    allNumbers.remove(0);
    int[] ordering = new int[numbers.size()];
    ordering[0] = 0;
    return generateOrders(allNumbers, ordering, 1);
  }

  private long solveB(List<String> input) {
    // Visit every number at least once, return to 0

    prepareMaze(input);

    Set<Integer> allNumbers = new HashSet<>(numbers.keySet());
    allNumbers.remove(0);
    int[] ordering = new int[numbers.size() + 1];
    ordering[0] = 0;
    return generateOrders(allNumbers, ordering, 1);
  }

  @Test
  public void testDay24() {
    List<String> testInput = List.of( //
        "###########", //
        "#0.1.....2#", //
        "#.#######.#", //
        "#4.......3#", //
        "###########");
    assertEquals(14, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(412, sum);

    announceResultB();
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(664, sum);
  }

}
