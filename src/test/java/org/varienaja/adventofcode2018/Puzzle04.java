package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(38813, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(141071, sum);
  }

  private long solve(List<String> input, boolean puzzleA) {
    List<String> sorted = new LinkedList<>(input);
    Collections.sort(sorted); // Fix ordering of input

    Map<Integer, Integer> guard2sleep = new HashMap<>();

    int guard = 0;
    AtomicInteger asleep = new AtomicInteger();
    int sleepStart = -1;
    for (String s : sorted) {
      int ix = s.indexOf('#');
      if (ix >= 0) {
        guard = 0;
        asleep.set(0);
        sleepStart = -1;

        ix++;
        do {
          guard = guard * 10 + Integer.parseInt("" + s.charAt(ix++));
        } while (s.charAt(ix) != ' ');
      } else if (s.contains("falls asleep")) {
        // [1518-08-31 00:57] wakes up
        sleepStart = Integer.parseInt(s.substring(15, 17));
      } else if (s.contains("wakes up")) {
        int wakeUp = Integer.parseInt(s.substring(15, 17));
        asleep.addAndGet(wakeUp - sleepStart);
        guard2sleep.compute(guard, (k, v) -> v == null ? asleep.get() : v + asleep.get());
      }
    }

    Map<Integer, Integer> guard2mostSleptMinute = new HashMap<>();
    Map<Integer, Integer> guard2sleepInMinute = new HashMap<>();
    for (int g : guard2sleep.keySet()) {
      int[] minutes = new int[60];
      boolean found = false;
      for (String s : sorted) {
        if (!found) {
          found = s.contains("#" + g);
        } else {
          if (s.contains("falls asleep")) {
            sleepStart = Integer.parseInt(s.substring(15, 17));
          } else if (s.contains("wakes up")) {
            int wakeUp = Integer.parseInt(s.substring(15, 17));
            for (int m = sleepStart; m < wakeUp; m++) {
              minutes[m]++;
            }
          } else {
            found = s.contains("#" + g);
          }
        }
      }
      int sleepiestMinute = -1;
      int max = -1;
      for (int m = 0; m < minutes.length; m++) {
        if (minutes[m] > max) {
          max = minutes[m];
          sleepiestMinute = m;
        }
      }
      guard2sleepInMinute.put(g, max);
      guard2mostSleptMinute.put(g, sleepiestMinute);
    }

    if (puzzleA) {
      int sleepiestGuard = -1;
      int maxSleptFor = Integer.MIN_VALUE;
      for (Entry<Integer, Integer> e : guard2sleep.entrySet()) {
        if (e.getValue() > maxSleptFor) {
          maxSleptFor = e.getValue();
          sleepiestGuard = e.getKey();
        }
      }

      return sleepiestGuard * guard2mostSleptMinute.get(sleepiestGuard);
    }

    int sleepiestGuard = -1;
    int maxSleptFor = Integer.MIN_VALUE;
    for (Entry<Integer, Integer> e : guard2sleepInMinute.entrySet()) {
      if (e.getValue() > maxSleptFor) {
        maxSleptFor = e.getValue();
        sleepiestGuard = e.getKey();
      }
    }

    return sleepiestGuard * guard2mostSleptMinute.get(sleepiestGuard);
  }

  private long solveA(List<String> input) {
    return solve(input, true);
  }

  private long solveB(List<String> input) {
    return solve(input, false);
  }

  @Test
  public void testA() {
    assertEquals(240, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(4455, solveB(testInput()));
  }

  List<String> testInput() {
    return List.of( //
        "[1518-11-01 00:00] Guard #10 begins shift", //
        "[1518-11-01 00:05] falls asleep", //
        "[1518-11-01 00:25] wakes up", //
        "[1518-11-01 00:30] falls asleep", //
        "[1518-11-01 00:55] wakes up", //
        "[1518-11-01 23:58] Guard #99 begins shift", //
        "[1518-11-02 00:40] falls asleep", //
        "[1518-11-02 00:50] wakes up", //
        "[1518-11-03 00:05] Guard #10 begins shift", //
        "[1518-11-03 00:24] falls asleep", //
        "[1518-11-03 00:29] wakes up", //
        "[1518-11-04 00:02] Guard #99 begins shift", //
        "[1518-11-04 00:36] falls asleep", //
        "[1518-11-04 00:46] wakes up", //
        "[1518-11-05 00:03] Guard #99 begins shift", //
        "[1518-11-05 00:45] falls asleep", //
        "[1518-11-05 00:55] wakes up");
  }

}
