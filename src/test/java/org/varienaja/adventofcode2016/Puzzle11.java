package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {
  class State {
    int hash = 0;
    String tostring = "";
    int elevatorPos;
    int elevatorMoves;
    Map<Integer, Set<String>> floor2Items;

    State() {
      floor2Items = new HashMap<>();
      elevatorPos = 1;
      elevatorMoves = 0;
    }

    State(State s) {
      floor2Items = new HashMap<>();
      for (Entry<Integer, Set<String>> e : s.floor2Items.entrySet()) {
        floor2Items.put(e.getKey(), new HashSet<>(e.getValue()));
      }

      elevatorPos = s.elevatorPos;
      elevatorMoves = s.elevatorMoves;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof State) {
        State other = (State)o;
        // whether a floor contains x and x-compatible or y and y-compatible does not matter
        // The states are equivalent; therefore, equals should return true for these cases
        // And hashcode should return the same value too..

        return toString().equals(other.toString());
      }
      return false;
    }

    boolean finished() {
      return floor2Items.get(0).isEmpty() //
          && floor2Items.get(1).isEmpty() //
          && floor2Items.get(2).isEmpty() //
          && floor2Items.get(3).isEmpty();
    }

    @Override
    public int hashCode() {
      if (hash == 0) {
        hash = toString().hashCode();
      }
      return hash;
    }

    boolean isAllowed(int floor) {
      // So: - chips can be together
      // - Generators can be together
      // - Pairs can be together with generators
      Set<String> generators = new HashSet<>();
      Set<String> chips = new HashSet<>();
      for (String item : floor2Items.get(floor)) {
        if (item.endsWith("-compatible")) {
          chips.add(item.substring(0, item.indexOf('-')));
        } else {
          generators.add(item);
        }
      }
      chips.removeAll(generators);
      if (chips.isEmpty()) {
        return true;
      }

      if (generators.isEmpty()) {
        return true;
      }

      return false;
    }

    boolean isAllowed(int floor1, int floor2) {
      // So: - chips can be together
      // - Generators can be together
      // - Pairs can be together with generators
      Set<String> generators = new HashSet<>();
      Set<String> chips = new HashSet<>();
      List<Integer> floors = List.of(floor1, floor2);
      for (int f : floors) {
        for (String item : floor2Items.get(f)) {
          if (item.endsWith("-compatible")) {
            chips.add(item.substring(0, item.indexOf('-')));
          } else {
            generators.add(item);
          }
        }
      }
      chips.removeAll(generators);
      if (chips.isEmpty()) {
        return true;
      }

      if (generators.isEmpty()) {
        return true;
      }

      return false;
    }

    /**
     * Load item into Elevator.
     *
     * @param item the item
     * @return if state allowed
     */
    boolean load(String item) {
      Set<String> elevatorItems = floor2Items.get(0);
      if (elevatorItems.size() == 2) {
        return false;
      }

      boolean result = floor2Items.get(elevatorPos).remove(item);
      result &= elevatorItems.add(item);
      return result && isAllowed(0) && isAllowed(elevatorPos);
    }

    @Override
    public String toString() {
      if (tostring.isEmpty()) {
        StringBuilder sb = new StringBuilder();
        for (int f = 4; f >= 0; f--) {
          sb.append("F").append(f).append(" ");
          sb.append(elevatorPos == f ? "E " : ". ");

          Set<String> items = floor2Items.get(f);
          Set<String> generators = new HashSet<>();
          Set<String> chips = new HashSet<>();
          for (String item : items) {
            if (item.endsWith("-compatible")) {
              chips.add(item.replace("-compatible", ""));
            } else {
              generators.add(item);
            }
          }

          chips.retainAll(generators);
          Set<String> translated = new HashSet<>();
          int ix = 0;
          for (String item : items) {
            String itemName = item.replace("-compatible", "");
            if (chips.contains(itemName)) {
              if (item.endsWith("-compatible")) {
                translated.add("" + ix);
                ix++;
              }
            } else {
              translated.add(item);
            }
          }
          sb.append(translated).append("\n");
        }

        tostring = sb.toString();
      }
      return tostring;
    }

    /**
     * Unload item from Elevator to floor.
     *
     * @param item the item
     * @return if state allowed
     */
    boolean unload(String item) {
      boolean result = floor2Items.get(0).remove(item);
      result &= floor2Items.get(elevatorPos).add(item);
      return result && isAllowed(0) && isAllowed(elevatorPos) && isAllowed(0, elevatorPos);
    }
  }

  private Set<State> seen;
  private Map<String, String> item2short;

  private State compile(State s) {
    item2short = new HashMap<>();

    Set<String> generators = new HashSet<>();
    Set<String> chips = new HashSet<>();
    for (int f = 1; f <= 4; f++) {
      for (String item : s.floor2Items.get(f)) {
        if (item.endsWith("-compatible")) {
          chips.add(item.substring(0, item.indexOf('-')));
        } else {
          generators.add(item);
        }
      }
    }

    char start = 'a';
    for (String c : chips) {
      item2short.put(c, "" + start);
      item2short.put(c + "-compatible", "" + start + "-c");
      start++;
    }

    return s;
  }

  private State parseInput(List<String> input) {
    seen = new HashSet<>();
    State s = new State();
    for (int i = 1; i <= input.size(); i++) {
      Set<String> items = new HashSet<>();

      int start = 0;
      int ix = 0;
      String line = input.get(i - 1);
      while (ix != -1) {
        ix = line.indexOf("a ", start);
        if (ix >= 0) {
          String item = line.substring(ix + 2, line.indexOf(' ', ix + 2));
          items.add(item);
          start = ix + 2;
        }
      }

      s.floor2Items.put(i, items);
    }
    s.floor2Items.put(0, new HashSet<>()); // Lift contents

    return compile(s);
  }

  private int simulateElevatorMove(Set<State> states) {
    Set<State> nextStates = new HashSet<>();

    for (State s : states) {
      // Elevator Up
      if (s.elevatorPos < 4) {
        State s1 = new State(s);
        s1.elevatorPos++;
        s1.elevatorMoves++;
        nextStates.add(s1);
      }

      // Elevator Down
      if (s.elevatorPos > 1) {
        if (s.floor2Items.get(1).isEmpty() && s.elevatorPos == 2) {
          // Don't bother
        } else if (s.floor2Items.get(1).isEmpty() && s.floor2Items.get(2).isEmpty() && s.elevatorPos == 3) {
          // Don't bother either
        } else {
          State s1 = new State(s);
          s1.elevatorPos--;
          s1.elevatorMoves++;
          nextStates.add(s1);
        }
      }
    }

    return simulateUnload(nextStates);
  }

  // Generator must be connected to chip
  // Chips not connected to their generator will be fried when in same room with another generator

  private int simulateLoad(Set<State> states) {

    // Breadth-first search of possible moves?

    // --> put zero, one or two elements in elevator
    // --> move up or down
    // --> unload zero, one or two elements
    seen.addAll(states);

    Set<State> nextStates = new HashSet<>();

    for (State s : states) {
      Set<String> loadCandidates = s.floor2Items.get(s.elevatorPos);
      // ...one...
      for (String lc : loadCandidates) {
        State s1 = new State(s);
        if (s1.load(lc)) {
          nextStates.add(s1);
        }
      }
      // ...or two
      if (loadCandidates.size() >= 2 && s.floor2Items.get(0).size() == 0) {
        for (String lc : loadCandidates) {
          State s1 = new State(s);
          s1.load(lc);
          for (String lc2 : s1.floor2Items.get(s.elevatorPos)) {
            State s11 = new State(s1);
            if (s11.load(lc2)) {
              nextStates.add(s11);
            }
          }
        }
      }
    }

    return simulateElevatorMove(nextStates);
  }

  private int simulateUnload(Set<State> states) {
    Set<State> nextStates = new HashSet<>();

    for (State s : states) {

      // Unload 0...
      State s1 = new State(s);
      nextStates.add(s1);

      Set<String> unloadCandidates = s.floor2Items.get(0);

      // ...one...
      if (unloadCandidates.size() > 0) {
        for (String uc : unloadCandidates) {
          State su1 = new State(s);
          if (su1.unload(uc)) {
            nextStates.add(su1);
          }
        }
      }

      // ..or two items
      if (unloadCandidates.size() == 2) {
        for (String uc : unloadCandidates) {
          State su2 = new State(s);
          su2.unload(uc);
          for (String uc2 : su2.floor2Items.get(0)) {
            State su22 = new State(su2);
            if (su22.unload(uc2)) {
              nextStates.add(su22);
            }
          }
        }
      }
    }

    Optional<State> finishedState = nextStates.stream().filter(State::finished).findFirst();
    if (finishedState.isPresent()) {
      return finishedState.get().elevatorMoves;
    }

    int cnt = nextStates.size();
    nextStates.removeAll(seen);
    int newcnt = nextStates.size();
    System.out.println("Removed " + (cnt - newcnt) + " already seen states of " + cnt + " total");

    return simulateLoad(nextStates);
  }

  // So: - chips can be together
  // - Generators can be together
  // - Pairs can be together with generators
  // - Lift can only carry 2 items
  // - All has to be moved to 4th floor
  // How many movements does the lift make?
  private long solveA(List<String> input) {
    State s = parseInput(input);
    return simulateLoad(Collections.singleton(s));
  }

  private long solveB(List<String> input) {
    State s = parseInput(input);
    s.floor2Items.get(1).add("elerium-compatible");
    s.floor2Items.get(1).add("elerium");
    s.floor2Items.get(1).add("dilithium-compatible");
    s.floor2Items.get(1).add("dilithium");
    s = compile(s);
    return simulateLoad(Collections.singleton(s));
  }

  @Test
  public void testDay11() {
    State s = new State();
    Set<String> items = new HashSet<>();
    s.floor2Items.put(0, items);
    items.add("lithium-compatible");
    assertTrue(s.isAllowed(0));
    items.add("hydrogen-compatible");
    assertTrue(s.isAllowed(0));

    items.add("hydrogen");
    assertFalse(s.isAllowed(0));

    items.clear();
    items.add("lithium");
    items.add("hydrogen");
    assertTrue(s.isAllowed(0));
    items.add("lithium-compatible");
    items.add("hydrogen-compatible");
    assertTrue(s.isAllowed(0));

    List<String> testInput = List.of( //
        "The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.", //
        "The second floor contains a hydrogen generator.", //
        "The third floor contains a lithium generator.", //
        "The fourth floor contains nothing relevant.");
    assertEquals(11, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(33, sum);

    // https://www.reddit.com/r/adventofcode/comments/5hoia9/2016_day_11_solutions/
    // "Any complete pairs on floor 1 add 12 steps to the solution"
    // "Complete pairs on floor 2 add 8 steps"
    // "Complete pairs on floor 3 add 4 steps"

    announceResultB();
    sum = sum + 12 + 12;
    System.out.println(sum);
    assertEquals(57, sum);
  }

}
