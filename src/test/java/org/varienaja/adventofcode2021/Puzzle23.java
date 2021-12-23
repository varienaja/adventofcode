package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {

  class State implements Comparable<State> {
    private Map<Character, Integer> char2moves = new HashMap<>();
    char[] hallway = "..x.x.x.x..".toCharArray();
    private int roomDepth;
    String[] dest;

    State(State s) {
      dest = new String[4];
      char2moves = new HashMap<>(s.char2moves);
      System.arraycopy(s.hallway, 0, hallway, 0, hallway.length);
      System.arraycopy(s.dest, 0, dest, 0, dest.length);
      roomDepth = s.roomDepth;
    }

    public State(String room1, String room2, String room3, String room4) {
      char2moves.put('A', 0);
      char2moves.put('B', 0);
      char2moves.put('C', 0);
      char2moves.put('D', 0);
      roomDepth = room1.length();
      dest = new String[] {
          room1, room2, room3, room4
      };
    }

    /**
     * Returns whether a letter can exit its designated room (to let another letter out).
     *
     * @param letter the letter
     * @return whether this one can leave
     */
    public boolean canLeave(char letter, int r) {
      int room = letter - 'A';
      if (room == r) {
        boolean result = false;
        for (int i = 0; i < dest[r].length(); i++) {
          char c = dest[r].charAt(i);
          if (c != '.' && c != letter) {
            result = true;
          }
        }
        return result;
      }

      return true;
    }

    @Override
    public int compareTo(State o) {
      int result = Integer.compare(score(), o.score());
      if (result == 0) {
        result = toSmall().compareTo(o.toSmall());
      }
      return -result;
    }

    boolean done() {
      if (roomDepth == 2) {
        return dest[0].equals("AA") && dest[1].equals("BB") && dest[2].equals("CC") && dest[3].equals("DD");
      }
      return dest[0].equals("AAAA") && dest[1].equals("BBBB") && dest[2].equals("CCCC") && dest[3].equals("DDDD");
    }

    @Override
    public boolean equals(Object other) {
      if (other instanceof State) {
        State o = (State)other;
        return Arrays.equals(hallway, o.hallway) && Arrays.equals(dest, o.dest) && char2moves.equals(o.char2moves);
      }

      return false;
    }

    /**
     * Removes letter from room r.
     *
     * @param r the room
     */
    public void fromRoom(int r) {
      int depth = dest[r].lastIndexOf('.');
      if (depth < 0) {
        dest[r] = '.' + dest[r].substring(1);
      } else {
        dest[r] = dest[r].substring(0, depth + 1) + '.' + dest[r].substring(depth + 2);
      }
    }

    @Override
    public int hashCode() {
      return Objects.hash(hallway, char2moves, dest);
    }

    /**
     * Returns whether room r would be a valid destination for the given
     * letter. The room is valid, when empty or when the only occupied
     * spaces are the same letter.
     *
     * @param r room number
     * @param letter the candidate letter
     * @return the amount to go down into this room, otherwise 100
     */
    public int isValidDestination(int r, char letter) {
      int depth = 1 + dest[r].lastIndexOf('.');
      if (depth < 1) {
        return 100;
      }

      for (int i = depth; i < dest[r].length(); i++) {
        if (dest[r].charAt(i) != letter) {
          return 100;
        }
      }
      return depth;
    }

    public int score() {
      int a = char2moves.get('A');
      int b = char2moves.get('B') * 10;
      int c = char2moves.get('C') * 100;
      int d = char2moves.get('D') * 1000;
      return a + b + c + d;
    }

    /**
     * Places the given letter into room r
     *
     * @param r the room
     * @param letter
     */
    public void toRoom(int r, char letter) {
      int depth = dest[r].lastIndexOf('.');
      dest[r] = dest[r].substring(0, depth) + letter + dest[r].substring(depth + 1);
    }

    String toSmall() {
      StringBuilder sb = new StringBuilder();
      sb.append(hallway);
      for (int i = 0; i < roomDepth; i++) {
        sb.append(dest[i]);
      }
      return sb.toString();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(hallway);
      sb.append("\n");
      for (int i = 0; i < roomDepth; i++) {
        sb.append(" #");
        sb.append(dest[0].charAt(i));
        sb.append("#");
        sb.append(dest[1].charAt(i));
        sb.append("#");
        sb.append(dest[2].charAt(i));
        sb.append("#");
        sb.append(dest[3].charAt(i));
        sb.append("#\n");
      }
      return sb.toString();
    }

  }

  private static final Map<Integer, Integer> room2position = new HashMap<>();
  static {
    room2position.put(0, 3);
    room2position.put(1, 5);
    room2position.put(2, 7);
    room2position.put(3, 9);
  }
  private static final Map<Character, Integer> char2roomPosition = new HashMap<>();
  static {
    char2roomPosition.put('A', 3);
    char2roomPosition.put('B', 5);
    char2roomPosition.put('C', 7);
    char2roomPosition.put('D', 9);
  }

  Set<State> doStep(State s) {
    Set<State> outcomes = new HashSet<>();

    // 1 From source Room directly into destination Room
    Set<Character> movedtoFinal = new HashSet<>();
    for (int src = 0; src < 4; src++) {
      int up = 1;
      char letter = s.dest[src].charAt(0);
      while (letter == '.' && up < s.dest[src].length()) {
        letter = s.dest[src].charAt(up++);
      }
      if (letter != '.') { // we can do this
        int x = room2position.get(src);
        int dest = char2roomPosition.get(letter);

        if (x != dest) {
          int r = letter - 'A';
          int down = s.isValidDestination(r, letter);
          if (down <= s.roomDepth) { // We could go there
            // check hallway
            int right = 0;
            dest--;
            x--;

            boolean ok = true;
            int p = x;
            while (ok && p != dest) {
              if (s.hallway[p] == 'x' || s.hallway[p] == '.') {
              } else {
                ok = false;
              }
              p += Integer.signum(dest - x);
              right += Integer.signum(dest - x);
            }

            if (ok) { // We could go there
              // We did up + right+down steps
              int steps = up + Math.abs(right) + down;
              State newS = new State(s);
              newS.fromRoom(src);
              newS.toRoom(r, letter);
              newS.char2moves.compute(letter, (k, v) -> v + steps);
              outcomes.add(newS);
              movedtoFinal.add(letter);
            }
          }
        }
      }
    }

    // 2 From source Room to Hallway
    letter: for (int src = 0; src < 4; src++) {
      for (int h = 0; h < s.hallway.length; h++) {
        int up = 1;
        char letter = s.dest[src].charAt(0);
        while (letter == '.' && up < s.dest[src].length()) {
          letter = s.dest[src].charAt(up++);
        }

        if (letter != '.' && !movedtoFinal.contains(letter)) { // we can do this
          if (!s.canLeave(letter, src)) {
            continue letter;
          }

          if (s.hallway[h] == '.') { // new state
            // can letter go to h? from x to h without seeing anything other than x or .?
            int x = room2position.get(src) - 1;
            int right = 0;

            boolean ok = true;
            int p = x;
            while (ok && p != h) {
              if (s.hallway[p] == 'x' || s.hallway[p] == '.') {
              } else {
                ok = false;
              }
              p += Integer.signum(h - x);
              right += Integer.signum(h - x);
            }

            if (ok) { // We could go there
              // We did up + right steps
              int steps = up + Math.abs(right);
              State newS = new State(s);
              newS.hallway[h] = letter;
              newS.fromRoom(src);
              newS.char2moves.compute(letter, (k, v) -> v + steps);
              outcomes.add(newS);
            }
          }
        }
      }
    }

    // 3 From Hallway to destination room
    for (int h = 0; h < s.hallway.length; h++) {
      if (s.hallway[h] != '.' && s.hallway[h] != 'x') {
        char letter = s.hallway[h];
        int x = h;
        int dest = char2roomPosition.get(letter);

        int d = (dest - 3) / 2;
        int down = s.isValidDestination(d, letter);

        if (down <= s.roomDepth) { // We could go there
          // check hallway
          dest--;
          int p = x;
          int right = Integer.signum(dest - x);
          p += Integer.signum(dest - x);

          boolean ok = true;

          while (ok && p != dest) {
            if (s.hallway[p] == 'x' || s.hallway[p] == '.') {
            } else {
              ok = false;
            }
            p += Integer.signum(dest - x);
            right += Integer.signum(dest - x);
          }

          if (ok) { // We could go there
            // We did up + right+down steps
            int steps = Math.abs(right) + down;
            State newS = new State(s);
            newS.hallway[h] = '.';
            newS.toRoom(d, letter);
            newS.char2moves.compute(letter, (k, v) -> v + steps);
            outcomes.add(newS);
          }
        }
      }
    }

    return outcomes;
  }

  private long solve(State begin) {
    int minCost = Integer.MAX_VALUE;
    Map<String, Integer> visited2score = new HashMap<>();
    Set<State> unvisited = new TreeSet<>(Collections.singleton(begin));

    while (!unvisited.isEmpty()) {
      Iterator<State> it = unvisited.iterator();
      State s = it.next();
      it.remove();

      for (State no : doStep(s)) {
        int cost = no.score();
        if (no.done()) {
          minCost = Math.min(minCost, cost);
        } else {
          String key = no.toSmall();
          if (cost == visited2score.compute(key, (k, v) -> v == null ? cost : Math.min(v, cost))) {
            unvisited.add(no);
          }
        }
      }
    }

    return minCost;
  }

  @Test
  public void testDay23() {
    assertEquals(12521, solve(new State("BA", "CD", "BC", "DA")));

    announceResultA();
    long result = solve(new State("DB", "AC", "DB", "CA"));
    System.out.println(result);
    assertEquals(14348, result);

    assertEquals(44169, solve(new State("BDDA", "CCBD", "BBAC", "DACA")));
    announceResultB();
    result = solve(new State("DDDB", "ACBC", "DBAB", "CACA"));
    System.out.println(result);
    assertEquals(40954, result);
  }

}
