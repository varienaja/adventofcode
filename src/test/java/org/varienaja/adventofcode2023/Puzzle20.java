package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {

  record Pulse(String source, boolean value) {
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(807069600L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(221453937522197L, result);
  }

  @Test
  public void testA() {
    assertEquals(32000000, solveA(getTestInput('a')));
    assertEquals(11687500, solveA(getTestInput('b')));
  }

  private long solve(List<String> lines, boolean partB) {
    Map<String, List<String>> source2destinations = new HashMap<>();
    Map<String, Boolean> flipflop2signal = new HashMap<>();
    Map<String, Map<String, Boolean>> conjunction2inputs = new HashMap<>();
    for (String line : lines) {
      String parts[] = line.split(" -> ");
      String src = parts[0].replaceAll("\\W", "");
      String[] destinations = parts[1].split(", ");
      source2destinations.put(src, List.of(destinations));
      if (parts[0].startsWith("&")) {
        conjunction2inputs.put(src, new HashMap<>());
      } else if (parts[0].startsWith("%")) {
        flipflop2signal.put(src, false);
      }
    }

    // After some experimenting and reverse engineering with part two, it seems that rx is
    // triggered by a single conjunction module. This trigger itself is triggered by a number
    // of others that only fire 'high' every so often. We want to find after how many button
    // presses all 'others' fire 'high', so that the trigger fires low to rx. My experiments
    // showed that the others fire very regularly: 'vd' after 3767, 'pc' after 3881,
    // 'tx' after 3769 and 'nd' after 4019 button presses.
    // Therefore, the trigger will fire 'low' after 3767*3881*3769*4019 button presses!
    String trigger = source2destinations.entrySet().stream() //
        .filter(e -> e.getValue().contains("rx")) //
        .map(Entry::getKey) //
        .findFirst().orElse("");
    Map<String, Long> others2buttonPresses = new HashMap<>();

    for (Entry<String, List<String>> e : source2destinations.entrySet()) {
      for (String d : e.getValue()) {
        if (conjunction2inputs.containsKey(d)) {
          conjunction2inputs.get(d).put(e.getKey(), false);
        }
        if (trigger.equals(d)) {
          others2buttonPresses.put(e.getKey(), -1L);
        }
      }
    }

    long highPulses = 0;
    long lowPulses = 0;

    // Send pulses until 1000 button presses (A) or until we found the cycle length for all 'others' (B)
    Queue<Pulse> currentPulses = new LinkedList<>();
    long buttonPresses = 0;
    while (buttonPresses < 1000) {
      boolean allFlipFlopsLow = true;

      do {
        currentPulses.add(new Pulse("broadcaster", false));
        ++buttonPresses;
        ++lowPulses;

        while (!currentPulses.isEmpty()) {
          Pulse p = currentPulses.poll();

          if (p.value && others2buttonPresses.containsKey(p.source)) {
            others2buttonPresses.put(p.source, buttonPresses);
            if (!others2buttonPresses.values().contains(-1L)) { // Part B
              return others2buttonPresses.values().stream().reduce((a, b) -> a * b).orElseThrow();
            }
          }

          List<String> destinations = source2destinations.get(p.source);
          highPulses += p.value ? destinations.size() : 0;
          lowPulses += p.value ? 0 : destinations.size();
          // System.out.println(p.source + " " + (p.value ? "high" : "low") + destinations);

          for (String dest : destinations) {
            if (flipflop2signal.containsKey(dest) && !p.value) {
              boolean ffValue = !flipflop2signal.get(dest);
              flipflop2signal.put(dest, ffValue);
              currentPulses.add(new Pulse(dest, ffValue));
            } else if (conjunction2inputs.containsKey(dest)) {
              Map<String, Boolean> inputs = conjunction2inputs.get(dest);
              inputs.put(p.source, p.value);
              boolean boolAnd = inputs.values().stream().reduce((a, b) -> a & b).get();
              currentPulses.add(new Pulse(dest, !boolAnd));
            }
          }
        }

        boolean boolOr = flipflop2signal.values().stream().reduce((a, b) -> a | b).get();
        allFlipFlopsLow = !boolOr;
      } while (!allFlipFlopsLow && (partB || buttonPresses < 1000));
    }

    return highPulses * lowPulses; // Part A
  }

  private long solveA(List<String> lines) {
    return solve(lines, false);
  }

  private long solveB(List<String> lines) {
    return solve(lines, true);
  }

}
