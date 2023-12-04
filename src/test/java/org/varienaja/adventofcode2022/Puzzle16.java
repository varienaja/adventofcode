package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {

  private static class State {
    final int pos;
    final long vOpen;
    final int time;
    final boolean partB;

    State(int pos, long vOpen, int time, boolean partB) {
      this.vOpen = vOpen;
      this.pos = pos;
      this.time = time;
      this.partB = partB;
    }

    @Override
    public boolean equals(Object o) {
      State other = (State)o;
      return other.pos == pos && other.vOpen == vOpen && other.time == time && other.partB == partB;
    }

    @Override
    public int hashCode() {
      return Objects.hash(pos, vOpen, time, partB);
    }
  }

  private Map<State, Long> states;
  private int[] rates;
  private int[][] connections;
  private int startPos;
  int[][] dist;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1896L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(2576L, result);
  }

  @Test
  public void testA() {
    assertEquals(1651L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(1707L, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB", //
        "Valve BB has flow rate=13; tunnels lead to valves CC, AA", //
        "Valve CC has flow rate=2; tunnels lead to valves DD, BB", //
        "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE", //
        "Valve EE has flow rate=3; tunnels lead to valves FF, DD", //
        "Valve FF has flow rate=0; tunnels lead to valves EE, GG", //
        "Valve GG has flow rate=0; tunnels lead to valves FF, HH", //
        "Valve HH has flow rate=22; tunnel leads to valve GG", //
        "Valve II has flow rate=0; tunnels lead to valves AA, JJ", //
        "Valve JJ has flow rate=21; tunnel leads to valve II");
  }

  private void calcFloydWarshall() {
    // https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
    // let dist be a |V| × |V| array of minimum distances initialized to ∞ (infinity)
    dist = new int[rates.length][rates.length];
    for (int i = 0; i < dist.length; i++) {
      for (int j = 0; j < dist[i].length; j++) {
        dist[i][j] = 9999;
      }
    }

    // for each edge (u, v) do
    // ...dist[u][v] ← w(u, v) // The weight of the edge (u, v)
    for (int i = 0; i < connections.length; i++) {
      for (int j : connections[i]) {
        dist[i][j] = 1; // connections[i][j];
      }
    }

    // for each vertex v do
    // ...dist[v][v] ← 0
    for (int i = 0; i < dist.length; i++) {
      dist[i][i] = 0;
    }

    // for k from 1 to |V|
    // for i from 1 to |V|
    // for j from 1 to |V|
    // if dist[i][j] > dist[i][k] + dist[k][j]
    // dist[i][j] ← dist[i][k] + dist[k][j]
    // end if
    for (int k = 0; k < dist.length; k++) {
      for (int i = 0; i < dist.length; i++) {
        for (int j = 0; j < dist.length; j++) {
          dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
        }
      }
    }
  }

  private void parse(List<String> lines) {
    states = new HashMap<>();
    Map<String, Integer> name2ix = new HashMap<>();
    rates = new int[lines.size()];

    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      name2ix.put(line.substring(6, 8), i);
      String rate = line.substring(line.indexOf('=') + 1, line.indexOf(';'));
      rates[i] = Integer.parseInt(rate);
    }

    connections = new int[lines.size()][];
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      int ix = line.indexOf("valves ") + 7;
      if (ix == 6) {
        ix = line.indexOf("valve ") + 6;
      }
      connections[i] = Pattern.compile(",\\s").splitAsStream(line.substring(ix)).mapToInt(name2ix::get).toArray();
    }

    startPos = name2ix.get("AA");
    calcFloydWarshall();
  }

  private long solve(State s) {
    // from https://github.com/betaveros/advent-of-code-2022/blob/main/p16.noul
    if (states.containsKey(s)) {
      return states.get(s);
    }

    long result = 0L;

    for (int nextPos = 0; nextPos < rates.length; ++nextPos) {
      if (rates[nextPos] > 0) {
        boolean newValveOpened = (s.vOpen & 1L << nextPos) == 0L;
        if (newValveOpened) {
          long newOpen = s.vOpen | 1L << nextPos;

          int distance = dist[s.pos][nextPos];
          if (distance < s.time) {
            result = Math.max(result, rates[nextPos] * (s.time - distance - 1) + solve(new State(nextPos, newOpen, s.time - distance - 1, s.partB)));
          }
        }
      }
    }
    if (s.partB) {
      result = Math.max(result, solve(new State(startPos, s.vOpen, 26, false)));
    }

    states.put(s, result);
    return result;
  }

  private long solveA(List<String> lines) {
    parse(lines);
    return solve(new State(startPos, 0L, 30, false));
  }

  private long solveB(List<String> lines) {
    parse(lines);
    return solve(new State(startPos, 0L, 26, true));
  }

}
