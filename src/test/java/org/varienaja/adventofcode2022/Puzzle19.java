package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {
  private static class State {
    final short ore;
    final short clay;
    final short obsidian;
    final short geode;
    final short rOre;
    final short rClay;
    final short rObsidian;
    final short rGeode;
    final short t;

    State(int o, int c, int ob, int g, int rO, int rC, int rObs, int rG, int time) {
      ore = (short)o;
      clay = (short)c;
      obsidian = (short)ob;
      geode = (short)g;

      rOre = (short)rO;
      rClay = (short)rC;
      rObsidian = (short)rObs;
      rGeode = (short)rG;
      t = (short)time;
    }

    @Override
    public boolean equals(Object o) {
      State s = (State)o;
      return ore == s.ore && clay == s.clay && obsidian == s.obsidian && geode == s.geode && //
          rOre == s.rOre && rClay == s.rClay && rObsidian == s.rObsidian && rGeode == s.rGeode && //
          t == s.t;
    }

    @Override
    public int hashCode() {
      return Objects.hash(ore, clay, obsidian, geode, rOre, rClay, rObsidian, rGeode, t);
    }
  }

  private int oreCostOre;
  private int oreCostClay;
  private int oreCostObsidian;
  private int clayCostObsidian;
  private int oreCostGeode;
  private int obsidianCostGeode;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1624L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(12628L, result);
  }

  @Test
  public void testA() {
    assertEquals(33L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(3472L, solveB(getTestInput()));
  }

  private long calcQ(int time) {
    int costOre = IntStream.of(oreCostOre, oreCostClay, oreCostObsidian, oreCostGeode).max().orElseThrow();
    long result = 0;
    State start = new State(0, 0, 0, 0, 1, 0, 0, 0, time);

    Queue<State> q = new LinkedList<>();
    q.add(start);
    Set<State> known = new HashSet<>();
    int currentTime = time;

    while (!q.isEmpty()) {
      State s = q.poll();
      result = Math.max(result, s.geode);
      if (s.t == 0) {
        continue;
      }

      int o = s.ore;
      int c = s.clay;
      int ob = s.obsidian;
      int g = s.geode;
      int r1 = s.rOre;
      int r2 = s.rClay;
      int r3 = s.rObsidian;
      int r4 = s.rGeode;
      int t = s.t;

      if (t != currentTime) {
        currentTime = t;
        System.out.print(t);
        System.out.print(",");
      }

      int plannedGeodes = g + r3 * t;
      if (plannedGeodes + (t * t - t) / 2 <= result) {
        continue;
      }

      // Minimize State count; map to equivalents
      r1 = Math.min(r1, costOre);
      r2 = Math.min(r2, clayCostObsidian);
      r3 = Math.min(r3, obsidianCostGeode);

      o = Math.min(o, t * costOre - r1 * (t - 1));
      c = Math.min(c, t * clayCostObsidian - r2 * (t - 1));
      ob = Math.min(ob, t * obsidianCostGeode - r3 * (t - 1));

      if (!known.add(new State(o, c, ob, g, r1, r2, r3, r4, t))) {
        continue;
      }

      o += r1;
      c += r2;
      ob += r3;
      g += r4;
      t = t - 1;

      if (o >= oreCostGeode + r1 && ob >= obsidianCostGeode + r3) {
        q.offer(new State(o - oreCostGeode, c, ob - obsidianCostGeode, g, r1, r2, r3, r4 + 1, t));
        continue; // Don't expand state if building geode bot is possible
      }
      if (o >= oreCostOre + r1) {
        q.offer(new State(o - oreCostOre, c, ob, g, r1 + 1, r2, r3, r4, t));
      }
      if (o >= oreCostClay + r1) {
        q.offer(new State(o - oreCostClay, c, ob, g, r1, r2 + 1, r3, r4, t));
      }
      if (o >= oreCostObsidian + r1 && c >= clayCostObsidian + r2) {
        q.offer(new State(o - oreCostObsidian, c - clayCostObsidian, ob, g, r1, r2, r3 + 1, r4, t));
      }
      q.offer(new State(o, c, ob, g, r1, r2, r3, r4, t));
    }

    return result;
  }

  private List<String> getTestInput() {
    return List.of(
        "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.", //
        "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.");
  }

  private long solveA(List<String> lines) {
    long result = 0;
    Pattern p = Pattern.compile(
        "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
    for (String line : lines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int bp = Integer.parseInt(m.group(1));
        oreCostOre = Integer.parseInt(m.group(2));
        oreCostClay = Integer.parseInt(m.group(3));
        oreCostObsidian = Integer.parseInt(m.group(4));
        clayCostObsidian = Integer.parseInt(m.group(5));
        oreCostGeode = Integer.parseInt(m.group(6));
        obsidianCostGeode = Integer.parseInt(m.group(7));

        long q = bp * calcQ(24);
        System.out.println(q);
        result += q;
      }
    }
    return result; // sum of q
  }

  private long solveB(List<String> lines) {
    long result = 1;
    Pattern p = Pattern.compile(
        "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
    Iterator<String> it = lines.iterator();
    int i = 0;
    while (it.hasNext() && i < 3) {
      String line = it.next();
      i++;

      Matcher m = p.matcher(line);
      if (m.matches()) {
        oreCostOre = Integer.parseInt(m.group(2));
        oreCostClay = Integer.parseInt(m.group(3));
        oreCostObsidian = Integer.parseInt(m.group(4));
        clayCostObsidian = Integer.parseInt(m.group(5));
        oreCostGeode = Integer.parseInt(m.group(6));
        obsidianCostGeode = Integer.parseInt(m.group(7));

        long q = calcQ(32);
        System.out.println(q);
        result *= q;
      }
    }

    return result;
  }

}
