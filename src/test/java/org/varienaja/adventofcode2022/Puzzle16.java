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

  private class Key {
    int pos;
    long vOpen;
    int time;
    boolean partB;

    Key(int pos, long vOpen, int time, boolean partB) {
      this.pos = pos;
      this.vOpen = vOpen;
      this.time = time;
      this.partB = partB;
    }

    @Override
    public boolean equals(Object o) {
      Key other = (Key)o;
      return other.pos == pos && other.vOpen == vOpen && other.time == time && other.partB == partB;
    }

    @Override
    public int hashCode() {
      return Objects.hash(pos, vOpen, time, partB);
    }
  }

  private Map<Key, Long> solutions;
  private long[] rates;
  private int[][] connections;
  private int startPos;

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

  private List<String> getTestInput() {
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

  private void parse(List<String> lines) {
    solutions = new HashMap<>();
    Map<String, Integer> name2ix = new HashMap<>();
    rates = new long[lines.size()];

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
  }

  private long solve(Key key) {
    if (key.time == 0) {
      return key.partB ? solve(new Key(startPos, key.vOpen, 26, false)) : 0L;
    }

    if (solutions.containsKey(key)) {
      return solutions.get(key);
    }

    long result = 0L;
    boolean newValveOpened = (key.vOpen & 1L << key.pos) == 0L;
    if (newValveOpened && rates[key.pos] > 0L) {
      long newOpen = key.vOpen | 1L << key.pos;
      result = Math.max(result, (key.time - 1) * rates[key.pos] + solve(new Key(key.pos, newOpen, key.time - 1, key.partB)));
    } // else { //this else makes things much quicker but unfortunately does not reliably work: sometimes you have to
      // move first, not open first
    for (int nextPos : connections[key.pos]) {
      result = Math.max(result, solve(new Key(nextPos, key.vOpen, key.time - 1, key.partB)));
    }
    // }
    solutions.put(key, result);

    return result;
  }

  private long solveA(List<String> lines) {
    parse(lines);
    try {
      return solve(new Key(startPos, 0L, 30, false));
    } finally {
      System.out.println("Solutions: " + solutions.size());
    }
  }

  private long solveB(List<String> lines) {
    parse(lines);
    try {
      return solve(new Key(startPos, 0L, 26, true));
    } finally {
      System.out.println("Solutions: " + solutions.size());
    }
  }

}
