package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {
  class Solution {
    int keychain;
    int distance;
    Set<Point> position;

    private Solution(Set<Point> start) {
      keychain = 0;
      distance = 0;
      position = Objects.requireNonNull(start);
    }

    public Solution add(Point p, Entry<Character, Integer> e) {
      char c = Character.toLowerCase(e.getKey());

      Set<Point> positionNew = new HashSet<>(position);
      positionNew.remove(p);
      positionNew.add(keys.get(c));

      Solution result = new Solution(positionNew);
      result.keychain = keychain;
      result.addKey(c);
      result.distance = distance + e.getValue();
      return result;
    }

    void addKey(char c) {
      int ix = c - 'a';
      keychain = keychain | (int)Math.pow(2, ix);
    }

    public boolean contains(char c) {
      return Puzzle18.contains(keychain, c);
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Solution) {
        Solution s = (Solution)o;
        return keychain == s.keychain;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(keychain, distance, position);
    }

    boolean isComplete() {
      return Integer.bitCount(keychain) == keys.size();
    }

    @Override
    public String toString() {
      return distance + "," + keychain;
    }
  }

  static boolean contains(int keychain, char c) {
    int ix = c - 'a';
    int v = (int)Math.pow(2, ix);
    return (keychain & v) == v;
  }

  private Map<Character, Point> keys;
  private Map<Character, Point> doors;
  private long minDistance;
  private Map<Point, Character> world;
  private Map<String, Map<Character, Integer>> floodCache;

  private long calcSolution(Collection<Solution> solutions) {
    // I am at @. I can go (with my current keys) to a set of keys)
    // Iterate: visit all those keys. Keep distance
    // I am now at @'. I can go (with my current keys) to a set of keys)
    // Iterate: visit all those keys. Keep distance
    // If I have a final solution, I can already discard all others which have a longer way
    // Keep BFSing until no more candidates.
    if (solutions.isEmpty()) {
      // System.out.println(floodCache.size());
      return minDistance;
    }

    // System.out.print(solutions.iterator().next().distance);
    // System.out.print(" ");
    // System.out.print(solutions.iterator().next().kk.size());
    // System.out.print(" ");
    // System.out.println(solutions.size());

    Collection<Solution> nextSolutions = new HashSet<>();

    Iterator<Solution> is = solutions.iterator();
    while (is.hasNext()) {
      Solution s = is.next();

      if (s.isComplete()) {
        if (s.distance < minDistance) {
          minDistance = s.distance;
          // System.out.println(s);
        }
      } else {
        if (s.distance < minDistance) { // Don't calc nextSolutions if distance already greater than known minimum
          for (Point p : s.position) {
            Map<Character, Integer> reachable = flood(s.keychain, p);
            if (!reachable.isEmpty()) {
              for (Entry<Character, Integer> e : reachable.entrySet()) {
                nextSolutions.add(s.add(p, e));
              }
            }
          }
        }
      }
    }

    return calcSolution(nextSolutions);
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solve(getInput());
    System.out.println(sum);
    assertEquals(5402, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    List<String> i = getInput();
    String l39 = "#.......................#.....#........@#@..#.#.............#.....#...........#.#";
    String l40 = "#################################################################################";
    String l41 = "#...............#.....#.....#..........@#@#.........................D.......#..u#";

    i.set(39, l39);
    i.set(40, l40);
    i.set(41, l41);

    long sum = solve(i);
    System.out.println(sum);
    assertEquals(2138, sum);
  }

  /**
   * Returns all reachable keys and the amount of steps to get there from the given position with the given keychain.
   *
   * @param keychain keys I already have
   * @param myPos my position
   * @return all reachable keys + distances
   */
  private Map<Character, Integer> flood(int keychain, Point myPos) {
    String key = myPos.toString() + keychain;
    Map<Character, Integer> result = floodCache.get(key);
    if (result != null) {
      return result;
    }

    result = new HashMap<>();
    int steps = 0;
    Set<Point> visited = new HashSet<>();
    Queue<Set<Point>> queue = new LinkedList<>();
    queue.add(Collections.singleton(myPos));
    while (!queue.isEmpty()) {
      Set<Point> pts = queue.poll();
      if (pts.isEmpty()) {
        break;
      }

      steps++;
      Set<Point> nxt = new HashSet<>();
      for (Point p : pts) {
        Set<Point> next = p.getNSWENeighbours().stream() //
            .filter(nb -> !visited.contains(nb)) //
            .filter(nb -> world.get(nb) != '#') //
            .collect(Collectors.toSet());
        for (Point possible : next) {
          visited.add(possible);

          char c = world.get(possible);
          if (c >= 'a' && c <= 'z') {
            if (!contains(keychain, Character.toLowerCase(c))) {
              result.put(c, steps);
            } else {
              nxt.add(possible);
            }
          } else if (c >= 'A' && c <= 'Z') {
            if (contains(keychain, Character.toLowerCase(c))) {
              nxt.add(possible);
            }
          } else {
            nxt.add(possible);
          }
        }
      }
      if (!nxt.isEmpty()) {
        queue.add(nxt);
      }
    }

    floodCache.put(key, result);
    return result;
  }

  private long solve(List<String> input) {
    world = new HashMap<>();
    keys = new HashMap<>();
    doors = new HashMap<>();
    floodCache = new HashMap<>();
    Set<Point> myPoss = new HashSet<>();

    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        char c = line.charAt(x);
        Point p = new Point(x, y);
        world.put(p, c);
        if (c >= 'a' && c <= 'z') {
          keys.put(c, p);
        } else if (c >= 'A' && c <= 'Z') {
          doors.put(c, p);
        } else if (c == '@') {
          myPoss.add(p);
        }
      }
    }

    minDistance = Long.MAX_VALUE;
    return calcSolution(Collections.singleton(new Solution(myPoss)));
  }

  @Test
  public void testA() {
    assertEquals(8, solve(testInput1()));
    assertEquals(86, solve(testInput2()));
    assertEquals(132, solve(testInput3()));
    assertEquals(136, solve(testInput4()));
    assertEquals(81, solve(testInput5()));
  }

  @Test
  public void testB() {
    assertEquals(8, solve(testInput6()));
    assertEquals(24, solve(testInput7()));
    assertEquals(32, solve(testInput8()));
    assertEquals(72, solve(testInput9()));
  }

  private List<String> testInput1() {
    return List.of( //
        "#########", //
        "#b.A.@.a#", //
        "#########");
  }

  private List<String> testInput2() {
    return List.of( //
        "########################", //
        "#f.D.E.e.C.b.A.@.a.B.c.#", //
        "######################.#", //
        "#d.....................#", //
        "########################");
  }

  private List<String> testInput3() {
    return List.of( //
        "########################", //
        "#...............b.C.D.f#", //
        "#.######################", //
        "#.....@.a.B.c.d.A.e.F.g#", //
        "########################");
  }

  private List<String> testInput4() {
    return List.of( //
        "#################", //
        "#i.G..c...e..H.p#", //
        "########.########", //
        "#j.A..b...f..D.o#", //
        "########@########", //
        "#k.E..a...g..B.n#", //
        "########.########", //
        "#l.F..d...h..C.m#", //
        "#################");
  }

  private List<String> testInput5() {
    return List.of( //
        "########################", //
        "#@..............ac.GI.b#", //
        "###d#e#f################", //
        "###A#B#C################", //
        "###g#h#i################", //
        "########################");
  }

  private List<String> testInput6() {
    return List.of( //
        "#######", //
        "#a.#Cd#", //
        "##@#@##", //
        "#######", //
        "##@#@##", //
        "#cB#Ab#", //
        "#######");
  }

  private List<String> testInput7() {
    return List.of( //
        "###############", //
        "#d.ABC.#.....a#", //
        "######@#@######", //
        "###############", //
        "######@#@######", //
        "#b.....#.....c#", //
        "###############");
  }

  private List<String> testInput8() {
    return List.of( //
        "#############", //
        "#DcBa.#.GhKl#", //
        "#.###@#@#I###", //
        "#e#d#####j#k#", //
        "###C#@#@###J#", //
        "#fEbA.#.FgHi#", //
        "#############");
  }

  private List<String> testInput9() {
    return List.of( //
        "#############", //
        "#g#f.D#..h#l#", //
        "#F###e#E###.#", //
        "#dCba@#@BcIJ#", //
        "#############", //
        "#nK.L@#@G...#", //
        "#M###N#H###.#", //
        "#o#m..#i#jk.#", //
        "#############");
  }

}
